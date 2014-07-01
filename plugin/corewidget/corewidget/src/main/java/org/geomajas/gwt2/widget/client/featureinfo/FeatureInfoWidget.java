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

package org.geomajas.gwt2.widget.client.featureinfo;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.widget.client.CoreWidget;
import org.geomajas.gwt2.widget.client.featureinfo.resource.FeatureInfoResource;

/**
 * Widget to display feature information of a {@link Feature}. By default, the widget displays
 * the attributes of the feature and provides some options to interact with the feature such
 * as zooming to the feature. The options can be hidden if needed.
 *
 * @author Youri Flement
 * @since 2.1.0
 */
@Api(allMethods = true)
public class FeatureInfoWidget implements IsWidget {

	private FeatureInfoPresenter presenter;

	private FeatureInfoView view;

	/**
	 * Create a new feature info widget with the default resources.
	 *
	 * @param mapPresenter the map presenter.
	 */
	public FeatureInfoWidget(MapPresenter mapPresenter) {
		this(CoreWidget.getInstance().getClientBundleFactory().createFeatureInfoResource(), mapPresenter);
	}

	/**
	 * Create a new feature info widget with the given map presenter
	 * and resource.
	 *
	 * @param resource     the feature info widget resource.
	 * @param mapPresenter the map presenter.
	 */
	public FeatureInfoWidget(FeatureInfoResource resource, MapPresenter mapPresenter) {
		view = CoreWidget.getInstance().getViewFactory().createFeatureInfoView(resource);
		presenter = new FeatureInfoPresenterImpl(view);
		presenter.setMapPresenter(mapPresenter);
		view.setPresenter(presenter);
	}

	/**
	 * Set the feature to display.
	 *
	 * @param feature the feature.
	 */
	public void setFeature(Feature feature) {
		presenter.setFeature(feature);
	}

	/**
	 * Hide or show the options to interact with the feature (information).
	 *
	 * @param show <code>true</code> if the options should be shown,
	 *             <code>false</code> otherwise.
	 */
	public void showOptions(boolean show) {
		presenter.showOptions(show);
	}

	@Override
	public Widget asWidget() {
		return view.asWidget();
	}
}
