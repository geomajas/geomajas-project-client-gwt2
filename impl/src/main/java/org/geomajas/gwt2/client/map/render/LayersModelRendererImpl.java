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

package org.geomajas.gwt2.client.map.render;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.gwt2.client.event.LayerAddedEvent;
import org.geomajas.gwt2.client.event.LayerOrderChangedEvent;
import org.geomajas.gwt2.client.event.LayerOrderChangedHandler;
import org.geomajas.gwt2.client.event.LayerRemovedEvent;
import org.geomajas.gwt2.client.event.MapCompositionHandler;
import org.geomajas.gwt2.client.event.NavigationStartEvent;
import org.geomajas.gwt2.client.event.NavigationStartHandler;
import org.geomajas.gwt2.client.event.NavigationStopEvent;
import org.geomajas.gwt2.client.event.NavigationStopHandler;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.layer.LayersModel;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlContainer;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlGroup;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlObject;
import org.geomajas.gwt2.client.service.DomService;

/**
 * Default implementation of the {@link LayersModelRenderer}. Delegates the rendering to specific {@link LayerRenderer}
 * s. Also checks the {@link MapConfiguration} to see if layer should be animated or not before delegating.
 * 
 * @author Pieter De Graef
 */
public class LayersModelRendererImpl implements LayersModelRenderer {

	private final LayersModel layersModel;

	private final ViewPort viewPort;

	private final Map<Layer, LayerRenderer> layerRenderers;

	private final Map<Layer, HtmlContainer> layerContainers;

	private final Map<Layer, Boolean> layerAnimation;

	private MapConfiguration configuration;

	private HtmlContainer layersModelContainer;

	private boolean navigating;

	// ------------------------------------------------------------------------
	// Constructor:
	// ------------------------------------------------------------------------

	public LayersModelRendererImpl(LayersModel layersModel, ViewPort viewPort, MapEventBus eventBus) {
		this.layersModel = layersModel;
		this.viewPort = viewPort;
		this.layerRenderers = new HashMap<Layer, LayerRenderer>();
		this.layerContainers = new HashMap<Layer, HtmlContainer>();
		this.layerAnimation = new HashMap<Layer, Boolean>();

		// Keep the list of LayerRenderers synchronized with the list of layers:
		eventBus.addMapCompositionHandler(new MapCompositionHandler() {

			@Override
			public void onLayerRemoved(LayerRemovedEvent event) {
				layersModelContainer.remove(getOrCreateLayerContainer(event.getLayer()));
				layerRenderers.remove(event.getLayer());
				layerContainers.remove(event.getLayer());
			}

			@Override
			public void onLayerAdded(LayerAddedEvent event) {
				LayerRenderer layerRenderer = event.getLayer().getRenderer();
				if (layerRenderer != null) {
					registerLayerRenderer(event.getLayer(), layerRenderer);
					layerRenderer.render(new RenderingInfo(getOrCreateLayerContainer(event.getLayer()),
							LayersModelRendererImpl.this.viewPort.getView(), null));
				}
			}
		});

		// Keep the layer order synchronized with the LayersModel:
		eventBus.addLayerOrderChangedHandler(new LayerOrderChangedHandler() {

			@Override
			public void onLayerOrderChanged(LayerOrderChangedEvent event) {
				if (event.getFromIndex() < layersModelContainer.getChildCount()) {
					int toIndex = event.getToIndex() > layersModelContainer.getChildCount() ? layersModelContainer
							.getChildCount() : event.getToIndex();
					HtmlObject layerContainer = layersModelContainer.getChild(event.getFromIndex());
					if (layerContainer != null) {
						layersModelContainer.remove(layerContainer);
						layersModelContainer.insert(layerContainer, toIndex);
					}
				}
			}
		});

		eventBus.addNavigationStartHandler(new NavigationStartHandler() {

			@Override
			public void onNavigationStarted(NavigationStartEvent event) {
				navigating = true;

				// Go over all layer to see if they should be animated or not:
				for (Layer layer : layerRenderers.keySet()) {
					if (!isAnimated(layer)) {
						// This layer is not animated, hide it before the navigation starts:
						HtmlContainer layerContainer = getOrCreateLayerContainer(layer);
						LayerRenderer layerRenderer = layerRenderers.get(layer);
						layerRenderer.render(new RenderingInfo(layerContainer, event.getTrajectory().getView(0.0),
								event.getTrajectory()));
						DomService.applyTransition(layerContainer.asWidget().getElement(), new String[] { "opacity" },
								new Integer[] { 0 });
						layerContainer.asWidget().getElement().getStyle().setOpacity(0.0f);
					}
				}
			}
		});
		eventBus.addNavigationStopHandler(new NavigationStopHandler() {

			@Override
			public void onNavigationStopped(NavigationStopEvent event) {
				navigating = false;

				// Go over all layer to see if they should be animated or not:
				for (Layer layer : layerRenderers.keySet()) {
					if (!isAnimated(layer)) {
						// This layer is not animated, hide it before the navigation starts:
						HtmlContainer layerContainer = getOrCreateLayerContainer(layer);
						LayerRenderer layerRenderer = layerRenderers.get(layer);
						layerRenderer.render(new RenderingInfo(layerContainer, event.getView(), null));
						if (LayersModelRendererImpl.this.configuration != null) {
							DomService.applyTransition(layerContainer.asWidget().getElement(),
									new String[] { "opacity" },
									new Integer[] { LayersModelRendererImpl.this.configuration
											.getHintValue(MapConfiguration.FADE_IN_TIME) });
						}
						layerContainer.asWidget().getElement().getStyle().setOpacity(1.0f);
					}
				}
			}
		});
	}

