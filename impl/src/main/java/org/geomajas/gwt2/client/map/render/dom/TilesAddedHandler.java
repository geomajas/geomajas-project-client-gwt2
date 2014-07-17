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

package org.geomajas.gwt2.client.map.render.dom;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;

/**
 * Interface for handling events that indicate tiles were added to the map. These tiles will typically be unloaded
 * so the {@link org.geomajas.gwt2.client.map.MapPresenter} can load them after the render cycle.
 *
 * @author Youri Flement
 */
public interface TilesAddedHandler extends EventHandler {

	/**
	 * The type of the handler.
	 */
	Type<TilesAddedHandler> TYPE = new Type<TilesAddedHandler>();

	/**
	 * Called when (unloaded) tiles are added to the map.
	 *
	 * @param event The event.
	 */
	void onTilesAdded(TilesAddedEvent event);
}
