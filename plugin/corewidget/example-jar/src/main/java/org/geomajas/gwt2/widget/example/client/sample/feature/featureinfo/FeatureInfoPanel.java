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

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.event.MapInitializationEvent;
import org.geomajas.gwt2.client.event.MapInitializationHandler;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.example.base.client.ExampleBase;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.widget.client.feature.event.FeatureClickedEvent;
import org.geomajas.gwt2.widget.client.feature.event.FeatureClickedHandler;
import org.geomajas.gwt2.widget.example.client.sample.feature.featureinfo.control.FeatureInfoControlWidget;

/**
 * Panel for the feature info sample.
 *
 * @author Youri Flement
 */
public class FeatureInfoPanel implements SamplePanel, FeatureClickedHandler {

	private MapPresenter mapPresenter;

	protected DockLayoutPanel rootPanel;

	@UiField
	protected HTMLPanel leftPanel;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	private static final FeatureInfoPanelUiBinder UI_BINDER = GWT.create(FeatureInfoPanelUiBinder.class);

	/**
	 * The Feature Info Widget (with actions) we will display in the left panel.
	 */
	private FeatureInfoWithActions featureInfoWidget;

	/**
	 * {@link UiBinder} for this class.
	 *
	 * @author Youri Flement
	 */
	interface FeatureInfoPanelUiBinder extends UiBinder<DockLayoutPanel, FeatureInfoPanel> {

	}

	public FeatureInfoPanel() {
		rootPanel = UI_BINDER.createAndBindUi(this);

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter();
		mapPresenter.setSize(480, 480);
		mapPresenter.getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());

		// Add the map to the GUI, using a decorator for nice borders:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		// Add a feature info control to the map (displays feature info in a dialogbox):
		leftPanel.add(new FeatureInfoControlWidget(mapPresenter).asWidget());

		// Also show feature information in the side panel:
		mapPresenter.getEventBus().addHandler(FeatureClickedHandler.TYPE, this);

		// Initialize the map
		GeomajasServerExtension.getInstance().initializeMap(mapPresenter, "gwt-app", "mapCountries");
	}

	/**
	 * Action called when a feature is clicked. Displays the information of the clicked
	 * feature in the left panel of the sample, overwriting any feature info that may
	 * be already there.
	 *
	 * @param event {@link FeatureClickedEvent}
	 */
	@Override
	public void onFeatureClicked(FeatureClickedEvent event) {
		// Create feature info widget if this is the first time displaying a feature:
		if (featureInfoWidget == null) {
			// Create info widget with some actions:
			FeatureInfoWidgetFactory factory = new FeatureInfoWidgetFactory();
			featureInfoWidget = factory.getFeatureInfoWidgetWithActions(mapPresenter);
			leftPanel.add(featureInfoWidget);
		}

		// (Over)write the feature of the widget:
		Feature feature = event.getFeature();
		if (feature != null) {
			featureInfoWidget.setFeature(event.getFeature());
		}
	}

	@Override
	public Widget asWidget() {
		return rootPanel;
	}

	/**
	 * {@link MapInitializationHandler} for this sample.
	 *
	 * @author Youri Flement
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		public void onMapInitialized(MapInitializationEvent event) {
			mapPresenter.getViewPort().applyBounds(ExampleBase.BBOX_AFRICA);
		}
	}
}
