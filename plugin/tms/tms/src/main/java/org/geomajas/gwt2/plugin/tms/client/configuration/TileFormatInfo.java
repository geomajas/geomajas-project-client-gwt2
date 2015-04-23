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

package org.geomajas.gwt2.plugin.tms.client.configuration;

import org.geomajas.annotation.Api;

import java.io.Serializable;

/**
 * Definition the format used for tiles in a tile map service.
 *
 * @author Pieter De Graef
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface TileFormatInfo extends Serializable {

	/**
	 * Get the width of the tiles in pixels.
	 *
	 * @return The width of the tiles in pixels.
	 */
	int getWidth();

	/**
	 * Get the height of the tiles in pixels.
	 *
	 * @return The height of the tiles in pixels.
	 */
	int getHeight();

	/**
	 * Get the mime type for the tile images.
	 *
	 * @return The mime type for the tile images.
	 */
	String getMimeType();

	/**
	 * Get the extension for the tile images.
	 *
	 * @return The extension for the tile images.
	 */
	String getExtension();
}
