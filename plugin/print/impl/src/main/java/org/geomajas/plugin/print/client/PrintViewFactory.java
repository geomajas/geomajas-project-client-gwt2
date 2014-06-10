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
package org.geomajas.plugin.print.client;


import org.geomajas.plugin.print.client.widget.PrintPanel;
import org.geomajas.plugin.print.client.widget.PrintWidgetView;

/**
 * Factory for the widgets.
 * 
 * @author Jan Venstermans
 * 
 */
public class PrintViewFactory {

	 public PrintWidgetView createPrintWidgetView() {
		 return new PrintPanel();
	 }

}
