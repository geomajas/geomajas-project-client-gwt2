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

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.plugin.wms.client.capabilities.WmsLayerInfo;
import org.geomajas.plugin.wms.client.layer.WmsLayer;
import org.geomajas.plugin.wms.client.layer.WmsLayerConfiguration;
import org.geomajas.plugin.wms.client.layer.WmsLayerImpl;
import org.geomajas.plugin.wms.client.layer.WmsTileConfiguration;
import org.geomajas.plugin.wms.client.service.WmsService;
import org.geomajas.plugin.wms.client.service.WmsService.WmsVersion;
import org.geomajas.plugin.wms.client.service.WmsServiceImpl;

/**
 * Starting point for the WMS client plugin.
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public final class WmsClient {

	private static WmsClient instance;

	private final WmsService wmsService;

	private WmsClient() {
		wmsService = new WmsServiceImpl();
	}

	/**
	 * Get a singleton instance.
	 *
	 * @return Return WmsClient!
	 */
	public static WmsClient getInstance() {
		if (instance == null) {
			instance = new WmsClient();
		}
		return instance;
	}

	// ------------------------------------------------------------------------
	// WMS utility methods:
	// ------------------------------------------------------------------------

	/**
	 * <p>Create a new WMS layer. Use this method if you want to create a WMS layer from a GetCapabilities object you
	 * have just acquired.</p> <p>This layer does not support a GetFeatureInfo call! If you need that, you'll have to
	 * use the server extension of this plug-in.</p>
	 *
	 * @param baseUrl    The WMS base URL. This is the same URL you fed the GetCapabilities call. See {@link
	 *                   WmsService#getCapabilities(String, WmsVersion, com.google.gwt.core.client.Callback)}.
	 * @param version    The WMS version.
	 * @param layerInfo  The layer info object. Acquired from a WMS GetCapabilities.
	 * @param crs        The coordinate reference system to describe the configuration in.
	 * @param tileWidth  The tile width in pixels.
	 * @param tileHeight The tile height in pixels.
	 * @return A new WMS layer.
	 */
	public WmsLayer createLayer(String baseUrl, WmsVersion version, WmsLayerInfo layerInfo,
			String crs, int tileWidth, int tileHeight) {
		WmsTileConfiguration tileConf = createTileConfig(layerInfo, crs, tileWidth, tileHeight);
		WmsLayerConfiguration layerConf = createLayerConfig(layerInfo, baseUrl, version);
		return createLayer(layerInfo.getTitle(), tileConf, layerConf, layerInfo);
	}

	/**
	 * Create a new WMS layer. This layer does not support a GetFeatureInfo call! If you need that, you'll have to use
	 * the server extension of this plug-in.
	 *
	 * @param title       The layer title.
	 * @param tileConfig  The tile configuration object.
	 * @param layerConfig The layer configuration object.
	 * @param layerInfo   The layer info object. Acquired from a WMS GetCapabilities. This object is optional.
	 * @return A new WMS layer.
	 */
	public WmsLayer createLayer(String title, WmsTileConfiguration tileConfig, WmsLayerConfiguration layerConfig,
			WmsLayerInfo layerInfo) {
		return new WmsLayerImpl(title, layerConfig, tileConfig, layerInfo);
	}

	/**
	 * Create a new tile configuration object from a WmsLayerInfo object.
	 *
	 * @param layerInfo  The layer info object. Acquired from a WMS GetCapabilities.
	 * @param crs        The coordinate reference system to describe the configuration in.
	 * @param tileWidth  The tile width in pixels.
	 * @param tileHeight The tile height in pixels.
	 * @return Returns a tile configuration object.
	 * @throws IllegalArgumentException Throw when the CRS is not supported for this layerInfo object.
	 */
	public WmsTileConfiguration createTileConfig(WmsLayerInfo layerInfo, String crs, int tileWidth, int tileHeight)
			throws IllegalArgumentException {
		Bbox bbox = layerInfo.getBoundingBox(crs);
		if (bbox == null) {
			throw new IllegalArgumentException("Layer does not support map CRS (" + crs + ").");
		}
		Coordinate origin = new Coordinate(bbox.getX(), bbox.getY());
		return new WmsTileConfiguration(tileWidth, tileHeight, origin);
	}

	/**
	 * Create a WMS layer configuration object from a LayerInfo object acquired through a WMS GetCapabilities call.
	 *
	 * @param layerInfo The layer info object. Acquired from a WMS GetCapabilities.
	 * @param baseUrl   The WMS base URL. This is the same URL you fed the GetCapabilities call. See {@link
	 *                  WmsService#getCapabilities(String, WmsVersion, com.google.gwt.core.client.Callback)}.
	 * @param version   The WMS version.
	 * @return Returns the WMS layer configuration object.
	 */
	public WmsLayerConfiguration createLayerConfig(WmsLayerInfo layerInfo, String baseUrl, WmsVersion version) {
		WmsLayerConfiguration layerConfig = new WmsLayerConfiguration();
		layerConfig.setBaseUrl(baseUrl);
		layerConfig.setLayers(layerInfo.getName());
		layerConfig.setVersion(version);
		return layerConfig;
	}

	// ------------------------------------------------------------------------
	// Getting services:
	// ------------------------------------------------------------------------

	/**
	 * Get a service that is able to execute various WMS calls.
	 *
	 * @return The WMS service.
	 */
	public WmsService getWmsService() {
		return wmsService;
	}
}
