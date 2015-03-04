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

import com.google.gwt.user.client.ui.IsWidget;

/**
 * View interface of
 * {@link FeatureInfoControl}.
 * The view has only methods for setting the presenter.
 *
 * @author Youri Flement
 */
public interface FeatureInfoControlView extends IsWidget {

	/**
	 * Set the presenter that will handle the events when the control is clicked.
	 *
	 * @param presenter the presenter.
	 */
	void setPresenter(FeatureInfoControlPresenter presenter);

}
