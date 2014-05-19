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

package org.geomajas.gwt2.client.map.render.canvas;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.gwt2.client.event.LayerAddedEvent;
import org.geomajas.gwt2.client.event.LayerOrderChangedEvent;
import org.geomajas.gwt2.client.event.LayerOrderChangedHandler;
import org.geomajas.gwt2.client.event.LayerRemovedEvent;
import org.geomajas.gwt2.client.event.MapCompositionHandler;
import org.geomajas.gwt2.client.event.NavigationStopEvent;
import org.geomajas.gwt2.client.event.NavigationStopHandler;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.layer.LayersModel;
import org.geomajas.gwt2.client.map.render.LayerRenderer;
import org.geomajas.gwt2.client.map.render.LayersModelRenderer;
import org.geomajas.gwt2.client.map.render.RenderingInfo;

import com.google.gwt.canvas.client.Canvas;

/**
 * LayersModelRenderer implementation that uses a HTML5 Canvas tag to render upon. Keeps layers synchronized and
 * re-renders continuously.
 * 
 * @author Jan De Moerloose
 */
public class CanvasLayersModelRenderer implements LayersModelRenderer {

	private final LayersModel layersModel;

	private final ViewPort viewPort;

	private final Map<Layer, LayerRenderer> layerRenderers;

	private Canvas canvas;

	public CanvasLayersModelRenderer(MapPresenter mapPresenter) {
		layersModel = mapPresenter.getLayersModel();
		viewPort = mapPresenter.getViewPort();
		this.layerRenderers = new HashMap<Layer, LayerRenderer>();
		MapEventBus eventBus = mapPresenter.getEventBus();		
		// render all layers
		for (int i = 0; i < layersModel.getLayerCount(); i++) {
			LayerRenderer layerRenderer = layersModel.getLayer(i).getRenderer();
			if (layerRenderer != null) {
				registerLayerRenderer(layersModel.getLayer(i), layerRenderer);
			}
		}

		// Keep the list of LayerRenderers synchronized with the list of layers:
		eventBus.addMapCompositionHandler(new MapCompositionHandler() {

			@Override
			public void onLayerRemoved(LayerRemovedEvent event) {
				layerRenderers.remove(event.getLayer());
				renderAll(getCurrentRenderingInfo());
			}

			@Override
			public void onLayerAdded(LayerAddedEvent event) {
				LayerRenderer layerRenderer = event.getLayer().getRenderer();
				if (layerRenderer != null) {
					registerLayerRenderer(event.getLayer(), layerRenderer);
				}
				renderAll(getCurrentRenderingInfo());
			}
		});

		// Keep the layer order synchronized with the LayersModel:
		eventBus.addLayerOrderChangedHandler(new LayerOrderChangedHandler() {

			@Override
			public void onLayerOrderChanged(LayerOrderChangedEvent event) {
				renderAll(getCurrentRenderingInfo());
			}
		});

		eventBus.addNavigationStopHandler(new NavigationStopHandler() {

			@Override
			public void onNavigationStopped(NavigationStopEvent event) {
				// render without the trajectory to notify that new tiles should be shown (TODO)
				renderAll(getCurrentRenderingInfo());
			}
		});

	}

	private RenderingInfo getCurrentRenderingInfo() {
		return new RenderingInfo(canvas, viewPort.getView(), null);
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
		if (!(renderingInfo.getWidget() instanceof Canvas)) {
			throw new IllegalArgumentException("This renderer requires a Canvas to render in.");
		}
		canvas = (Canvas) renderingInfo.getWidget();

		renderAll(renderingInfo);
	}

	private void renderAll(RenderingInfo renderingInfo) {
		if (canvas != null) {
			// Clear the canvas
			canvas.getContext2d().clearRect(0, 0, canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceHeight());

			//TODO: get rid of this code because it is time consuming (native android browser issue canvas is not cleared)
			int w = canvas.getElement().getPropertyInt("width");
			canvas.getElement().setPropertyInt("width", 1);
			canvas.getElement().setPropertyInt("width", w);


			// Delegate to the layers in layer order:
			for (int i = 0; i < layersModel.getLayerCount(); i++) {
				Layer layer = layersModel.getLayer(i);
				// Adjust the rendering info, to use a layer specific container widget:
				RenderingInfo layerInfo = new RenderingInfo(canvas, renderingInfo.getView(),
						renderingInfo.getTrajectory());
				LayerRenderer layerRenderer = layerRenderers.get(layer);
				if (layerRenderer != null) {
					layerRenderer.render(layerInfo);
				}
			}
		}
	}

	@Override
	public boolean isAnimated(Layer layer) {
		return false;
	}

	@Override
	public void setAnimated(Layer layer, boolean animated) {

	}

}
