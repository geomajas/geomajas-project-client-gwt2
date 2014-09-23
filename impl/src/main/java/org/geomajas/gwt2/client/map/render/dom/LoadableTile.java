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

package org.geomajas.gwt2.client.map.render.dom;

import com.google.gwt.core.client.Callback;

import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.map.layer.tile.TileBasedLayer;
import org.geomajas.gwt2.client.map.render.TileCode;

/**
 * Interface for a {@link org.geomajas.gwt2.client.map.render.Tile} that is loadable.
 *
 * @author Youri Flement
 * @author Jan De Moerloose
 */
public interface LoadableTile {

	/**
	 * Load the tile.
	 * @param onLoadingDone
	 */
	void load(Callback<String, String> onLoadingDone);
	
	/**
	 * Cancels loading.
	 */
	void cancel();

	/**
	 * Is this tile canceled ?
	 * @return
	 */
	boolean isCancelled();

	/**
	 * Returns the unique code for this tile. Consider this it's unique identifier within a raster layer.
	 *
	 * @return Tile code.
	 */
	TileCode getCode();

	/**
	 * Return the URL to the actual image for this raster tile. It is that image that will really display the rendered
	 * tile.
	 *
	 * @return URL for the raster image.
	 */
	String getUrl();

	/**
	 * Returns the bounds for the image on the client side.
	 *
	 * @return Tile bounding box.
	 */
	Bbox getBounds();
	
	/**
	 * Is the tile loaded ?
	 * 
	 * @return true if loaded
	 */
	boolean isLoaded();

	/**
	 * Get a unique id for this tile.
	 */
	String getId();
	
	/**
	 * Get the layer associated with this tile.
	 * @return
	 */
	TileBasedLayer getLayer();
}
