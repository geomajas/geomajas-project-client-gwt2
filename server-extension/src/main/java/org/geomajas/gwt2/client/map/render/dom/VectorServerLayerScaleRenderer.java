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

import com.google.gwt.core.client.Callback;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.gwt.client.command.Deferred;
import org.geomajas.gwt2.client.map.View;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.VectorServerLayer;
import org.geomajas.gwt2.client.map.render.TileLevelRenderer;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlContainer;
import org.geomajas.gwt2.client.map.render.TileLevelRenderedEvent;
import org.geomajas.gwt2.client.map.render.TileLevelRenderedHandler;
import org.geomajas.layer.tile.TileCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tiled resolution presenter for a vector layer. It displays a single tile level for a single vector layer.
 *
 * @author Pieter De Graef
 */
public class VectorServerLayerScaleRenderer implements TileLevelRenderer {

	private final ViewPort viewPort;

	private final VectorServerLayer layer;

	private final HtmlContainer container;

	private final Bbox layerBounds;

	private final int tileLevel;

	private final double resolution;

	private final Map<String, VectorTilePresenter> tiles;

	private double mapExtentScaleAtFetch = 1.1;

	private Deferred deferred;

	private int nrLoadingTiles;

	// TODO replace me...
	private final EventBus eventBus = new SimpleEventBus();

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public VectorServerLayerScaleRenderer(VectorServerLayer layer, int tileLevel, double resolution, ViewPort viewPort,
			HtmlContainer container) {
		this.layer = layer;
		this.tileLevel = tileLevel;
		this.resolution = resolution;
		this.viewPort = viewPort;
		this.container = container;
		this.layerBounds = layer.getLayerInfo().getMaxExtent();
		this.tiles = new HashMap<String, VectorTilePresenter>();
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
		Bbox bounds = asBounds(view);
		render(bounds);
	}

	@Override
	public boolean isRendered(View view) {
		Bbox bounds = asBounds(view);
		return isRendered(bounds);
	}

	@Override
	public HandlerRegistration addTileLevelRenderedHandler(TileLevelRenderedHandler handler) {
		return eventBus.addHandler(TileLevelRenderedHandler.TYPE, handler);
	}

	@Override
	public void cancel() {
		// Perhaps we where busy fetching the tiles?
		if (deferred != null) {
			deferred.cancel();
			deferred = null;
		}
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	private void render(Bbox bbox) {
		// Only fetch when inside the layer bounds:
		if (BboxService.intersects(bbox, layerBounds) && layer.isShowing()) {

			// Find needed tile codes:
			List<TileCode> tempCodes = calcCodesForBounds(BboxService.scale(bbox, mapExtentScaleAtFetch));
			for (TileCode tileCode : tempCodes) {

				VectorTilePresenter tilePresenter = tiles.get(tileCode.toString());
				if (tilePresenter == null) {
					// New tile
					tilePresenter = addTile(tileCode);
					tilePresenter.render();
				}
			}
		}
	}

	private VectorTilePresenter addTile(TileCode tileCode) {
		VectorTilePresenter tilePresenter = tiles.get(tileCode.toString());
		if (tilePresenter == null) {
			tilePresenter = new VectorTilePresenter(layer, container, tileCode.clone(), resolution, viewPort.getCrs(),
					new TileLoadCallback());
			nrLoadingTiles++;
			tiles.put(tileCode.toString(), tilePresenter);
		}
		return tilePresenter;
	}

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	protected boolean isRendered(Bbox bounds) {
		if (nrLoadingTiles > 0) {
			return false;
		}
		List<TileCode> tempCodes = calcCodesForBounds(bounds);
		for (TileCode tileCode : tempCodes) {

			VectorTilePresenter tilePresenter = tiles.get(tileCode.toString());
			if (tilePresenter == null) {
				return false;
			}
		}
		return true;
	}

	// TODO Move to abstract?
	protected Bbox asBounds(View view) {
		double deltaScale = view.getResolution() / resolution;
		Bbox bounds = viewPort.asBounds(view);
		return scale(bounds, deltaScale);
	}

	private Bbox scale(Bbox bbox, double factor) {
		double scaledWidth = bbox.getWidth() * factor;
		double scaledHeight = bbox.getHeight() * factor;
		Coordinate center = BboxService.getCenterPoint(bbox);
		return new Bbox(center.getX() - scaledWidth / 2, center.getY() - scaledHeight / 2, scaledWidth, scaledHeight);
	}

	/**
	 * Saves the complete array of TileCode objects for the given bounds (and the current resolution).
	 *
	 * @param bounds view bounds
	 * @return list of tiles in these bounds
	 */
	private List<TileCode> calcCodesForBounds(Bbox bounds) {
		int currentTileLevel = calculateTileLevel(bounds);

		// Calculate tile width and height for tileLevel=currentTileLevel
		double div = Math.pow(2, currentTileLevel); // tile level must be correct!
		double tileWidth = Math.ceil(layerBounds.getWidth() / (div * resolution)) * resolution;
		double tileHeight = Math.ceil(layerBounds.getHeight() / (div * resolution)) * resolution;

		// For safety (to prevent division by 0):
		List<TileCode> codes = new ArrayList<TileCode>();
		if (tileWidth == 0 || tileHeight == 0) {
			return codes;
		}

		// Calculate bounds relative to extents:
		Bbox clippedBounds = BboxService.intersection(bounds, layerBounds);
		if (clippedBounds == null) {
			// TODO throw error? If this is null, then the server configuration is incorrect.
			return codes;
		}
		double relativeBoundX = Math.abs(clippedBounds.getX() - layerBounds.getX());
		double relativeBoundY = Math.abs(clippedBounds.getY() - layerBounds.getY());
		int currentMinX = (int) Math.floor(relativeBoundX / tileWidth);
		int currentMinY = (int) Math.floor(relativeBoundY / tileHeight);
		int currentMaxX = (int) Math.ceil((relativeBoundX + clippedBounds.getWidth()) / tileWidth) - 1;
		int currentMaxY = (int) Math.ceil((relativeBoundY + clippedBounds.getHeight()) / tileHeight) - 1;

		// Now fill the list with the correct codes:
		for (int x = currentMinX; x <= currentMaxX; x++) {
			for (int y = currentMinY; y <= currentMaxY; y++) {
				codes.add(new TileCode(currentTileLevel, x, y));
			}
		}
		return codes;
	}

	/**
	 * Calculate the best tile level to use for a certain view-bounds.
	 *
	 * @param bounds view bounds
	 * @return best tile level for view bounds
	 */
	private int calculateTileLevel(Bbox bounds) {
		double baseX = layerBounds.getWidth();
		double baseY = layerBounds.getHeight();
		// choose the tile level so the area is between 256*256 and 512*512 pixels
		double baseArea = baseX * baseY;
		double osmArea = 256 * 256 * (resolution * resolution);
		int tileLevel = (int) Math.floor(Math.log(baseArea / osmArea) / Math.log(4.0));
		if (tileLevel < 0) {
			tileLevel = 0;
		}
		return tileLevel;
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * Callback that keeps track of the number of tiles still underway.
	 *
	 * @author Pieter De Graef
	 */
	private class TileLoadCallback implements Callback<String, String> {

		public void onFailure(String reason) {
			onLoadingDone();
		}

		public void onSuccess(String result) {
			onLoadingDone();
		}

		private void onLoadingDone() {
			nrLoadingTiles--;
			if (nrLoadingTiles == 0) {
				eventBus.fireEvent(new TileLevelRenderedEvent(VectorServerLayerScaleRenderer.this));
			}
		}
	}
}