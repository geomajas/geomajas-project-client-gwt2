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

package org.geomajas.gwt2.plugin.tilebasedlayer.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapConfigurationImpl;
import org.geomajas.gwt2.client.map.MapConfiguration.CrsType;
import org.geomajas.gwt2.client.map.layer.AbstractTileBasedLayer;
import org.geomajas.gwt2.client.map.layer.tile.TileBasedLayer;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.client.map.render.TileRenderer;
import org.geomajas.gwt2.plugin.tilebasedlayer.client.layer.DefaultTileRenderer;
import org.geomajas.gwt2.plugin.tilebasedlayer.client.layer.OsmLayer;

/**
 * Starting point for the tile based layer plugin. Has helper methods to create
 * {@link TileBasedLayer}s and {@link OsmLayer}s.
 *
 * @author Youri Flement
 * @since 2.1.0
 */
@Api(allMethods = true)
public final class TileBasedLayerClient {

	private static TileBasedLayerClient instance;
	
	private static final double OSM_HALF_WIDTH = 20037508.342789244;

	private static final int OSM_TILE_SIZE = 256;

	private static final String OSM_EPSG = "EPSG:3857";

	private static final String[] DEFAULT_OSM_URLS = new String[] {
			"http://a.tile.openstreetmap.org/{z}/{x}/{y}.png",
			"http://b.tile.openstreetmap.org/{z}/{x}/{y}.png",
			"http://c.tile.openstreetmap.org/{z}/{x}/{y}.png",
	};

	private TileBasedLayerClient() {

	}

	/**
	 * Get a singleton instance.
	 *
	 * @return The tile based client.
	 */
	public static TileBasedLayerClient getInstance() {
		if (instance == null) {
			instance = new TileBasedLayerClient();
		}
		return instance;
	}

	/**
	 * Create a new tile based layer with an existing tile renderer.
	 *
	 * @param id           The unique ID of the layer.
	 * @param conf         The tile configuration.
	 * @param tileRenderer The tile renderer to use.
	 * @return A new tile based layer.
	 */
	public TileBasedLayer createLayer(String id, TileConfiguration conf, final TileRenderer tileRenderer) {
		return new AbstractTileBasedLayer(id, conf) {
			@Override
			public TileRenderer getTileRenderer() {
				return tileRenderer;
			}
		};
	}
	
	/**
	 * Create an OSM compliant map configuration with this number of zoom levels.
	 * 
	 * @param nrOfLevels
	 * @return
	 */
	public MapConfiguration createOsmMap(int nrOfLevels) {
		MapConfiguration configuration = new MapConfigurationImpl();
		Bbox bounds = new Bbox(-OSM_HALF_WIDTH, -OSM_HALF_WIDTH, 2 * OSM_HALF_WIDTH, 2 * OSM_HALF_WIDTH);
		configuration.setCrs(OSM_EPSG, CrsType.METRIC);
		configuration.setHintValue(MapConfiguration.INITIAL_BOUNDS, bounds);
		configuration.setMaxBounds(Bbox.ALL);
		List<Double> resolutions = new ArrayList<Double>();
		for (int i = 0; i < nrOfLevels; i++) {
			resolutions.add(OSM_HALF_WIDTH / (OSM_TILE_SIZE * Math.pow(2, i - 1)));
		}
		configuration.setResolutions(resolutions);
		return configuration;
	}
	
	/**
	 * Create a new OSM layer with the given ID and tile configuration. The layer will be configured
	 * with the default OSM tile services so you don't have to specify these URLs yourself.
	 *
	 * @param id The unique ID of the layer.
	 * @param conf The tile configuration.
	 * @return A new OSM layer.
	 * @since 2.2.1
	 */
	public OsmLayer createDefaultOsmLayer(String id, int nrOfLevels) {
		OsmLayer layer = new OsmLayer(id, createOsmTileConfiguration(nrOfLevels));
		layer.addUrls(Arrays.asList(DEFAULT_OSM_URLS));
		return layer;
	}

	/**
	 * Create a new OSM layer with the given ID and tile configuration. The layer will be configured
	 * with the default OSM tile services so you don't have to specify these URLs yourself.
	 *
	 * @param id The unique ID of the layer.
	 * @param conf The tile configuration.
	 * @return A new OSM layer.
	 * @deprecated use {@link #createDefaultOsmLayer(String, int)}
	 */
	@Deprecated
	public OsmLayer createDefaultOsmLayer(String id, TileConfiguration conf) {
		OsmLayer layer = new OsmLayer(id, conf);
		layer.addUrls(Arrays.asList(DEFAULT_OSM_URLS));
		return layer;
	}
	
