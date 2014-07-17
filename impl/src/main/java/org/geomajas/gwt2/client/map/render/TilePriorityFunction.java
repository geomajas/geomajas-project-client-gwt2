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

package org.geomajas.gwt2.client.map.render;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.render.dom.LoadableTile;

/**
 * Interface for a priority function of a {@link LoadableTile}.
 *
 * @author Youri Flement
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface TilePriorityFunction {

	/**
	 * Get a priority for a {@link LoadableTile} based on the current resolution index,
	 * the resolution and the {@link Coordinate} to focus on, which is typically the center of the
	 * {@link org.geomajas.gwt2.client.map.ViewPort}.
	 *
	 * @param tile            The tile to calculate a priority for.
	 * @param resolutionIndex The current resolution index.
	 * @param resolution      The resolution.
	 * @param focus           The coordinate the focus on.
	 * @return A priority for the tile.
	 */
	Double getPriority(LoadableTile tile, int resolutionIndex, double resolution, Coordinate focus);
}
