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

package org.geomajas.gwt2.plugin.geocoder.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * Messages for the {@link org.geomajas.gwt2.plugin.geocoder.client.widget.GeocoderWidget}.
 *
 * @author Joachim Van der Auwera
 */
public interface GeocoderMessages extends Messages {

	String alternativeSelectTitle();

	String locationNotFound(String searchString);

	String findPlaceOnMap();
}
