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
import org.geomajas.gwt2.client.map.render.TilePriorityFunction;
import org.geomajas.gwt2.client.map.render.dom.LoadableTile;

/**
 * Implementation of a {@link org.geomajas.gwt2.client.map.render.TilePriorityFunction}.
 *
 * @author Youri Flement
 */
public class TilePriorityFunctionImpl implements TilePriorityFunction {

	/**
	 * The priority function drops tiles higher than the current resolution.
	 * Tiles at higher zoom levels are prioritized and within a zoom level the tiles closer
	 * to the focus are prioritized.
	 *
	 * @param tile            The tile to calculate the priority for.
	 * @param resolutionIndex The current resolution index.
	 * @param resolution      The resolution.
	 * @param focus           The focus.
	 * @return The priority of the tile.
	 */
	@Override
	public Double getPriority(LoadableTile tile, int resolutionIndex, double resolution, Coordinate focus) {
		// Drop tiles higher than the current zoom level:
		if (tile.getCode().getTileLevel() > resolutionIndex) {
			return Double.MAX_VALUE;
		}

		Bbox bounds = tile.getBounds();
		Coordinate tileCenter = new Coordinate(bounds.getX() + bounds.getWidth() / 2,
				bounds.getY() + bounds.getHeight() / 2);
		return (65536 * Math.log(resolution) + tileCenter.distance(focus) / resolution);
	}

}
