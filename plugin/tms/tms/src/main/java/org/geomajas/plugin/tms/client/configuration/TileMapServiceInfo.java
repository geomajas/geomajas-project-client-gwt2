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
import java.util.List;

/**
 * Generic tile map service definition. This is the top object that represents a TMS service.
 *
 * @author Pieter De Graef
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface TileMapServiceInfo extends Serializable {

	/**
	 * The service title.
	 *
	 * @return The service title.
	 */
	String getTitle();

	/**
	 * A more detailed description of this TMS service.
	 *
	 * @return A more detailed description of this TMS service.
	 */
	String getAbstract();

	/**
	 * Get the version of this TMS service.
	 *
	 * @return The version of this TMS service.
	 */
	String getVersion();

	/**
	 * Get the list of tile maps supported by this service.
	 *
	 * @return The list of tile maps supported by this service.
	 */
	List<ListTileMapInfo> getTileMaps();
}
