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

package org.geomajas.gwt2.plugin.tilebasedlayer.client.layer;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.layer.AbstractTileBasedLayer;
import org.geomajas.gwt2.client.map.layer.tile.TileBasedLayer;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.client.map.render.TileRenderer;

import java.util.Collection;

/**
 * Implementation of an OSM layer which is simply a
 * {@link org.geomajas.gwt2.client.map.layer.AbstractTileBasedLayer} with an {@link OsmTileRenderer}.
 * <p/>
 * When providing URLs for the tile renderer, you don't have to explicitly invert the Y-coordinate.
 *
 * @author Youri Flement
 * @since 2.1.0
 */
@Api(allMethods = true)
public class OsmLayer extends AbstractTileBasedLayer implements TileBasedLayer {

	private OsmTileRenderer renderer;

	/**
	 * Create a new layer that belongs to the given map model, using the given meta-data.
	 *
	 * @param id                The unique ID for this layer.
	 * @param tileConfiguration The configuration for the tiles.
	 */
	public OsmLayer(String id, TileConfiguration tileConfiguration) {
		super(id, tileConfiguration);
		renderer = new OsmTileRenderer();
	}

	/**
	 * Add an URL to the tile service.
	 *
	 * @param url The URL to the tile service.
	 */
	public void addUrl(String url) {
		if (url != null && !url.isEmpty()) {
			renderer.addUrl(url);
		}
	}

	/**
	 * Add a collection of URLs to tile services.
	 *
	 * @param urls The URLs to the tile service.
	 */
	public void addUrls(Collection<String> urls) {
		for (String url : urls) {
			addUrl(url);
		}
	}

	@Override
	public TileRenderer getTileRenderer() {
		return renderer;
	}
}
