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

package org.geomajas.gwt2.tools.action;

import org.geomajas.annotation.Api;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import org.geomajas.gwt2.tools.action.event.ActionDisabledEvent;
import org.geomajas.gwt2.tools.action.event.ActionEnabledEvent;
import org.geomajas.gwt2.tools.action.event.ActionHandler;

/**
 * Base action.
 *
 * @author Joachim Van der Auwera
 * @author Oliver May
 * @since 2.0.0
 */
@Api(allMethods = true)
public abstract class BaseAction {

	private String icon; // Link to the image icon that should represent the action's button in the tool bar.

	private String tooltip; // Text that appears when hovering over the tool bar button.

	private String title; // Text that appears in the button under (or to the right of) the icon.

	/** Is the button for this action disabled or not? */
	private boolean disabled;

	private HandlerManager handlerManager;

	/**
	 * Constructor.
	 *
	 * @param icon icon
	 * @param tooltip tooltip
	 */
	public BaseAction(String icon, String tooltip) {
		this(icon, tooltip, tooltip);
	}

	/**
	 * Constructor for ToolbarBaseAction.
	 *
	 * @param icon icon
	 * @param title title
	 * @param tooltip tool tip
	 */
	public BaseAction(String icon, String title, String tooltip) {
		this.icon = icon;
		this.title = title;
		this.tooltip = tooltip;
		handlerManager = new HandlerManager(this);
	}

	/**
	 * Add action handler.
	 *
	 * @param handler action handler
	 * @return handler registration
	 */
	public HandlerRegistration addToolbarActionHandler(ActionHandler handler) {
		return handlerManager.addHandler(ActionHandler.TYPE, handler);
	}

	/**
	 * Link to the image icon that should represent the action's button in the tool bar.
	 *
	 * @return icon link
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * Link to the image icon that should represent the action's button in the tool bar.
	 *
	 * @param icon
	 *            The new icon value
	 * */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * Text that appears when hovering over the tool bar button.
	 *
	 * @return tool tip
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * Text that appears when hovering over the tool bar button.
	 *
	 * @param tooltip
	 *            The new tooltip value
	 */
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
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

	/**
	 * Text that appears in the button under (or to the right of) the icon.
	 *
	 * @return title
	 * @since 1.10.0
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Text that appears in the button under (or to the right of) the icon.
	 *
	 * @param title
	 *            The new value
	 * @since 1.10.0
	 */
	public void setTitle(String title) {
		this.title = title;
	}

}