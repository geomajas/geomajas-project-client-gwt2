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

import com.google.web.bindery.event.shared.HandlerRegistration;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.event.TileLevelRenderedHandler;
import org.geomajas.gwt2.client.map.MapPresenterImpl;
import org.geomajas.gwt2.client.map.View;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.tile.TileBasedLayer;
import org.geomajas.gwt2.client.map.render.RenderingInfo;
import org.geomajas.gwt2.client.map.render.TileCode;
import org.geomajas.gwt2.client.map.render.TileLevelRenderer;
import org.geomajas.gwt2.client.map.render.TileRenderer;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Definition for a renderer for WMS layers.
 *
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
public class DomTileLevelRenderer implements TileLevelRenderer {

	private static Logger logger = Logger.getLogger(DomTileLevelRenderer.class.getName());

	private static final int MAX_NR_TILES = 500;

	private final HtmlContainer container;

	private final TileBasedLayer layer;

	private final double resolution;

	private final Map<TileCode, LoadableTile> tiles;

	private final int tileLevel;

	private final ViewPort viewPort;

	private final TileRenderer tileRenderer;

	public DomTileLevelRenderer(TileBasedLayer layer, int tileLevel, ViewPort viewPort, HtmlContainer container,
			TileRenderer tileRenderer) {
		this.layer = layer;
		this.tileLevel = tileLevel;
		this.viewPort = viewPort;
		this.container = container;
		this.tileRenderer = tileRenderer;
		this.tiles = new HashMap<TileCode, LoadableTile>();
		this.resolution = layer.getTileConfiguration().getResolution(tileLevel);
	}

	// ------------------------------------------------------------------------
	// TileLevelRenderer implementation:
	// ------------------------------------------------------------------------

	@Override
	public int getTileLevel() {
		return tileLevel;
	}

	@Override
	public void render(RenderingInfo info, View view) {
		if (layer.isShowing()) {
			// Get the tiles in the current view and add them to the queue if they were not yet added:
			List<TileCode> tilesForBounds = getTileCodesForView(view);
			for (TileCode code : tilesForBounds) {
				if (!tiles.containsKey(code)) {
					LoadableTile tile = createTile(code);
					info.getHintValue(MapPresenterImpl.QUEUE).add(tile);
				}
			}
		}
	}

	@Override
	public void cancel() {
		// no longer relevant, the queue is loading the tles now !
	}

	@Override
	public boolean isRendered(View view) {
		// make this more precise by checking only tiles within the view ?
		for (LoadableTile tile : tiles.values()) {
			if (!tile.isLoaded()) {
				return false;
			}
		}
		return true;
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

	private LoadableTile createTile(TileCode tileCode) {
		Bbox worldBounds = getWorldBounds(tileCode);

		// Create a dom tile - this is a tile with an image that can be loaded later (by the queue):
		DomTile tile = new DomTile(layer, tileCode, tileRenderer.getUrl(tileCode),
				getScreenBounds(worldBounds));
		tiles.put(tileCode, tile);

		// Add the image to the container:
		container.add(tile.getImage());
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
		// clip to maximum bounds
		bounds = BboxService.intersection(bounds, layer.getMaxBounds());
		List<TileCode> codes = new ArrayList<TileCode>();
		if (bounds == null || bounds.getHeight() == 0 || bounds.getWidth() == 0) {
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

		int count = 0;
		for (int x = xmin; x <= xmax; x++) {
			for (int y = ymin; y <= ymax; y++) {
				codes.add(new TileCode(tileLevel, x, y));
				if (count++ > MAX_NR_TILES) {
					logger.severe("Too many tiles for level " + tileLevel + " of layer " + layer.getId());
					break;
				}
			}
		}
		return codes;
	}

}