	/**
	 * Create a new OSM layer with an URL to an OSM tile service.
	 * <p/>
	 * The URL should have placeholders for the x- and y-coordinate and tile level in the form of {x}, {y}, {z}.
	 * The file extension of the tiles should be part of the URL.
	 *
	 * @param id   The unique ID of the layer.
	 * @param conf The tile configuration.
	 * @param url  The URL to the tile service.
	 * @return A new OSM layer.
	 * @since 2.2.1
	 */
	public OsmLayer createOsmLayer(String id, int nrOfLevels, String url) {
		OsmLayer layer = new OsmLayer(id, createOsmTileConfiguration(nrOfLevels));
		layer.addUrl(url);
		return layer;
	}

	/**
	 * Create a new OSM layer with an URL to an OSM tile service.
	 * <p/>
	 * The URL should have placeholders for the x- and y-coordinate and tile level in the form of {x}, {y}, {z}.
	 * The file extension of the tiles should be part of the URL.
	 *
	 * @param id   The unique ID of the layer.
	 * @param conf The tile configuration.
	 * @param url  The URL to the tile service.
	 * @return A new OSM layer.
	 * @deprecated use {@link #createOsmLayer(String, int, String)}
	 */
	@Deprecated
	public OsmLayer createOsmLayer(String id, TileConfiguration conf, String url) {
		OsmLayer layer = new OsmLayer(id, conf);
		layer.addUrl(url);
		return layer;
	}

	/**
	 * Create a new OSM layer with URLs to OSM tile services.
	 * <p/>
	 * The URLs should have placeholders for the x- and y-coordinate and tile level in the form of {x}, {y}, {z}.
	 * The file extension of the tiles should be part of the URL.
	 *
	 * @param id   The unique ID of the layer.
	 * @param conf The tile configuration.
	 * @param urls The URL to the tile services.
	 * @return A new OSM layer.
	 * @since 2.2.1
	 */
	public OsmLayer createOsmLayer(String id, int nrOfLevels, String... urls) {
		OsmLayer layer = new OsmLayer(id, createOsmTileConfiguration(nrOfLevels));
		layer.addUrls(Arrays.asList(urls));
		return layer;
	}

	/**
	 * Create a new OSM layer with URLs to OSM tile services.
	 * <p/>
	 * The URLs should have placeholders for the x- and y-coordinate and tile level in the form of {x}, {y}, {z}.
	 * The file extension of the tiles should be part of the URL.
	 *
	 * @param id   The unique ID of the layer.
	 * @param conf The tile configuration.
	 * @param urls The URL to the tile services.
	 * @return A new OSM layer.
	 * @deprecated use {@link #createOsmLayer(String, int, String...)}
	 */
	@Deprecated
	public OsmLayer createOsmLayer(String id, TileConfiguration conf, List<String> urls) {
		OsmLayer layer = new OsmLayer(id, conf);
		layer.addUrls(urls);
		return layer;
	}

	/**
	 * Create a new tile based layer with an URL to a tile service.
	 * <p/>
	 * The URL should have placeholders for the x- and y-coordinate and tile level in the form of {x}, {y}, {z}.
	 * The Y-coordinate can be inverted by specifying {-y} instead of {y}.
	 * The file extension of the tiles should be part of the URL.
	 *
	 * @param id   The unique ID of the layer.
	 * @param conf The tile configuration.
	 * @param url  The URL to the tile service.
	 * @return A new tile based layer.
	 */
	public TileBasedLayer createLayer(String id, TileConfiguration conf, String url) {
		List<String> urls = new ArrayList<String>();
		urls.add(url);
		return createLayer(id, conf, urls);
	}

	/**
	 * Create a new tile based layer with URLs to tile services.
	 * <p/>
	 * The URLs should have placeholders for the x- and y-coordinate and tile level in the form of {x}, {y}, {z}.
	 * The Y-coordinate can be inverted by specifying {-y} instead of {y}.
	 * The file extension of the tiles should be part of the URL.
	 *
	 * @param id   The unique ID of the layer.
	 * @param conf The tile configuration.
	 * @param urls The URLs to the tile services.
	 * @return A new tile based layer.
	 */
	public TileBasedLayer createLayer(String id, TileConfiguration conf, final List<String> urls) {
		return new AbstractTileBasedLayer(id, conf) {
			@Override
			public TileRenderer getTileRenderer() {
				return new DefaultTileRenderer(urls);
			}
		};
	}
	
	private TileConfiguration createOsmTileConfiguration(int nrOfLevels) {
		Coordinate tileOrigin = new Coordinate(-OSM_HALF_WIDTH, -OSM_HALF_WIDTH);
		List<Double> resolutions = new ArrayList<Double>();
		for (int i = 0; i < nrOfLevels; i++) {
			resolutions.add(OSM_HALF_WIDTH / (OSM_TILE_SIZE * Math.pow(2, i - 1)));
		}
		TileConfiguration tileConfig = new TileConfiguration(OSM_TILE_SIZE, OSM_TILE_SIZE, tileOrigin, resolutions);
		return tileConfig;
	}

}
