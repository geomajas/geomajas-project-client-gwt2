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
import org.geomajas.annotation.UserImplemented;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event.Type;

/**
 * Event handler for catching animation stop events from the {@link org.geomajas.puregwt.client.map.ViewPort}. It means
 * that an animated navigation sequence has just ended.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface NavigationStopHandler extends EventHandler {

	/** Event handler type definition. */
	Type<NavigationStopHandler> TYPE = new Type<NavigationStopHandler>();

	/**
	 * Catches events where the {@link org.geomajas.puregwt.client.map.ViewPort} has just ended a navigation animation.
	 * 
	 * @param event
	 *            The actual {@link org.geomajas.gwt2.client.event.puregwt.client.event.NavigationStopEvent}.
	 */
	void onNavigationStopped(NavigationStopEvent event);
}