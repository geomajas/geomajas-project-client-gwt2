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


package org.geomajas.gwt2.widget.example.client.sample.feature.featureinfo;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.widget.client.feature.featureinfo.FeatureInfoWidget;
import org.geomajas.gwt2.widget.client.feature.featureinfo.HasFeature;

/**
 * Wrapper for a {@link FeatureInfoWidget} with actions.
 *
 * @author Youri Flement
 */
public class FeatureInfoWithActions implements HasFeature, IsWidget {

	private VerticalPanel panel;

	private FeatureInfoWidget widget;

	/**
	 * Create a new feature info widget with no widgets.
	 */
	public FeatureInfoWithActions() {
		this.widget = new FeatureInfoWidget();
		panel = new VerticalPanel();
		panel.add(widget);
	}

	/**
	 * Add a {@link HasFeature} to the feature info widget. This will typically
	 * be an action to interact with the feature (info). If the HasFeature is a widget
	 * it will be added to the panel.
	 *
	 * @param hasFeature The HasFeature to add.
	 */
	public void addHasFeature(HasFeature hasFeature) {
		widget.addHasFeature(hasFeature);

		if (hasFeature instanceof Widget) {
			panel.insert((Widget) hasFeature, panel.getWidgetCount() - 1);
		}
	}

	@Override
	public Widget asWidget() {
		return panel;
	}

	@Override
	public void setFeature(Feature feature) {
		widget.setFeature(feature);
	}
}
