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

package org.geomajas.gwt2.plugin.geocoder.client.nostyle;

import com.google.gwt.core.client.GWT;
import org.geomajas.gwt2.plugin.geocoder.client.GeocoderClientBundleFactory;
import org.geomajas.gwt2.plugin.geocoder.client.widget.GeocoderWidgetResource;

/**
 * No style version of {@link org.geomajas.gwt2.plugin.geocoder.client.GeocoderClientBundleFactory}.
 *
 * @author Jan Venstermans
 */
public class GeocoderClientBundleNoStyleFactory extends GeocoderClientBundleFactory {

	public GeocoderWidgetResource createGeocoderWidgetResource() {
		return GWT.create(GeocoderWidgetResourceNoStyle.class);
	}
}