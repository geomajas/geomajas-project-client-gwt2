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

package org.geomajas.plugin.editing.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Handler for catching edit resume events.
 * 
 * @author Jan De Moerloose
 * @since 2.1.0
 */
@Api(allMethods = true)
@UserImplemented
public interface GeometryEditResumeHandler extends EventHandler {

	/** The type of the handler. */
	GwtEvent.Type<GeometryEditResumeHandler> TYPE = new GwtEvent.Type<GeometryEditResumeHandler>();

	/**
	 * Executed when the editing process has resumed.
	 * 
	 * @param event
	 *            The geometry edit resume event.
	 */
	void onGeometryEditResume(GeometryEditResumeEvent event);
}