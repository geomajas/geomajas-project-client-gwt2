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

import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;

/**
 * Base URL and extension for consistency with other layers like WMS, TMS.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ServerLayerConfiguration {

	private String baseUrl;

	private String extension;

	private TileConfiguration tileConfiguration ;

	public ServerLayerConfiguration(String baseUrl, String extension, TileConfiguration tileConfiguration) {
		this.baseUrl = baseUrl;
		this.extension = extension;
		this.tileConfiguration = tileConfiguration;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public String getExtension() {
		return extension;
	}
	
	public TileConfiguration getTileConfiguration() {
		return tileConfiguration;
	}

}
