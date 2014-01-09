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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.geometry.Bbox;

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

	public static final Double DEFAULT_MAXIMUM_SCALE = 1024.0;

	private Map<MapHint<?>, Object> hintValues;

	// ------------------------------------------------------------------------
	// Constructor:
	// ------------------------------------------------------------------------

	public MapConfigurationImpl() {
		hintValues = new HashMap<MapHint<?>, Object>();

		// Now apply the default values:
		setMapHintValue(MapConfiguration.ANIMATION_TIME, DEFAULT_ANIMATION_TIME);
		setMapHintValue(MapConfiguration.FADE_IN_TIME, DEFAULT_FADE_IN_TIME);
		setMapHintValue(MapConfiguration.ANIMATION_CANCEL_SUPPORT, DEFAULT_ANIMATION_CANCEL_SUPPORT);
		setMapHintValue(MapConfiguration.DPI, DEFAULT_DPI);
		setMapHintValue(MapConfiguration.MAXIMUM_BOUNDS, DEFAULT_BOUNDS);
		setMapHintValue(MapConfiguration.INITIAL_BOUNDS, DEFAULT_BOUNDS);
		setMapHintValue(MapConfiguration.CRS, DEFAULT_CRS);
		setMapHintValue(MapConfiguration.UNIT_LENGTH, DEFAULT_UNIT_LENGTH);
		setMapHintValue(MapConfiguration.MAXIMUM_SCALE, DEFAULT_MAXIMUM_SCALE);
		setMapHintValue(MapConfiguration.RESOLUTIONS, new ArrayList<Double>());
	}

	// ------------------------------------------------------------------------
	// Working with map hints:
	// ------------------------------------------------------------------------

	@Override
	public <T> void setMapHintValue(MapHint<T> hint, T value) {
		if (value == null) {
			throw new IllegalArgumentException("Null value passed.");
		}
		hintValues.put(hint, value);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getMapHintValue(MapHint<T> hint) {
		return (T) hintValues.get(hint);
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	@Override
	public Bbox getMaxBounds() {
		return getMapHintValue(MapConfiguration.MAXIMUM_BOUNDS);
	}

	@Override
	public void setMaxBounds(Bbox maxBounds) {
		setMapHintValue(MapConfiguration.MAXIMUM_BOUNDS, maxBounds);
		if (getMapHintValue(MapConfiguration.INITIAL_BOUNDS) == null) {
			setMapHintValue(MapConfiguration.INITIAL_BOUNDS, maxBounds);
		}
	}

	@Override
	public String getCrs() {
		return getMapHintValue(MapConfiguration.CRS);
	}

	@Override
	public void setCrs(String crs, CrsType crsType) {
		setMapHintValue(MapConfiguration.CRS, crs);
		switch (crsType) {
			case DEGREES:
				setMapHintValue(MapConfiguration.UNIT_LENGTH, DEFAULT_UNIT_LENGTH);
				break;
			case METRIC:
				setMapHintValue(MapConfiguration.UNIT_LENGTH, 1.0);
				break;
			default:
				throw new IllegalArgumentException("When the CrsType is custom, please provide a 'unitLength'");
		}
	}

	@Override
	public void setCrs(String crs, double unitLength) {
		setMapHintValue(MapConfiguration.CRS, crs);
		setMapHintValue(MapConfiguration.UNIT_LENGTH, unitLength);
	}

	@Override
	public List<Double> getResolutions() {
		return getMapHintValue(MapConfiguration.RESOLUTIONS);
	}

	@Override
	public void setResolutions(List<Double> resolutions) {
		setMapHintValue(MapConfiguration.RESOLUTIONS, resolutions);
	}

	@Override
	public double getMaximumScale() {
		return getMapHintValue(MapConfiguration.MAXIMUM_SCALE);
	}

	@Override
	public void setMaximumScale(double maximumScale) {
		setMapHintValue(MapConfiguration.MAXIMUM_SCALE, maximumScale);
	}

	@Override
	public double getUnitLength() {
		return getMapHintValue(MapConfiguration.UNIT_LENGTH);
	}
}