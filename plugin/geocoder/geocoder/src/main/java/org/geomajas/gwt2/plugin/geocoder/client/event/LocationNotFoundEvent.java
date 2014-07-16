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

package org.geomajas.gwt2.plugin.geocoder.client.event;

import org.geomajas.annotation.Api;

/**
 * Event that is used when the geocoder returns no result for a given location.
 *
 * @author Jan Venstermans
 * @since 2.1.0
 */
@Api()
public class LocationNotFoundEvent extends GeocoderEvent<LocationNotFoundHandler>  {

	private String location;

	public LocationNotFoundEvent(String location) {
		this.location = location;
	}

	@Override
	public Type<LocationNotFoundHandler> getAssociatedType() {
		return LocationNotFoundHandler.TYPE;
	}

	@Override
	protected void dispatch(LocationNotFoundHandler handler) {
		handler.onLocationNotFound(this);
	}

	/**
	 * Returns the location that has not been found.
	 * @return
	 */
	@Api
	public String getLocation() {
		return location;
	}
}
