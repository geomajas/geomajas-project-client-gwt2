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

package org.geomajas.gwt2.widget.client.feature.featureinfo;

import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.feature.Feature;

/**
 * Presenter interface for the {@link org.geomajas.gwt2.widget.client.feature.featureinfo.FeatureInfoWidget}.
 * The presenter should be able to show a feature, change the visibility of options and zoom
 * to a specific feature on the map.
 *
 * @author Youri Flement
 */
public interface FeatureInfoPresenter extends HasFeature {

	/**
	 * Set the map presenter.
	 *
	 * @param mapPresenter The map presenter.
	 */
	void setMapPresenter(MapPresenter mapPresenter);

	/**
	 * Set the feature to be displayed.
	 *
	 * @param feature the feature to display.
	 */
	void setFeature(Feature feature);

	/**
	 * Get the displayed feature.
	 *
	 * @return The displayed feature.
	 */
	Feature getFeature();

}
