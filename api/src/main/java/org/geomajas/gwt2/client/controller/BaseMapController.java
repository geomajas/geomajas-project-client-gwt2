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

package org.geomajas.gwt2.client.controller;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.gwt2.client.map.MapPresenter;

/**
 * <p>
 * General interface for an event controller that catches different types of mouse events on a map. Implementations of
 * this interface must than decide how to react on these events. Since a <code>MapController</code> receives the
 * original mouse events, it can influence their behavior. As such only one <code>MapController</code> can be active at
 * any one time on a {@link org.geomajas.gwt2.client.map.MapPresenter}.
 * </p>
 *
 * @author Dosi Bingov
 * @since 2.1.0
 */
@UserImplemented
@Api(allMethods = true)
public interface BaseMapController {

	/**
	 * Function executed when the controller instance is applied on the map. If something needs initializing, do it
	 * here.
	 * 
	 * @param presenter
	 *            The map presenter onto which this controller has been activated.
	 */
	void onActivate(MapPresenter presenter);

	/**
	 * Function executed when the controller instance is removed from the map. The perfect moment to clean up all the
	 * mess this controller made.
	 * 
	 * @param presenter
	 *            The map presenter onto which this controller has been deactivated.
	 */
	void onDeactivate(MapPresenter presenter);
}