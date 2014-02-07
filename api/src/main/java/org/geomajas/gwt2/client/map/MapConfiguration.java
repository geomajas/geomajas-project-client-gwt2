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

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;

import java.util.List;

/**
 * Map configuration definition. Contains a server configuration object and a series of map hints to apply specific
 * parameters. One should define a {@link Hint} constant for each configuration parameter.
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public interface MapConfiguration {

	/**
	 * Defines the type of coordinate reference system used on the map. Most coordinate system are expressed in degrees
	 * or meters.
	 *
	 * @author Pieter De Graef
	 */
	public enum CrsType {
		/**
		 * Defines a coordinate system of which the units are expressed in meters, such as Mercator.
		 */
		METRIC,

		/**
		 * Defines a coordinate system of which the units are expressed in degrees, such as EPSG:4326.
		 */
		DEGREES
	}

	// ------------------------------------------------------------------------
	// List of known map hints:
	// ------------------------------------------------------------------------

	/**
	 * {@link Hint} used to determine how long the animations should take during navigation (zooming). The value should
	 * be expressed in milliseconds. It's value should be of type <code>Long</code>.
	 *
	 * @since 2.0.0
	 */
	Hint<Integer> ANIMATION_TIME = new Hint<Integer>("animation_time");

	/**
	 * {@link Hint} used to determine how long fading in of scale or tiles should take while rendering the map.
	 *
	 * @since 2.0.0
	 */
	Hint<Integer> FADE_IN_TIME = new Hint<Integer>("fade_in_time");

	/**
	 * {@link Hint} that determines whether or not the {@link ViewPort} will cancel the currently running animation in
	 * favor of a new animation. If this value is false, the new animation will keep running, and the new animation is
	 * discarded. If this value is true, the current animation is discontinued, and the new one is started.
	 *
	 * @since 2.0.0
	 */
	Hint<Boolean> ANIMATION_CANCEL_SUPPORT = new Hint<Boolean>("animation_cancel_support");

	/**
	 * {@link Hint} used to determine the DPI on the map.
	 *
	 * @since 2.0.0
	 */
	Hint<Double> DPI = new Hint<Double>("dpi");

	/**
	 * {@link Hint} that determines the maximum extent of the map. It is not possible to navigate outside of this
	 * region. Do not change this value once the map has been initialized.
	 *
	 * @since 2.0.0
	 */
	Hint<Bbox> MAXIMUM_BOUNDS = new Hint<Bbox>("maximum_bounds");

	/**
	 * {@link Hint} that determines the initial extent of the map. This is the extent shown when the map is first
	 * loaded. If no initial bounds is set, the map will take the maximum bounds as initial bounds.  Do not change this
	 * value once the map has been initialized.
	 *
	 * @since 2.0.0
	 */
	Hint<Bbox> INITIAL_BOUNDS = new Hint<Bbox>("initial_bounds");

	/**
	 * {@link Hint} that determines the map Coordinate Reference System. The default value is EPSG:4326.  Do not change
	 * this value once the map has been initialized.
	 *
	 * @since 2.0.0
	 */
	Hint<String> CRS = new Hint<String>("crs");

	/**
	 * The length if a map unit, expressed in meter. This is an approximate value in the horizontal direction and in the
	 * initial center of the map. This value depends on the CRS. Most reference systems are expressed in either meters
	 * or degrees. For those the convenience method {@link #setCrs(String, CrsType)} is recommended.  Do not change this
	 * value once the map has been initialized.
	 *
	 * @since 2.0.0
	 */
	Hint<Double> UNIT_LENGTH = new Hint<Double>("unit_length");

	/**
	 * {@link Hint} that determines the maximum scale (maximum zoom in). This value is not required if a list of
	 * resolutions is passed.  Do not change this value once the map has been initialized.
	 *
	 * @@since 2.0.0
	 */
	Hint<Double> MAXIMUM_SCALE = new Hint<Double>("maximum_scale");

	/**
	 * {@link Hint} that determines the set of resolutions used as fixed scales in the {@link ViewPort}. This list is
	 * not required if a maximum scale value has been set. Do not change this value once the map has been initialized.
	 *
	 * @since 2.0.0
	 */
	Hint<List<Double>> RESOLUTIONS = new Hint<List<Double>>("resolutions");

	// ------------------------------------------------------------------------
	// Working with map hints:
	// ------------------------------------------------------------------------

	/**
	 * Apply a new value for a specific map hint.
	 *
	 * @param hint  The hint to change the value for.
	 * @param value The new actual value. If the value is null, an IllegalArgumentException is thrown.
	 */
	<T> void setHintValue(Hint<T> hint, T value);

	/**
	 * Get the value for a specific map hint. All hints have a default value, so this method will never return
	 * <code>null</code>.
	 *
	 * @param hint The hint to retrieve the current value for.
	 * @return The map hint value.
	 */
	<T> T getHintValue(Hint<T> hint);

	// ------------------------------------------------------------------------
	// Convenience methods:
	// ------------------------------------------------------------------------

	/**
	 * Convenience method that returns the maximum bounds/extent of this map.
	 *
	 * @return the maximum bounds
	 */
	Bbox getMaxBounds();

	/**
	 * Convenience method that sets the maximum bounds/extent of this map.  Do not change this value once the map has
	 * been initialized.
	 *
	 * @param maxBounds the maximum bounds
	 */
	void setMaxBounds(Bbox maxBounds);

	/**
	 * Convenience method that gets the coordinate reference system of this map (SRS notation).
	 *
	 * @return the CRS (SRS notation)
	 */
	String getCrs();

	/**
	 * Convenience method that sets the coordinate reference system of this map (SRS notation).  Do not change this
	 * value once the map has been initialized.
	 *
	 * @param crs     the CRS (SRS notation)
	 * @param crsType The type of CRS. Use this method if the units of the CRS are expressed in either meters or
	 *                degrees. Otherwise use the {@link #setCrs(String, double)} method.
	 */
	void setCrs(String crs, CrsType crsType);

	/**
	 * Convenience method that sets the coordinate reference system of this map (SRS notation).  Do not change this
	 * value once the map has been initialized.
	 *
	 * @param crs        the CRS (SRS notation)
	 * @param unitLength The length of a single unit of this map in actual meters. This is an approximate value in the
	 *                   horizontal direction and in the initial center of the map. If the units of your CRS are
	 *                   expressed in either meters or degrees, you can use the convenience method {@link
	 *                   #setCrs(String, CrsType)}.
	 */
	void setCrs(String crs, double unitLength);

	/**
	 * Convenience method that returns the list of resolutions (inverse scale values) allowed by this map. This
	 * determines the predefined scale levels at which this map will be shown. If this list is non-empty, the map will
	 * not adjust to arbitrary scale levels but will instead snap to one of the scale levels defined in this list when
	 * zooming.
	 *
	 * @return a list of resolutions (unit/pixel or pure number if relative)
	 */
	List<Double> getResolutions();

	/**
	 * Convenience method that sets the list of resolutions (inverse scale values) allowed by this map. This determines
	 * the predefined scale levels at which this map will be shown. If this list is non-empty, the map will not adjust
	 * to arbitrary scale levels but will instead snap to one of the scale levels defined in this list when zooming. Do
	 * not change this value once the map has been initialized.
	 *
	 * @param resolutions a list of resolutions (unit/pixel or pure number if relative)
	 */
	void setResolutions(List<Double> resolutions);

	/**
	 * Convenience method that returns the maximum scale (maximum zoom in) of this map. This value is only required if
	 * no resolutions are specified.
	 *
	 * @return the maximum scale (pixels/unit)
	 */
	double getMaximumScale();

	/**
	 * Convenience method that sets the maximum scale (maximum zoom in) of this map. This value is only required if no
	 * resolutions are specified. Do not change this value once the map has been initialized.
	 *
	 * @param maximumScale The maximum scale.
	 */
	void setMaximumScale(double maximumScale);

	/**
	 * Convenience method that gets the unit length of this map in actual meters. This is an approximate value in the
	 * horizontal direction and in the initial center of the map.
	 *
	 * @return unit length in m.
	 */
	double getUnitLength();
}
