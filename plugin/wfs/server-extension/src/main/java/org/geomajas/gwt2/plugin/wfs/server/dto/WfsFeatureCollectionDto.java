package org.geomajas.gwt2.plugin.wfs.server.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.layer.feature.Feature;

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
