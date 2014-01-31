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

package org.geomajas.gwt2.client.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.geometry.Matrix;
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt.client.util.Dom;
import org.geomajas.gwt2.client.controller.MapController;
import org.geomajas.gwt2.client.controller.MapEventParserImpl;
import org.geomajas.gwt2.client.controller.NavigationController;
import org.geomajas.gwt2.client.controller.TouchNavigationController;
import org.geomajas.gwt2.client.event.LayerAddedEvent;
import org.geomajas.gwt2.client.event.LayerRemovedEvent;
import org.geomajas.gwt2.client.event.MapCompositionHandler;
import org.geomajas.gwt2.client.event.MapInitializationEvent;
import org.geomajas.gwt2.client.event.MapResizedEvent;
import org.geomajas.gwt2.client.event.ViewPortChangedEvent;
import org.geomajas.gwt2.client.event.ViewPortChangedHandler;
import org.geomajas.gwt2.client.gfx.CanvasContainer;
import org.geomajas.gwt2.client.gfx.TransformableWidgetContainer;
import org.geomajas.gwt2.client.gfx.VectorContainer;
import org.geomajas.gwt2.client.map.layer.LayersModel;
import org.geomajas.gwt2.client.map.layer.LayersModelImpl;
import org.geomajas.gwt2.client.map.render.LayersModelRenderer;
import org.geomajas.gwt2.client.map.render.LayersModelRendererImpl;
import org.geomajas.gwt2.client.map.render.RenderingInfo;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlContainer;
import org.geomajas.gwt2.client.widget.MapWidgetImpl;
import org.geomajas.gwt2.client.widget.control.scalebar.Scalebar;
import org.geomajas.gwt2.client.widget.control.watermark.Watermark;
import org.geomajas.gwt2.client.widget.control.zoom.ZoomControl;
import org.geomajas.gwt2.client.widget.control.zoomtorect.ZoomToRectangleControl;
import org.vaadin.gwtgraphics.client.Transformable;

import com.google.gwt.event.dom.client.HasAllGestureHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
import com.google.gwt.event.dom.client.HasMouseWheelHandlers;
import com.google.gwt.event.dom.client.HasTouchCancelHandlers;
import com.google.gwt.event.dom.client.HasTouchEndHandlers;
import com.google.gwt.event.dom.client.HasTouchMoveHandlers;
import com.google.gwt.event.dom.client.HasTouchStartHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Default implementation of the map presenter interface. In other words this is the default GWT map object.
 * 
 * @author Pieter De Graef
 */
public final class MapPresenterImpl implements MapPresenter {

	/**
	 * Map view definition.
	 * 
	 * @author Pieter De Graef
	 */
	public interface MapWidget extends HasMouseDownHandlers, HasMouseUpHandlers, HasMouseOutHandlers,
			HasMouseOverHandlers, HasMouseMoveHandlers, HasMouseWheelHandlers, HasDoubleClickHandlers, IsWidget,
			RequiresResize, HasTouchStartHandlers, HasTouchEndHandlers, HasTouchCancelHandlers, HasTouchMoveHandlers,
			HasAllGestureHandlers {

		/**
		 * Returns the HTML container of the map. This is a normal HTML container that contains the images of rasterized
		 * tiles (both vector and raster layers).
		 * 
		 * @return the container
		 */
		HtmlContainer getMapHtmlContainer();
//
//		/**
//		 * Returns the vector container that contains the vectorized tiles (SVG/VML) of vector layers.
//		 *
//		 * @return the container
//		 */
//		VectorContainer getMapVectorContainer();
//
//		/**
//		 * Returns the list of user-defined vector containers for world-space objects.
//		 *
//		 * @return the container
//		 */
//		List<VectorContainer> getWorldVectorContainers();

		/**
		 * Returns the list of user-defined containers (vector + canvas) for world-space objects.
		 * 
		 * @return the container
		 */
		List<Transformable> getWorldTransformables();

		/**
		 * Returns a new user-defined container for screen space objects.
		 * 
		 * @return the container
		 */
		VectorContainer getNewScreenContainer();

		/**
		 * Returns a new user-defined container for world space objects.
		 * 
		 * @return the container
		 */
		VectorContainer getNewWorldContainer();

		/**
		 * Returns a new user-defined container for world space widgets.
		 * 
		 * @return the container
		 */
		TransformableWidgetContainer getNewWorldWidgetContainer();

		/**
		 * Removes a user-defined container.
		 * 
		 * @param container
		 *            container
		 * @return true if removed, false if unknown
		 */
		boolean removeVectorContainer(VectorContainer container);

		/**
		 * Removes a user-defined container.
		 * 
		 * @param container
		 *            container
		 * @return true if removed, false if unknown
		 */
		boolean removeWorldWidgetContainer(TransformableWidgetContainer container);

		/**
		 * Brings the user-defined container to the front (relative to its world-space or screen-space peers!).
		 * 
		 * @param container
		 *            container
		 * @return true if successful
		 */
		boolean bringToFront(VectorContainer container);

		/**
		 * Returns a new user-defined container of map gadgets.
		 * 
		 * @return the container
		 */
		AbsolutePanel getWidgetContainer();

		/**
		 * Get the total width of the view.
		 * 
		 * @return width in pixels
		 */
		int getWidth();

		/**
		 * Get the total height of the view.
		 * 
		 * @return height in pixels
		 */
		int getHeight();

		/**
		 * Set the total size of the view.
		 * 
		 * @param width
		 *            width
		 * @param height
		 *            height
		 */
		void setPixelSize(int width, int height);

		CanvasContainer getNewWorldCanvas();
	}

