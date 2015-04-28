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
package org.geomajas.gwt2.plugin.wfs.example.client.sample;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

/**
 * Handler for {@link ShowFeatureEvent}.
 * 
 * @author Jan De Moerloose
 *
 */
public interface ShowFeatureHandler extends EventHandler {

	/**
	 * The type of the handler.
	 */
	Event.Type<ShowFeatureHandler> TYPE = new Event.Type<ShowFeatureHandler>();

	/**
	 * Called when feature is selected.
	 *
	 * @param event {@link ShowFeatureEvent}
	 */
	void onShowFeature(ShowFeatureEvent event);

}
