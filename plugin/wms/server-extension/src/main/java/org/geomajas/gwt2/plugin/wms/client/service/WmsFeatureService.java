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

package org.geomajas.gwt2.plugin.wms.client.service;

import com.google.gwt.core.client.Callback;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.plugin.wms.client.layer.FeaturesSupportedWmsLayer;

import java.util.List;

/**
 * Client service that assists in performing requests to the WMS server.
 *
 * @author Pieter De Graef
 * @author An Buyle
 */
public interface WmsFeatureService extends WmsService {

	/**
	 * Execute a WMS GetFeatureInfo request. This request will use the format 'application/vnd.ogc.gml', so that the
	 * response can be parsed.
	 *
	 * @param viewPort The map ViewPort.
	 * @param layer    The {@link FeaturesSupportedWmsLayer} to search features for. Note that a normal {@link
	 *                 org.geomajas.gwt2.plugin.wms.client.layer.WmsLayer} is not enough.
	 *                 It must support the GetFeatureInfo request.
	 * @param location The location to search at. Must be in the map CRS.
	 * @param cb       The callback that will return a list containing features that have been found at the location.
	 *                 This can be an empty collection.
	 */
	void getFeatureInfo(ViewPort viewPort, FeaturesSupportedWmsLayer layer, Coordinate location, Callback<List<Feature>,
			String> cb);

	/**
	 * Execute a WMS GetFeatureInfo request.
	 *
	 * @param viewPort The map ViewPort.
	 * @param layer    The {@link FeaturesSupportedWmsLayer} to search features for. Note that a normal {@link
	 *                 org.geomajas.gwt2.plugin.wms.client.layer.WmsLayer} is not enough.
	 *                 It must support the GetFeatureInfo request.
	 * @param location The location to search at. Must be in the map CRS.
	 * @param format   The requested format for the response. Depending on this format, the callback will receive a
	 *                 different answer from the server.
	 * @param cb       The callback that will return the response from the WMS server. The type of object in the
	 *                 response, depends on the requested format. In case the format is GML, a list of features will be
	 *                 returned, otherwise, the result will be a string containing the HTTP body.
	 */
	void getFeatureInfo(ViewPort viewPort, FeaturesSupportedWmsLayer layer, Coordinate location,
			String format, Callback<Object, String> cb);
}
