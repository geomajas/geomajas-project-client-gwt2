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

package org.geomajas.gwt2.widget.example.client.sample.feature.featureinfo.control;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.client.map.MapPresenter;

/**
 * Widget for a control that allows to display feature information of features.
 *
 * @author Youri Flement
 */
public class FeatureInfoControlWidget implements IsWidget {

	private FeatureInfoControlView view;

	private FeatureInfoControlPresenter presenter;

	public FeatureInfoControlWidget(MapPresenter mapPresenter) {
		view = FeatureInfoControl.getInstance().getViewFactory().createFeatureInfoControlView();
		presenter = new FeatureInfoControlPresenterImpl();
		presenter.setMapPresenter(mapPresenter);
		view.setPresenter(presenter);
	}

	@Override
	public Widget asWidget() {
		return view.asWidget();
	}
}
