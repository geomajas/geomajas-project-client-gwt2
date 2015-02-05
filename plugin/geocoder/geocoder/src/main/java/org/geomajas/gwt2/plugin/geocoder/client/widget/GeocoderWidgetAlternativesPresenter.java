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

import org.geomajas.geometry.Bbox;

/**
 * Interface for methods used by the {@link GeocoderWidgetAlternativesView}.
 *
 * @author Jan Venstermans
 */
public interface GeocoderWidgetAlternativesPresenter {

	/**
	 * Find the location which matches the given string.
	 *
	 * @param location location
	 */
	void findLocation(String location) ;

	/**
	 *  Indicates the alternatives window has just been closed.
	 */
	void onAlternativesViewClosed();

	/**
	 * Returns the dimensions box that can be used to display the alternatives view.
	 *
	 * @return bbox used when alternatives view is shown
	 */
	Bbox getAlternativesViewShowBbox();
}