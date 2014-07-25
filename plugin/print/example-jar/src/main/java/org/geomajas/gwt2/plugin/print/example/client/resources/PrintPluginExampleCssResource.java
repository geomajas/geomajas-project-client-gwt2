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

package org.geomajas.gwt2.plugin.print.example.client.resources;

import com.google.gwt.resources.client.CssResource;

/**
 * CSS resource bundle that contains all generic styles used in the widgets example jar.
 *
 * @author Jan Venstermans
 */
public interface PrintPluginExampleCssResource extends CssResource {

	@ClassName("gm-print-panel")
	String printPanel();

	@ClassName("gm-print-panel-button")
	String printPanelButton();

	@ClassName("gm-print-panel-wait")
	String printPanelWait();
}