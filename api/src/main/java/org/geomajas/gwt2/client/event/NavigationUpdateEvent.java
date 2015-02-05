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

package org.geomajas.gwt2.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.animation.Trajectory;
import org.geomajas.gwt2.client.map.View;

import com.google.web.bindery.event.shared.Event;

/**
 * Event that is fired when the {@link org.geomajas.puregwt.client.map.ViewPort} reports an update in the navigation
 * animation.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public class NavigationUpdateEvent extends Event<NavigationUpdateHandler> {

	private final View view;

	private final Trajectory trajectory;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * Create a navigation update event.
	 */
	public NavigationUpdateEvent(Trajectory trajectory, View view) {
		this.trajectory = trajectory;
		this.view = view;
	}

	// -------------------------------------------------------------------------
	// Event implementation:
	// -------------------------------------------------------------------------

	/**
	 * The event type.
	 * 
	 * @return The event type.
	 */
	public final Type<NavigationUpdateHandler> getAssociatedType() {
		return NavigationUpdateHandler.TYPE;
	}

	/**
	 * Get the full trajectory for the animation this event is part of.
	 * 
	 * @return The full trajectory for the navigation animation.
	 */
	public Trajectory getTrajectory() {
		return trajectory;
	}

	/**
	 * Return the current view.
	 * 
	 * @return The current view.
	 */
	public View getView() {
		return view;
	}

	// ------------------------------------------------------------------------
	// Protected methods:
	// ------------------------------------------------------------------------

	protected void dispatch(NavigationUpdateHandler handler) {
		handler.onNavigationUpdated(this);
	}
}