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
package org.geomajas.gwt2.widget.client.feature.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

/**
 * Interface for handling {@link FeaturesClickedEvent}s.
 *
 * @author Oliver May
 */
public interface FeaturesClickedHandler extends EventHandler {
	/**
	 * The type of the handler.
	 */
	Event.Type<FeaturesClickedHandler> TYPE = new Event.Type<FeaturesClickedHandler>();

	/**
	 * Called when feature is selected.
	 *
	 * @param event {@link FeatureClickedEvent}
	 */
	void onFeatureClicked(FeaturesClickedEvent event);

}
