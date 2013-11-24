/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.client.map.render.dom;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Matrix;
import org.geomajas.gwt2.client.event.LayerAddedEvent;
import org.geomajas.gwt2.client.event.LayerHideEvent;
import org.geomajas.gwt2.client.event.LayerRefreshedEvent;
import org.geomajas.gwt2.client.event.LayerRefreshedHandler;
import org.geomajas.gwt2.client.event.LayerRemovedEvent;
import org.geomajas.gwt2.client.event.LayerShowEvent;
import org.geomajas.gwt2.client.event.LayerStyleChangedEvent;
import org.geomajas.gwt2.client.event.LayerStyleChangedHandler;
import org.geomajas.gwt2.client.event.LayerVisibilityHandler;
import org.geomajas.gwt2.client.event.LayerVisibilityMarkedEvent;
import org.geomajas.gwt2.client.event.MapCompositionHandler;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.View;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.layer.OpacitySupported;
import org.geomajas.gwt2.client.map.layer.RasterServerLayer;
import org.geomajas.gwt2.client.map.layer.VectorServerLayer;
import org.geomajas.gwt2.client.map.render.LayerRenderer;
import org.geomajas.gwt2.client.map.render.RenderingInfo;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlContainer;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlGroup;
import org.geomajas.gwt2.client.service.DomService;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Layer renderer implementation for layers that use renderers at fixed scales.
 * 
 * @author Pieter De Graef
 */
public class FixedScaleLayerRenderer implements LayerRenderer, OpacitySupported {

	private final ViewPort viewPort;

	private final Layer layer;

	private final Map<Integer, FixedScaleRenderer> tileLevelRenderers;

	private final Map<Integer, HtmlContainer> tileLevelContainers;

	private HtmlContainer container;

	private FixedScaleRenderer currentRenderer;

	private FixedScaleRenderer targetRenderer;

	private int cacheSize = 3;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public FixedScaleLayerRenderer(final ViewPort viewPort, final Layer layer, final MapEventBus eventBus) {
		this.viewPort = viewPort;
		this.layer = layer;
		this.tileLevelRenderers = new HashMap<Integer, FixedScaleRenderer>();
		this.tileLevelContainers = new HashMap<Integer, HtmlContainer>();

		// Refresh the contents of this renderer when the layer is refreshed:
		eventBus.addLayerRefreshedHandler(new LayerRefreshedHandler() {

			@Override
			public void onLayerRefreshed(LayerRefreshedEvent event) {
				refresh();
			}
		}, layer);

		eventBus.addMapCompositionHandler(new MapCompositionHandler() {

			@Override
			public void onLayerRemoved(LayerRemovedEvent event) {
				if (event.getLayer() == layer) {
					clear();
				}
			}

			@Override
			public void onLayerAdded(LayerAddedEvent event) {
			}
		});

		// When a layers visibility status changes, the rendering must change accordingly:
		eventBus.addLayerVisibilityHandler(new LayerVisibilityHandler() {

			@Override
			public void onVisibilityMarked(LayerVisibilityMarkedEvent event) {
			}

			@Override
			public void onShow(LayerShowEvent event) {
				if (container != null) {
					container.setVisible(true);
					render(new RenderingInfo(container, viewPort.getView(), null));
				}
			}

			@Override
			public void onHide(LayerHideEvent event) {
				if (container != null) {
					container.setVisible(false);
				}
			}
		}, layer);

		// Refresh the rendering when the style changes:
		eventBus.addLayerStyleChangedHandler(new LayerStyleChangedHandler() {

			@Override
			public void onLayerStyleChanged(LayerStyleChangedEvent event) {
				refresh();
			}
		}, layer);
	}

	// ------------------------------------------------------------------------
	// LayerRenderer implementation:
	// ------------------------------------------------------------------------

	@Override
	public Layer getLayer() {
		return layer;
	}

	@Override
	public void render(RenderingInfo renderingInfo) {
		if (!(renderingInfo.getWidget() instanceof HtmlContainer)) {
			throw new IllegalArgumentException("This implementation requires HtmlContainers to render.");
		}
		if (renderingInfo.getView() == null) {
			throw new IllegalArgumentException("No view is specified.");
		}
		setContainer((HtmlContainer) renderingInfo.getWidget());

		// Prepare the target view. Try to make sure it's rendered when the animation arrives there:
		View targetView = renderingInfo.getView();
		if (renderingInfo.getTrajectory() != null) {
			targetView = renderingInfo.getTrajectory().getView(1.0);
		}
		prepareView(container, targetView);

		// Now render the current view:
		try {
			FixedScaleRenderer renderer = getRendererForView(renderingInfo.getView());
			renderTileLevel(renderer, renderingInfo.getView().getScale());
			cleanupCache();
		} catch (Exception e) {
		}
	}
	
	// ------------------------------------------------------------------------
	// OpacitySupported implementation:
	// ------------------------------------------------------------------------

	@Override
	public void setOpacity(double opacity) {
		container.setOpacity(opacity);
	}

	@Override
	public double getOpacity() {
		return container.getOpacity();
	}

	// ------------------------------------------------------------------------
	// Protected & private methods:
	// ------------------------------------------------------------------------

	protected FixedScaleRenderer getRendererForView(View view) throws IllegalStateException {
		int tileLevel = viewPort.getFixedScaleIndex(view.getScale());

		// Do we have a renderer at the tileLevel that is rendered?
		FixedScaleRenderer renderer = getOrCreateTileLevelRenderer(tileLevel, view);
		if (currentRenderer == null || renderer.isRendered(view)) {
			return renderer;
		}
		return currentRenderer;
	}

