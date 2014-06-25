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

package org.geomajas.gwt2.example.client.sample.layer;

import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.event.LayerAddedEvent;
import org.geomajas.gwt2.client.event.LayerRemovedEvent;
import org.geomajas.gwt2.client.event.MapCompositionHandler;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.example.base.client.resource.ShowcaseResource;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates changing layer order.
 * 
 * @author Pieter De Graef
 */
public class LayerOrderPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, LayerOrderPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private static final ShowcaseResource RESOURCE = GWT.create(ShowcaseResource.class);

	private MapPresenter mapPresenter;

	private PickupDragController layerDragController;

	@UiField
	protected VerticalPanel layerPanel;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	@UiField
	protected AbsolutePanel dndBoundary;

	public Widget asWidget() {
		RESOURCE.css().ensureInjected();

		// Define the left layout:
		Widget layout = UI_BINDER.createAndBindUi(this);

		layerDragController = new PickupDragController(dndBoundary, false);
		layerDragController.setBehaviorMultipleSelection(false);
		layerDragController.registerDropController(new VerticalPanelDropController(layerPanel));
		layerDragController.addDragHandler(new LayerDragHandler());

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter();
		mapPresenter.setSize(480, 480);
		mapPresenter.getEventBus().addMapCompositionHandler(new MyLayerAddHandler());

		// Define the whole layout:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		// Initialize the map, and return the layout:
		GeomajasServerExtension.getInstance().initializeMap(mapPresenter, "gwt-app", "mapLegend");
		return layout;
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * DragHandler that triggers the moving of layers in the LayersModel.
	 * 
	 * @author Pieter De Graef
	 */
	private class LayerDragHandler implements DragHandler {

		private Layer dragLayer;

		public void onDragEnd(DragEndEvent event) {
			// The order has been reversed to better display the situation on the map....
			int dropIndex = layerPanel.getWidgetIndex(event.getContext().selectedWidgets.get(0));
			mapPresenter.getLayersModel().moveLayer(dragLayer,
					mapPresenter.getLayersModel().getLayerCount() - dropIndex);
		}

		public void onDragStart(DragStartEvent event) {
			dragLayer = ((LayerWidget) event.getSource()).getLayer();
		}

		public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
		}

		public void onPreviewDragStart(DragStartEvent event) throws VetoDragException {
		}
	}

	/**
	 * Add a draggable label to the layer panel.
	 * 
	 * @author Pieter De Graef
	 */
	private class MyLayerAddHandler implements MapCompositionHandler {

		@Override
		public void onLayerAdded(LayerAddedEvent event) {
			LayerWidget widget = new LayerWidget(event.getLayer());
			layerDragController.makeDraggable(widget);
			layerPanel.insert(widget, mapPresenter.getLayersModel().getLayerCount()
					- mapPresenter.getLayersModel().getLayerPosition(event.getLayer()));
		}

		@Override
		public void onLayerRemoved(LayerRemovedEvent event) {
		}
	}

	/**
	 * Definition of a layer label widget.
	 * 
	 * @author Pieter De Graef
	 */
	private final class LayerWidget extends Label {

		private Layer layer;

		private LayerWidget(Layer layer) {
			super(layer.getTitle());
			setWidth("100%");
			this.layer = layer;
			addStyleName(RESOURCE.css().sampleHasBorder());
		}

		public Layer getLayer() {
			return layer;
		}
	}
}