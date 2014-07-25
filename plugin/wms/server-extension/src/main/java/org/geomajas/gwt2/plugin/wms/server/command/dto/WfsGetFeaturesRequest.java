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
import org.geomajas.geometry.Geometry;

/**
 * Request for the {@link org.geomajas.gwt2.plugin.wms.server.command.WfsGetFeaturesCommand}.
 * Should contain all the info needed to create an actual WFS request.
 *
 * @author Pieter De Graef
 * @author An Buyle
 */
public class WfsGetFeaturesRequest implements CommandRequest {

	private static final long serialVersionUID = 200L;

	public static final String COMMAND_NAME = "command.WfsGetFeatures";

	private String layer;

	private Geometry location;

	private String baseUrl;

	private int maxNumOfFeatures = 1000;

	private int maxCoordsPerFeature = -1;

	public WfsGetFeaturesRequest() {
	}

	public WfsGetFeaturesRequest(String baseUrl, String layer, Geometry location) {
		this.baseUrl = baseUrl;
		this.layer = layer;
		this.location = location;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getLayer() {
		return layer;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}

	public Geometry getLocation() {
		return location;
	}

	public void setLocation(Geometry location) {
		this.location = location;
	}

	public int getMaxNumOfFeatures() {
		return maxNumOfFeatures;
	}

	public void setMaxNumOfFeatures(int maxNumOfFeatures) {
		this.maxNumOfFeatures = maxNumOfFeatures;
	}

	public int getMaxCoordsPerFeature() {
		return maxCoordsPerFeature;
	}

	public void setMaxCoordsPerFeature(int maxCoordsPerFeature) {
		this.maxCoordsPerFeature = maxCoordsPerFeature;
	}
}