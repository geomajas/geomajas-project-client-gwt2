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
package org.geomajas.gwt2.plugin.wfs.server.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.layer.feature.Feature;

/**
 * A collection of features that results from a WFS GetFeature request.
 * 
 * @author Jan De Moerloose
 *
 */
public class WfsFeatureCollectionDto implements Serializable {

	private static final long serialVersionUID = 100L;

	private String baseUrl;

	private String typeName;

	private List<Feature> features = new ArrayList<Feature>();

	private Bbox boundingBox;

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public List<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}

	public Bbox getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(Bbox boundingBox) {
		this.boundingBox = boundingBox;
	}

	public int size() {
		return features.size();
	}

}
