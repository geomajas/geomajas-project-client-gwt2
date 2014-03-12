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

package org.geomajas.gwt2.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;

/**
 * Handler for events that indicate a certain tile level has been rendered. For a tile based system, this could mean
 * that all tiles are rendered.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface TileLevelRenderedHandler extends EventHandler {

	/**
	 * The type of the handler.
	 */
	Type<TileLevelRenderedHandler> TYPE = new Type<TileLevelRenderedHandler>();

	/**
	 * Called when a tile level has been fully rendered.
	 * 
	 * @param event
	 *            The tile level rendered event.
	 */
	void onScaleLevelRendered(TileLevelRenderedEvent event);
}