	public void setMapConfiguration(MapConfiguration configuration) {
		this.configuration = configuration;
	}

	// ------------------------------------------------------------------------
	// LayerRenderer registration:
	// ------------------------------------------------------------------------

	@Override
	public void registerLayerRenderer(Layer layer, LayerRenderer layerRenderer) {
		if (layerRenderers.containsKey(layer)) {
			layerRenderers.remove(layer);
		}
		layerRenderers.put(layer, layerRenderer);
	}

	@Override
	public LayerRenderer getLayerRenderer(Layer layer) {
		return layerRenderers.get(layer);
	}

	// ------------------------------------------------------------------------
	// BasicRenderer implementation:
	// ------------------------------------------------------------------------

	@Override
	public void render(RenderingInfo renderingInfo) {
		if (renderingInfo == null) {
			throw new NullPointerException("RenderingInfo cannot be null.");
		}
		if (!(renderingInfo.getWidget() instanceof HtmlContainer)) {
			throw new IllegalArgumentException("This renderer requires a HtmlContainer to render in.");
		}
		layersModelContainer = (HtmlContainer) renderingInfo.getWidget();

		// Delegate to the layers in layer order:
		for (int i = 0; i < layersModel.getLayerCount(); i++) {
			Layer layer = layersModel.getLayer(i);

			// If we're in the middle of an animation and the layer does not support it, skip this layer:
			if (navigating && !isAnimated(layer)) {
				continue;
			}

			// Adjust the rendering info, to use a layer specific container widget:
			RenderingInfo layerInfo = new RenderingInfo(getOrCreateLayerContainer(layer), renderingInfo.getView(),
					renderingInfo.getTrajectory());
			LayerRenderer layerRenderer = layerRenderers.get(layer);
			layerRenderer.render(layerInfo);
		}
	}

	@Override
	public boolean isAnimated(Layer layer) {
		if (!layerAnimation.containsKey(layer)) {
			return false;
		}
		return layerAnimation.get(layer);
	}

	@Override
	public void setAnimated(Layer layer, boolean animated) {
		if (layerAnimation.containsKey(layer)) {
			layerAnimation.remove(layer);
		}
		layerAnimation.put(layer, animated);
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private HtmlContainer getOrCreateLayerContainer(Layer layer) {
		if (layerContainers.containsKey(layer)) {
			return layerContainers.get(layer);
		}
		HtmlContainer layerContainer = new HtmlGroup();
		layerContainer.asWidget().getElement().setId(layer.getId());
		layersModelContainer.add(layerContainer);
		layerContainers.put(layer, layerContainer);
		return layerContainer;
	}
}
