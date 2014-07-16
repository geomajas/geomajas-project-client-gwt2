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

package org.geomajas.gwt2.plugin.geocoder.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Handler for {@link SelectLocationEvent}.
 *
 * @author Joachim Van der Auwera
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface SelectLocationHandler extends EventHandler {

	/** Event type. */
	GwtEvent.Type<SelectLocationHandler> TYPE = new GwtEvent.Type<SelectLocationHandler>();

	/**
	 * Called when the location is selected either because the geocoder returned a match or because the user
	 * chose one of the alternatives.
	 *
	 * @param event event, contains the location which is selected
	 */
	void onSelectLocation(SelectLocationEvent event);
}
