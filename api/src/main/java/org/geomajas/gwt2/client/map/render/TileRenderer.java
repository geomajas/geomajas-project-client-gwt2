/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.client.map.render;

import org.geomajas.annotation.Api;

/**
 * Renderer for a single tile. It should provide the actual URL where the tile image can be found.
 *
 * @author Pieter De Graef
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface TileRenderer {

	/**
	 * Get the URL that points to the image representing the given tile code.
	 *
	 * @param tileCode The tile code to fetch an image for.
	 * @return The image URL.
	 */
	String getUrl(TileCode tileCode);
}
