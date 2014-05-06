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
package org.geomajas.gwt2.client.map.render.canvas.container;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.client.map.render.Tile;
import org.geomajas.gwt2.client.map.render.TileCode;
import org.geomajas.gwt2.client.map.render.TileRenderer;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.Callback;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * In-memory canvas that contains a grid of tiles. The grid is constantly extended when the map extends due to panning.
 * 
 * @author Jan De Moerloose
 * 
 */
public class CanvasTileGrid implements IsWidget {

	private Canvas canvas;

	private Range range;

	private int tileLevel;

	private boolean visible;

	private Map<TileCode, CanvasTile> tiles = new HashMap<TileCode, CanvasTile>();

	private TileConfiguration tileConfiguration;

	private TileRenderer tileRenderer;

	private Callback<String, String> callback;

	public CanvasTileGrid(TileConfiguration tileConfiguration, TileRenderer tileRenderer, int tileLevel) {
		this.canvas = Canvas.createIfSupported();
		this.tileConfiguration = tileConfiguration;
		this.tileLevel = tileLevel;
		this.tileRenderer = tileRenderer;
		range = new Range();
	}

	public void setCallback(Callback<String, String> callback) {
		this.callback = callback;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	@Override
	public Widget asWidget() {
		return canvas;
	}

	public void addBounds(Bbox bounds) {
		Range newRange = calcTileRange(bounds);
		if (!range.containsRange(newRange)) {
			// we need to extend, find the united range of all tiles
			Range union = range.union(newRange);
			// set the new width and height
			canvas.setCoordinateSpaceWidth(union.getNx() * tileConfiguration.getTileWidth());
			canvas.setCoordinateSpaceHeight(union.getNy() * tileConfiguration.getTileHeight());
			// resizing the canvas clears it, so we have to redraw everything
			for (CanvasTile loader : tiles.values()) {
				loader.setRendered(false);
			}
			range = union;
		}
		for (int i = newRange.getXmin(); i <= newRange.getXmax(); i++) {
			for (int j = newRange.getYmin(); j <= newRange.getYmax(); j++) {
				TileCode tileCode = new TileCode(tileLevel, i, j);
				if (!tiles.containsKey(tileCode)) {
					RenderTileOnCallback c = new RenderTileOnCallback(callback);
					Tile tile = createTile(tileCode);
					CanvasTile ct = new CanvasTile(tile.getUrl(), tile.getCode(), c);
					c.setTile(ct);
					tiles.put(tileCode, ct);
				}
			}
		}
	}

	public CanvasElement getCanvasElement() {
		return canvas.getCanvasElement();
	}

	public Coordinate getUpperLeftCorner() {
		double resolution = tileConfiguration.getResolution(tileLevel);
		double width = tileConfiguration.getTileWidth() * resolution;
		double height = tileConfiguration.getTileHeight() * resolution;
		Coordinate o = tileConfiguration.getTileOrigin();
		return new Coordinate(o.getX() + range.getXmin() * width, o.getY() + (range.getYmax() + 1) * height);
	}

	public Bbox getGridBounds() {
		return new Bbox(0, 0, range.getNx() * tileConfiguration.getTileWidth(), range.getNy()
				* tileConfiguration.getTileHeight());
	}
	
	public boolean isEmpty() {
		return range.isEmpty();
	}

	public void render() {
		for (CanvasTile tile : tiles.values()) {
			// canvas can only render loaded tiles !
			if (tile.isLoaded() && !tile.isRendered()) {
				renderTile(tile);
			}
		}
	}

	public boolean isFullyLoaded() {
		for (CanvasTile tile : tiles.values()) {
			if (!tile.isLoaded()) {
				return false;
			}
		}
		return true;
	}

	public double getResolution() {
		return tileConfiguration.getResolution(tileLevel);
	}

	protected void renderTile(CanvasTile tile) {
		Bbox box = getGridBounds(tile.getTileCode());
		canvas.getContext2d()
				.drawImage(tile.getImageElement(), box.getX(), box.getY(), box.getWidth(), box.getHeight());
		// render once
		tile.setRendered(true);
	}

	private Tile createTile(TileCode tileCode) {
		Bbox bounds = getGridBounds(tileCode);
		Tile tile = new Tile(tileCode, bounds);
		tile.setCode(tileCode);
		tile.setUrl(tileRenderer.getUrl(tileCode));
		return tile;
	}

	private Bbox getGridBounds(TileCode tileCode) {
		Bbox bb = new Bbox((tileCode.getX() - range.getXmin()) * tileConfiguration.getTileWidth(),
				(range.getYmax() - tileCode.getY()) * tileConfiguration.getTileHeight(),
				tileConfiguration.getTileWidth(), tileConfiguration.getTileHeight());
		return bb;
	}

	private Range calcTileRange(Bbox bounds) {
		double resolution = tileConfiguration.getResolution(tileLevel);
		double worldTileWidth = tileConfiguration.getTileWidth() * resolution;
		double worldTileHeight = tileConfiguration.getTileHeight() * resolution;

		Coordinate tileOrigin = tileConfiguration.getTileOrigin();
		int ymin = (int) Math.floor((bounds.getY() - tileOrigin.getY()) / worldTileHeight);
		int ymax = (int) Math.floor((bounds.getMaxY() - tileOrigin.getY()) / worldTileHeight);
		int xmin = (int) Math.floor((bounds.getX() - tileOrigin.getX()) / worldTileWidth);
		int xmax = (int) Math.floor((bounds.getMaxX() - tileOrigin.getX()) / worldTileWidth);
		
		if(tileConfiguration.isLimitXYByTileLevel()) {
			int maxIndex = (int) Math.pow(2, tileLevel) - 1;

			if (ymin < 0) {
				ymin = 0;
			}
			if (xmin < 0) {
				xmin = 0;
			}
			if (xmax < 0) {
				xmax = -1;
			}
			if (ymax < 0) {
				ymax = -1;
			}
			if (xmax > maxIndex) {
				xmax = maxIndex;
			}
			if (ymax > maxIndex) {
				ymax = maxIndex;
			}
		}
		return new Range(xmin, xmax, ymin, ymax);
	}

	/**
	 * Callback for rendering.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public class RenderTileOnCallback implements Callback<String, String> {

		private CanvasTile tile;

		private Callback<String, String> callback;

		public RenderTileOnCallback(Callback<String, String> callback) {
			this.callback = callback;
		}

		public void destroy() {
			callback = null;
		}

		public void setTile(CanvasTile tile) {
			this.tile = tile;
		}

		@Override
		public void onFailure(String reason) {
			if (callback != null) {
				callback.onFailure(reason);
			}
		}

		@Override
		public void onSuccess(String result) {
			if (callback != null) {
				renderTile(tile);
				callback.onSuccess(result);
			}
		}

	}

	/**
	 * Represents a block of indices.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public class Range {

		private int xmin;

		private int xmax = -1;

		private int ymin;

		private int ymax = -1;

		public Range(int xmin, int xmax, int ymin, int ymax) {
			this.xmin = xmin;
			this.xmax = xmax;
			this.ymin = ymin;
			this.ymax = ymax;
		}

		public int getNy() {
			return ymax - ymin + 1;
		}

		public int getNx() {
			return xmax - xmin + 1;
		}

		public boolean containsRange(Range other) {
			return xmin <= other.getXmin() && ymin <= other.getYmin() && xmax >= other.getXmax()
					&& ymax >= other.getYmax();
		}

		public Range() {
		}

		Range union(Range range) {
			if (isEmpty()) {
				return new Range(range.xmin, range.xmax, range.ymin, range.ymax);
			} else {
				return new Range(Math.min(xmin, range.xmin), Math.max(xmax, range.xmax), Math.min(ymin, range.ymin),
						Math.max(ymax, range.ymax));
			}
		}

		private boolean isEmpty() {
			return getNx() == 0 || getNy() == 0;
		}

		public int getXmin() {
			return xmin;
		}

		public int getXmax() {
			return xmax;
		}

		public int getYmin() {
			return ymin;
		}

		public int getYmax() {
			return ymax;
		}

	}

}
