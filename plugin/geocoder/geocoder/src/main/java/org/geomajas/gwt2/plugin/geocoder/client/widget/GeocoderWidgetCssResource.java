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

package org.geomajas.gwt2.plugin.geocoder.client.widget;

import com.google.gwt.resources.client.CssResource;

/**
 *
 * @author Jan Venstermans
 */
public interface GeocoderWidgetCssResource extends CssResource {

	@ClassName("gm-GeocoderGadget")
	String geocoderGadget();

	@ClassName("gm-GeocoderGadget-textBox")
	String geocoderGadgetTextBox();

	@ClassName("gm-GeocoderGadget-textBox-withAlts")
	String geocoderGadgetTextBoxWithAlts();

	@ClassName("gm-GeocoderGadget-tip")
	String geocoderGadgetTip();

	// is done via sprite
	@ClassName("gm-GeocoderGadget-glass")
	String geocoderGadgetGlass();

	@ClassName("gm-GeocoderGadget-altPanel")
	String geocoderGadgetAltPanel();

	@ClassName("gm-GeocoderGadget-altLabel")
	String geocoderGadgetAltLabel();
}