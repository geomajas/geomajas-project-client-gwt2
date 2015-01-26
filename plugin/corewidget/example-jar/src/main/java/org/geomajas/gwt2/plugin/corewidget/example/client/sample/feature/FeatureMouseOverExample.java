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
package org.geomajas.gwt2.plugin.corewidget.example.client.sample.feature;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.plugin.corewidget.example.client.sample.feature.controller.FeatureMouseOverListener;
import org.geomajas.gwt2.plugin.corewidget.example.client.sample.feature.controller.FeatureMouseOverEvent;
import org.geomajas.gwt2.plugin.corewidget.example.client.sample.feature.controller.FeatureMouseOverHandler;
import org.geomajas.gwt2.client.event.MapInitializationEvent;
import org.geomajas.gwt2.client.event.MapInitializationHandler;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.widget.MapLayoutPanel;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.plugin.corewidget.example.client.sample.feature.tooltip.ToolTip;

import java.util.ArrayList;
import java.util.List;

/**
 * FeatureMouseOver showcase example.
 *
 * @author David Debuck
 */
public class FeatureMouseOverExample implements SamplePanel {

	protected DockLayoutPanel rootElement;

	private MapPresenter mapPresenter;

	private final MapLayoutPanel layout;

	private ToolTip toolTip;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	@UiField
	protected VerticalPanel layerEventLayout;

	@UiField
	protected ScrollPanel scrollPanel;

	@Override
	public Widget asWidget() {
		// return root layout element
		return rootElement;
	}

	/**
	 * UI binder interface.
	 *
	 * @author David Debuck
	 */
	interface FeatureSelectedExampleUiBinder extends UiBinder<DockLayoutPanel, FeatureMouseOverExample> {
	}

	private static final FeatureSelectedExampleUiBinder UIBINDER = GWT.create(FeatureSelectedExampleUiBinder.class);

	/**
	 * Default constructor.
	 */
	public FeatureMouseOverExample() {
		rootElement = UIBINDER.createAndBindUi(this);

		// Create the MapPresenter
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter();

		// add FeatureClickedHandler where we handle FeaturesClickedEvent
		mapPresenter.getEventBus().addHandler(FeatureMouseOverHandler.TYPE, new MyFeatureMouseOverHandler());

		// Define the layout:
		ResizeLayoutPanel resizeLayoutPanel = new ResizeLayoutPanel();
		layout = new MapLayoutPanel();
		resizeLayoutPanel.setWidget(layout);
		resizeLayoutPanel.setSize("100%", "100%");
		layout.setPresenter(mapPresenter);

		mapPanel.add(resizeLayoutPanel);

		// Initialize the map
		GeomajasServerExtension.getInstance().initializeMap(mapPresenter, "appCoreWidget", "mapCoreWidget");

		///////////////////////////////////////////////////////////////////////////////////////////
		// We have to add the FeatureMouseOverListener after the map has been initialized.
		///////////////////////////////////////////////////////////////////////////////////////////

		// add feature mouse over listener.
		mapPresenter.getEventBus().addMapInitializationHandler(new MapInitializationHandler() {
			@Override
			public void onMapInitialized(MapInitializationEvent event) {
				FeatureMouseOverListener listener = new FeatureMouseOverListener();
				mapPresenter.addMapListener(listener);
			}
		});

		///////////////////////////////////////////////////////////////////////////////////////////
		// Create the tooltip and keep track of the mouse pointer position.
		///////////////////////////////////////////////////////////////////////////////////////////

		toolTip = new ToolTip();

	}

	/**
	 * Handler that handles FeatureMouseOverEvent.
	 */
	private class MyFeatureMouseOverHandler implements FeatureMouseOverHandler {

		@Override
		public void onFeatureMouseOver(FeatureMouseOverEvent event) {

			///////////////////////////////////////////////////////////////////////////////////////////
			// Hide the tooltip when we receive a null value.
			// This means that the mouse is not hovering over a feature.
			///////////////////////////////////////////////////////////////////////////////////////////

			if (event.getFeatures() == null) {
				toolTip.hide();
				return;
			}

			List<Feature> features = event.getFeatures();

			///////////////////////////////////////////////////////////////////////////////////////////
			// Show the tooltip when there are features found.
			///////////////////////////////////////////////////////////////////////////////////////////

			if (!features.isEmpty()) {

				toolTip.clearContent();

				List<Label> content = new ArrayList<Label>();

				for (Feature feature : features) {
					final Label label = new Label(feature.getLabel());
					content.add(label);
				}

				// Calculate a position for where to show the tooltip.
				int left = RootPanel.get().getAbsoluteLeft() + layout.getAbsoluteLeft();
				int top = RootPanel.get().getAbsoluteTop() + layout.getAbsoluteTop();

				// Add some extra pixels to the tooltip so we can still drag the map.
				toolTip.addContentAndShow(
						content,
						left + (int) event.getCoordinate().getX() + 5,
						top + (int) event.getCoordinate().getY() + 5,
						false
				);

			}

			///////////////////////////////////////////////////////////////////////////////////////////
			// Log all FeatureMouseOver events in our showcase example, even when there are non found.
			///////////////////////////////////////////////////////////////////////////////////////////

			layerEventLayout.add(new Label("On FeatureMouseOverEvent: ( " + features.size()  + " feature(s) found. )"));
			for (Feature feature : features) {
				Coordinate coordinate = feature.getGeometry().getCoordinates()[0];
				layerEventLayout.add(new Label("# Feature => " + feature.getLabel()));
				layerEventLayout.add(new Label("- Coordinate x  => " + coordinate.getX()));
				layerEventLayout.add(new Label("- Coordinate y  => " + coordinate.getY()));
				layerEventLayout.add(new Label("- layer title => " + feature.getLayer().getTitle()));
			}
			layerEventLayout.add(new Label("-------------------------------------------------"));

			scrollPanel.scrollToBottom();

		}

	}
}