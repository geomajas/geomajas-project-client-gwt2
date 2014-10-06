package org.geomajas.gwt2.plugin.wms.client.layer;

import java.util.List;

import org.geomajas.geometry.Geometry;
import org.geomajas.gwt2.client.map.feature.Feature;

import com.google.gwt.core.client.Callback;

/**
 * Adds support for geometry-based feature search to layers.
 * 
 * @author Jan De Moerloose
 *
 */
public interface FeatureSearchSupported {

	void searchFeatures(Geometry geometry, Callback<List<Feature>, String> callback);

}
