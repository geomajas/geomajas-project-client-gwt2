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

import org.geomajas.gwt2.client.map.View;
import org.geomajas.gwt2.client.map.render.dom.LoadableTile;

/**
 * Interface for a priority function of a {@link LoadableTile}.
 * 
 * @author Youri Flement
 * @author Jan De Moerloose
 */
public interface TilePriorityFunction {

	/**
	 * Get a priority for a {@link LoadableTile}. Higher priority value means lower priority.
	 * 
	 * @param tile The tile to calculate a priority for.
	 * @param view The view to calculate a priority for.
	 * @return A priority for the tile.
	 */
	TilePriority getPriority(LoadableTile tile, View view);
}
