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

package org.geomajas.gwt2.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.animation.Trajectory;

import com.google.web.bindery.event.shared.Event;

/**
 * Event that is fired when the {@link org.geomajas.puregwt.client.map.ViewPort} starts an animated navigation sequence.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class NavigationStartEvent extends Event<NavigationStartHandler> {

	private final Trajectory trajectory;

	/**
	 * Create a new navigation start event and immediately provide the trajectory.
	 * 
	 * @param trajectory
	 *            The trajectory to follow during navigation.
	 */
	public NavigationStartEvent(Trajectory trajectory) {
		this.trajectory = trajectory;
	}

	// -------------------------------------------------------------------------
	// Event implementation:
	// -------------------------------------------------------------------------

	/**
	 * Get the event type.
	 * 
	 * @return The event type.
	 */
	public final Type<NavigationStartHandler> getAssociatedType() {
		return NavigationStartHandler.TYPE;
	}

	// ------------------------------------------------------------------------
	// Protected methods:
	// ------------------------------------------------------------------------

	protected void dispatch(NavigationStartHandler handler) {
		handler.onNavigationStarted(this);
	}

	/**
	 * Get the full trajectory for the animation this event is part of.
	 * 
	 * @return The full trajectory for the navigation animation.
	 */
	public Trajectory getTrajectory() {
		return trajectory;
	}
}