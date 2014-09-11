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

package org.geomajas.gwt2.plugin.tilebasedlayer.client.layer;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.render.TileCode;
import org.geomajas.gwt2.client.map.render.TileRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * TileRenderer implementation that builds URLs for tile based layers. By default the
 * renderer uses Round-Robin to determine the next URL to fetch tiles from and by default the
 * Y-coordinates are not inverted.
 * <p/>
 * All URLs for the renderer should have placeholders in the form of {x}, {y} and {z} for the x-coordinate,
 * y-coordinate and tile level respectively. To invert the Y-coordinate {-y} instead of {y} can be supplied.
 *
 * @author Youri Flement
 * @since 2.1.0
 */
@Api(allMethods = true)
public class DefaultTileRenderer implements TileRenderer {

	private List<String> urls;

	private int currentIndex;

	/**
	 * Create a new default tile renderer with an URL to a tile service.
	 *
	 * @param url The URL to the tile service.
	 */
	public DefaultTileRenderer(String url) {
		this(new ArrayList<String>());
		urls.add(url);
	}

	/**
	 * Create a new default tile renderer with URLs to various tile services.
	 *
	 * @param urls The URLs to the tile services.
	 */
	public DefaultTileRenderer(List<String> urls) {
		this.urls = urls;
		currentIndex = 0;
	}

	/**
	 * Get the URL that points to the image representing the given tile code.
	 *
	 * @param tileCode The tile code to fetch an image for.
	 * @return The image URL.
	 */
	@Override
	public String getUrl(TileCode tileCode) {
		int y = isYAxisInverted() ? invertedY(tileCode) : tileCode.getY();
		return fillPlaceHolders(getNextUrl(), tileCode.getX(), y, tileCode.getTileLevel());
	}

	/**
	 * Add an URL to a tile service.
	 * <p/>
	 * The URL should have placeholders for the x- and y-coordinate and tile level in the form of {x}, {y}, {z}.
	 * The Y-coordinate can be inverted by specifying {-y} instead of {y}.
	 * The file extension of the tiles should be part of the URL.
	 *
	 * @param url The URL to the tile service.
	 */
	public void addUrl(String url) {
		urls.add(url);
	}

	/**
	 * Remove an URL to a tile service.
	 *
	 * @param url The URL to the tile service.
	 * @return <code>true</code> if the URL was found and removed and <code>false</code> otherwise.
	 */
	public boolean removeUrl(String url) {
		return urls.remove(url);
	}

	/**
	 * Check whether the Y-coordinates in this renderer are inverted or not.
	 * Returns <code>false</code> if there were no URLs configured.
	 *
	 * @return <code>true</code> if the Y-coordinates are inverted and <code>false</code> otherwise.
	 */
	public boolean isYAxisInverted() {
		if (urls.isEmpty()) {
			return false;
		}
		return urls.get(0).contains("{-y}");
	}

	/**
	 * Fill in the {x}, {y} and {z} placeholders in the URL.
	 *
	 * @param url The URL with placeholders.
	 * @param x   The x-coordinate to the tile.
	 * @param y   The y-coordinate to the tile.
	 * @param z   The tile level.
	 * @return The URL with filled placeholders.
	 */
	protected String fillPlaceHolders(String url, int x, int y, int z) {
		url = url.replace("{x}", Integer.toString(x));
		url = url.replace("{y}", Integer.toString(y));
		url = url.replace("{-y}", Integer.toString(y));
		return url.replace("{z}", Integer.toString(z));
	}

	/**
	 * We use Round-Robin to determine the next URL to fetch tiles from.
	 *
	 * @return The URL to fetch tiles from.
	 */
	protected String getNextUrl() {
		String url = urls.get(currentIndex);
		currentIndex = (currentIndex + 1) % urls.size();
		return url;
	}

	/**
	 * Invert the TMS Y-ordinate.
	 *
	 * @param code The tilecode of the tile.
	 * @return The inverted Y-ordinate.
	 */
	protected int invertedY(TileCode code) {
		return (int) Math.pow(2, code.getTileLevel()) - code.getY() - 1;
	}
}
