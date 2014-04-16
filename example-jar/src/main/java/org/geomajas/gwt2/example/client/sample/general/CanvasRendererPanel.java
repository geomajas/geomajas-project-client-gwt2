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
package org.geomajas.gwt2.example.client.sample.general;

import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.controller.MapController;
import org.geomajas.gwt2.client.event.MapInitializationEvent;
import org.geomajas.gwt2.client.event.MapInitializationHandler;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.example.base.client.ExampleBase;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Canvas rendering sample.
 * 
 * @author Jan De Moerloose
 * 
 */
public class CanvasRendererPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Jan De Moerloose
	 */
	interface MyUiBinder extends UiBinder<Widget, CanvasRendererPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	private PinchController pinchController;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	private MapController oldController;

	public Widget asWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Create the mapPresenter and add an InitializationHandler:
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter();
		mapPresenter.setSize(480, 480);
		mapPresenter.getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());

		// Define the whole layout:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		// Initialize the map, and return the layout:
		mapPresenter.getConfiguration().setHintValue(MapConfiguration.RENDERER_TYPE,
				MapConfiguration.RendererType.CANVAS);
		GeomajasServerExtension.getInstance().initializeMap(mapPresenter, "gwt-app", "mapOsm");
		pinchController = new PinchController(mapPresenter);
		return layout;
	}

	@UiHandler("enlargeButton")
	public void onEnlargeBtnClicked(ClickEvent event) {
		int width = mapPresenter.getViewPort().getMapWidth() + 20;
		int height = mapPresenter.getViewPort().getMapHeight() + 15;
		mapPresenter.setSize(width, height);
	}

	@UiHandler("shrinkButton")
	public void onShrinkBtnClicked(ClickEvent event) {
		int width = mapPresenter.getViewPort().getMapWidth() - 20;
		int height = mapPresenter.getViewPort().getMapHeight() - 15;
		mapPresenter.setSize(width, height);
	}

	@UiHandler("pinchButton")
	public void onTogglePinchBtnClicked(ClickEvent event) {
		if (oldController != null) {
			mapPresenter.setMapController(oldController);
			oldController = null;
		} else {
			oldController = mapPresenter.getMapController();
			mapPresenter.setMapController(pinchController);
		}
	}

	/**
	 * Map initialization handler that zooms in.
	 * 
	 * @author Pieter De Graef
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		public void onMapInitialized(MapInitializationEvent event) {
			mapPresenter.getViewPort().applyBounds(ExampleBase.BBOX_AFRICA);
		}
	}
}
