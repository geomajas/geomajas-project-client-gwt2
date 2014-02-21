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

package org.geomajas.gwt2.client.map.layer.tile;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.layer.Layer;

import java.util.List;

/**
 * Base definition for a layer that uses tiles for it's rendering.
 *
 * @author Pieter De Graef
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface TileBasedLayer extends Layer {

	/**
	 * Get the tile configuration object associated with this layer.
	 *
	 * @return The tile configuration object.
	 */
	TileConfiguration getTileConfiguration();

	/**
	 * Get the scales for the tile levels. This list if ordered top-down.
	 *
	 * @return The list of scale levels.
	 */
	List<Double> getTileLevels();
}
