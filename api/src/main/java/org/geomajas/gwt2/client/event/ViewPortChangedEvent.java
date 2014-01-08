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

package org.geomajas.gwt2.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.animation.Trajectory;
import org.geomajas.gwt2.client.map.View;

import com.google.web.bindery.event.shared.Event;

/**
 * Event that is fired when the view on the {@link org.geomajas.gwt2.client.map.ViewPort} has been changed so that both
 * scaling and translation have occurred or the view has resized.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class ViewPortChangedEvent extends Event<ViewPortChangedHandler> {

	private final View from;

	private final View to;

	private final Trajectory trajectory;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------
	/**
	 * Create an event for the specified view port.
	 * 
	 * @param viewPort
	 *            the view port
	 */
	public ViewPortChangedEvent(View from, View to, Trajectory trajectory) {
		this.from = from;
		this.to = to;
		this.trajectory = trajectory;
	}

	// -------------------------------------------------------------------------
	// Event implementation:
	// -------------------------------------------------------------------------

	@Override
	public Type<ViewPortChangedHandler> getAssociatedType() {
		return ViewPortChangedHandler.TYPE;
	}

	// ------------------------------------------------------------------------
	// Protected methods:
	// ------------------------------------------------------------------------

	protected void dispatch(ViewPortChangedHandler handler) {
		handler.onViewPortChanged(this);
	}

	/**
	 * Get the previous view on the map.
	 * 
	 * @return The previous view on the map.
	 */
	public View getFrom() {
		return from;
	}

	/**
	 * Get the current view on the map.
	 * 
	 * @return The current view on the map.
	 */
	public View getTo() {
		return to;
	}

	/**
	 * If this ViewPort update is part of a navigation animation, this trajectory will describe it's course.
	 * 
	 * @return The trajectory this update is part of. This value may be null if this update is not part of a navigation
	 *         animation.
	 */
	public Trajectory getTrajectory() {
		return trajectory;
	}
}