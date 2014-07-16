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

package org.geomajas.gwt2.plugin.geocoder.client;

import com.google.gwt.core.client.GWT;
import org.geomajas.annotation.Api;
import org.geomajas.gwt2.plugin.geocoder.client.widget.GeocoderWidgetResource;

/**
 * Resource bundle.
 *
 * @author Jan Venstermans
 * @since 2.1.0
 */
@Api(allMethods = true)
public class GeocoderClientBundleFactory {

	/**
	 * Create a resource for the factory, containing css, images, ...
	 * @return resource for the widget
	 */
	public GeocoderWidgetResource createGeocoderWidgetResource() {
		return GWT.create(GeocoderWidgetResource.class);
	}
}