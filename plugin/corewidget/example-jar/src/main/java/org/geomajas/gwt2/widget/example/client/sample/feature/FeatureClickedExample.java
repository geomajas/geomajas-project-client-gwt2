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
package org.geomajas.gwt2.widget.example.client.sample.feature;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import org.geomajas.gwt2.client.controller.FeatureClickedListener;
import org.geomajas.gwt2.client.event.FeatureClickedEvent;
import org.geomajas.gwt2.client.event.FeatureClickedHandler;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.widget.MapLayoutPanel;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.widget.example.client.sample.feature.tooltip.ToolTip;

import java.util.ArrayList;
import java.util.List;

/**
 * FeatureClicked showcase example.
 *
 * @author David Debuck
 */
public class FeatureClickedExample implements SamplePanel {

	protected DockLayoutPanel rootElement;

	private final MapLayoutPanel layout;

	private MapPresenter mapPresenter;

	private ToolTip toolTip;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	@UiField
	protected VerticalPanel layerEventLayout;

	@UiField
	protected ScrollPanel scrollPanel;

	@Override
	public Widget asWidget() {
		return rootElement;
	}

	/**
	 * UI binder interface.
	 *
	 * @author David Debuck
	 */
	interface FeatureSelectedExampleUiBinder extends UiBinder<DockLayoutPanel, FeatureClickedExample> {

	}

	private static final FeatureSelectedExampleUiBinder UIBINDER = GWT.create(FeatureSelectedExampleUiBinder.class);

	/**
	 * Default constructor.
	 */
	public FeatureClickedExample() {
		rootElement = UIBINDER.createAndBindUi(this);

		// Create the MapPresenter
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter();

		// add FeatureClickedHandler where we handle FeaturesClickedEvent
		mapPresenter.getEventBus().addHandler(FeatureClickedHandler.TYPE, new MyFeatureClickedHandler());

		// Define the layout:
		ResizeLayoutPanel resizeLayoutPanel = new ResizeLayoutPanel();
		layout = new MapLayoutPanel();
		resizeLayoutPanel.setWidget(layout);
		resizeLayoutPanel.setSize("100%", "100%");
		layout.setPresenter(mapPresenter);

		mapPanel.add(resizeLayoutPanel);

		// Initialize the map
		GeomajasServerExtension.getInstance().initializeMap(mapPresenter, "appCoreWidget", "mapCoreWidget");

		// add feature clicked listener.
		FeatureClickedListener mapListener = new FeatureClickedListener();
		mapPresenter.addMapListener(mapListener);

		// Create the tooltip.
		toolTip = new ToolTip();

	}

	/**
	 * Handler that handles FeatureClickedEvent.
	 */
	private class MyFeatureClickedHandler implements FeatureClickedHandler {

		@Override
		public void onFeatureClicked(FeatureClickedEvent event) {

			List<Feature> features = event.getFeatures();

			///////////////////////////////////////////////////////////////////////////////////////////
			// Show the tooltip when there are features found.
			///////////////////////////////////////////////////////////////////////////////////////////

			if (!features.isEmpty()) {

				toolTip.clearContent();

				List<Label> content = new ArrayList<Label>();

				for (Feature feature : features) {
					final Label label = new Label(feature.getLabel());
					label.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {

							layerEventLayout.add(new Label("# Feature => " + label.getText() + " selected!"));
							layerEventLayout.add(new Label("-------------------------------------------------"));
							scrollPanel.scrollToBottom();

						}
					});
					content.add(label);
				}

				// Calculate a position for where to show the tooltip.
				int left = RootPanel.get().getAbsoluteLeft() + layout.getAbsoluteLeft();
				int top = RootPanel.get().getAbsoluteTop() + layout.getAbsoluteTop();

				toolTip.addContentAndShow(
						content,
						left + (int) event.getCoordinate().getX(),
						top + (int) event.getCoordinate().getY(),
						true
				);

			} else {

				toolTip.hide();

			}

			///////////////////////////////////////////////////////////////////////////////////////////
			// Log all FeatureClicked events in our showcase example, even when there are non found.
			///////////////////////////////////////////////////////////////////////////////////////////

			layerEventLayout.add(new Label("On FeatureClickedEvent: ( " + features.size()  + " feature(s) found. )"));
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