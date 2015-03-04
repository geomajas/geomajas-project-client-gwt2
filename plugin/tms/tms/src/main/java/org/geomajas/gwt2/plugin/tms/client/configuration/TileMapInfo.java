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
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;

import java.io.Serializable;
import java.util.List;

/**
 * Definition of a single tile-map within the TMS service. Such a tile-map can be represented by a single layer on the
 * map.
 *
 * @author Pieter De Graef
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface TileMapInfo extends Serializable {

	/**
	 * Get the base URL for this tile map. Can serve as base URL when creating TMS layers.
	 *
	 * @return The base URL for this tile map.
	 */
	String getHref();

	/**
	 * Get the version of this TMS service.
	 *
	 * @return The version of this TMS service.
	 */
	String getVersion();

	/**
	 * Get the URL to the TMS service.
	 *
	 * @return The URL to the TMS service.
	 */
	String getTilemapservice();

	/**
	 * The title for this tile map.
	 *
	 * @return The title for this tile map.
	 */
	String getTitle();

	/**
	 * A more detailed description of this tile map.
	 *
	 * @return A more detailed description of this tile map.
	 */
	String getAbstract();

	/**
	 * The coordinate reference system that describes this tile map.
	 *
	 * @return The coordinate reference system that describes this tile map.
	 */
	String getSrs();

	/**
	 * Get the bounding box that specifies the spatial extent of this tile map.
	 *
	 * @return The bounding box that specifies the spatial extent of this tile map.
	 */
	Bbox getBoundingBox();

	/**
	 * Get the origin for this tile map.
	 *
	 * @return The origin for this tile map.
	 */
	Coordinate getOrigin();

	/**
	 * Get the tile format information object.
	 *
	 * @return The tile format information object.
	 */
	TileFormatInfo getTileFormat();

	/**
	 * The profile for this tile map.
	 *
	 * @return The profile for this tile map.
	 */
	String getProfile();

	/**
	 * Get the ordered list of tile sets within this tile map.
	 *
	 * @return The ordered list of tile sets within this tile map.
	 */
	List<TileSetInfo> getTileSets();
}
