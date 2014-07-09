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

package org.geomajas.plugin.tms.client.configuration;

import org.geomajas.annotation.Api;

import java.io.Serializable;

/**
 * Definition of a single tile-map within the TMS service. Such a tile-map can be represented by a single layer on the
 * map.
 *
 * @author Pieter De Graef
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface ListTileMapInfo extends Serializable {

	/**
	 * The service title.
	 *
	 * @return The service title.
	 */
	String getTitle();

	/**
	 * The coordinate reference system that describes this tile map.
	 *
	 * @return The coordinate reference system that describes this tile map.
	 */
	String getSrs();

	/**
	 * The profile for this tile map.
	 *
	 * @return The profile for this tile map.
	 */
	String getProfile();

	/**
	 * The URL that points to the description of this tile map.
	 *
	 * @return The URL that points to the description of this tile map.
	 */
	String getHref();
}
