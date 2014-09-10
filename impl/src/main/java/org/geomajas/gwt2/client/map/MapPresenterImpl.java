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
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import org.geomajas.geometry.Matrix;
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt.client.util.Dom;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.animation.Trajectory;
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
import org.geomajas.gwt2.client.map.render.RenderingInfo;
import org.geomajas.gwt2.client.map.render.TileQueue;
import org.geomajas.gwt2.client.map.render.dom.DomLayersModelRenderer;
import org.geomajas.gwt2.client.map.render.dom.TilesAddedEvent;
import org.geomajas.gwt2.client.map.render.dom.TilesAddedHandler;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlContainer;
import org.geomajas.gwt2.client.widget.DefaultMapWidget;
import org.geomajas.gwt2.client.widget.control.pan.PanControl;
import org.geomajas.gwt2.client.widget.control.scalebar.Scalebar;
import org.geomajas.gwt2.client.widget.control.watermark.Watermark;
import org.geomajas.gwt2.client.widget.control.zoom.ZoomControl;
import org.geomajas.gwt2.client.widget.control.zoom.ZoomStepControl;
import org.geomajas.gwt2.client.widget.control.zoomtorect.ZoomToRectangleControl;
import org.geomajas.gwt2.client.widget.map.MapWidgetImpl;
import org.vaadin.gwtgraphics.client.Transformable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		 * @param container container
		 * @return true if removed, false if unknown
		 */
		boolean removeVectorContainer(VectorContainer container);

		/**
		 * Removes a user-defined container.
		 *
		 * @param container container
		 * @return true if removed, false if unknown
		 */
		boolean removeWorldWidgetContainer(TransformableWidgetContainer container);

		/**
		 * Brings the user-defined container to the front (relative to its world-space or screen-space peers!).
		 *
		 * @param container container
		 * @return true if successful
		 */
		boolean bringToFront(VectorContainer container);

		/**
		 * Returns a new user-defined container of map gadgets.
		 *
		 * @return the container
		 */
		HasWidgets getWidgetContainer();

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
		 * @param width  width
		 * @param height height
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

	private TileQueue tilequeue;

	private static final int MAX_LOADING_TILES = 5;

	public static final Hint<TileQueue> QUEUE = new Hint<TileQueue>("tile_queue");

	// ------------------------------------------------------------------------
	// Constructor:
	// ------------------------------------------------------------------------

	public MapPresenterImpl(final EventBus eventBus) {
		this.handlers = new ArrayList<HandlerRegistration>();
		this.listeners = new HashMap<MapController, List<HandlerRegistration>>();
		this.eventBus = new MapEventBusImpl(this, eventBus);
		this.display = new MapWidgetImpl();
		this.viewPort = new ViewPortImpl(this.eventBus);
		this.layersModel = new LayersModelImpl(this.viewPort, this.eventBus);
		this.mapEventParser = new MapEventParserImpl(this);
		this.tilequeue = new TileQueueImpl<String>(new TilePriorityFunctionImpl(), new TileKeyProviderImpl());
		this.renderer = new DomLayersModelRenderer(layersModel, viewPort, this.eventBus, tilequeue);
		this.containerManager = new ContainerManagerImpl(display, viewPort);
		this.isTouchSupported = Dom.isTouchSupported();

		this.eventBus.addViewPortChangedHandler(new ViewPortChangedHandler() {

			@Override
			public void onViewPortChanged(ViewPortChangedEvent event) {
				renderFrame(event.getTo(), event.getTrajectory());
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

		// Listen when new tiles are added
		GeomajasImpl.getInstance().getEventBus().addHandler(TilesAddedHandler.TYPE, new TilesAddedHandler() {
			@Override
			public void onTilesAdded(TilesAddedEvent event) {
				// Re-render the frame when tiles were added
				renderFrame(event.getView(), event.getTrajectory());
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
	public void initialize(MapConfiguration configuration, DefaultMapWidget... mapWidgets) {
		initialize(configuration, true, mapWidgets);
	}

	public void initialize(MapConfiguration configuration, boolean fireEvent, DefaultMapWidget... mapWidgets) {
		this.configuration = configuration;

		// Apply this configuration on the LayersModelRenderer:
		if (renderer instanceof DomLayersModelRenderer) {
			((DomLayersModelRenderer) renderer).setMapConfiguration(configuration);
		}

		// Configure the ViewPort. This will immediately zoom to the initial bounds:
		// viewPort.setMapSize(display.getWidth(), display.getHeight());
		viewPort.initialize(configuration);

		// Immediately zoom to the initial bounds as configured:
		viewPort.applyBounds(configuration.getHintValue(MapConfiguration.INITIAL_BOUNDS), ZoomOption.LEVEL_CLOSEST);
		renderFrame(viewPort.getView(), null);

		// Adding the default map control widgets:
		if (getWidgetPane() != null) {
			getWidgetPane().add(new Watermark()); // We always add the watermark...
			for (DefaultMapWidget widget : mapWidgets) {
				switch (widget) {
					case SCALEBAR:
						getWidgetPane().add(new Scalebar(MapPresenterImpl.this));
						break;
					case PAN_CONTROL:
						getWidgetPane().add(new PanControl(MapPresenterImpl.this));
						break;
					case ZOOM_CONTROL:
						getWidgetPane().add(new ZoomControl(MapPresenterImpl.this));
						break;
					case ZOOM_STEP_CONTROL:
						getWidgetPane().add(new ZoomStepControl(MapPresenterImpl.this));
						break;
					case ZOOM_TO_RECTANGLE_CONTROL:
						getWidgetPane().add(new ZoomToRectangleControl(MapPresenterImpl.this));
						break;
				}
			}
		}
		// Fire initialization event
		if (fireEvent) {
			eventBus.fireEvent(new MapInitializationEvent(this));
		}
	}

	/**
	 * Render a frame of the view. Let all renderers add tiles to the queue and then prioritize the queue.
	 * When the queue is prioritized, empty the queue with a maximum of {@link #MAX_LOADING_TILES} tiles and render
	 * these tiles.
	 *
	 * @param view       The view to render.
	 * @param trajectory The trajectory.
	 */
	public void renderFrame(View view, Trajectory trajectory) {
		// Let all layers add tiles to the queue:
		RenderingInfo info = new RenderingInfo(display.getMapHtmlContainer(), view, trajectory);
		info.setHintValue(QUEUE, tilequeue);
		renderer.render(info);

		// Prioritize tiles in the queue:
		double resolution = viewPort.getResolution();
		tilequeue.prioritize(viewPort.getResolutionIndex(resolution), resolution, viewPort.getPosition());

		// Get tiles and load them:
		int size = Math.min(MAX_LOADING_TILES, tilequeue.size());
		for (int i = 0; i < size; i++) {
			tilequeue.poll().load();
		}

		// Keep loading tiles if the queue isn't empty:
		if (tilequeue.size() > 0) {
			renderFrame(view, trajectory);
		}
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
		if (configuration == null) {
			configuration = new MapConfigurationImpl();
		}
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
