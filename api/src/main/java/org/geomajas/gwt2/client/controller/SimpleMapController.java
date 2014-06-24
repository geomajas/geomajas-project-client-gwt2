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

import org.geomajas.gwt2.client.map.MapPresenter;

/**
 *
 * <p>
 * General interface for an event controller. Implementations of
 * this interface must than decide how to interact with the map via events.
 * </p>
 *
 * @author Dosi Bingov
 */
public interface SimpleMapController {

	/**
	 * Function executed when the controller instance is applied on the map. If something needs initializing, do it
	 * here. Here is the place where all map events related to this controller should be registered.
	 *
	 * @param presenter
	 *            The map presenter onto which this controller has been activated.
	 */
	void onActivate(MapPresenter presenter);

	/**
	 * Function executed when the controller instance is removed from the map. The perfect moment to clean up all the
	 * mess this controller made, so remove all events that were registered in
	 * {@link SimpleMapController#onActivate(MapPresenter)}.
	 * 
	 * @param presenter
	 *            The map presenter onto which this controller has been deactivated.
	 */
	void onDeactivate(MapPresenter presenter);
}