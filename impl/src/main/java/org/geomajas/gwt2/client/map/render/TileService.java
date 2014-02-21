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

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;

import java.util.List;

/**
 * Service definition with helper methods for tile calculations. These may help when rendering a tile based layer.<p/>
 * TODO this class takes tile levels from the ViewPort, not from layers directly. This must be fixed before this class
 * can become API.
 *
 * @author Pieter De Graef
 */
public interface TileService {

//	int getTileLevel(List<Double> tileLevels, double scale);
//
//	List<TileCode> getTileCodes(ViewPort viewPort, TileConfiguration tileConfiguration, Bbox bounds, double scale);
//
//	Bbox getBoundingBox(TileCode tileCode);

	// ------------------------------------------------------------------------
	// Methods regarding tiles:
	// ------------------------------------------------------------------------

	/**
	 * Get a list of tile codes for a certain location (bounding box).
	 *
	 * @param viewPort          The ViewPort on the map we are calculating tile codes for. We need the ViewPort for
	 *                          scale to tile-level translations.
	 * @param tileConfiguration The basic tile configuration.
	 * @param bounds            The bounds in world space (map CRS).
	 * @param scale             The scale at which to search for tiles.
	 * @return A list of all tiles that lie within the location.
	 */
	List<TileCode> getTileCodesForBounds(ViewPort viewPort, TileConfiguration tileConfiguration, Bbox bounds,
			double scale);

	/**
	 * Given a tile for a layer, what are the tiles bounds in world space.
	 *
	 * @param viewPort   The ViewPort on the map we are calculating tile codes for. We need the ViewPort for scale to
	 *                   tile-level translations.
	 * @param tileConfig The basic tile configuration.
	 * @param tileCode   The tile code.
	 * @return The tile bounds in map CRS.
	 */
	Bbox getWorldBoundsForTile(ViewPort viewPort, TileConfiguration tileConfig, TileCode tileCode);

	/**
	 * Given a certain location at a certain scale, what tile lies there? TODO remove this method....
	 *
	 * @param viewPort   The ViewPort on the map we are calculating tile codes for. We need the ViewPort for scale to
	 *                   tile-level translations.
	 * @param tileConfig The basic tile configuration.
	 * @param location   The location to retrieve a tile for.
	 * @param scale      The scale at which to retrieve a tile.
	 * @return Returns the tile code for the requested location.
	 */
	TileCode getTileCodeForLocation(ViewPort viewPort, TileConfiguration tileConfig, Coordinate location,
			double scale);
}