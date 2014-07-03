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

import com.google.gwt.dom.client.Style.Unit;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.example.base.client.widget.ShowcaseDialogBox;
import org.geomajas.gwt2.widget.client.feature.controller.FeatureClickedListener;
import org.geomajas.gwt2.widget.client.feature.event.FeatureClickedEvent;
import org.geomajas.gwt2.widget.client.feature.event.FeatureClickedHandler;
import org.geomajas.gwt2.widget.client.feature.featureinfo.FeatureInfoWidget;
import org.geomajas.gwt2.widget.example.client.sample.feature.featureinfo.FeatureInfoWidgetFactory;

/**
 * Presenter implementation for the {@link FeatureInfoControl}.
 * The presenter listens to {@link FeatureClickedEvent}s and
 * displays the features in a dialog box.
 *
 * @author Youri Flement
 */
public class FeatureInfoControlPresenterImpl implements FeatureInfoControlPresenter, FeatureClickedHandler {

	private MapPresenter mapPresenter;

	private boolean enabled;

	public FeatureInfoControlPresenterImpl() {
		enabled = false;
	}

	@Override
	public void setMapPresenter(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
		this.mapPresenter.addMapListener(new FeatureClickedListener());
		this.mapPresenter.getEventBus().addHandler(FeatureClickedHandler.TYPE, this);
	}

	@Override
	public void enableFeatureInfo() {
		enabled = true;
	}

	@Override
	public void disableFeatureInfo() {
		enabled = false;
	}

	@Override
	public void onFeatureClicked(FeatureClickedEvent event) {
		if (enabled) {
			Feature feature = event.getFeature();
			if (feature != null) {
				// Create a default feature info widget:
				FeatureInfoWidgetFactory factory = new FeatureInfoWidgetFactory();
				FeatureInfoWidget featureInfo = factory.getDefaultFeatureInfoWidget();
				featureInfo.asWidget().getElement().getStyle().setWidth(250, Unit.PX);
				featureInfo.setFeature(feature);

				// Put it all in a dialog box:
				ShowcaseDialogBox dialogBox = new ShowcaseDialogBox();
				String title = "Feature detail: " + feature.getLabel();
				dialogBox.setWidget(featureInfo);
				dialogBox.setText(title);
				dialogBox.setTitle(title);
				dialogBox.setModal(false);
				dialogBox.show();
			}
		}
	}
}
