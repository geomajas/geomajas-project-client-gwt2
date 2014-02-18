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

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of the {@link org.geomajas.gwt2.client.map.render.TileService}.
 *
 * @author Pieter De Graef
 */
public class TileServiceImpl implements TileService {

	private static TileService instance;

	public static TileService getInstance() {
		if (instance == null) {
			instance = new TileServiceImpl();
		}
		return instance;
	}

	@Override
	public List<TileCode> getTileCodesForBounds(ViewPort viewPort, TileConfiguration tileConfig, Bbox bounds,
			double scale) {
		List<TileCode> codes = new ArrayList<TileCode>();
		if (bounds.getHeight() == 0 || bounds.getWidth() == 0) {
			return codes;
		}

		int tileLevel = viewPort.getFixedScaleIndex(scale);
		double actualScale = viewPort.getFixedScale(tileLevel);

		double resolution = 1 / actualScale;
		double worldTileWidth = tileConfig.getTileWidth() * resolution;
		double worldTileHeight = tileConfig.getTileHeight() * resolution;

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

	@Override
	public Bbox getWorldBoundsForTile(ViewPort viewPort, TileConfiguration tileConfig, TileCode tileCode) {
		double resolution = 1 / viewPort.getFixedScale(tileCode.getTileLevel());
		double worldTileWidth = tileConfig.getTileWidth() * resolution;
		double worldTileHeight = tileConfig.getTileHeight() * resolution;

		double x = tileConfig.getTileOrigin().getX() + tileCode.getX() * worldTileWidth;
		double y = tileConfig.getTileOrigin().getY() + tileCode.getY() * worldTileHeight;
		return new Bbox(x, y, worldTileWidth, worldTileHeight);
	}

	@Override
	public TileCode getTileCodeForLocation(ViewPort viewPort, TileConfiguration tileConfig, Coordinate location,
			double scale) {
		int tileLevel = viewPort.getFixedScaleIndex(scale);
		double actualScale = viewPort.getFixedScale(tileLevel);
		double resolution = 1 / actualScale;
		double worldTileWidth = tileConfig.getTileWidth() * resolution;
		double worldTileHeight = tileConfig.getTileHeight() * resolution;

		Coordinate tileOrigin = tileConfig.getTileOrigin();
		int x = (int) Math.floor((location.getX() - tileOrigin.getX()) / worldTileWidth);
		int y = (int) Math.floor((location.getY() - tileOrigin.getY()) / worldTileHeight);

		return new TileCode(tileLevel, x, y);
	}
}