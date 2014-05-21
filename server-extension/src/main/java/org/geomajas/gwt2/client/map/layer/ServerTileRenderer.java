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

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.render.TileCode;
import org.geomajas.gwt2.client.map.render.TileRenderer;

/**
 * {@link TileRenderer} for server-side layers.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ServerTileRenderer implements TileRenderer {

	private final ServerLayerConfiguration layerConfiguration;

	/**
	 * This constructor used a {@link ServerLayerConfiguration} object to create the actual tile URLs.
	 * 
	 * @param layerConfiguration The server layer configuration object.
	 */
	protected ServerTileRenderer(ServerLayerConfiguration layerConfiguration) {
		this.layerConfiguration = layerConfiguration;
	}

	@Override
	public String getUrl(TileCode tileCode) {
		double resolution = layerConfiguration.getTileConfiguration().getResolution(tileCode.getTileLevel());
		Coordinate tileOrigin = layerConfiguration.getTileConfiguration().getTileOrigin();
		int tileWidth = layerConfiguration.getTileConfiguration().getTileWidth();
		int tileHeight = layerConfiguration.getTileConfiguration().getTileHeight();
		StringBuilder urlBuilder = new StringBuilder(layerConfiguration.getBaseUrl());
		urlBuilder.append(tileCode.getTileLevel());
		urlBuilder.append("/");
		urlBuilder.append(tileCode.getX());
		urlBuilder.append("/");
		urlBuilder.append(tileCode.getY());
		urlBuilder.append(layerConfiguration.getExtension());
		urlBuilder.append("?resolution=" + resolution);
		urlBuilder.append("&tileOrigin=" + tileOrigin.getX() + "," + tileOrigin.getY());
		urlBuilder.append("&tileWidth=" + tileWidth);
		urlBuilder.append("&tileHeight=" + tileHeight);
		return urlBuilder.toString();
	}
}
