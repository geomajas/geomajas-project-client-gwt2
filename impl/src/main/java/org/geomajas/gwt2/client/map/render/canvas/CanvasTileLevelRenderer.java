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

package org.geomajas.gwt2.client.map.render.canvas;

import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.map.View;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.tile.TileBasedLayer;
import org.geomajas.gwt2.client.map.render.TileLevelRenderedHandler;
import org.geomajas.gwt2.client.map.render.TileLevelRenderer;
import org.geomajas.gwt2.client.map.render.TileRenderer;
import org.geomajas.gwt2.client.map.render.canvas.container.CanvasTileGrid;

import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Definition for a single tile level renderer for tile layers.
 * 
 * @author Jan De Moerloose
 */
public class CanvasTileLevelRenderer implements TileLevelRenderer {

	private final CanvasTileGrid grid;

	private final TileBasedLayer layer;

	private final int tileLevel;

	private final ViewPort viewPort;

	/**
	 * 
	 * @param layer
	 * @param tileLevel
	 * @param viewPort
	 * @param grid
	 * @param tileRenderer
	 */
	public CanvasTileLevelRenderer(TileBasedLayer layer, int tileLevel, ViewPort viewPort, CanvasTileGrid grid,
			TileRenderer tileRenderer) {
		this.layer = layer;
		this.tileLevel = tileLevel;
		this.viewPort = viewPort;
		this.grid = grid;
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
			grid.addBounds(viewPort.asBounds(view));
			grid.render();
		}
	}

	@Override
	public void cancel() {
	}

	@Override
	public boolean isRendered(View view) {
		return grid.isFullyLoaded();
	}

	@Override
	public HandlerRegistration addTileLevelRenderedHandler(TileLevelRenderedHandler handler) {
		return GeomajasImpl.getInstance().getEventBus().addHandler(TileLevelRenderedHandler.TYPE, handler);
	}


}