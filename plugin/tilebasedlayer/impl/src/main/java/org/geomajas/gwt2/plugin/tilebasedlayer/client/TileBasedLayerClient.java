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

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.layer.AbstractTileBasedLayer;
import org.geomajas.gwt2.client.map.layer.tile.TileBasedLayer;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.client.map.render.TileRenderer;
import org.geomajas.gwt2.plugin.tilebasedlayer.client.layer.DefaultTileRenderer;
import org.geomajas.gwt2.plugin.tilebasedlayer.client.layer.OsmLayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	 * Create a new OSM layer with the given ID and tile configuration. The layer will be configured
	 * with the default OSM tile services so you don't have to specify these URLs yourself.
	 *
	 * @param id The unique ID of the layer.
	 * @param conf The tile configuration.
	 * @return A new OSM layer.
	 */
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
	 */
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
	 */
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
}
