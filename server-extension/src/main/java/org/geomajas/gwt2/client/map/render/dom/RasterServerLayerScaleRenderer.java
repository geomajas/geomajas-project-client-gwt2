/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.client.map.render.dom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.command.dto.GetRasterTilesRequest;
import org.geomajas.command.dto.GetRasterTilesResponse;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.Deferred;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.View;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.RasterServerLayer;
import org.geomajas.gwt2.client.map.render.FixedScaleRenderer;
import org.geomajas.gwt2.client.map.render.TileLevelRenderedEvent;
import org.geomajas.gwt2.client.map.render.TileLevelRenderedHandler;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlContainer;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlImageImpl;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.layer.tile.TileCode;

import com.google.gwt.core.client.Callback;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * Fixed scale renderer implementation for {@link RasterServerLayer}.
 * 
 * @author Pieter De Graef
 */
public class RasterServerLayerScaleRenderer implements FixedScaleRenderer {

	private final RasterServerLayer layer;

	private final int tileLevel;

	private final double scale;

	private final ViewPort viewPort;

	private final HtmlContainer container;

	private final Map<TileCode, RasterTile> tiles;

	private double mapExtentScaleAtFetch = 1.1;

	private Bbox fetchingTileBounds;

	private Bbox fetchedTileBounds;

	private int nrLoadingTiles;

	private Deferred deferred;

	private Coordinate origin;

	// TODO replace me...
	private final EventBus eventBus = new SimpleEventBus();

	public RasterServerLayerScaleRenderer(RasterServerLayer layer, int tileLevel, double scale, ViewPort viewPort,
			HtmlContainer container) {
		this.layer = layer;
		this.tileLevel = tileLevel;
		this.scale = scale;
		this.viewPort = viewPort;
		this.container = container;
		this.tiles = new HashMap<TileCode, RasterTile>();
	}

	// ------------------------------------------------------------------------
	// TileLevelRenderer implementation:
	// ------------------------------------------------------------------------

	@Override
	public int getTileLevel() {
		return tileLevel;
	}

	@Override
	public void render(View view) {
		if (layer.isShowing()) {
			if (origin == null) {
				origin = view.getPosition();
			}
			Bbox bounds = asBounds(view);

			// First we check whether or not the requested bounds is already rendered:
			if (fetchingTileBounds != null && BboxService.contains(fetchingTileBounds, bounds)) {
				return; // Bounds already rendered or busy rendering: nothing to do here.
			}

			// Scale the bounds to fetch tiles for (we want a bigger area than the map bounds):
			fetchingTileBounds = BboxService.scale(bounds, mapExtentScaleAtFetch);

			// Create the command:
			GetRasterTilesRequest request = new GetRasterTilesRequest();
			request.setBbox(new org.geomajas.geometry.Bbox(fetchingTileBounds.getX(), fetchingTileBounds.getY(),
					fetchingTileBounds.getWidth(), fetchingTileBounds.getHeight()));
			request.setCrs(viewPort.getCrs());
			request.setLayerId(layer.getServerLayerId());
			request.setScale(scale);
			GwtCommand command = new GwtCommand(GetRasterTilesRequest.COMMAND);
			command.setCommandRequest(request);

			// Execute the fetch, and render on success:
			deferred = GeomajasServerExtension.getInstance().getCommandService().execute(command,
					new AbstractCommandCallback<GetRasterTilesResponse>() {

						public void execute(GetRasterTilesResponse response) {
							addTiles(response.getRasterData());
							fetchedTileBounds = fetchingTileBounds;
						}
					});
			nrLoadingTiles = 0;
		}
	}

	@Override
	public void cancel() {
		if (deferred != null) {
			deferred.cancel();
			deferred = null;
			fetchingTileBounds = null;
			nrLoadingTiles = 0;
		}
	}

	@Override
	public boolean isRendered(View view) {
		// TODO we can do better here...
		if (nrLoadingTiles > 0 || fetchedTileBounds == null) {
			return false;
		}

		// First calculate what bounds that view represents at our tile level:
		Bbox scaledBounds = asBounds(view);
		return BboxService.contains(fetchedTileBounds, scaledBounds);
	}

	@Override
	public HandlerRegistration addTileLevelRenderedHandler(TileLevelRenderedHandler handler) {
		return eventBus.addHandler(TileLevelRenderedHandler.TYPE, handler);
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	protected void addTiles(List<org.geomajas.layer.tile.RasterTile> rasterTiles) {
		for (RasterTile tile : rasterTiles) {
			// Add only new tiles to the list:
			if (!tiles.containsKey(tile.getCode())) {
				// Add the tile to the list and render it:
				renderTile(tile, new ImageCounter());
			}
		}
	}

	protected void renderTile(RasterTile tile, Callback<String, String> callback) {
		nrLoadingTiles++;
		container.add(new HtmlImageImpl(tile.getUrl(), tile.getBounds(), callback));
		tiles.put(tile.getCode().clone(), tile);
	}

	protected Bbox asBounds(View view) {
		double deltaScale = view.getScale() / scale;
		Bbox bounds = viewPort.asBounds(view);
		return BboxService.scale(bounds, deltaScale);
	}

	/**
	 * Counts the number of images that are still inbound. If all images are effectively rendered, we fire a
	 * {@link TileLevelRenderedEvent}.
	 * 
	 * @author Pieter De Graef
	 */
	private class ImageCounter implements Callback<String, String> {

		// In case of failure, we can't just sit and wait. Instead we immediately consider the scale level rendered?!?
		public void onFailure(String reason) {
		}

		public void onSuccess(String result) {
			nrLoadingTiles--;
			if (nrLoadingTiles == 0) {
				eventBus.fireEvent(new TileLevelRenderedEvent(RasterServerLayerScaleRenderer.this));
			}
		}
	}
}