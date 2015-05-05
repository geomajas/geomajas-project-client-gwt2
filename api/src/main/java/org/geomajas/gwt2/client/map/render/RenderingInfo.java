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

package org.geomajas.gwt2.client.map.render;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.animation.Trajectory;
import org.geomajas.gwt2.client.map.View;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * General information object for the {@link BasicRenderer}s to do their thing. It provides both essential and optional
 * parameters for a {@link BasicRenderer} to make use of. In the end, it is up to the {@link BasicRenderer} to decide
 * what to use and what not.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public class RenderingInfo {

	private final IsWidget widget;

	private final View view;

	private final Trajectory trajectory;
	
	private final boolean intermediate;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a new instance and immediately supply all required fields.
	 * 
	 * @param widget
	 *            The widget onto which the {@link BasicRenderer} is supposed to add it's rendering.
	 * @param view
	 *            The view on the map that needs to be displayed.
	 * @param trajectory
	 *            The expected trajectory, or null.
	 */
	public RenderingInfo(final IsWidget widget, final View view, final Trajectory trajectory, final boolean intermediate) {
		this.widget = widget;
		this.view = view;
		this.trajectory = trajectory;
		this.intermediate = intermediate;
	}

	// ------------------------------------------------------------------------
	// Getters:
	// ------------------------------------------------------------------------

	/**
	 * Get the widget onto which the {@link BasicRenderer} is supposed to add it's rendering.
	 * 
	 * @return The target widget. This can be a {@link org.geomajas.gwt2.client.gfx.HtmlContainer} or a Canvas, or....
	 */
	public IsWidget getWidget() {
		return widget;
	}

	/**
	 * Get the view on the map that needs to be displayed. The {@link BasicRenderer} will usually follow the position of
	 * the {@link org.geomajas.gwt2.client.map.ViewPort} and visualize it.
	 * 
	 * @return The view to visualize.
	 */
	public View getView() {
		return view;
	}

	/**
	 * If the request to render is part of a navigation animation, then this method will return the expected trajectory.
	 * Using this trajectory a {@link BasicRenderer} may try to prepare the views to come in order to provide a more
	 * smooth rendering.
	 * 
	 * @return The expected trajectory. This object may be null if the request to render is not part of a navigation
	 *         animation.
	 */
	public Trajectory getTrajectory() {
		return trajectory;
	}

	
	public boolean isIntermediate() {
		return intermediate;
	}
	
	
}