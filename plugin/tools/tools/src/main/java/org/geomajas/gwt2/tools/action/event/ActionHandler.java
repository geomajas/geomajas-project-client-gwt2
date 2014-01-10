/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.tools.action.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

/**
 * <p>
 * Handler for enabled and disabled events on a single <code>BaseAction</code>.
 * </p>
 * 
 * @author Pieter De Graef
 * @author Oliver May
 * @since 2.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface ActionHandler extends EventHandler {

	/** Event type. */
	GwtEvent.Type<ActionHandler> TYPE = new GwtEvent.Type<ActionHandler>();

	/**
	 * Called when a  action is enabled.
	 * 
	 * @param event
	 *            {@link ActionEnabledEvent}
	 */
	void onActionEnabled(ActionEnabledEvent event);

	/**
	 * Called when a  action is disabled.
	 * 
	 * @param event
	 *            {@link ActionDisabledEvent}
	 */
	void onActionDisabled(ActionDisabledEvent event);
}
