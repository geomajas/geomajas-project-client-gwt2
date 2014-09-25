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

package org.geomajas.gwt2.plugin.corewidget.client.feature.featureinfo;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.plugin.corewidget.client.CoreWidget;
import org.geomajas.gwt2.plugin.corewidget.client.feature.featureinfo.resource.FeatureInfoResource;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Widget to display feature information of a {@link Feature}. By default, the widget displays
 * the attributes of the feature and provides some options to interact with the feature such
 * as zooming to the feature. The options can be hidden if needed.
 *
 * @author Youri Flement
 * @since 2.1.0
 */
@Api(allMethods = true)
public class FeatureInfoWidget implements IsWidget, HasFeature {

	private FeatureInfoPresenter presenter;

	private FeatureInfoView view;

	private Collection<HasFeature> actions;

	/**
	 * Create a new feature info widget with the default resources.
	 */
	public FeatureInfoWidget() {
		this(CoreWidget.getInstance().getClientBundleFactory().createFeatureInfoResource());
	}

	/**
	 * Create a new feature info widget with the given map presenter
	 * and resource.
	 *
	 * @param resource The feature info widget resource.
	 */
	public FeatureInfoWidget(FeatureInfoResource resource) {
		view = CoreWidget.getInstance().getViewFactory().createFeatureInfoView(resource);
		presenter = new FeatureInfoPresenterImpl(view);
		view.setPresenter(presenter);
		actions = new ArrayList<HasFeature>();
	}

	/**
	 * Set the feature to display.
	 *
	 * @param feature The feature.
	 */
	@Override
	public void setFeature(Feature feature) {
		for (HasFeature action : actions) {
			action.setFeature(feature);
		}
		presenter.setFeature(feature);
	}

	/**
	 * Add an object for this feature to the widget. When the displayed feature
	 * changes, the feature associated with the object is automatically updated.
	 *
	 * @param action The action to add.
	 */
	public void addHasFeature(HasFeature action) {
		actions.add(action);
		action.setFeature(presenter.getFeature());
	}

	/**
	 * Remove an object associated with this widget. If the displayed feature changes,
	 * the feature associated with the object will not be updated anymore.
	 *
	 * @param action The action to remove.
	 */
	public void removeHasFeature(HasFeature action) {
		actions.remove(action);
	}

	@Override
	public Widget asWidget() {
		return view.asWidget();
	}
}
