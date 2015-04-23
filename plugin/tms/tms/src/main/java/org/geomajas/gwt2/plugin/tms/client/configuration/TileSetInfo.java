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
 * Definition of a single set of tiles. Such a single set describes a tile-level in a tile map.
 *
 * @author Pieter De Graef
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface TileSetInfo extends Serializable {

	/**
	 * Get the base URL for this tile set.
	 *
	 * @return The base URL for this tile set.
	 */
	String getHref();

	/**
	 * Get the units per pixels that describe the scale level at which this tile set is mapped.
	 *
	 * @return The units per pixels that describe the scale level at which this tile set is mapped.
	 */
	double getUnitsPerPixel();

	/**
	 * Get the order for this tile set in the tile map.
	 *
	 * @return The order for this tile set in the tile map.
	 */
	int getOrder();
}