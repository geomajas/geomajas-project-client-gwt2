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

package org.geomajas.gwt2.client.map.layer;

import com.google.gwt.http.client.URL;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.client.map.render.TileCode;
import org.geomajas.gwt2.client.map.render.TileRenderer;

/**
 * {@link TileRenderer} for server-side vector layers.
 *
 * @author Jan De Moerloose
 */
public class VectorServerTileRenderer implements TileRenderer {

	private final VectorServerLayer layer;

	private String baseUrl;

	private String extension;

	private TileConfiguration tileConfig;

	public VectorServerTileRenderer(VectorServerLayer layer, TileConfiguration tileConfig, String baseUrl,
			String extension) {
		this.layer = layer;
		this.baseUrl = baseUrl;
		this.extension = extension;
		this.tileConfig = tileConfig;
	}

	@Override
	public String getUrl(TileCode tileCode) {
		double resolution = tileConfig.getResolution(tileCode.getTileLevel());
		Coordinate tileOrigin = tileConfig.getTileOrigin();
		int tileWidth = tileConfig.getTileWidth();
		int tileHeight = tileConfig.getTileHeight();
		StringBuilder urlBuilder = new StringBuilder(baseUrl);
		urlBuilder.append(tileCode.getTileLevel());
		urlBuilder.append("/");
		urlBuilder.append(tileCode.getX());
		urlBuilder.append("/");
		urlBuilder.append(tileCode.getY());
		urlBuilder.append(extension);
		urlBuilder.append("?resolution=" + resolution);
		urlBuilder.append("&tileOrigin=" + tileOrigin.getX() + "," + tileOrigin.getY());
		urlBuilder.append("&tileWidth=" + tileWidth);
		urlBuilder.append("&tileHeight=" + tileHeight);
		urlBuilder.append("&showLabels=");
		urlBuilder.append(layer.isLabeled());
		if (layer.getFilter() != null) {
			urlBuilder.append("&filter=");
			urlBuilder.append(URL.encode(layer.getFilter()));
		}
		if (GwtCommandDispatcher.getInstance().getUserToken() != null) {
			urlBuilder.append("&userToken=");
			urlBuilder.append(GwtCommandDispatcher.getInstance().getUserToken());
		}
		return urlBuilder.toString();
	}
}
