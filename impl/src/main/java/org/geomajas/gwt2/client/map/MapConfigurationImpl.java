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

package org.geomajas.gwt2.client.map;

import org.geomajas.geometry.Bbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default map configuration implementation.
 * 
 * @author Pieter De Graef
 */
public class MapConfigurationImpl implements MapConfiguration {

	public static final Integer DEFAULT_ANIMATION_TIME = 400;

	public static final Integer DEFAULT_FADE_IN_TIME = 250;

	public static final Boolean DEFAULT_ANIMATION_CANCEL_SUPPORT = false;

	/** This default DPI value equals the default as proposed in the WMS specification. */
	public static final Double DEFAULT_DPI = 90.714285714;

	public static final String DEFAULT_CRS = "EPSG:4326";

	public static final Bbox DEFAULT_BOUNDS = new Bbox(-180, 90, 360, 180);

	public static final Double DEFAULT_UNIT_LENGTH = 111319.4907932264;

	public static final Double DEFAULT_MINIMUM_RESOLUTION = Double.MAX_VALUE;

	private Map<Hint<?>, Object> hintValues;

	// ------------------------------------------------------------------------
	// Constructor:
	// ------------------------------------------------------------------------

	public MapConfigurationImpl() {
		hintValues = new HashMap<Hint<?>, Object>();

		// Now apply the default values:
		setHintValue(MapConfiguration.ANIMATION_TIME, DEFAULT_ANIMATION_TIME);
		setHintValue(MapConfiguration.FADE_IN_TIME, DEFAULT_FADE_IN_TIME);
		setHintValue(MapConfiguration.ANIMATION_CANCEL_SUPPORT, DEFAULT_ANIMATION_CANCEL_SUPPORT);
		setHintValue(MapConfiguration.DPI, DEFAULT_DPI);
		setHintValue(MapConfiguration.MAXIMUM_BOUNDS, DEFAULT_BOUNDS);
		setHintValue(MapConfiguration.INITIAL_BOUNDS, DEFAULT_BOUNDS);
		setHintValue(MapConfiguration.CRS, DEFAULT_CRS);
		setHintValue(MapConfiguration.UNIT_LENGTH, DEFAULT_UNIT_LENGTH);
		setHintValue(MapConfiguration.MINIMUM_RESOLUTION, DEFAULT_MINIMUM_RESOLUTION);
		setHintValue(MapConfiguration.RESOLUTIONS, new ArrayList<Double>());
	}

	// ------------------------------------------------------------------------
	// Working with map hints:
	// ------------------------------------------------------------------------

	@Override
	public <T> void setHintValue(Hint<T> hint, T value) {
		if (value == null) {
			throw new IllegalArgumentException("Null value passed.");
		}
		hintValues.put(hint, value);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getHintValue(Hint<T> hint) {
		return (T) hintValues.get(hint);
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	@Override
	public Bbox getMaxBounds() {
		return getHintValue(MapConfiguration.MAXIMUM_BOUNDS);
	}

	@Override
	public void setMaxBounds(Bbox maxBounds) {
		setHintValue(MapConfiguration.MAXIMUM_BOUNDS, maxBounds);
		if (getHintValue(MapConfiguration.INITIAL_BOUNDS) == null) {
			setHintValue(MapConfiguration.INITIAL_BOUNDS, maxBounds);
		}
	}

	@Override
	public String getCrs() {
		return getHintValue(MapConfiguration.CRS);
	}

	@Override
	public void setCrs(String crs, CrsType crsType) {
		setHintValue(MapConfiguration.CRS, crs);
		switch (crsType) {
			case DEGREES:
				setHintValue(MapConfiguration.UNIT_LENGTH, DEFAULT_UNIT_LENGTH);
				break;
			case METRIC:
				setHintValue(MapConfiguration.UNIT_LENGTH, 1.0);
				break;
			default:
				throw new IllegalArgumentException("When the CrsType is custom, please provide a 'unitLength'");
		}
	}

	@Override
	public void setCrs(String crs, double unitLength) {
		setHintValue(MapConfiguration.CRS, crs);
		setHintValue(MapConfiguration.UNIT_LENGTH, unitLength);
	}

	@Override
	public List<Double> getResolutions() {
		return getHintValue(MapConfiguration.RESOLUTIONS);
	}

	@Override
	public void setResolutions(List<Double> resolutions) {
		setHintValue(MapConfiguration.RESOLUTIONS, resolutions);
	}

	@Override
	public double getMinimumResolution() {
		return getHintValue(MapConfiguration.MINIMUM_RESOLUTION);
	}

	@Override
	public void setMinimumResolution(double maximumScale) {
		setHintValue(MapConfiguration.MINIMUM_RESOLUTION, maximumScale);
	}

	@Override
	public double getUnitLength() {
		return getHintValue(MapConfiguration.UNIT_LENGTH);
	}
}
