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

package org.geomajas.plugin.wms.client;

import com.google.gwt.core.client.Callback;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt2.client.map.Hint;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.plugin.wms.client.capabilities.WmsLayerInfo;
import org.geomajas.plugin.wms.client.layer.FeaturesSupportedWmsLayer;
import org.geomajas.plugin.wms.client.layer.FeaturesSupportedWmsLayerImpl;
import org.geomajas.plugin.wms.client.layer.WmsLayerConfiguration;
import org.geomajas.plugin.wms.client.service.WmsFeatureService;
import org.geomajas.plugin.wms.client.service.WmsFeatureServiceImpl;
import org.geomajas.plugin.wms.client.service.WmsService;
import org.geomajas.plugin.wms.server.command.dto.WfsDescribeLayerRequest;
import org.geomajas.plugin.wms.server.command.dto.WfsDescribeLayerResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Starting point for the WMS server extension. It provides additional functionality on top of the normal WMS client.
 *
 * @author Pieter De Graef
 */
public final class WmsServerExtension {

	/**
	 * Hint that determines the maximum number of coordinates feature geometries may contain when requested from a WMS
	 * GetFeatureInfo through GML.
	 */
	public static final Hint<Integer> GET_FEATUREINFO_MAX_COORDS = new Hint<Integer>("get_featureinfo_maxcoords");

	/**
	 * Hint that determines the maximum number of features a search on FeaturesSupported layer may contain.
	 */
	public static final Hint<Integer> GET_FEATUREINFO_MAX_FEATURES = new Hint<Integer>("get_featureinfo_maxfeatures");

	private static WmsServerExtension instance;

	private final WmsFeatureServiceImpl featureService;

	private final Map<Hint<?>, Object> hintValues;

	private WmsServerExtension() {
		this.hintValues = new HashMap<Hint<?>, Object>();
		this.featureService = new WmsFeatureServiceImpl();

		// Set the default maximum number of coordinates the features of a GetFeatureInfo should contain:
		setHintValue(GET_FEATUREINFO_MAX_COORDS, -1); // No maximum
		setHintValue(GET_FEATUREINFO_MAX_FEATURES, -1); // No maximum
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
	 * Find out if a certain WMS layer has support for features.
	 *
	 * @param baseUrl  The base URL to the WMS service.
	 * @param layerId  The name of the layer.
	 * @param callback Callback that will be given the answer through a boolean value. If true, it supports features. In
	 *                 that case, it's best to create a {@link FeaturesSupportedWmsLayer} for it. If it does not support
	 *                 features, better stick to the default {@link org.geomajas .plugin.wms.client.layer.WmsLayer}.
	 */
	public void supportsFeatures(String baseUrl, String layerId, final Callback<Boolean, String> callback) {
		GwtCommand command = new GwtCommand(WfsDescribeLayerRequest.COMMAND_NAME);
		command.setCommandRequest(new WfsDescribeLayerRequest(baseUrl, layerId));
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<WfsDescribeLayerResponse>() {

			@Override
			public void execute(WfsDescribeLayerResponse response) {
				callback.onSuccess(response.getAttributeDescriptors() != null && response.getAttributeDescriptors()
						.size() > 0);
			}
		});
	}

	/**
	 * <p>Create a new WMS layer. Use this method if you want to create a WMS layer from a GetCapabilities object you
	 * have just acquired.</p> <p>This layer does not support a GetFeatureInfo call! If you need that, you'll have to
	 * use the server extension of this plug-in.</p>
	 *
	 * @param baseUrl    The WMS base URL. This is the same URL you fed the GetCapabilities call.
	 * @param version    The WMS version.
	 * @param layerInfo  The layer info object. Acquired from a WMS GetCapabilities.
	 * @param viewPort   The ViewPort to take the CRS and fixed resolutions from.
	 * @param tileWidth  The tile width in pixels.
	 * @param tileHeight The tile height in pixels.
	 * @return A new WMS layer.
	 */
	public FeaturesSupportedWmsLayer createLayer(String baseUrl, WmsService.WmsVersion version, WmsLayerInfo layerInfo,
			ViewPort viewPort, int tileWidth, int tileHeight) {
		TileConfiguration tileConf = WmsClient.getInstance().createTileConfig(layerInfo, viewPort, tileWidth,
				tileHeight);
		WmsLayerConfiguration layerConf = WmsClient.getInstance().createLayerConfig(layerInfo, baseUrl, version);
		return createLayer(layerInfo.getTitle(), viewPort.getCrs(), tileConf, layerConf, layerInfo);
	}

	/**
	 * Create a new WMS layer. This layer extends the default {@link org.geomajas.plugin.wms.client.layer.WmsLayer} by
	 * supporting GetFeatureInfo calls.
	 *
	 * @param title       The layer title.
	 * @param crs         The CRS for this layer.
	 * @param tileConfig  The tile configuration object.
	 * @param layerConfig The layer configuration object.
	 * @param layerInfo   The layer info object. Acquired from a WMS GetCapabilities. This is optional.
	 * @return A new WMS layer.
	 */
	public FeaturesSupportedWmsLayer createLayer(String title, String crs,
			TileConfiguration tileConfig, WmsLayerConfiguration layerConfig, WmsLayerInfo layerInfo) {
		return new FeaturesSupportedWmsLayerImpl(title, crs, layerConfig, tileConfig, layerInfo);
	}

	/**
	 * Create a new WMS layer. This layer extends the default {@link org.geomajas.plugin.wms.client.layer.WmsLayer} by
	 * supporting GetFeatureInfo calls.
	 *
	 * @param title         The layer title.
	 * @param crs           The CRS for this layer.
	 * @param tileConfig    The tile configuration object.
	 * @param layerConfig   The layer configuration object.
	 * @param layerInfo     The layer info object. Acquired from a WMS GetCapabilities. This is optional.
	 * @param onInitialized Callback that is called when the layer has been initialized.
	 * @return A new WMS layer.
	 */
	public FeaturesSupportedWmsLayer createLayer(String title, String crs,
			TileConfiguration tileConfig, WmsLayerConfiguration layerConfig, WmsLayerInfo layerInfo,
			Callback<List<AttributeDescriptor>, String> onInitialized) {
		return new FeaturesSupportedWmsLayerImpl(title, crs, layerConfig, tileConfig, layerInfo, onInitialized);
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
