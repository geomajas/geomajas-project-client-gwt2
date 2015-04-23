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
 * Handler for operation event in a geometry. This will catch all events that signal the shape of the geometry has
 * changed. The reason can be another event, such a insert, move, delete, but it can also come from undo/redo
 * operations.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface GeometryEditShapeChangedHandler extends EventHandler {

	/** The type of the handler. */
	GwtEvent.Type<GeometryEditShapeChangedHandler> TYPE = new GwtEvent.Type<GeometryEditShapeChangedHandler>();

	/**
	 * Executed on a GeometryEditShapeChangedEvent.
	 * 
	 * @param event
	 *            The event.
	 */
	void onGeometryShapeChanged(GeometryEditShapeChangedEvent event);
}