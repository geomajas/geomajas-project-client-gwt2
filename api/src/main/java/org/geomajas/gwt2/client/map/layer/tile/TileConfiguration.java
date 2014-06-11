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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.ViewPort;

/**
 * Basic configuration object for a tile based layer.
 *
 * @author Pieter De Graef
 * @since 2.1.0
 */
@Api(allMethods = true)
public class TileConfiguration implements Serializable {

	private static final long serialVersionUID = 210L;

	private final List<Double> resolutions = new ArrayList<Double>();

	private int tileWidth;

	private int tileHeight;

	private Coordinate tileOrigin;
	
	private Bbox maxBounds;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a new empty instance. Do not forget to apply all setters before using this object.
	 */
	public TileConfiguration() {
	}

	/**
	 * Create a new instance using, specifying all values.
	 *
	 * @param tileWidth   The width in pixels for image tiles.
	 * @param tileHeight  The height in pixels for image tiles.
	 * @param tileOrigin  The position in world space where tile (0,0) begins.
	 * @param resolutions The list of resolutions for this configuration object. Each should represent a tile level.
	 * @param maxBounds   The maximum bounds of the tile configuration (same for each tile level).
	 */
	public TileConfiguration(int tileWidth, int tileHeight, Coordinate tileOrigin, List<Double> resolutions, Bbox maxBounds) {
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.tileOrigin = tileOrigin;
		this.maxBounds = maxBounds;
		setResolutions(resolutions);
	}

	/**
	 * Create a new instance using, specifying all values.
	 *
	 * @param tileWidth  The width in pixels for image tiles.
	 * @param tileHeight The height in pixels for image tiles.
	 * @param tileOrigin The position in world space where tile (0,0) begins.
	 * @param viewPort   The ViewPort from which to take over the list of resolutions. Better make sure the map has
	 *                   already been initialized before calling this method. Otherwise the ViewPort won't have any
	 *                   resolutions.
	 * @param maxBounds  The maximum bounds of the tile configuration (same for each tile level).
	 */
	public TileConfiguration(int tileWidth, int tileHeight, Coordinate tileOrigin, ViewPort viewPort, Bbox maxBounds) {
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.tileOrigin = tileOrigin;
		this.maxBounds = maxBounds;

		List<Double> vpResolutions = new ArrayList<Double>(viewPort.getResolutionCount());
		for (int i = 0; i < viewPort.getResolutionCount(); i++) {
			vpResolutions.add(viewPort.getResolution(i));
		}
		setResolutions(vpResolutions);
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	/**
	 * Get the tile image width in pixels.
	 *
	 * @return The tile width.
	 */
	public int getTileWidth() {
		return tileWidth;
	}

	/**
	 * Set the tile image width in pixels.
	 *
	 * @param tileWidth The tile width.
	 */
	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}

	/**
	 * Get the tile image height in pixels.
	 *
	 * @return The tile height.
	 */
	public int getTileHeight() {
		return tileHeight;
	}

	/**
	 * Set the tile image height in pixels.
	 *
	 * @param tileHeight The tile height.
	 */
	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}

	/**
	 * Get the origin in world space for tile (0,0). Usually this is the lower-left corner for your layer.
	 *
	 * @return The tile origin in world space.
	 */
	public Coordinate getTileOrigin() {
		return tileOrigin;
	}

	/**
	 * Set the origin in world space for tile (0,0). Usually this is the lower-left corner for your layer.
	 *
	 * @param tileOrigin The tile origin in world space.
	 */
	public void setTileOrigin(Coordinate tileOrigin) {
		this.tileOrigin = tileOrigin;
	}

	/**
	 * Set the list of resolutions for this configuration object. Each should represent a tile level. Know that
	 * resolutions passed through this method will be ordered from large values to small values (from zoom out to zoom
	 * in).
	 *
	 * @param resolutions The new list of resolutions.
	 */
	public void setResolutions(List<Double> resolutions) {
		this.resolutions.clear();
		this.resolutions.addAll(resolutions);
		Collections.sort(this.resolutions, new Comparator<Double>() {

			@Override
			public int compare(Double o1, Double o2) {
				return o2.compareTo(o1);
			}
		});
	}

	// ------------------------------------------------------------------------
	// Utility methods:
	// ------------------------------------------------------------------------

	/**
	 * Get the total number of preferred fixed resolutions. These resolutions are used among others by the zooming
	 * controls on the map.
	 *
	 * @return The total number of fixed zoom resolutions, or -1 if no fixed list of scales is known.
	 */
	public int getResolutionCount() {
		return resolutions.size();
	}

	/**
	 * Get a preferred fixed resolution at a certain index.
	 *
	 * @param index The index to get a scale for. Index 0 means the maximum resolution (=zoomed out).
	 * @return Returns the preferred resolution.
	 */
	public double getResolution(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("Resolution cannot be found.");
		}
		if (index >= resolutions.size()) {
			throw new IllegalArgumentException("Resolution cannot be found.");
		}
		return resolutions.get(index);
	}

	/**
	 * Get the index for the fixed resolution that is closest to the provided resolution.
	 *
	 * @param resolution The resolution to request the closest fixed resolution level for.
	 * @return Returns the fixed resolution level index.
	 */
	public int getResolutionIndex(double resolution) {
		double maximumResolution = getMaximumResolution();
		if (resolution >= maximumResolution) {
			return 0;
		}
		double minimumResolution = getMinimumResolution();
		if (resolution <= minimumResolution) {
			return resolutions.size() - 1;
		}

		for (int i = 0; i < resolutions.size(); i++) {
			double upper = resolutions.get(i);
			double lower = resolutions.get(i + 1);
			if (resolution < upper && resolution >= lower) {
				if (Math.abs(upper - resolution) >= Math.abs(lower - resolution)) {
					return i + 1;
				} else {
					return i;
				}
			}
		}
		return 0;
	}

	/**
	 * Return the minimum allowed resolution. This means the maximum zoom out.
	 *
	 * @return The minimum allowed resolution.
	 */
	public double getMaximumResolution() {
		if (resolutions.size() == 0) {
			return Double.MAX_VALUE;
		}
		return resolutions.get(0);
	}

	/**
	 * Return the maximum allowed resolution. This means the maximum zoom in.
	 *
	 * @return The maximum allowed resolution.
	 */
	public double getMinimumResolution() {
		if (resolutions.size() == 0) {
			return 0;
		}
		return resolutions.get(resolutions.size() - 1);
	}
	
	/**
	 * Return the maximum bounds.
	 * 
	 * @return the maximum bounds
	 */
	public Bbox getMaxBounds() {
		return maxBounds;
	}

	/**
	 * Set the maximum bounds. Only tiles within the maximum bounds will be fetched. To avoid rounding errors, the
	 * center of the tile may be used for checking the condition.
	 * 
	 * @param maxBounds
	 */
	public void setMaxBounds(Bbox maxBounds) {
		this.maxBounds = maxBounds;
	}
	
}