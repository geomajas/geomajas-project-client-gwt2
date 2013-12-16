/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.client.map;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;

/**
 * Configuration options for a map.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public class MapOptions {

	private Bbox initialBounds;

	private Bbox maxBounds;

	private String crs;

	private double unitLength = 1.0;

	private double maximumScale;

	private List<Double> resolutions = new ArrayList<Double>();

	/**
	 * Returns the startup bounds/extent of this map. Warning: the map's view will be fitted to the initial bounds, but
	 * the view's aspect ratio will not be affected !
	 * 
	 * @return the initial bounds
	 */
	public Bbox getInitialBounds() {
		return initialBounds;
	}

	/**
	 * Set the startup bounds/extent of this map. Warning: the map's view will be fitted to the initial bounds, but the
	 * view's aspect ratio will not be affected !
	 * 
	 * @param initialBounds
	 *            initial bounds
	 */
	public void setInitialBounds(Bbox initialBounds) {
		this.initialBounds = initialBounds;
	}

	/**
	 * Returns the maximum bounds/extent of this map.
	 * 
	 * @return the maximum bounds
	 */
	public Bbox getMaxBounds() {
		return maxBounds;
	}

	/**
	 * Sets the maximum bounds/extent of this map.
	 * 
	 * @param maxBounds
	 *            the maximum bounds
	 */
	public void setMaxBounds(Bbox maxBounds) {
		this.maxBounds = maxBounds;
		if (initialBounds == null) {
			initialBounds = new Bbox(maxBounds.getX(), maxBounds.getY(), maxBounds.getWidth(), maxBounds.getHeight());
		}
	}

	/**
	 * Get the coordinate reference system of this map (SRS notation).
	 * 
	 * @return the CRS (SRS notation)
	 */
	public String getCrs() {
		return crs;
	}

	/**
	 * Set the coordinate reference system of this map (SRS notation).
	 * 
	 * @param crs
	 *            the CRS (SRS notation)
	 */
	public void setCrs(String crs) {
		this.crs = crs;
	}

	/**
	 * Returns the list of resolutions (inverse scale values) allowed by this map. This determines the predefined scale
	 * levels at which this map will be shown. If this list is non-empty, the map will not adjust to arbitrary scale
	 * levels but will instead snap to one of the scale levels defined in this list when zooming.
	 * 
	 * @return a list of resolutions (unit/pixel or pure number if relative)
	 */
	public List<Double> getResolutions() {
		return resolutions;
	}

	/**
	 * Sets the list of resolutions (inverse scale values) allowed by this map. This determines the predefined scale
	 * levels at which this map will be shown. If this list is non-empty, the map will not adjust to arbitrary scale
	 * levels but will instead snap to one of the scale levels defined in this list when zooming.
	 * 
	 * @param resolutions
	 *            a list of resolutions (unit/pixel or pure number if relative)
	 */
	public void setResolutions(List<Double> resolutions) {
		this.resolutions = resolutions;
	}

	/**
	 * Returns the maximum scale (maximum zoom in) of this map. This value is only required if no resolutions are
	 * specified.
	 * 
	 * @return the maximum scale (pixels/unit)
	 */
	public double getMaximumScale() {
		return maximumScale;
	}

	/**
	 * Set the maximum scale (maximum zoom in) of this map. This value is only required if no resolutions are specified.
	 * 
	 * @param maximumScale
	 *            The maximum scale.
	 */
	public void setMaximumScale(double maximumScale) {
		this.maximumScale = maximumScale;
	}

	/**
	 * Get the unit length of this map in actual meters. This is an approximate value in the horizontal direction and in
	 * the initial center of the map.
	 * 
	 * @return unit length in m
	 */
	public double getUnitLength() {
		return unitLength;
	}

	/**
	 * Set the unit length of the map (auto-set by Spring).
	 * 
	 * @param unitLength
	 *            unit length in m
	 */
	public void setUnitLength(double unitLength) {
		this.unitLength = unitLength;
	}
}