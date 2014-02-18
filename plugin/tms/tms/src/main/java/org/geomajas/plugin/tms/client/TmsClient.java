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

package org.geomajas.plugin.tms.client;

import com.google.gwt.core.client.Callback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;
import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.plugin.tms.client.configuration.TileMapInfo;
import org.geomajas.plugin.tms.client.configuration.TileMapServiceInfo;
import org.geomajas.plugin.tms.client.configuration.v1_0_0.TileMapInfo100;
import org.geomajas.plugin.tms.client.configuration.v1_0_0.TileMapServiceInfo100;
import org.geomajas.plugin.tms.client.layer.TmsLayer;
import org.geomajas.plugin.tms.client.layer.TmsLayerConfiguration;

/**
 * Starting point for the TMS client plugin.
 *
 * @author Pieter De Graef
 * @since 2.1.0
 */
@Api(allMethods = true)
public final class TmsClient {

	private static TmsClient instance;

	private TmsClient() {
	}

	/**
	 * Get a singleton instance.
	 *
	 * @return Return WmsClient!
	 */
	public static TmsClient getInstance() {
		if (instance == null) {
			instance = new TmsClient();
		}
		return instance;
	}

	// ------------------------------------------------------------------------
	// TMS utility methods:
	// ------------------------------------------------------------------------

	/**
	 * Create a new TMS layer instance.
	 *
	 * @param id                 The unique layer ID.
	 * @param tileConfiguration  The tile configuration object.
	 * @param layerConfiguration The layer configuration object.
	 * @return A new TMS layer.
	 */
	public TmsLayer createLayer(String id, TileConfiguration tileConfiguration, TmsLayerConfiguration
			layerConfiguration) {
		return new TmsLayer(id, tileConfiguration, layerConfiguration);
	}

	/**
	 * Create a new TMS layer from a tile map info object.
	 *
	 * @param tileMapInfo The tile map object that is the result from parsing a TMS service XML.
	 * @return A new TMS layer.
	 */
	public TmsLayer createLayer(TileMapInfo tileMapInfo) {
		TileConfiguration tileConfiguration = new TileConfiguration(tileMapInfo.getTileFormat().getWidth(),
				tileMapInfo.getTileFormat().getHeight(), tileMapInfo.getOrigin());
		TmsLayerConfiguration layerConfiguration = new TmsLayerConfiguration();
		layerConfiguration.setBaseUrl(tileMapInfo.getHref());
		layerConfiguration.setFileExtension(tileMapInfo.getTileFormat().getExtension());
		return new TmsLayer(tileMapInfo.getTitle(), tileConfiguration, layerConfiguration);
	}

	/**
	 * Fetch the capabilities of a TileMapService and parse it. This is the base URL that contains a list of TileMaps.
	 *
	 * @param baseUrl  The URL that points to the TileMapService.
	 * @param callback The callback tat contains the parsed capabilities as a {@link org.geomajas.plugin.tms.client
	 *                 .configuration.TileMapServiceInfo} object.
	 */
	public void getTileMapService(final String baseUrl, final Callback<TileMapServiceInfo, String> callback) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, baseUrl);
		builder.setHeader("Cache-Control", "no-cache");
		builder.setHeader("Pragma", "no-cache");
		try {
			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable e) {
					callback.onFailure(e.getMessage());
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						Document messageDom = XMLParser.parse(response.getText());
						callback.onSuccess(new TileMapServiceInfo100(messageDom.getDocumentElement()));
					} else {
						callback.onFailure(response.getText());
					}
				}
			});
		} catch (RequestException e) {
			// Couldn't connect to server
			callback.onFailure(e.getMessage());
		}
	}

	/**
	 * Fetch the capabilities of a single TileMap configuration XML and parse it. A single TileMap can be used to create
	 * a {@link org.geomajas.plugin.tms.client.layer.TmsLayer}.
	 *
	 * @param baseUrl  The URL that points to the TileMap XML.
	 * @param callback The callback that contains the parsed capabilities as a {@link org.geomajas.plugin.tms.client
	 *                 .configuration.TileMapInfo} object.
	 */
	public void getTileMap(final String baseUrl, final Callback<TileMapInfo, String> callback) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, baseUrl);
		builder.setHeader("Cache-Control", "no-cache");
		builder.setHeader("Pragma", "no-cache");
		try {
			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable e) {
					callback.onFailure(e.getMessage());
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						Document messageDom = XMLParser.parse(response.getText());
						callback.onSuccess(new TileMapInfo100(messageDom.getDocumentElement()));
					} else {
						callback.onFailure(response.getText());
					}
				}
			});
		} catch (RequestException e) {
			// Couldn't connect to server
			callback.onFailure(e.getMessage());
		}
	}
}
