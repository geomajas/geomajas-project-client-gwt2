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

package org.geomajas.gwt2.plugin.wms.server.command.dto;

import org.geomajas.command.CommandRequest;

/**
 * Request for the GetFeatureInfoCommand. Should contain the actual request URL.
 *
 * @author Pieter De Graef
 */
public class WmsGetFeatureInfoRequest implements CommandRequest {

	private static final long serialVersionUID = 100L;

	public static final String COMMAND_NAME = "command.WmsGetFeatureInfo";

	private String url;

	private int maxCoordsPerFeature = -1;

	public WmsGetFeatureInfoRequest() {
	}

	public WmsGetFeatureInfoRequest(String url) {
		this.url = url;
	}

	public WmsGetFeatureInfoRequest(String url, int maxCoordsPerFeature) {
		this.url = url;
		this.maxCoordsPerFeature = maxCoordsPerFeature;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getMaxCoordsPerFeature() {
		return maxCoordsPerFeature;
	}

	public void setMaxCoordsPerFeature(int maxCoordsPerFeature) {
		this.maxCoordsPerFeature = maxCoordsPerFeature;
	}
}
