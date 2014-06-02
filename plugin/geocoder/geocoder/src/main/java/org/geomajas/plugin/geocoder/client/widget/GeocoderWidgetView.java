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

package org.geomajas.plugin.geocoder.client.widget;

import com.google.gwt.user.client.ui.IsWidget;
import org.geomajas.annotation.Api;

/**
 * Interface for the main view of the {@link org.geomajas.plugin.geocoder.client.widget.GeocoderWidget}.
 *
 * @author Jan Venstermans
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface GeocoderWidgetView extends IsWidget {

	/**
	 * Sets the presenter for callback.
	 *
	 * @param presenter
	 */
	void setPresenter(GeocoderWidgetPresenter presenter);

	/**
	 * Set the value of the location in the display element.
	 *
	 * @param value
	 */
	void setValue(String value);

	/**
	 * Indicates whether the {@link org.geomajas.plugin.geocoder.client.widget.GeocoderWidgetAlternativesView}
	 * has been shown or has been hidden.
	 *
	 * @param shown true means {@link GeocoderWidgetAlternativesView#show()} has been called;
	 *              false means {@link GeocoderWidgetAlternativesView#hide()} has been called.
	 */
	void alternativesViewIsShown(boolean shown);
}