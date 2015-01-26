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

package org.geomajas.gwt2.plugin.wms.server.command.dto;

import org.geomajas.command.CommandResponse;
import org.geomajas.layer.feature.Feature;

import java.util.List;

/**
 * Response for the GetFeatureInfo command. It returns the features found.
 *
 * @author Pieter De Graef
 */
public class WfsGetFeaturesResponse extends CommandResponse {

	private static final long serialVersionUID = 1L;

	private List<Feature> features;

	public WfsGetFeaturesResponse() {
	}

	public WfsGetFeaturesResponse(List<Feature> features) {
		this.features = features;
	}

	public List<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}
}