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

package org.geomajas.plugin.geocoder.client.nostyle;

import org.geomajas.plugin.geocoder.client.widget.GeocoderWidgetCssResource;
import org.geomajas.plugin.geocoder.client.widget.GeocoderWidgetResource;

/**
 *   No style version of {@link GeocoderWidgetResource}.
 * @author Jan Venstermans
 */
public interface GeocoderWidgetResourceNoStyle extends GeocoderWidgetResource {

	@Source("geomajas-geocoderGadget-nostyle.css")
	GeocoderWidgetCssResource css();
}