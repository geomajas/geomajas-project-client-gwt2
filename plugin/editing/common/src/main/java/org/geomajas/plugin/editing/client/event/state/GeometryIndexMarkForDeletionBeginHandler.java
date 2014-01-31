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

package org.geomajas.plugin.editing.client.event.state;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Interface for event handlers that catch {@link GeometryIndexMarkForDeletionBeginEvent}s.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface GeometryIndexMarkForDeletionBeginHandler extends EventHandler {

	/** The type of the handler. */
	GwtEvent.Type<GeometryIndexMarkForDeletionBeginHandler> TYPE = new GwtEvent.
			Type<GeometryIndexMarkForDeletionBeginHandler>();

	/**
	 * Called when a part of the geometry has been marked for deletion (not deleted just yet).
	 * 
	 * @param event
	 *            {@link GeometryIndexMarkForDeletionBeginEvent}
	 */
	void onGeometryIndexMarkForDeletionBegin(GeometryIndexMarkForDeletionBeginEvent event);
}