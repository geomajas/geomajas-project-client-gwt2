/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.gwt2.plugin.print.client.nostyle;

import org.geomajas.gwt2.plugin.print.client.widget.PrintWidgetCssResource;
import org.geomajas.gwt2.plugin.print.client.widget.PrintWidgetResource;

/**
 * No style versiobn of the {@link PrintWidgetResource}.
 *
 * @author Jan Venstermans
 *
 */
public interface PrintWidgetResourceNoStyle extends PrintWidgetResource {

	@Source("print-widget-nostyle.css")
	PrintWidgetCssResource css();
}
