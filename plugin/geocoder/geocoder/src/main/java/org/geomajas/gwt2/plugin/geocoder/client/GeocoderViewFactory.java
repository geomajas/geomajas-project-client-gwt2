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

package org.geomajas.gwt2.plugin.geocoder.client;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.plugin.geocoder.client.widget.GeocoderWidgetAlternativesView;
import org.geomajas.gwt2.plugin.geocoder.client.widget.GeocoderWidgetView;
import org.geomajas.gwt2.plugin.geocoder.client.widget.GeocoderWidgetViewImpl;
import org.geomajas.gwt2.plugin.geocoder.client.widget.GeocoderWidgetAlternativesViewImpl;

/**
 * Default factory for the views of the {@link org.geomajas.gwt2.plugin.geocoder.client.widget.GeocoderWidget}.
 *
 * @author Jan Venstermans
 * @since 2.1.0
 */
@Api(allMethods = true)
public class GeocoderViewFactory {

	/**
	 * Create the view that permits to type in a location name.
	 * @return the search location view
	 */
	public GeocoderWidgetView createGeocoderWidgetView() {
		return new GeocoderWidgetViewImpl();
	}

	/**
	 * Return the view that displays search result in case multiple results are returned.
	 * @return the alternative locations view
	 */
	public GeocoderWidgetAlternativesView createGeocoderWidgetAlternativesView() {
		return new GeocoderWidgetAlternativesViewImpl();
	}
}