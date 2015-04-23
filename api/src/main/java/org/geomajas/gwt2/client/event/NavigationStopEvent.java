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
import org.geomajas.gwt2.client.map.View;

import com.google.web.bindery.event.shared.Event;

/**
 * Event that is fired when an animated navigation sequence in the {@link org.geomajas.puregwt.client.map.ViewPort} has
 * just ended.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public class NavigationStopEvent extends Event<NavigationStopHandler> {

	private final View view;

	/**
	 * Create a new event.
	 * 
	 * @param view
	 *            The final view of the navigation.
	 */
	public NavigationStopEvent(View view) {
		this.view = view;
	}

	// -------------------------------------------------------------------------
	// Event implementation:
	// -------------------------------------------------------------------------

	/**
	 * Get the event type.
	 * 
	 * @return The event type.
	 */
	public final Type<NavigationStopHandler> getAssociatedType() {
		return NavigationStopHandler.TYPE;
	}

	// ------------------------------------------------------------------------
	// Protected methods:
	// ------------------------------------------------------------------------

	protected void dispatch(NavigationStopHandler handler) {
		handler.onNavigationStopped(this);
	}

	/**
	 * Get the final view of the animation.
	 * 
	 * @return The final view. Also the current view on the map.
	 */
	public View getView() {
		return view;
	}
}