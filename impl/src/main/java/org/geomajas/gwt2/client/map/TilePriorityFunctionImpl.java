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

package org.geomajas.gwt2.client.map;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.client.map.render.TilePriorityFunction;
import org.geomajas.gwt2.client.map.render.dom.LoadableTile;

/**
 * Implementation of a {@link org.geomajas.gwt2.client.map.render.TilePriorityFunction}.
 * 
 * @author Youri Flement
 */
public class TilePriorityFunctionImpl implements TilePriorityFunction {

	/**
	 * The priority function drops tiles higher than the current resolution. Tiles at higher zoom levels are prioritized
	 * and within a zoom level the tiles closer to the focus are prioritized.
	 * 
	 * @param tile The tile to calculate the priority for.
	 * @param view The view to calculate the priority for.
	 * @return The priority of the tile.
	 */
	@Override
	public Double getPriority(LoadableTile tile, View view) {
		// shouldn't there be a shortcut for this ?
		TileConfiguration tileConfig = tile.getLayer().getTileConfiguration();
		int currentLevel = tileConfig.getResolutionIndex(view.getResolution());
		Coordinate focus = view.getPosition();
		double resolution = tileConfig.getResolution(tile.getCode().getTileLevel());

		// Drop tiles higher than the current zoom level:
		if (tile.getCode().getTileLevel() > currentLevel) {
			return Double.MAX_VALUE;
		}

		Bbox bounds = tile.getBounds();
		Coordinate tileCenter = new Coordinate(bounds.getX() + bounds.getWidth() / 2, bounds.getY()
				+ bounds.getHeight() / 2);
		// priority depends on resolution and distance (see ol)
		return (65536 * Math.log(resolution) + tileCenter.distance(focus) / resolution);
	}

}