	protected void prepareView(IsWidget widget, View targetView) {
		// Given a trajectory, try to fetch the target tiles before rendering.
		int tileLevel = viewPort.getFixedScaleIndex(targetView.getScale());
		if (tileLevel < viewPort.getFixedScaleCount()) {
			targetRenderer = getOrCreateTileLevelRenderer(tileLevel, targetView);
			targetRenderer.render(targetView);
		}
	}

	protected FixedScaleRenderer getOrCreateTileLevelRenderer(int tileLevel, View view) {
		// Can we find it?
		if (tileLevelRenderers.containsKey(tileLevel)) {
			return tileLevelRenderers.get(tileLevel);
		}

		// If we can't find it, we create it:
		HtmlContainer tileLevelContainer = new HtmlGroup();
		tileLevelContainer.asWidget().getElement().setId("TileLevel-" + tileLevel);

		// Set origin:
		Matrix translation = viewPort.getTransformationService().getTranslationMatrix(view);
		tileLevelContainer.setOrigin(new Coordinate(translation.getDx(), translation.getDy()));

		FixedScaleRenderer renderer = null;
		try {
			if (layer instanceof RasterServerLayer) {
				renderer = new RasterServerLayerScaleRenderer((RasterServerLayer) layer, tileLevel,
						viewPort.getFixedScale(tileLevel), viewPort, tileLevelContainer);
			} else if (layer instanceof VectorServerLayer) {
				renderer = new VectorServerLayerScaleRenderer((VectorServerLayer) layer, tileLevel,
						viewPort.getFixedScale(tileLevel), viewPort, tileLevelContainer);
			}
		} catch (Exception e) {
		}
		if (renderer == null) {
			throw new IllegalStateException("Cannot create a TileLevelRenderer for layer " + layer.getTitle());
		}
		renderer.addTileLevelRenderedHandler(new TileLevelRenderedHandler() {

			@Override
			public void onTileLevelRendered(TileLevelRenderedEvent event) {
				FixedScaleRenderer renderer = event.getRenderer();

				// See if we can replace the current renderer with the one that just rendered:
				int viewPortTileLevel = viewPort.getFixedScaleIndex(viewPort.getScale());
				if (renderer.getTileLevel() == viewPortTileLevel) {
					if (!renderer.isRendered(viewPort.getView())) {
						// TODO are we sure about this? Why else did we prepare this view?
						prepareView(tileLevelContainers.get(viewPortTileLevel), viewPort.getView());
					}

					// Render this tile level:
					renderTileLevel(renderer, viewPort.getScale());
				}
			}
		});
		container.insert(tileLevelContainer, 0);
		tileLevelRenderers.put(tileLevel, renderer);
		tileLevelContainers.put(tileLevel, tileLevelContainer);
		return renderer;
	}

	protected void renderTileLevel(FixedScaleRenderer renderer, double currentScale) {
		// Set the current renderer:
		currentRenderer = renderer;

		// Apply the correct transformation on the container:
		double rendererScale = viewPort.getFixedScale(renderer.getTileLevel());
		Matrix transformation = viewPort.getTransformationService().getTranslationMatrix(currentScale);
		HtmlContainer tileLevelContainer = tileLevelContainers.get(renderer.getTileLevel());
		Coordinate origin = tileLevelContainer.getOrigin();
		tileLevelContainer.applyScale(currentScale / rendererScale, 0, 0);
		double left = transformation.getDx() - origin.getX() * tileLevelContainer.getScale();
		double top = transformation.getDy() - origin.getY() * tileLevelContainer.getScale();
		tileLevelContainer.setLeft((int) Math.round(left));
		tileLevelContainer.setTop((int) Math.round(top));

		// Now make sure it's visible:
		container.bringToFront(tileLevelContainer);
		setVisible(renderer.getTileLevel());
	}

	protected void setContainer(HtmlContainer container) {
		if (this.container == null || this.container != container) {
			this.container = container;
			DomService.applyTransition(this.container.asWidget().getElement(), new String[] { "opacity" },
					new Integer[] { 300 });
		}
	}

	protected void setVisible(int tileLevel) {
		// First set the correct level to visible, so as to make sure the map never gets white:
		tileLevelContainers.get(tileLevel).setVisible(true);

		// Now go over all containers (we could leave out the correct one here...):
		for (Entry<Integer, HtmlContainer> containerEntry : tileLevelContainers.entrySet()) {
			containerEntry.getValue().setVisible(tileLevel == containerEntry.getKey());
		}
	}

	protected void refresh() {
		clear();
		render(new RenderingInfo(container, viewPort.getView(), null));
	}

	private void clear() {
		currentRenderer = null;
		tileLevelRenderers.clear();
		tileLevelContainers.clear();
		if (container != null) {
			container.clear();
		}
	}

	private void cleanupCache() {
		while (tileLevelRenderers.size() > cacheSize) {
			int distance = -1;
			int tileLevel = -1;
			for (FixedScaleRenderer renderer : tileLevelRenderers.values()) {
				if (renderer != currentRenderer && renderer != targetRenderer) {
					int d;
					if (targetRenderer != null) {
						d = Math.abs(targetRenderer.getTileLevel() - renderer.getTileLevel());
					} else {
						d = Math.abs(currentRenderer.getTileLevel() - renderer.getTileLevel());
					}
					if (d > distance) {
						distance = d;
						tileLevel = renderer.getTileLevel();
					}
				}
			}
			if (tileLevel < 0) {
				return;
			}
			removeTileLevel(tileLevel);
		}
	}

	private void removeTileLevel(int tileLevel) {
		if (container != null) {
			HtmlContainer toRemove = tileLevelContainers.get(tileLevel);
			container.remove(toRemove);
		}
		tileLevelRenderers.remove(tileLevel);
		tileLevelContainers.remove(tileLevel);
	}
}