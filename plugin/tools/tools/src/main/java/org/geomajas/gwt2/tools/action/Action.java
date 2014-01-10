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

import com.google.gwt.core.client.Callback;

/**
 * Abstract Action class.
 *
 * @param <T> The type returned on success
 * @param <F> The type returned on failure
 *
 * @author Oliver May
 */
public abstract class Action<T, F> extends BaseAction {

	/**
	 * Create a new Action.
	 *
	 * @param icon the icon
	 * @param tooltip the tooltip
	 */
	public Action(String icon, String tooltip) {
		super(icon, tooltip);
	}

	/**
	 * Create a new Action.
	 *
	 * @param icon the icon
	 * @param title the title
	 * @param tooltip the tooltip
	 */
	public Action(String icon, String title, String tooltip) {
		super(icon, title, tooltip);
	}

	/**
	 * Perform the action.
	 *
	 * @param callback failure and success callback.
	 */
	public abstract void actionPerformed(Callback<T, F> callback);
}
