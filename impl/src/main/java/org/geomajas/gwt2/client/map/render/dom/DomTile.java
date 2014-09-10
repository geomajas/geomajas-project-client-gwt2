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
import org.geomajas.gwt2.client.map.render.Tile;
import org.geomajas.gwt2.client.map.render.TileCode;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlImageImpl;
import org.geomajas.gwt2.client.map.render.dom.container.LoadableImage;

/**
 * Implementation of a {@link Tile} that has an image that can be lazy loaded at a later time.
 *
 * @author Youri Flement
 */
public class DomTile extends Tile implements LoadableTile {

	private LoadableImage<String, String> image;

	private transient Callback<String, String> callback;

	private static final int NR_RETRIES = 2;

	/**
	 * Constructor for serialization.
	 */
	public DomTile() {
	}

	/**
	 * Create a new tile with no bbox or tilecode. {@code onLoadingDone} is executed when the
	 * image is loaded through the {@link #load()} method.
	 *
	 * @param onLoadingDone The method to call when the image is loaded.
	 */
	public DomTile(Callback<String, String> onLoadingDone) {
		this(onLoadingDone, new TileCode(), new Bbox());
	}

	/**
	 * Create a new tile with the given tile code and bounding box. {@code onLoadingDone} is executed when the
	 * image is loaded through the {@link #load()} method.
	 *
	 * @param onLoadingDone The method to call when the image is loaded.
	 * @param code The tile code of the tile.
	 * @param screenBounds The screen bounds of the image of the tile.
	 */
	public DomTile(Callback<String, String> onLoadingDone, TileCode code, Bbox screenBounds) {
		this(onLoadingDone, code, screenBounds, new HtmlImageImpl(screenBounds));
	}

	/**
	 * Create a new tile with a given code and bbox. {@code onLoadingDone} is executed when the image
	 * is loaded through the {@link #load()} method.
	 *
	 * @param onLoadingDone The method to call when the image is loaded.
	 * @param code          The tile code of the tile.
	 * @param screenBounds  The screen bounds of the image of the tile.
	 */
	public DomTile(Callback<String, String> onLoadingDone, TileCode code, Bbox screenBounds,
			LoadableImage<String, String> image) {
		super(code, screenBounds);
		this.image = image;
		this.callback = onLoadingDone;
	}

	/**
	 * Load the tile. The URL to the image should have been set prior to calling this method.
	 */
	public void load() {
		if (url != null && !url.isEmpty()) {
			image.load(url, callback, NR_RETRIES);
		}
	}

	/**
	 * Get the underlying image of the dom tile.
	 *
	 * @return The image.
	 */
	public LoadableImage<String, String> getImage() {
		return image;
	}
}
