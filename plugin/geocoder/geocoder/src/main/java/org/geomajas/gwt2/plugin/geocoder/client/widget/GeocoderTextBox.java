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

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.TextBox;
import org.geomajas.gwt2.plugin.geocoder.client.event.GeocoderEvent;

/**
 * Extension of {@link TextBox} that will redirect
 * {@link org.geomajas.gwt2.plugin.geocoder.client.event.GeocoderEvent}s to a {@link GeocoderWidgetPresenter}.
 *
 * @author Emiel Ackermann
 * @author Jan Venstermans
 */
public class GeocoderTextBox extends TextBox {

	private GeocoderWidgetPresenter geocoderPresenter;

	public void setGeocoderPresenter(GeocoderWidgetPresenter geocoderPresenter) {
		this.geocoderPresenter = geocoderPresenter;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		// redirect all GeocoderEvents to the presenter
		if (event instanceof GeocoderEvent) {
			geocoderPresenter.fireGeocoderEvent((GeocoderEvent) event);
		} else {
			super.fireEvent(event);
		}
	}
}