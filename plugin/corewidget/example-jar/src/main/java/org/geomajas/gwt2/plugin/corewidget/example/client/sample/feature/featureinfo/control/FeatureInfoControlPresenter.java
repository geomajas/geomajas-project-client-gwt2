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

package org.geomajas.gwt2.plugin.corewidget.example.client.sample.feature.featureinfo.control;

import org.geomajas.gwt2.client.map.MapPresenter;

/**
 * Presenter interface for the {@link FeatureInfoControl}.
 * The presenter allows to disable/enable the displaying of feature info, e.g. the underlying control can be a
 * toggle button.
 *
 * @author Youri Flement
 */
public interface FeatureInfoControlPresenter {

	void setMapPresenter(MapPresenter mapPresenter);

	/**
	 * Enable showing feature info when a feature is clicked.
	 */
	void enableFeatureInfo();

	/**
	 * Disable showing feature info when a feature is clicked.
	 */
	void disableFeatureInfo();
}
