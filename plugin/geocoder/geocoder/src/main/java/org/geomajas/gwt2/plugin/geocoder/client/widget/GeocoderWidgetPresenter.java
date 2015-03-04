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

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.plugin.geocoder.client.event.GeocoderEvent;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringResponse;

/**
 * Interface for basic Geocoder presenter methods.
 *
 * @author Jan Venstermans
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface GeocoderWidgetPresenter {

	/**
	 * Find the location which matches the given string.
	 *
	 * @param location location
	 */
	void findLocation(String location) ;

	/**
	 * Go to the location which matches the given string.
	 *
	 * @param response get location command response
	 * @param location location to go to
	 */
	void goToLocation(GetLocationForStringResponse response, String location);

	/**
	 * Fire a {@link GeocoderEvent}.
	 *
	 * @param event geocoder event to fire
	 */
	void fireGeocoderEvent(GeocoderEvent event);

}