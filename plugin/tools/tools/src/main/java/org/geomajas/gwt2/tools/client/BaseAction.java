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

package org.geomajas.gwt2.tools.client;

import org.geomajas.annotation.Api;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import org.geomajas.gwt2.tools.client.event.ActionDisabledEvent;
import org.geomajas.gwt2.tools.client.event.ActionEnabledEvent;
import org.geomajas.gwt2.tools.client.event.ActionHandler;

/**
 * Base action.
 *
 * @author Joachim Van der Auwera
 * @author Oliver May
 * @since 2.0.0
 */
@Api(allMethods = true)
public abstract class BaseAction {

	/** Is the button for this action disabled or not? */
	private boolean disabled;

	private HandlerManager handlerManager = new HandlerManager(this);

	/**
	 * Add action handler.
	 *
	 * @param handler action handler
	 * @return handler registration
	 */
	public HandlerRegistration addActionHandler(ActionHandler handler) {
		return handlerManager.addHandler(ActionHandler.TYPE, handler);
	}

	/**
	 * Is the button for this action disabled or not?
	 *
	 * @return true when disabled
	 */
	public boolean isDisabled() {
		return disabled;
	}

	/**
	 * Is the button for this action disabled or not?
	 *
	 * @param disabled
	 *            The new value
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
		if (disabled) {
			handlerManager.fireEvent(new ActionDisabledEvent());
		} else {
			handlerManager.fireEvent(new ActionEnabledEvent());
		}
	}
}