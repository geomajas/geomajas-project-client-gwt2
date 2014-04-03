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
import com.google.web.bindery.event.shared.HandlerRegistration;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.map.View;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.tile.TileBasedLayer;
import org.geomajas.gwt2.client.map.render.Tile;
import org.geomajas.gwt2.client.map.render.TileCode;
import org.geomajas.gwt2.client.map.render.TileLevelRenderedEvent;
import org.geomajas.gwt2.client.map.render.TileLevelRenderedHandler;
import org.geomajas.gwt2.client.map.render.TileLevelRenderer;
import org.geomajas.gwt2.client.map.render.TileRenderer;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlContainer;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlImageImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Definition for a renderer for WMS layers.
 *
 * @author Pieter De Graef
 */
public class DomTileLevelRenderer implements TileLevelRenderer {

	private final HtmlContainer container;

	private final TileBasedLayer layer;

	private final double resolution;

	private final Map<TileCode, Tile> tiles;

	private final int tileLevel;

	private final ViewPort viewPort;

	private final TileRenderer tileRenderer;

	private int nrLoadingTiles;

	public DomTileLevelRenderer(TileBasedLayer layer, int tileLevel, ViewPort viewPort, HtmlContainer container,
			TileRenderer tileRenderer) {
		this.layer = layer;
		this.tileLevel = tileLevel;
		this.viewPort = viewPort;
		this.container = container;
		this.tileRenderer = tileRenderer;
		this.tiles = new HashMap<TileCode, Tile>();
		this.resolution = layer.getResolution(tileLevel);
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
			List<TileCode> tilesForBounds = getTileCodesForView(view);
			for (TileCode tileCode : tilesForBounds) {
				if (!tiles.containsKey(tileCode)) {
					Tile tile = createTile(tileCode);

					// Add the tile to the list and render it:
					tiles.put(tileCode, tile);
					nrLoadingTiles++;
					renderTile(tile, new ImageCounter());
				}
			}
		}
	}

	@Override
	public void cancel() {
		nrLoadingTiles = 0;
	}

	@Override
	public boolean isRendered(View view) {
		return nrLoadingTiles == 0 && tiles.size() > 0;
	}

	@Override
	public HandlerRegistration addTileLevelRenderedHandler(TileLevelRenderedHandler handler) {
		return GeomajasImpl.getInstance().getEventBus().addHandler(TileLevelRenderedHandler.TYPE, handler);
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	protected Bbox asBounds(View view) {
		double deltaScale = view.getResolution() / resolution;
		Bbox bounds = viewPort.asBounds(view);
		return BboxService.scale(bounds, deltaScale);
	}

	protected void renderTile(Tile tile, Callback<String, String> callback) {
		container.add(new HtmlImageImpl(tile.getUrl(), tile.getBounds(), callback));
	}

	private Tile createTile(TileCode tileCode) {
		Bbox worldBounds = getWorldBounds(tileCode);
		Tile tile = new Tile(tileCode, getScreenBounds(worldBounds));
		tile.setCode(tileCode);
		tile.setUrl(tileRenderer.getUrl(tileCode));
		return tile;
	}

	private Bbox getScreenBounds(Bbox worldBox) {
		return new Bbox(Math.round(worldBox.getX() / resolution), -Math.round(worldBox.getMaxY() / resolution),
				Math.round(worldBox.getMaxX() / resolution) - Math.round(worldBox.getX() / resolution),
				Math.round(worldBox.getMaxY() / resolution) - Math.round(worldBox.getY() / resolution));
	}

	private Bbox getWorldBounds(TileCode tileCode) {
		double worldTileWidth = layer.getTileConfiguration().getTileWidth() * resolution;
		double worldTileHeight = layer.getTileConfiguration().getTileHeight() * resolution;

		double x = layer.getTileConfiguration().getTileOrigin().getX() + tileCode.getX() * worldTileWidth;
		double y = layer.getTileConfiguration().getTileOrigin().getY() + tileCode.getY() * worldTileHeight;
		return new Bbox(x, y, worldTileWidth, worldTileHeight);
	}

	private List<TileCode> getTileCodesForView(View view) {
		Bbox bounds = asBounds(view);
		List<TileCode> codes = new ArrayList<TileCode>();
		if (bounds.getHeight() == 0 || bounds.getWidth() == 0) {
			return codes;
		}

		double worldTileWidth = layer.getTileConfiguration().getTileWidth() * resolution;
		double worldTileHeight = layer.getTileConfiguration().getTileHeight() * resolution;

		Coordinate tileOrigin = layer.getTileConfiguration().getTileOrigin();
		int ymin = (int) Math.floor((bounds.getY() - tileOrigin.getY()) / worldTileHeight);
		int ymax = (int) Math.floor((bounds.getMaxY() - tileOrigin.getY()) / worldTileHeight);
		int xmin = (int) Math.floor((bounds.getX() - tileOrigin.getX()) / worldTileWidth);
		int xmax = (int) Math.floor((bounds.getMaxX() - tileOrigin.getX()) / worldTileWidth);

		if (ymin < 0) {
			ymin = 0;
		}
		if (xmin < 0) {
			xmin = 0;
		}
		if (xmax < 0 || ymax < 0) {
			return codes;
		}

		for (int x = xmin; x <= xmax; x++) {
			for (int y = ymin; y <= ymax; y++) {
				codes.add(new TileCode(tileLevel, x, y));
			}
		}
		return codes;
	}

	/**
	 * Counts the number of images that are still inbound. If all images are effectively rendered, we fire an event.
	 *
	 * @author Pieter De Graef
	 */
	private class ImageCounter implements Callback<String, String> {

		// In case of failure, we can't just sit and wait. Instead we consider the resolution level rendered.
		public void onFailure(String reason) {
			GeomajasImpl.getInstance().getEventBus().fireEventFromSource(new TileLevelRenderedEvent(
					DomTileLevelRenderer.this), DomTileLevelRenderer.this);
		}

		public void onSuccess(String result) {
			if (nrLoadingTiles > 0) { // A cancel may have reset the number of loading tiles.
				nrLoadingTiles--;
				if (nrLoadingTiles == 0) {
					GeomajasImpl.getInstance().getEventBus().fireEventFromSource(new TileLevelRenderedEvent(
							DomTileLevelRenderer.this), DomTileLevelRenderer.this);
				}
			}
		}
	}
}