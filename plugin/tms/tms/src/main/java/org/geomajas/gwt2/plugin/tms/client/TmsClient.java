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

package org.geomajas.gwt2.plugin.tms.client;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.map.Hint;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapConfiguration.CrsType;
import org.geomajas.gwt2.client.map.MapConfigurationImpl;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.plugin.tms.client.configuration.TileMapInfo;
import org.geomajas.gwt2.plugin.tms.client.configuration.TileMapServiceInfo;
import org.geomajas.gwt2.plugin.tms.client.configuration.v1_0_0.TileMapInfo100;
import org.geomajas.gwt2.plugin.tms.client.configuration.v1_0_0.TileMapServiceInfo100;
import org.geomajas.gwt2.plugin.tms.client.layer.TmsLayer;
import org.geomajas.gwt2.plugin.tms.client.layer.TmsLayerConfiguration;

import com.google.gwt.core.client.Callback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

/**
 * Starting point for the TMS client plugin.
 *
 * @author Pieter De Graef
 * @since 2.1.0
 */
@Api(allMethods = true)
public final class TmsClient {
	
	private static final double MERCATOR = 20037508.342789244;
	
	/**
	 * The various TMS profiles.
	 * 
	 * @author Jan De Moerloose
	 *
	 */
	public enum Profile {
		/**
		 * Global mercator profile (EPSG:3857, initial world bounds, resolutions based on 256 x 256).
		 */
		GLOBAL_MERCATOR("global-mercator"),
		/**
		 * Global geodetic profile (EPSG:4326, initial world bounds, resolutions based on 256 x 256).
		 */
		GLOBAL_GEODETIC("global-geodetic"),
		/**
		 * Local profile (custom EPSG, initial custom bounds, resolutions based on 256 x ?).
		 */
		LOCAL("local");
		
		private String profile;

		private Profile(String profile) {
			this.profile = profile;
		}
		
		/**
		 * Get the official profile name.
		 * 
		 * @return
		 */
		public String getProfile() {
			return profile;
		}
	}
	
	/**
	 * A map hint for the TMS profile.
	 */
	public static final Hint<Profile> PROFILE = new Hint<Profile>("TMS profile");

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
	 * Create a map configuration with the global mercator profile and default zoom levels (21).
	 * 
	 * @return
	 */
	public MapConfiguration createMercatorMap() {
		return createTmsMap(Profile.GLOBAL_MERCATOR, 21);
	}

	/**
	 * Create a map configuration with the global mercator profile and a specified number of zoom levels.
	 * 
	 * @param nrOfZoomLevels
	 * @return
	 */
	public MapConfiguration createMercatorMap(int nrOfZoomLevels) {
		return createTmsMap(Profile.GLOBAL_MERCATOR, nrOfZoomLevels);
	}

	/**
	 * Create a map configuration with the global geodetic profile and default zoom levels (21).
	 * 
	 * @return
	 */
	public MapConfiguration createGeodeticMap() {
		return createTmsMap(Profile.GLOBAL_GEODETIC, 21);
	}

	/**
	 * Create a map configuration with the global geodetic profile and a specified number of zoom levels.
	 * 
	 * @param nrOfZoomLevels
	 * @return
	 */
	public MapConfiguration createGeodeticMap(int nrOfZoomLevels) {
		return createTmsMap(Profile.GLOBAL_GEODETIC, nrOfZoomLevels);
	}

	/**
	 * Create a map configuration with a local profile.
	 * 
	 * @return
	 */
	public MapConfiguration createLocalMap(String crs, CrsType type, Bbox bounds, int minTileSize, int nrOfZoomLevels) {
		MapConfiguration mapConfiguration = createTmsMap(crs, type, bounds, minTileSize, nrOfZoomLevels);
		mapConfiguration.setHintValue(PROFILE, Profile.LOCAL);
		return mapConfiguration;
	}
	
	
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
	 * Create a new TMS layer instance.
	 *
	 * @param id                 The unique layer ID.
	 * @param title              The title for this layer. It's the title that is usually used in the GUI as label.
	 * @param tileConfiguration  The tile configuration object.
	 * @param layerConfiguration The layer configuration object.
	 * @return A new TMS layer.
	 */
	public TmsLayer createLayer(String id, String title, TileConfiguration tileConfiguration, TmsLayerConfiguration
			layerConfiguration) {
		return new TmsLayer(id, title, tileConfiguration, layerConfiguration);
	}

