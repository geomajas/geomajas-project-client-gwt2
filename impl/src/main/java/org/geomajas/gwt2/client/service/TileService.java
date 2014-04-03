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
import org.geomajas.gwt2.client.map.HasResolutions;
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
	 * @param hasResolutions Basic layer or ViewPort that supports resolutions. This is used to determine the resolution
	 *                       at which the tileLevel in the TileCode lies.
	 * @param tileConfig     The basic tile configuration.
	 * @param bounds         The bounds in world space (map CRS).
	 * @param resolution     The resolution at which to search for tiles.
	 * @return A list of all tiles that lie within the location.
	 */
	public static List<TileCode> getTileCodesForBounds(HasResolutions hasResolutions, TileConfiguration tileConfig,
			Bbox bounds,
			double resolution) {
		List<TileCode> codes = new ArrayList<TileCode>();
		if (bounds.getHeight() == 0 || bounds.getWidth() == 0) {
			return codes;
		}

		int tileLevel = hasResolutions.getResolutionIndex(resolution);
		double actualResolution = hasResolutions.getResolution(tileLevel);

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
	 * @param hasResolutions Basic layer or ViewPort that supports resolutions. This is used to determine the resolution
	 *                       at which the tileLevel in the TileCode lies.
	 * @param tileConfig     The basic tile configuration.
	 * @param tileCode       The tile code.
	 * @return The tile bounds in map CRS.
	 */
	public static Bbox getWorldBoundsForTile(HasResolutions hasResolutions, TileConfiguration tileConfig,
			TileCode tileCode) {
		double resolution = hasResolutions.getResolution(tileCode.getTileLevel());
		double worldTileWidth = tileConfig.getTileWidth() * resolution;
		double worldTileHeight = tileConfig.getTileHeight() * resolution;

		double x = tileConfig.getTileOrigin().getX() + tileCode.getX() * worldTileWidth;
		double y = tileConfig.getTileOrigin().getY() + tileCode.getY() * worldTileHeight;
		return new Bbox(x, y, worldTileWidth, worldTileHeight);
	}

	/**
	 * Given a certain location at a certain scale, what tile lies there?
	 *
	 * @param hasResolutions Basic layer or ViewPort that supports resolutions. This is used to determine the resolution
	 *                       at which the tileLevel in the TileCode lies.
	 * @param tileConfig     The basic tile configuration.
	 * @param location       The location to retrieve a tile for.
	 * @param resolution     The resolution at which to retrieve a tile.
	 * @return Returns the tile code for the requested location.
	 */
	public static TileCode getTileCodeForLocation(HasResolutions hasResolutions, TileConfiguration tileConfig,
			Coordinate location, double resolution) {
		int tileLevel = hasResolutions.getResolutionIndex(resolution);
		double actualResolution = hasResolutions.getResolution(tileLevel);
		double worldTileWidth = tileConfig.getTileWidth() * actualResolution;
		double worldTileHeight = tileConfig.getTileHeight() * actualResolution;

		Coordinate tileOrigin = tileConfig.getTileOrigin();
		int x = (int) Math.floor((location.getX() - tileOrigin.getX()) / worldTileWidth);
		int y = (int) Math.floor((location.getY() - tileOrigin.getY()) / worldTileHeight);

		return new TileCode(tileLevel, x, y);
	}
}
