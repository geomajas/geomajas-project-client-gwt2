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
package org.geomajas.gwt2.plugin.print.client.nostyle;


import com.google.gwt.core.client.GWT;
import org.geomajas.gwt2.plugin.print.client.PrintClientBundleFactory;
import org.geomajas.gwt2.plugin.print.client.widget.PrintWidgetView;

/**
 * Overrides {@link PrintClientBundleFactory} with empty styles.
 * 
 * @author Jan Venstermans
 * 
 */
public class PrintClientBundleFactoryNoStyle extends PrintClientBundleFactory {

	 public PrintWidgetView createPrintWidgetResource() {
		 return GWT.create(PrintWidgetResourceNoStyle.class);
	 }

}