	private final MapEventBus eventBus;

	private final MapEventParser mapEventParser;

	private final LayersModel layersModel;

	private final ViewPortImpl viewPort;

	private final MapWidget display;

	private final ContainerManager containerManager;

	private final Map<MapController, List<HandlerRegistration>> listeners;

	private MapConfiguration configuration;

	private List<HandlerRegistration> handlers;

	private MapController mapController;

	private MapController fallbackController;

	private LayersModelRenderer renderer;

	private boolean isTouchSupported;

	public MapPresenterImpl(final EventBus eventBus) {
		this.handlers = new ArrayList<HandlerRegistration>();
		this.listeners = new HashMap<MapController, List<HandlerRegistration>>();
		this.eventBus = new MapEventBusImpl(this, eventBus);
		this.display = new MapWidgetImpl();
		this.viewPort = new ViewPortImpl(this.eventBus);
		this.layersModel = new LayersModelImpl(this.viewPort, this.eventBus);
		this.mapEventParser = new MapEventParserImpl(this);
		this.renderer = new LayersModelRendererImpl(layersModel, viewPort, this.eventBus);
		this.containerManager = new ContainerManagerImpl(display, viewPort);
		this.isTouchSupported = Dom.isTouchSupported();

		this.eventBus.addViewPortChangedHandler(new ViewPortChangedHandler() {

			@Override
			public void onViewPortChanged(ViewPortChangedEvent event) {
				renderer.render(new RenderingInfo(display.getMapHtmlContainer(), event.getTo(), event.getTrajectory()));
			}
		});
		this.eventBus.addMapCompositionHandler(new MapCompositionHandler() {

			@Override
			public void onLayerRemoved(LayerRemovedEvent event) {
			}

			@Override
			public void onLayerAdded(LayerAddedEvent event) {
				if (layersModel.getLayerCount() == 1) {
					renderer.setAnimated(event.getLayer(), true);
				}
			}
		});

		this.eventBus.addViewPortChangedHandler(new WorldTransformableRenderer());

		if (isTouchSupported) {
			fallbackController = new TouchNavigationController();
		} else {
			fallbackController = new NavigationController();
		}

		setMapController(fallbackController);
		setSize(100, 100);
	}

	// ------------------------------------------------------------------------
	// MapPresenter implementation:
	// ------------------------------------------------------------------------

	public void initialize(MapConfiguration configuration) {
		this.configuration = configuration;

		// Apply this configuration on the LayersModelRenderer:
		if (renderer instanceof LayersModelRendererImpl) {
			((LayersModelRendererImpl) renderer).setMapConfiguration(configuration);
		}

		// Configure the ViewPort. This will immediately zoom to the initial bounds:
		// viewPort.setMapSize(display.getWidth(), display.getHeight());
		viewPort.initialize(configuration);

		// Immediately zoom to the initial bounds as configured:
		viewPort.applyBounds(configuration.getHintValue(MapConfiguration.INITIAL_BOUNDS), ZoomOption.LEVEL_CLOSEST);
		renderer.render(new RenderingInfo(display.getMapHtmlContainer(), viewPort.getView(), null));

		// Adding the default map control widgets:
		if (getWidgetPane() != null) {
			getWidgetPane().add(new Watermark());
			getWidgetPane().add(new Scalebar(MapPresenterImpl.this));
			getWidgetPane().add(new ZoomControl(MapPresenterImpl.this));
			if (!isTouchSupported) {
				getWidgetPane().add(new ZoomToRectangleControl(MapPresenterImpl.this));
			}
		}
		// Fire initialization event
		eventBus.fireEvent(new MapInitializationEvent());
	}

	@Override
	public Widget asWidget() {
		return display.asWidget();
	}

	public void setRenderer(LayersModelRenderer renderer) {
		this.renderer = renderer;
	}

