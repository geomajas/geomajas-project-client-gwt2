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

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a {@link org.geomajas.gwt2.client.map.render.TileRenderer} for OSM layers.
 * The tile renderer accepts URLs with placeholders and automatically inverts Y-coordinates.
 *
 * @author Youri Flement
 * @since 2.1.0
 */
@Api(allMethods = true)
public class OsmTileRenderer extends DefaultTileRenderer {

	/**
	 * Create a new OSM tile renderer with no URL to a tile service.
	 */
	public OsmTileRenderer() {
		this(new ArrayList<String>());
	}

	/**
	 * Create a new OSM tile renderer with an URL to a tile service. The URL should have
	 * placeholders for the x- and y-coordinate and tile level in the form of {x}, {y}, {z}.
	 * The extension of the tile should also be part of the URL.
	 *
	 * @param url The URL to the OSM tile service.
	 */
	public OsmTileRenderer(String url) {
		super(url);
	}

	/**
	 * Create a new OSM tile renderer with URL to tile services. The URLs should have
	 * placeholders for the x- and y-coordinate and tile level in the form of {x}, {y}, {z}.
	 * The extension of the tile should also be part of the URL.
	 *
	 * @param urls The URLs to the OSM tile services.
	 */
	public OsmTileRenderer(List<String> urls) {
		super(urls);
	}

	@Override
	public boolean isYAxisInverted() {
		return true;
	}

}
