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
package org.geomajas.gwt2.tools.action.tool.zoomin;

import com.google.gwt.core.client.Callback;
import org.geomajas.gwt2.tools.action.Action;

/**
 * @author Oliver May
 */
public class ZoomInAction extends Action<Boolean, Boolean> {

	public ZoomInAction(String icon, String tooltip) {
		super(icon, tooltip);
	}

	public ZoomInAction(String icon, String title, String tooltip) {
		super(icon, title, tooltip);
	}

	@Override
	public void actionPerformed(Callback<Boolean, Boolean> callback) {

	}
}