	public LayersModelRenderer getLayersModelRenderer() {
		return renderer;
	}

	@Override
	public MapConfiguration getConfiguration() {
		return configuration;
	}

	@Override
	public void setSize(int width, int height) {
		display.setPixelSize(width, height);
		viewPort.setMapSize(width, height);
		eventBus.fireEvent(new MapResizedEvent(width, height));
	}

	@Override
	public LayersModel getLayersModel() {
		return layersModel;
	}

	@Override
	public ViewPort getViewPort() {
		return viewPort;
	}

	@Override
	public MapEventBus getEventBus() {
		return eventBus;
	}

	@Override
	public void setMapController(MapController mapController) {
		for (HandlerRegistration registration : handlers) {
			registration.removeHandler();
		}
		if (this.mapController != null) {
			this.mapController.onDeactivate(this);
			this.mapController = null;
		}
		handlers = new ArrayList<HandlerRegistration>();
		if (null == mapController) {
			mapController = fallbackController;
		}
		if (mapController != null) {
			if (isTouchSupported) {
				handlers.add(display.addTouchStartHandler(mapController));
				handlers.add(display.addTouchMoveHandler(mapController));
				handlers.add(display.addTouchCancelHandler(mapController));
				handlers.add(display.addGestureStartHandler(mapController));
				handlers.add(display.addGestureChangeHandler(mapController));
				handlers.add(display.addGestureEndHandler(mapController));

			} else {
				handlers.add(display.addMouseDownHandler(mapController));
				handlers.add(display.addMouseMoveHandler(mapController));
				handlers.add(display.addMouseOutHandler(mapController));
				handlers.add(display.addMouseOverHandler(mapController));
				handlers.add(display.addMouseUpHandler(mapController));
				handlers.add(display.addMouseWheelHandler(mapController));
				handlers.add(display.addDoubleClickHandler(mapController));
			}

			this.mapController = mapController;
			mapController.onActivate(this);
		}
	}

	@Override
	public MapController getMapController() {
		return mapController;
	}

	@Override
	public boolean addMapListener(MapController mapListener) {
		if (mapListener != null && !listeners.containsKey(mapListener)) {
			List<HandlerRegistration> registrations = new ArrayList<HandlerRegistration>();

			if (isTouchSupported) {
				registrations.add(display.addTouchStartHandler(mapListener));
				registrations.add(display.addTouchMoveHandler(mapListener));
				registrations.add(display.addTouchCancelHandler(mapListener));
				registrations.add(display.addGestureStartHandler(mapListener));
				registrations.add(display.addGestureChangeHandler(mapListener));
				registrations.add(display.addGestureEndHandler(mapListener));
			} else {
				registrations.add(display.addMouseDownHandler(mapListener));
				registrations.add(display.addMouseMoveHandler(mapListener));
				registrations.add(display.addMouseOutHandler(mapListener));
				registrations.add(display.addMouseOverHandler(mapListener));
				registrations.add(display.addMouseUpHandler(mapListener));
				registrations.add(display.addMouseWheelHandler(mapListener));
			}

			mapListener.onActivate(this);
			listeners.put(mapListener, registrations);
			return true;
		}
		return false;
	}

	@Override
	public boolean removeMapListener(MapController mapListener) {
		if (mapListener != null && listeners.containsKey(mapListener)) {
			List<HandlerRegistration> registrations = listeners.get(mapListener);
			for (HandlerRegistration registration : registrations) {
				registration.removeHandler();
			}
			listeners.remove(mapListener);
			mapListener.onDeactivate(this);
			return true;
		}
		return false;
	}

	@Override
	public Collection<MapController> getMapListeners() {
		return listeners.keySet();
	}

	@Override
	public void setCursor(String cursor) {
		DOM.setStyleAttribute(display.asWidget().getElement(), "cursor", cursor);
	}

	@Override
	public MapEventParser getMapEventParser() {
		return mapEventParser;
	}

	@Override
	public HasWidgets getWidgetPane() {
		return display.getWidgetContainer();
	}

	@Override
	public ContainerManager getContainerManager() {
		return containerManager;
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * Handler that redraws all world space objects whenever the view on the map changes.
	 * 
	 * @author Pieter De Graef
	 */
	private class WorldTransformableRenderer implements ViewPortChangedHandler {

		@Override
		public void onViewPortChanged(ViewPortChangedEvent event) {
			Matrix matrix = viewPort.getTransformationService().getTransformationMatrix(RenderSpace.WORLD,
					RenderSpace.SCREEN);

			for (Transformable worldTransformable : display.getWorldTransformables()) {
				worldTransformable.setTranslation(matrix.getDx(), matrix.getDy());
				worldTransformable.setScale(matrix.getXx(), matrix.getYy());
			}
		}
	}
}
