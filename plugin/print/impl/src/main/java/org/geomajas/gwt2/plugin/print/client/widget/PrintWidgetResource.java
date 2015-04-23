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
package org.geomajas.gwt2.plugin.print.client.widget;

import com.google.gwt.resources.client.ClientBundle;

/**
 * {@link ClientBundle} for {@link BasePrintPanel}.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface PrintWidgetResource extends ClientBundle {

	@Source("print-widget.css")
	PrintWidgetCssResource css();
}
