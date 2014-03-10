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

import org.geomajas.annotation.FutureApi;
import org.geomajas.annotation.UserImplemented;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Handler for catching edit suspend events.
 * 
 * @author Jan De Moerloose
 * @since 2.1.0
 */
@FutureApi(allMethods = true)
@UserImplemented
public interface GeometryEditSuspendHandler extends EventHandler {

	GwtEvent.Type<GeometryEditSuspendHandler> TYPE = new GwtEvent.Type<GeometryEditSuspendHandler>();

	/**
	 * Executed when the editing process has been suspended.
	 * 
	 * @param event
	 *            The geometry edit suspend event.
	 */
	void onGeometryEditSuspend(GeometryEditSuspendEvent event);
}