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

package org.geomajas.gwt2.plugin.geocoder.client.widget;

import com.google.gwt.user.client.ui.IsWidget;
import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringAlternative;

import java.util.List;

/**
 * Interface for the view that displays the alternative locations
 * in case a location search returns multiple location possibilities.
 *
 * @author Jan Venstermans
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface GeocoderWidgetAlternativesView extends IsWidget {

	/**
	 * Sets the presenter for callback.
	 *
	 * @param presenter the presenter
	 */
	void setPresenter(GeocoderWidgetAlternativesPresenter presenter);

	/**
	 * Set position of the view, based on a bbox.
	 * @param bbox the bbox that is a reference for positioning the alternatives view.
	 */
	void setPosition(Bbox bbox);

	/**
	 * Sets the alternative locations to the view.
	 * @param alternatives the alternative location where can be chosen from
	 */
	void setAlternatives(List<GetLocationForStringAlternative> alternatives);

	/**
	 * Show the view.
	 */
	void show();

	/**
	 * Hide the view.
	 */
	void hide();
}