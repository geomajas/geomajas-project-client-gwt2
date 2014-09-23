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
import org.geomajas.gwt2.client.map.render.Tile;
import org.geomajas.gwt2.client.map.render.TileCode;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlImage;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlImageImpl;

/**
 * Implementation of a {@link Tile} that has an image that can be lazy loaded at a later time.
 *
 * @author Youri Flement
 * @author Jan De Moerloose
 */
public class DomTile extends Tile implements LoadableTile {

	private HtmlImageImpl image;

	private TileBasedLayer layer;

	private static final int NR_RETRIES = 2;

	/**
	 * Create a new tile with a given code and bbox. {@code onLoadingDone} is executed when the image
	 * is loaded through the {@link #load()} method.
	 *
	 * @param onLoadingDone The method to call when the image is loaded.
	 * @param code          The tile code of the tile.
	 * @param screenBounds  The screen bounds of the image of the tile.
	 */
	public DomTile(TileBasedLayer layer, TileCode code, String src, Bbox screenBounds) {
		super(code, screenBounds);
		this.layer = layer;
		this.image = new HtmlImageImpl(src, screenBounds);
		setUrl(src);
	}

	/**
	 * Load the tile. The URL to the image should have been set prior to calling this method.
	 */
	public void load(Callback<String, String> onLoadingDone) {
		image.onLoadingDone(onLoadingDone, NR_RETRIES);
	}

	@Override
	public void cancel() {
		image.cancel();
	}
	
	

	@Override
	public boolean isCancelled() {
		return image.isCancelled();
	}

	/**
	 * Get the underlying image of the dom tile.
	 *
	 * @return The image.
	 */
	public HtmlImage getImage() {
		return image;
	}

	@Override
	public boolean isLoaded() {
		return image.isLoaded();
	}
	
	public TileBasedLayer getLayer() {
		return layer;
	}

	public String getId() {
		return getLayer().getId() + "#" + getCode().toString();
	}
	
}
