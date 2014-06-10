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
package org.geomajas.plugin.print.client.widget;

import com.google.gwt.user.client.ui.IsWidget;
import org.geomajas.plugin.print.client.template.PageSize;

/**
 * View interface for the widget.
 * 
 * @author Jan De Moerloose
 */
public interface PrintWidgetView extends IsWidget {

	void setHandler(PrintWidgetPresenter handler);

	String getTitle();

	/**
	 *
	 * @return true landscape
	 * 			false portrait
	 */
	boolean isLandscape();

	PageSize getPageSize();

	boolean isWithArrow();

	boolean isWithScaleBar();

	int getRasterDpi();
}
