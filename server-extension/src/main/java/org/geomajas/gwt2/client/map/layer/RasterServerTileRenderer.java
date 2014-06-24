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

package org.geomajas.gwt2.client.map.layer;

import org.geomajas.gwt2.client.map.render.TileCode;
import org.geomajas.gwt2.client.map.render.TileRenderer;

/**
 * {@link TileRenderer} for server-side raster layers.
 * 
 * @author Jan De Moerloose
 * 
 */
public class RasterServerTileRenderer implements TileRenderer {

	private String baseUrl;

	private String extension;

	public RasterServerTileRenderer(String baseUrl, String extension) {
		this.baseUrl = baseUrl;
		this.extension = extension;
	}

	@Override
	public String getUrl(TileCode tileCode) {
		StringBuilder urlBuilder = new StringBuilder(baseUrl);
		urlBuilder.append(tileCode.getTileLevel());
		urlBuilder.append("/");
		urlBuilder.append(tileCode.getX());
		urlBuilder.append("/");
		urlBuilder.append(tileCode.getY());
		urlBuilder.append(extension);
		return urlBuilder.toString();
	}

}
