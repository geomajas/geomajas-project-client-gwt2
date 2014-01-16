/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.wms.client;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.Hint;
import org.geomajas.plugin.wms.client.layer.FeaturesSupportedWmsLayer;
import org.geomajas.plugin.wms.client.layer.FeaturesSupportedWmsLayerImpl;
import org.geomajas.plugin.wms.client.layer.config.WmsLayerConfiguration;
import org.geomajas.plugin.wms.client.layer.config.WmsTileConfiguration;
import org.geomajas.plugin.wms.client.service.WmsFeatureService;
import org.geomajas.plugin.wms.client.service.WmsFeatureServiceImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Starting point for the WMS server extension. It provides additional functionality on top of the normal WMS client.
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public final class WmsServerExtension {

	/**
	 * Hint that determines the maximum number of coordinates feature geometries may contain when requested from a WMS
	 * GetFeatureInfo through GML.
	 */
	public static final Hint<Integer> GET_FEATUREINFO_MAX_COORDS = new Hint<Integer>("get_featureinfo_maxcoords");

	private static WmsServerExtension instance;

	private final WmsFeatureServiceImpl featureService;

	private final Map<Hint<?>, Object> hintValues;

	private WmsServerExtension() {
		this.hintValues = new HashMap<Hint<?>, Object>();
		this.featureService = new WmsFeatureServiceImpl();
	}

	/**
	 * Get a singleton instance.
	 *
	 * @return The singleton instance.
	 */
	public static WmsServerExtension getInstance() {
		if (instance == null) {
			instance = new WmsServerExtension();
		}
		return instance;
	}

	/**
	 * Create a new WMS layer. This layer extends the default {@link org.geomajas.plugin.wms.client.layer.WmsLayer} by
	 * supporting GetFeatureInfo calls.
	 *
	 * @param title       The layer title.
	 * @param tileConfig  The tile configuration object.
	 * @param layerConfig The layer configuration object.
	 * @return A new WMS layer.
	 */
	public FeaturesSupportedWmsLayer createFeatureSupportedLayer(String title, WmsTileConfiguration tileConfig,
			WmsLayerConfiguration layerConfig) {
		return new FeaturesSupportedWmsLayerImpl(title, layerConfig, tileConfig);
	}

	/**
	 * Get a service for building WMS requests related to features (GetFeatureInfo).
	 *
	 * @return the service.
	 */
	public WmsFeatureService getFeatureService() {
		return featureService;
	}

	/**
	 * Apply a new value for a specific WMS hint.
	 *
	 * @param hint  The hint to change the value for.
	 * @param value The new actual value. If the value is null, an IllegalArgumentException is thrown.
	 */
	public <T> void setHintValue(Hint<T> hint, T value) {
		if (value == null) {
			throw new IllegalArgumentException("Null value passed.");
		}
		hintValues.put(hint, value);
	}

	/**
	 * Get the value for a specific WMS hint. All hints have a default value, so this method will never return
	 * <code>null</code>.
	 *
	 * @param hint The hint to retrieve the current value for.
	 * @return The map hint value.
	 */
	public <T> T getHintValue(Hint<T> hint) {
		return (T) hintValues.get(hint);
	}
}
