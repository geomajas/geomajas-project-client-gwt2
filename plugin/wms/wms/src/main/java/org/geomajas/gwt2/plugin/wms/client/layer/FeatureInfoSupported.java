// Copyright (C) 2010-2014 DOV, http://dov.vlaanderen.be/
// All rights reserved
package org.geomajas.gwt2.plugin.wms.client.layer;

import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.feature.Feature;

import com.google.gwt.core.client.Callback;

/**
 * Implemented by WMS layer that has feature info support. Makes {@link FeaturesSupportedWmsLayer} obsolete.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface FeatureInfoSupported {

	/**
	 * Execute a WMS GetFeatureInfo request.
	 * 
	 * @param location The location in world space to get information for.
	 * @param callback The callback that is executed when the response returns. If features are found at the requested
	 *        location, they will be returned here.
	 */
	void getFeatureInfo(Coordinate location, Callback<List<Feature>, String> callback);

	/**
	 * Execute a WMS GetFeatureInfo request.
	 * 
	 * @param location The location in world space to get information for.
	 * @param format The GetFeatureInfo format for the response.
	 * @param callback The callback that is executed when the response returns. If features are found at the requested
	 *        location, they will be returned here. Note that the callback returns a string on success. It is up to you
	 *        to parse this.
	 */
	void getFeatureInfo(Coordinate location, String format, Callback<List<Feature>, String> callback);

	/**
	 * Execute a WMS GetFeatureInfo request.
	 * 
	 * @param location The location in world space to get information for.
	 * @param format The GetFeatureInfo format for the response.
	 */
	String getFeatureInfoUrl(Coordinate location, String format);

}
