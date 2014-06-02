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

package org.geomajas.plugin.geocoder.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;


/**
 * Common interface for all geocoder events.
 *
 * @author Jan Venstermans
 * @since 2.1.0
 * @param <T> the handler for the Gecoder Event
 */
public abstract class GeocoderEvent<T extends EventHandler> extends GwtEvent<T> {

}
