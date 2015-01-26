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

package org.geomajas.gwt2.plugin.wms.client.controller;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.controller.AbstractMapController;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.plugin.wms.client.layer.FeatureInfoSupported;
import org.geomajas.gwt2.plugin.wms.client.service.WmsService.GetFeatureInfoFormat;

import com.google.gwt.core.client.Callback;
import com.google.gwt.event.dom.client.MouseUpEvent;

/**
 * Default map controller that executes WMS GetFeatureInfo requests on the registered layers.
 *
 * @author Pieter De Graef
 */
public class WmsGetFeatureInfoController extends AbstractMapController {

	private final List<FeatureInfoSupported> layers;

	private Callback<List<Feature>, String> featureCallback;

	private Callback<String, String> htmlCallback;

	private String format = GetFeatureInfoFormat.JSON.toString();

	private int maxCoordsPerFeature = -1;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a new GetFeatureInfoController. Don't forget to register layers.
	 */
	public WmsGetFeatureInfoController() {
		this(null);
	}

	/**
	 * Create a new GetFeatureInfoController.
	 *
	 * @param layer Immediately add a layer onto which to execute GetFeatureInfo requests.
	 */
	public WmsGetFeatureInfoController(FeatureInfoSupported layer) {
		super(false);
		this.layers = new ArrayList<FeatureInfoSupported>();
		if (layer != null) {
			addLayer(layer);
		}
	}

	// ------------------------------------------------------------------------
	// MapController implementation:
	// ------------------------------------------------------------------------

	/**
	 * Execute a GetFeatureInfo on mouse up.
	 */
	public void onMouseUp(MouseUpEvent event) {
		// Do not interfere with default behaviour:
		super.onMouseUp(event);

		// Get the event location in world space:
		Coordinate worldLocation = getLocation(event, RenderSpace.WORLD);

		// Now execute the GetFeatureInfo for each layer:
		for (FeatureInfoSupported layer : layers) {
			GetFeatureInfoFormat f = GetFeatureInfoFormat.fromFormat(format);
			if (f != null) {
				switch (f) {
					case GML2:
					case GML3:
					case JSON:
						if (featureCallback == null) {
							throw new IllegalStateException(
									"No callback has been set on the WmsGetFeatureInfoController");
						}
						layer.getFeatureInfo(worldLocation, format, featureCallback);
						break;
					case HTML:
					case TEXT:
					default:
						if (htmlCallback == null) {
							throw new IllegalStateException(
									"No callback has been set on the WmsGetFeatureInfoController");
						}
						htmlCallback.onSuccess(layer.getFeatureInfoUrl(worldLocation, format));
						break;

				}
			}
		}
	}

	/**
	 * Add a layer for which a GetFeatureRequest should be fired on click.
	 *
	 * @param layer The layer to add.
	 */
	public void addLayer(FeatureInfoSupported layer) {
		layers.add(layer);
	}

	/**
	 * Remove a layer for which a WmsGetFeatureInfoRequest should no longer be fired on click.
	 *
	 * @param layer The layer to remove again.
	 */
	public void removeLayer(FeatureInfoSupported layer) {
		layers.remove(layer);
	}

	/**
	 * Get the default GetFeatureInfoFormat. By default this is {@link GetFeatureInfoFormat#GML2}.
	 *
	 * @return the GetFeatureInfoFormat used.
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * Set a new GetFeatureInfoFormat to use in the WmsGetFeatureInfoRequest.
	 *
	 * @param format The new GetFeatureInfoFormat.
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * Set the callback to use in case the GetFeatureInfoFormat is {@link GetFeatureInfoFormat#GML2}.
	 *
	 * @param gmlCallback The callback to execute when the response returns. This response already contains a list of
	 *                    features, and should not be parsed anymore.
	 */
	public void setFeatureCallback(Callback<List<Feature>, String> featureCallback) {
		this.featureCallback = featureCallback;
	}

	/**
	 * Set the callback to use in case the GetFeatureInfoFormat is NOT {@link GetFeatureInfoFormat#GML2}.
	 *
	 * @param htmlCallback The callback to execute when the response returns. Note that the response is the bare boned
	 *                     WMS. GetFeatureInfo. It is up to you to parse it.
	 */
	public void setHtmlCallback(Callback<String, String> htmlCallback) {
		this.htmlCallback = htmlCallback;
	}

	/**
	 * Get the maximum number of coordinates geometries may contain. This is only applied when the GetFeatureInfo format
	 * is GML.
	 *
	 * @return The maximum number of coordinates per geometry.
	 */
	public int getMaxCoordsPerFeature() {
		return maxCoordsPerFeature;
	}

	/**
	 * Set the maximum number of coordinates geometries may contain. This is only applied when the GetFeatureInfo format
	 * is GML.
	 *
	 * @param maxCoordsPerFeature The maximum number of coordinates per geometry.
	 */
	public void setMaxCoordsPerFeature(int maxCoordsPerFeature) {
		this.maxCoordsPerFeature = maxCoordsPerFeature;
	}
}
