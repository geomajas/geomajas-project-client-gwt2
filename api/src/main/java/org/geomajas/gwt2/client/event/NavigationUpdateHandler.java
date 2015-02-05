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
import org.geomajas.annotation.UserImplemented;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event.Type;

/**
 * Event handler for catching animation progress events during an animation within the
 * {@link org.geomajas.puregwt.client.map.ViewPort}.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface NavigationUpdateHandler extends EventHandler {

	/** Event handler type definition. */
	Type<NavigationUpdateHandler> TYPE = new Type<NavigationUpdateHandler>();

	/**
	 * Catches progress events in the navigation animation of a {@link org.geomajas.puregwt.client.map.ViewPort}.
	 * 
	 * @param event
	 *            The actual {@link org.geomajas.gwt2.client.event.puregwt.client.event.NavigationUpdateEvent}.
	 */
	void onNavigationUpdated(NavigationUpdateEvent event);
}