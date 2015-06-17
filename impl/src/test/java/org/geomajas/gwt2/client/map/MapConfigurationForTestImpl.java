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

package org.geomajas.gwt2.client.map;

import org.geomajas.geometry.Bbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Empty implementation of {@link MapConfiguration}.
 * Actually a copy of {@link MapConfigurationImpl}, without the default values.
 * 
 * @author Jan Venstermans
 */
public class MapConfigurationForTestImpl implements MapConfiguration {

	public static final Double DEFAULT_UNIT_LENGTH = 111319.4907932264;

	private Map<Hint<?>, Object> hintValues = new HashMap<Hint<?>, Object>();

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
