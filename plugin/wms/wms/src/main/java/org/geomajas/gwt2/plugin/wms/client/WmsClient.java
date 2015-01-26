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

package org.geomajas.gwt2.plugin.wms.client;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsLayerInfo;
import org.geomajas.gwt2.plugin.wms.client.layer.FeatureInfoSupportedWmsLayer;
import org.geomajas.gwt2.plugin.wms.client.layer.WmsLayer;
import org.geomajas.gwt2.plugin.wms.client.layer.WmsLayerImpl;
import org.geomajas.gwt2.plugin.wms.client.service.WmsService;
import org.geomajas.gwt2.plugin.wms.client.service.WmsServiceImpl;
import org.geomajas.gwt2.plugin.wms.client.layer.WmsLayerConfiguration;

/**
 * Starting point for the WMS client plugin.
 *
 * @author Pieter De Graef
 * @since 2.1.0
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
	 *                   WmsService#getCapabilities(String,
	 *                   org.geomajas.gwt2.plugin.wms.client.service.WmsService.WmsVersion,
	 *                   com.google.gwt.core.client.Callback)}.
	 * @param version    The WMS version.
	 * @param layerInfo  The layer info object. Acquired from a WMS GetCapabilities.
	 * @param viewPort   The ViewPort to get the CRS and fixed resolutions from.
	 * @param tileWidth  The tile width in pixels.
	 * @param tileHeight The tile height in pixels.
	 * @return A new WMS layer.
	 */
	public WmsLayer createLayer(String baseUrl, WmsService.WmsVersion version, WmsLayerInfo layerInfo,
								ViewPort viewPort, int tileWidth, int tileHeight) {
		TileConfiguration tileConf = createTileConfig(layerInfo, viewPort, tileWidth, tileHeight);
		WmsLayerConfiguration layerConf = createLayerConfig(layerInfo, baseUrl, version);
		return createLayer(layerInfo.getTitle(), viewPort.getCrs(), tileConf, layerConf, layerInfo);
	}

	/**
	 * Create a new WMS layer. This layer does not support a GetFeatureInfo call! If you need that, you'll have to use
	 * the server extension of this plug-in.
	 *
	 * @param title       The layer title.
	 * @param crs         The CRS for this layer.
	 * @param tileConfig  The tile configuration object.
	 * @param layerConfig The layer configuration object.
	 * @param layerInfo   The layer info object. Acquired from a WMS GetCapabilities. This object is optional.
	 * @return A new WMS layer.
	 */
	public WmsLayer createLayer(String title, String crs, TileConfiguration tileConfig,
								WmsLayerConfiguration layerConfig, WmsLayerInfo layerInfo) {
		if (layerInfo == null || layerInfo.isQueryable()) {
			return new FeatureInfoSupportedWmsLayer(title, crs, layerConfig, tileConfig, layerInfo);
		} else {
			return new WmsLayerImpl(title, crs, layerConfig, tileConfig, layerInfo);
		}
	}

	/**
	 * Create a new tile configuration object from a WmsLayerInfo object.
	 *
	 * @param layerInfo  The layer info object. Acquired from a WMS GetCapabilities.
	 * @param viewPort   The ViewPort to get the CRS and fixed resolutions from.
	 * @param tileWidth  The tile width in pixels.
	 * @param tileHeight The tile height in pixels.
	 * @return Returns a tile configuration object.
	 * @throws IllegalArgumentException Throw when the CRS is not supported for this layerInfo object.
	 */
	public TileConfiguration createTileConfig(WmsLayerInfo layerInfo, ViewPort viewPort, int tileWidth, int tileHeight)
			throws IllegalArgumentException {
		Bbox bbox = layerInfo.getBoundingBox(viewPort.getCrs());
		if (bbox == null) {
			throw new IllegalArgumentException("Layer does not support map CRS (" + viewPort.getCrs() + ").");
		}
		Coordinate origin = new Coordinate(bbox.getX(), bbox.getY());
		return new TileConfiguration(tileWidth, tileHeight, origin, viewPort);
	}

	/**
	 * Create a WMS layer configuration object from a LayerInfo object acquired through a WMS GetCapabilities call.
	 *
	 * @param layerInfo The layer info object. Acquired from a WMS GetCapabilities.
	 * @param baseUrl   The WMS base URL. This is the same URL you fed the GetCapabilities call. See {@link
	 *                  WmsService#getCapabilities(String,
	 *                  org.geomajas.gwt2.plugin.wms.client.service.WmsService.WmsVersion,
	 *                  com.google.gwt.core.client.Callback)}.
	 * @param version   The WMS version.
	 * @return Returns the WMS layer configuration object.
	 */
	public WmsLayerConfiguration createLayerConfig(WmsLayerInfo layerInfo,
												   String baseUrl, WmsService.WmsVersion version) {
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
