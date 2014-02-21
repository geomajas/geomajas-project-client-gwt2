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
import org.geomajas.plugin.tms.client.configuration.TileMapInfo;
import org.geomajas.plugin.tms.client.configuration.TileSetInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

	private final List<Double> tileLevels = new ArrayList<Double>();

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

	/**
	 * Create a new layer using a TileMapInfo object.
	 *
	 * @param tileMapInfo
	 */
	public TmsLayer(TileMapInfo tileMapInfo) {
		super(tileMapInfo.getTitle());
		this.tileConfiguration = new TileConfiguration(tileMapInfo.getTileFormat().getWidth(),
				tileMapInfo.getTileFormat().getHeight(), tileMapInfo.getOrigin());
		this.layerConfiguration = new TmsLayerConfiguration();
		this.layerConfiguration.setBaseUrl(tileMapInfo.getHref());
		this.layerConfiguration.setFileExtension(tileMapInfo.getTileFormat().getExtension());
		this.tileRenderer = new TmsTileRenderer(layerConfiguration);

		for (TileSetInfo tileSetInfo : tileMapInfo.getTileSets()) {
			tileLevels.add(1.0 / tileSetInfo.getUnitsPerPixel());
		}
		Collections.sort(tileLevels);
	}

	// ------------------------------------------------------------------------
	// TileBasedLayer implementation:
	// ------------------------------------------------------------------------

	@Override
	public TileConfiguration getTileConfiguration() {
		return tileConfiguration;
	}

	@Override
	public List<Double> getTileLevels() {
		return tileLevels;
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
