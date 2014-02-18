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

package org.geomajas.plugin.tms.client.layer;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.View;
import org.geomajas.gwt2.client.map.layer.AbstractLayer;
import org.geomajas.gwt2.client.map.layer.tile.TileBasedLayer;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.client.map.render.LayerRenderer;
import org.geomajas.gwt2.client.map.render.TileLevelRenderer;
import org.geomajas.gwt2.client.map.render.dom.DomFixedScaleLayerRenderer;
import org.geomajas.gwt2.client.map.render.dom.DomTileLevelRenderer;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlContainer;

/**
 * Represents a layer based upon a TMS service. A TmsLayer is a {@link TileBasedLayer}. TMS layer have no other
 * functionality then just displaying.
 *
 * @author Pieter De Graef
 * @since 2.1.0
 */
@Api(allMethods = true)
public class TmsLayer extends AbstractLayer implements TileBasedLayer {

	private final TileConfiguration tileConfiguration;

	private final TmsLayerConfiguration layerConfiguration;

	private final TmsTileRenderer tileRenderer;

	private DomFixedScaleLayerRenderer renderer;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a new layer that belongs to the given map model, using the given meta-data.
	 *
	 * @param id The unique ID for this layer.
	 */
	public TmsLayer(String id, TileConfiguration tileConfiguration, TmsLayerConfiguration layerConfiguration) {
		super(id);
		this.tileConfiguration = tileConfiguration;
		this.layerConfiguration = layerConfiguration;
		this.tileRenderer = new TmsTileRenderer(layerConfiguration);
	}

	// ------------------------------------------------------------------------
	// TileBasedLayer implementation:
	// ------------------------------------------------------------------------

	@Override
	public TileConfiguration getTileConfiguration() {
		return tileConfiguration;
	}

	@Override
	public LayerRenderer getRenderer() {
		if (renderer == null) {
			renderer = new DomFixedScaleLayerRenderer(viewPort, this, eventBus) {

				@Override
				public TileLevelRenderer createNewScaleRenderer(int tileLevel, View view, HtmlContainer container) {
					return new DomTileLevelRenderer(TmsLayer.this, tileLevel, viewPort, container, tileRenderer);
				}
			};
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

	// ------------------------------------------------------------------------
	// Other public methods:
	// ------------------------------------------------------------------------

	/**
	 * Get the configuration object that is used to build URLs to the tiles.
	 *
	 * @return The layer configuration object.
	 */
	public TmsLayerConfiguration getConfiguration() {
		return layerConfiguration;
	}
}
