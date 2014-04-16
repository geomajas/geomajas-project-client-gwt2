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

package org.geomajas.gwt2.client.map.layer;

import org.geomajas.gwt2.client.map.View;
import org.geomajas.gwt2.client.map.layer.tile.TileBasedLayer;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.client.map.render.LayerRenderer;
import org.geomajas.gwt2.client.map.render.TileLevelLayerRenderer;
import org.geomajas.gwt2.client.map.render.TileLevelRenderer;
import org.geomajas.gwt2.client.map.render.TileRenderer;
import org.geomajas.gwt2.client.map.render.canvas.CanvasTileLevelLayerRenderer;
import org.geomajas.gwt2.client.map.render.canvas.CanvasTileLevelRenderer;
import org.geomajas.gwt2.client.map.render.canvas.container.CanvasTileGrid;
import org.geomajas.gwt2.client.map.render.dom.DomTileLevelLayerRenderer;
import org.geomajas.gwt2.client.map.render.dom.DomTileLevelRenderer;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlContainer;

import com.google.gwt.canvas.client.Canvas;

/**
 * This is an abstract class representing a basic layer that depends on tile for it's rendering. Examples are TMS layers
 * or OSM or...<br/> While this base class does provide most functionalities, it does still require an implementation of
 * the {@link #getTileRenderer()} method. Also, you must provide the hasResolutions with the correct tile level
 * resolutions. If you forget this, no rendering will ever take place!
 *
 * @author Pieter De Graef
 */
public abstract class AbstractTileBasedLayer extends AbstractLayer implements TileBasedLayer {

	protected final TileConfiguration tileConfiguration;

	protected TileLevelLayerRenderer renderer;

	/**
	 * Create a new layer that belongs to the given map model, using the given meta-data.
	 *
	 * @param id The unique ID for this layer.
	 */
	public AbstractTileBasedLayer(String id, TileConfiguration tileConfiguration) {
		super(id);
		this.tileConfiguration = tileConfiguration;
	}

	// ------------------------------------------------------------------------
	// Abstract methods:
	// ------------------------------------------------------------------------

	/**
	 * Get the renderer for individual tiles that make up the rendering for this layer.
	 *
	 * @return The tile renderer for this layer.
	 */
	public abstract TileRenderer getTileRenderer();

	// ------------------------------------------------------------------------
	// TileBasedLayer implementation:
	// ------------------------------------------------------------------------

	@Override
	public TileConfiguration getTileConfiguration() {
		return tileConfiguration;
	}

	// ------------------------------------------------------------------------
	// Layer implementation:
	// ------------------------------------------------------------------------

	@Override
	public LayerRenderer getRenderer() {
		if (renderer == null) {
			if (Canvas.isSupported()) {
				renderer = new CanvasTileLevelLayerRenderer(viewPort, this, eventBus) {

					@Override
					public CanvasTileLevelRenderer createNewScaleRenderer(int tileLevel, View view,
							CanvasTileGrid container) {
						return new CanvasTileLevelRenderer(AbstractTileBasedLayer.this, tileLevel, viewPort, container,
								getTileRenderer());
					}
				};
			} else {
				renderer = new DomTileLevelLayerRenderer(viewPort, this, eventBus) {

					@Override
					public TileLevelRenderer createNewScaleRenderer(int tileLevel, View view, HtmlContainer container) {
						return new DomTileLevelRenderer(AbstractTileBasedLayer.this, tileLevel, viewPort, container,
								getTileRenderer());
					}
				};
			}
		}
		return renderer;
	}

	@Override
	public void setOpacity(double opacity) {
		renderer.setOpacity(opacity);
	}

	@Override
	public double getOpacity() {
		return renderer.getOpacity();
	}
}
