package org.geomajas.gwt2.plugin.wfs.client.protocol;

import java.io.Serializable;
import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.map.feature.Feature;


public interface WfsFeatureCollectionInfo extends Serializable {

	String getBaseUrl();

	String getTypeName();

	List<Feature> getFeatures();
	
	Bbox getBoundingBox();
}