	/**
	 * Create a new TMS layer from a tile map info object.
	 *
	 * @param tileMapInfo The tile map object that is the result from parsing a TMS service XML.
	 * @return A new TMS layer.
	 */
	public TmsLayer createLayer(TileMapInfo tileMapInfo) {
		return new TmsLayer(tileMapInfo);
	}

	/**
	 * Create a new TMS layer from a tile map info object.
	 *
	 * @param id The unique layer ID.
	 * @param tileMapInfo The tile map object that is the result from parsing a TMS service XML.
	 * @return A new TMS layer.
	 */
	public TmsLayer createLayer(String id, TileMapInfo tileMapInfo) {
		return new TmsLayer(tileMapInfo);
	}

	/**
	 * Fetch the capabilities of a TileMapService and parse it. This is the base URL that contains a list of TileMaps.
	 *
	 * @param baseUrl  The URL that points to the TileMapService.
	 * @param callback The callback tat contains the parsed capabilities as a {@link org.geomajas.gwt2.plugin.tms.client
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
	 * a {@link org.geomajas.gwt2.plugin.tms.client.layer.TmsLayer}.
	 *
	 * @param baseUrl  The URL that points to the TileMap XML.
	 * @param callback The callback that contains the parsed capabilities as a
	 * 		{@link org.geomajas.gwt2.plugin.tms.client.configuration.TileMapInfo} object.
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

	/**
	 * Create a map with one of the default profiles and a specific number of zoom levels (default = 21).
	 * 
	 * @param profile
	 * @param nrOfZoomLevels
	 * @return
	 */
	protected MapConfiguration createTmsMap(Profile profile, int nrOfZoomLevels) {
		MapConfiguration mapConfiguration = null;
		switch (profile) {
			case GLOBAL_GEODETIC:
				mapConfiguration = createTmsMap("EPSG:4326", CrsType.DEGREES, new Bbox(-180, -90, 360, 180), 256,
						nrOfZoomLevels);
				break;
			case GLOBAL_MERCATOR:
				mapConfiguration = createTmsMap("EPSG:3857", CrsType.METRIC, new Bbox(-MERCATOR, -MERCATOR,
						2 * MERCATOR, 2 * MERCATOR), 256, nrOfZoomLevels);
				break;
			default:
				throw new IllegalArgumentException("Local profiles not supported");
		}
		mapConfiguration.setHintValue(PROFILE, profile);
		return mapConfiguration;
	}

	/**
	 * Create a map with a local profile and specified crs, bounds and number of zoom levels. The resolution at level 0
	 * is based on mapping the bounds to a rectangular tile width minimum width and height of minTileSize pixels.
	 * 
	 * @param crs
	 * @param type
	 * @param bounds
	 * @param minTileSize
	 * @param nrOfZoomLevels
	 * @return
	 */
	protected MapConfiguration createTmsMap(String crs, CrsType type, Bbox bounds, int minTileSize,
			int nrOfZoomLevels) {
		MapConfigurationImpl mapConfiguration;
		mapConfiguration = new MapConfigurationImpl();
		mapConfiguration.setCrs(crs, type);
		double minSize = bounds.getWidth() >= bounds.getHeight() ? bounds.getHeight() : bounds.getWidth();
		List<Double> resolutions = new ArrayList<Double>();
		for (int i = 0; i < nrOfZoomLevels; i++) {
			resolutions.add(minSize / (minTileSize * Math.pow(2, i)));
		}
		mapConfiguration.setResolutions(resolutions);
		mapConfiguration.setMaxBounds(Bbox.ALL);
		mapConfiguration.setHintValue(MapConfiguration.INITIAL_BOUNDS, bounds);
		return mapConfiguration;
	}

}
