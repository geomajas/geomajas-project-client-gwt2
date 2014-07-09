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

package org.geomajas.plugin.tms.client.layer;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.render.TileCode;
import org.geomajas.gwt2.client.map.render.TileRenderer;

/**
 * TileRenderer implementation that builds URLs for TMS layers.
 *
 * @author Pieter De Graef
 * @since 2.1.0
 */
@Api(allMethods = true)
public class TmsTileRenderer implements TileRenderer {

	private final TmsLayerConfiguration layerConfiguration;

	/**
	 * This constructor used a {@link org.geomajas.plugin.tms.client.layer.TmsLayerConfiguration} object to create the
	 * actual tile URLs.
	 *
	 * @param layerConfiguration The TMS layer configuration object.
	 */
	protected TmsTileRenderer(TmsLayerConfiguration layerConfiguration) {
		this.layerConfiguration = layerConfiguration;
	}

	@Override
	public String getUrl(TileCode tileCode) {
		StringBuilder urlBuilder = new StringBuilder(layerConfiguration.getBaseUrl());
		urlBuilder.append(tileCode.getTileLevel());
		urlBuilder.append("/");
		urlBuilder.append(tileCode.getX());
		urlBuilder.append("/");
		urlBuilder.append(tileCode.getY());
		urlBuilder.append(layerConfiguration.getFileExtension());
		return urlBuilder.toString();
	}
}
