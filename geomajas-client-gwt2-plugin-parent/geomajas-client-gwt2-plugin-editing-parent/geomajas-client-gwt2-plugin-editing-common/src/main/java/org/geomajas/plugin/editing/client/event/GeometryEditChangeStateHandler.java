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

package org.geomajas.plugin.editing.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Interface for handling changes in the general editing state.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface GeometryEditChangeStateHandler extends EventHandler {

	/** The type of the handler. */
	GwtEvent.Type<GeometryEditChangeStateHandler> TYPE = new GwtEvent.Type<GeometryEditChangeStateHandler>();

	/**
	 * Executed on GeometryEditChangeStateEvents.
	 * 
	 * @param event
	 *            The event.
	 */
	void onChangeEditingState(GeometryEditChangeStateEvent event);
}