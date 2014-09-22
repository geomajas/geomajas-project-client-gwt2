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

import org.geomajas.gwt2.client.map.render.TileKeyProvider;
import org.geomajas.gwt2.client.map.render.dom.LoadableTile;

/**
 * Implementation of a {@link TileKeyProvider}. The provider simply returns the id.
 *
 * @author Youri Flement
 * @author Jan De Moerloose
 */
public class TileKeyProviderImpl implements TileKeyProvider<String> {

	@Override
	public String getKey(LoadableTile tile) {
		return tile.getId();
	}
}
