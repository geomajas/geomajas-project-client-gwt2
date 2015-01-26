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
 * Interface for handling validation events during geometry editing. The Geometry indices will point to the locations
 * (vertices/edges/sub-geometries) where coordinates have been inserted.
 * 
 * @author Jan De Moerloose
 * @since 2.1.0
 */
@Api(allMethods = true)
@UserImplemented
public interface GeometryEditValidationHandler extends EventHandler {

	/**
	 * The TYPE related to this handler.
	 */
	GwtEvent.Type<GeometryEditValidationHandler> TYPE = new GwtEvent.Type<GeometryEditValidationHandler>();

	/**
	 * Executed when a validation problem has occurred.
	 * 
	 * @param event
	 *            The geometry edit insert event.
	 */
	void onGeometryEditValidation(GeometryEditValidationEvent event);
}