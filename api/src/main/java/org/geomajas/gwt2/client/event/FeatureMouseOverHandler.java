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

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;
import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

/**
 * Interface for handling {@link org.geomajas.gwt2.client.event.FeatureMouseOverEvent}s.
 *
 * @author David Debuck
 * @since 2.1.0
 */
@Api(allMethods = true)
@UserImplemented
public interface FeatureMouseOverHandler extends EventHandler {
	/**
	 * The type of the handler.
	 */
	Event.Type<FeatureMouseOverHandler> TYPE = new Event.Type<FeatureMouseOverHandler>();

	/**
	 * Called when feature is found where the mouse is hovering.
	 *
	 * @param event {@link org.geomajas.gwt2.client.event.FeatureMouseOverEvent}
	 */
	void onFeatureMouseOver(FeatureMouseOverEvent event);

}
