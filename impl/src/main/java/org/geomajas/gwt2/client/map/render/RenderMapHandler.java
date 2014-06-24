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

package org.geomajas.gwt2.client.map.render;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event.Type;

/**
 * Interface for event handlers that catch {@link RenderMapEvent}s.
 * 
 * @author Jan De Moerloose
 */
public interface RenderMapHandler extends EventHandler {

	/**
	 * The type of the handler.
	 */
	Type<RenderMapHandler> TYPE = new Type<RenderMapHandler>();

	/**
	 * Called when map has to be rendered.
	 * 
	 * @param event {@link RenderMapEvent}
	 */
	void onRender(RenderMapEvent event);

}