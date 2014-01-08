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

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.animation.NavigationAnimation;

/**
 * <p>
 * Central view port definition that determines and influences that position of the map. It allows for zooming in and
 * out, translation, etc.<br/>
 * Note that all coordinates and bounding boxes must always be expressed in world space. See
 * {@link org.geomajas.gwt.client.map.RenderSpace} for more information.
 * </p>
 * <p>
 * Next to storing and changing the map location, implementation of this interface will also send out several types of
 * events that clearly define the changes in the view on the map.
 * </p>
 * 
 * @author Pieter De Graef
 * @author Oliver May
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface ViewPort {

	/**
	 * Get the maximum zooming extent that is allowed on this view port. These bounds are determined by the map
	 * configuration.
	 * 
	 * @return The maximum zooming extent that is allowed on this view port.
	 */
	Bbox getMaximumBounds();

	/**
	 * Return the minimum allowed scale. This means the maximum zoom out.
	 * 
	 * @return The minimum allowed scale.
	 */
	double getMinimumScale();

	/**
	 * Return the maximum allowed scale. This means the maximum zoom in.
	 * 
	 * @return The maximum allowed scale.
	 */
	double getMaximumScale();

	/**
	 * Returns a list of preferred fixed scale for the map to be in. These are typically the scales at which tiled
	 * layers will retrieve their tiles.
	 * 
	 * @return The list of preferred scales (zoom steps).
	 */

	/**
	 * Get the total number of preferred fixed scales. These scales are used among others by the zooming controls on the
	 * map.
	 * 
	 * @return The total number of fixed zoom scales, or -1 if no fixed list of scales is known.
	 */
	int getFixedScaleCount();

	/**
	 * Get a preferred fixed scale at a certain index.
	 * 
	 * @param index
	 *            The index to get a scale for. Index 0 means the minimum scale (=zoomed out).
	 * @return Returns the preferred scale.
	 */
	double getFixedScale(int index);

	/**
	 * Get the index for the fixed scale that is closest to the provided scale.
	 * 
	 * @param scale
	 *            The scale to request the closest fixed scale level for.
	 * @return Returns the fixed scale level index.
	 */
	int getFixedScaleIndex(double scale);

	/**
	 * Set the map's width and height in pixels. <code>ViewPort</code> implementations should pass these values to the
	 * {@link ZoomStrategy} they employ.
	 * 
	 * @param mapWidth
	 *            The current map width in pixels.
	 * @param mapHeight
	 *            The current map height in pixels.
	 */
	void setMapSize(int mapWidth, int mapHeight);

	/**
	 * Get the current map width in pixels.
	 * 
	 * @return The current map width in pixels.
	 */
	int getMapWidth();

	/**
	 * Get the current map height in pixels.
	 * 
	 * @return The current map height in pixels.
	 */
	int getMapHeight();

	/**
	 * Return the description of the coordinate reference system used in the map. Usually this value returns an EPSG
	 * code.
	 * 
	 * @return The CRS code. Example: 'EPSG:4326'.
	 */
	String getCrs();

	// -------------------------------------------------------------------------
	// Methods that retrieve what is visible on the map:
	// -------------------------------------------------------------------------

	/**
	 * Get the current center position expressed in world space.
	 * 
	 * @return The current center position expressed in world space.
	 */
	Coordinate getPosition();

	/**
	 * Return the current scale on the map.
	 */
	double getScale();

	/**
	 * Get the current view on the map.
	 * 
	 * @return Returns the current view on the map.
	 */
	View getView();

	/**
	 * Return the currently visible bounds on the map. These bounds are expressed in the CRS of the map.
	 * 
	 * @return Returns the maps bounding box.
	 */
	Bbox getBounds();

	// -------------------------------------------------------------------------
	// Methods that manipulate what is visible on the map:
	// -------------------------------------------------------------------------

	/**
	 * Register an animation to be executed as soon as possible. By default, animations registered here in IE8 will not
	 * actually run, but go directly to the final View.
	 * 
	 * @param animation
	 *            The navigation animation to run.
	 */
	void registerAnimation(NavigationAnimation animation);

	/**
	 * Re-centers the map to a new position while keeping the same scale.
	 * 
	 * @param coordinate
	 *            the new center position
	 */
	void applyPosition(Coordinate coordinate);

	/**
	 * Apply a new scale level on the map. This value needs to be between the minimum and maximum allowed scales, but is
	 * totally free otherwise. It will not be constrained to fixed resolutions.
	 * 
	 * @param scale
	 *            The preferred new scale.
	 */
	void applyScale(double scale);

	/**
	 * Apply a new scale level on the map. This value needs to be between the minimum and maximum allowed scales, but is
	 * totally free otherwise. It will not be constrained to fixed resolutions.
	 * 
	 * @param scale
	 *            The preferred new scale.
	 * @param zoomOption
	 *            An extra option that allows you to restrain the scaling.
	 */
	void applyScale(double scale, ZoomOption zoomOption);

	/**
	 * <p>
	 * Change the view on the map by applying a bounding box (world coordinates!). Since the width/height ratio of the
	 * bounding box may differ from that of the map, the fit is "as good as possible". This method is equivalent to
	 * {@link #applyBounds(Bbox, ZoomOption)} with ZoomOption.LEVEL_FIT.
	 * </p>
	 * <p>
	 * Also this function will almost certainly change the scale on the map, so if there have been resolutions defined,
	 * it will snap to them.
	 * </p>
	 * 
	 * @param bounds
	 *            A bounding box in world coordinates that determines the view from now on.
	 */
	void applyBounds(Bbox bounds);

	/**
	 * <p>
	 * Change the view on the map by applying a bounding box (world coordinates!). Since the width/height ratio of the
	 * bounding box may differ from that of the map, the fit is "as good as possible".
	 * </p>
	 * <p>
	 * Also this function will almost certainly change the scale on the map, so if there have been resolutions defined,
	 * it will snap to them.
	 * </p>
	 * 
	 * @param bounds
	 *            A bounding box in world coordinates that determines the view from now on.
	 * @param zoomOption
	 *            The way in which to zoom.
	 */
	void applyBounds(Bbox bounds, ZoomOption zoomOption);

	/**
	 * Apply a new view on the map.
	 * 
	 * @param view
	 *            The new view to apply on the map.
	 */
	void applyView(View view);

	/**
	 * Apply a new view on the map.
	 * 
	 * @param view
	 *            The new view to apply on the map.
	 */
	void applyView(View view, ZoomOption zoomOption);

	// ------------------------------------------------------------------------
	// ViewPort transformation methods:
	// ------------------------------------------------------------------------

	/**
	 * Get a transformation service that is capable of transforming geometric objects between different
	 * {@link org.geomajas.gwt.client.map.RenderSpace}s.
	 * 
	 * @return
	 */
	ViewPortTransformationService getTransformationService();

	/**
	 * Returns whether the view port is ready to be used. Mainly for internal use, normal clients should depend on map
	 * initialization.
	 * 
	 * @return true if ready to be used.
	 * @deprecated Do we still need this?
	 */
	@Deprecated
	boolean isInitialized();

	/**
	 * Transform a scale denominator (i.e. the number 5000 in scale 1:5000) into a usable scale for this view port.
	 * 
	 * @param scaleDenominator
	 *            The scale denominator.
	 * @return Returns a scale value for this view port.
	 */
	double toScale(double scaleDenominator);

	/**
	 * Transform a certain view into a bounding box.
	 * 
	 * @param view
	 *            A view on the map.
	 * @return The bounding box that relates to the provided view.
	 */
	Bbox asBounds(View view);

	/**
	 * Transform a bounding box into a view on the map.
	 * 
	 * @param bounds
	 *            The bounding box to request a view for.
	 * @param zoomOption
	 *            An option to constrain the returned view to fixed scale levels.
	 * @return The view that relates to the provided bounding box.
	 */
	View asView(Bbox bounds, ZoomOption zoomOption);
}