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
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.controller.FeatureMouseOverListener;
import org.geomajas.gwt2.client.event.FeatureMouseOverEvent;
import org.geomajas.gwt2.client.event.FeatureMouseOverHandler;
import org.geomajas.gwt2.client.event.MapInitializationEvent;
import org.geomajas.gwt2.client.event.MapInitializationHandler;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.widget.MapLayoutPanel;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.widget.example.client.sample.feature.tooltip.ToolTip;

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

	private ToolTip toolTip;

	private int clientX;

	private int clientY;

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
		final MapLayoutPanel layout = new MapLayoutPanel();
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

		// Get the current position of the mouse pointer.
		RootPanel.get().addDomHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {
				clientX = event.getX();
				clientY = event.getY();
			}

		}, MouseMoveEvent.getType());

	}

	/**
	 * Handler that handles FeatureMouseOverEvent.
	 */
	private class MyFeatureMouseOverHandler implements FeatureMouseOverHandler {

		@Override
		public void onFeatureMouseOver(FeatureMouseOverEvent event) {

			List<Feature> features = event.getFeatures();

			///////////////////////////////////////////////////////////////////////////////////////////
			// Show the tooltip when there are features found.
			///////////////////////////////////////////////////////////////////////////////////////////

			if (!features.isEmpty()) {

				toolTip.clearContent();

				List<String> content = new ArrayList<String>();

				for (Feature feature : features) {
					content.add(feature.getLabel());
				}

				toolTip.addContentAndShow(content, clientX, clientY, true);

			}

			///////////////////////////////////////////////////////////////////////////////////////////
			// Log all FeatureMouseOver events in our showcase example, even when there are non found.
			///////////////////////////////////////////////////////////////////////////////////////////

			layerEventLayout.add(new Label("### " + features.size() + " feature(s) hovered"));
			for (Feature feature : features) {
				layerEventLayout.add(new Label("-- feature label => " + feature.getLabel()));
				layerEventLayout.add(new Label("-- layer title => " + feature.getLayer().getTitle()));
			}
			layerEventLayout.add(new Label(""));

			scrollPanel.scrollToBottom();

		}

	}
}