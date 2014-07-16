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
package org.geomajas.gwt2.plugin.print.client.widget;

import com.google.gwt.user.client.ui.IsWidget;
import org.geomajas.annotation.Api;
import org.geomajas.gwt2.plugin.print.client.template.DefaultPrintRequestDataProvider;
import org.geomajas.gwt2.plugin.print.client.template.PrintRequestDataProvider;

/**
 * View interface for the {@link PrintWidget}.
 * 
 * @author Jan Venstermans
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface PrintWidgetView extends IsWidget, PrintRequestDataProvider {

	/**
	 * Registration of the handler, type {@link PrintWidgetPresenter}.
	 * @param handler
	 */
	void setHandler(PrintWidgetPresenter handler);

	/**
	 * Getter for the {@link org.geomajas.gwt2.plugin.print.client.template.DefaultPrintRequestDataProvider}.
	 * This interface contains both getters and setters for the data values.
	 *
	 * @return
	 */
	DefaultPrintRequestDataProvider getDefaultPrintRequestDataProvider();
}
