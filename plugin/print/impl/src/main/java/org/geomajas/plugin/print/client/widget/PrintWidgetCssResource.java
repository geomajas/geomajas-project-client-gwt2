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

import com.google.gwt.resources.client.CssResource;

/**
 * {@link CssResource} for {@link BasePrintPanel}.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface PrintWidgetCssResource extends CssResource {

	@ClassName("gm-print-button")
	String printButton();

	@ClassName("gm-print-buttons")
	String buttons();

	@ClassName("gm-print-fieldList")
	String fieldList();

	@ClassName("gm-print-fieldLabel")
	String fieldLabel();

	@ClassName("gm-print-printPanel")
	String printPanel();

	@ClassName("gm-print-radiobutton-margin")
	String radiobuttonMargin();

	@ClassName("gm-print-radioGroup")
	String radioGroup();

	@ClassName("gm-print-subMenuPanel")
	String subMenuPanel();

	@ClassName("gm-print-textbox-suitable-margin")
	String textboxSuitableMargin();
}