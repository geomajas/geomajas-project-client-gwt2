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

package org.geomajas.gwt2.plugin.wfs.server.command.dto;

import java.util.List;

import org.geomajas.command.CommandResponse;
import org.geomajas.geometry.Bbox;
import org.geomajas.layer.feature.Feature;

/**
 * Response for the GetFeatureInfo command. It returns the features found.
 *
 * @author Pieter De Graef
 */
public class WfsGetFeatureResponse extends CommandResponse {

	private static final long serialVersionUID = 1L;

	private List<Feature> features;

	private Bbox totalBounds;

	public WfsGetFeatureResponse() {
	}

	public WfsGetFeatureResponse(List<Feature> features) {
		this.features = features;
	}

	public List<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}

	public Bbox getTotalBounds() {
		return totalBounds;
	}

	public void setTotalBounds(Bbox totalBounds) {
		this.totalBounds = totalBounds;
	}

}