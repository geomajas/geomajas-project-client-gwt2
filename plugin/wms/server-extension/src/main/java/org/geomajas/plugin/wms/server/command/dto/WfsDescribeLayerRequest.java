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

package org.geomajas.plugin.wms.server.command.dto;

import org.geomajas.command.CommandRequest;

/**
 * Request for the GetFeatureInfoCommand. Should contain the actual request URL.
 *
 * @author Pieter De Graef
 */
public class WfsDescribeLayerRequest implements CommandRequest {

	private static final long serialVersionUID = 200L;

	public static final String COMMAND_NAME = "command.WfsDescribeLayer";

	private String baseUrl;

	private String layerName;

	public WfsDescribeLayerRequest() {
	}

	public WfsDescribeLayerRequest(String baseUrl, String layerName) {
		this.baseUrl = baseUrl;
		this.layerName = layerName;
	}

	public String getLayerName() {
		return layerName;
	}

	public void setLayerName(String layer) {
		this.layerName = layer;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
}
