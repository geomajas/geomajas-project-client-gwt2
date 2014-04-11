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

package org.geomajas.gwt2.client.service;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.client.map.render.TileCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Service definition with helper methods for tile calculations. These may help when rendering a tile based layer.
 *
 * @author Pieter De Graef
 */
public final class TileService {

	private TileService() {
	}

	/**
	 * Get a list of tile codes for a certain location (bounding box).
	 *
	 * @param tileConfig The basic tile configuration.
	 * @param bounds     The bounds in world space (map CRS).
	 * @param resolution The resolution at which to search for tiles.
	 * @return A list of all tiles that lie within the location.
	 */
	public static List<TileCode> getTileCodesForBounds(TileConfiguration tileConfig, Bbox bounds, double resolution) {
		List<TileCode> codes = new ArrayList<TileCode>();
		if (bounds.getHeight() == 0 || bounds.getWidth() == 0) {
			return codes;
		}

		int tileLevel = tileConfig.getResolutionIndex(resolution);
		double actualResolution = tileConfig.getResolution(tileLevel);

		double worldTileWidth = tileConfig.getTileWidth() * actualResolution;
		double worldTileHeight = tileConfig.getTileHeight() * actualResolution;

		Coordinate tileOrigin = tileConfig.getTileOrigin();
		int ymin = (int) Math.floor((bounds.getY() - tileOrigin.getY()) / worldTileHeight);
		int ymax = (int) Math.floor((bounds.getMaxY() - tileOrigin.getY()) / worldTileHeight);
		int xmin = (int) Math.floor((bounds.getX() - tileOrigin.getX()) / worldTileWidth);
		int xmax = (int) Math.floor((bounds.getMaxX() - tileOrigin.getX()) / worldTileWidth);

		if (ymin < 0) {
			ymin = 0;
		}
		if (xmin < 0) {
			xmin = 0;
		}
		if (xmax < 0 || ymax < 0) {
			return codes;
		}

		for (int x = xmin; x <= xmax; x++) {
			for (int y = ymin; y <= ymax; y++) {
				codes.add(new TileCode(tileLevel, x, y));
			}
		}
		return codes;
	}

	/**
	 * Given a tile for a layer, what are the tiles bounds in world space.
	 *
	 * @param tileConfiguration The basic tile configuration.
	 * @param tileCode          The tile code.
	 * @return The tile bounds in map CRS.
	 */
	public static Bbox getWorldBoundsForTile(TileConfiguration tileConfiguration, TileCode tileCode) {
		double resolution = tileConfiguration.getResolution(tileCode.getTileLevel());
		double worldTileWidth = tileConfiguration.getTileWidth() * resolution;
		double worldTileHeight = tileConfiguration.getTileHeight() * resolution;

		double x = tileConfiguration.getTileOrigin().getX() + tileCode.getX() * worldTileWidth;
		double y = tileConfiguration.getTileOrigin().getY() + tileCode.getY() * worldTileHeight;
		return new Bbox(x, y, worldTileWidth, worldTileHeight);
	}

	/**
	 * Given a certain location at a certain scale, what tile lies there?
	 *
	 * @param tileConfiguration The basic tile configuration.
	 * @param location          The location to retrieve a tile for.
	 * @param resolution        The resolution at which to retrieve a tile.
	 * @return Returns the tile code for the requested location.
	 */
	public static TileCode getTileCodeForLocation(TileConfiguration tileConfiguration, Coordinate location,
			double resolution) {
		int tileLevel = tileConfiguration.getResolutionIndex(resolution);
		double actualResolution = tileConfiguration.getResolution(tileLevel);
		double worldTileWidth = tileConfiguration.getTileWidth() * actualResolution;
		double worldTileHeight = tileConfiguration.getTileHeight() * actualResolution;

		Coordinate tileOrigin = tileConfiguration.getTileOrigin();
		int x = (int) Math.floor((location.getX() - tileOrigin.getX()) / worldTileWidth);
		int y = (int) Math.floor((location.getY() - tileOrigin.getY()) / worldTileHeight);

		return new TileCode(tileLevel, x, y);
	}
}
