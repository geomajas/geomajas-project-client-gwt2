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

package org.geomajas.plugin.geocoder.client.widget;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 *
 * @author Jan Venstermans
 */
public interface GeocoderWidgetResource extends ClientBundle {

	@Source("theme/default/geomajas-geocoderGadget-default.css")
	GeocoderWidgetCssResource css();

	@Source("theme/default/images/magnifying-glass16.png")
	ImageResource magnifyingGlass();
}