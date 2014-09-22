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

import org.geomajas.gwt2.client.map.render.dom.LoadableTile;

/**
 * Interface for a class that can provide keys for {@link LoadableTile}s.
 *
 * @param <T> The type of the key.
 * @author Youri Flement
 */
public interface TileKeyProvider<T> {

	/**
	 * Get a unique identifier of type {@link T} for the given tile.
	 *
	 * @param tile The tile.
	 * @return A unique identifier.
	 */
	T getKey(LoadableTile tile);

}
