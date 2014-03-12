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

package org.geomajas.plugin.wms.client.layer;

import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.View;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.AbstractLayer;
import org.geomajas.gwt2.client.map.layer.LegendConfig;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.client.map.render.LayerRenderer;
import org.geomajas.gwt2.client.map.render.Tile;
import org.geomajas.gwt2.client.map.render.TileCode;
import org.geomajas.gwt2.client.map.render.TileLevelRenderer;
import org.geomajas.gwt2.client.map.render.TileRenderer;
import org.geomajas.gwt2.client.map.render.TileServiceImpl;
import org.geomajas.gwt2.client.map.render.dom.DomFixedScaleLayerRenderer;
import org.geomajas.gwt2.client.map.render.dom.DomTileLevelRenderer;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlContainer;
import org.geomajas.plugin.wms.client.WmsClient;
import org.geomajas.plugin.wms.client.capabilities.WmsLayerInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of a {@link WmsLayer}.
 *
 * @author Pieter De Graef
 * @author An Buyle
 */
public class WmsLayerImpl extends AbstractLayer implements WmsLayer {

	protected final WmsLayerConfiguration wmsConfig;

	protected final TileConfiguration tileConfig;

	protected final WmsLayerInfo layerCapabilities;

	protected TileRenderer tileRenderer;

	protected DomFixedScaleLayerRenderer renderer;

	private double opacity = 1.0;

	public WmsLayerImpl(String title, WmsLayerConfiguration wmsConfig, TileConfiguration tileConfig,
			WmsLayerInfo layerCapabilities) {
		super(wmsConfig.getLayers());

		this.title = title;
		this.wmsConfig = wmsConfig;
		this.tileConfig = tileConfig;
		this.layerCapabilities = layerCapabilities;
	}

	@Override
	protected void setEventBus(MapEventBus eventBus) {
		super.setEventBus(eventBus);
		this.wmsConfig.setParentLayer(eventBus, this);
		this.wmsConfig.setCrs(viewPort.getCrs());
	}

	@Override
	protected void setViewPort(ViewPort viewPort) {
		super.setViewPort(viewPort);
		this.tileRenderer = new WmsTileRenderer(wmsConfig, tileConfig, viewPort);

		// Install minimum and maximum resolution:
		double minResolution = -1.0, maxResolution = -1.0;
		if (layerCapabilities != null) {
			int minSD = layerCapabilities.getMinScaleDenominator();
			if (minSD > 0) {
				maxResolution = viewPort.toResolution(minSD);
			}
			int maxSD = layerCapabilities.getMaxScaleDenominator();
			if (maxSD > 0) {
				minResolution = viewPort.toResolution(maxSD);
			}
		}
		if (minResolution < 0) {
			minResolution = Double.MIN_VALUE;
		}
		if (maxResolution < 0) {
			maxResolution = Double.MAX_VALUE;
		}
		wmsConfig.setMinimumResolution(minResolution);
		wmsConfig.setMaximumResolution(maxResolution);
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	@Override
	public WmsLayerConfiguration getConfig() {
		return wmsConfig;
	}

	@Override
	public TileConfiguration getTileConfiguration() {
		return tileConfig;
	}

	@Override
	public List<Double> getTileLevels() {
		List<Double> tileLevels = new ArrayList<Double>();
		for (int i = 0; i < viewPort.getFixedScaleCount(); i++) {
			tileLevels.add(viewPort.getFixedScale(i));
		}
		return tileLevels;
	}

	@Override
	public WmsLayerInfo getCapabilities() {
		return layerCapabilities;
	}

	@Override
	public void setOpacity(double opacity) {
		this.opacity = opacity;
		getRenderer();
		renderer.setOpacity(opacity);
	}

	@Override
	public double getOpacity() {
		return opacity;
	}

	// ------------------------------------------------------------------------
	// Layer implementation:
	// ------------------------------------------------------------------------

	@Override
	public boolean isShowing() {
		if (markedAsVisible) {
			if (viewPort.getResolution() >= wmsConfig.getMinimumResolution() && viewPort.getResolution() <
					wmsConfig.getMaximumResolution()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public LayerRenderer getRenderer() {
		if (renderer == null) {
			renderer = new DomFixedScaleLayerRenderer(viewPort, this, eventBus) {

				@Override
				public TileLevelRenderer createNewScaleRenderer(int tileLevel, View view, HtmlContainer container) {
					return new DomTileLevelRenderer(WmsLayerImpl.this, tileLevel, viewPort, container, tileRenderer);
				}
			};
		}
		return renderer;
	}

	// ------------------------------------------------------------------------
	// HasMapScalesRenderer implementation:
	// ------------------------------------------------------------------------

	@Override
	public List<Tile> getTiles(double scale, Bbox worldBounds) {
		List<TileCode> codes = TileServiceImpl.getInstance().getTileCodesForBounds(viewPort, tileConfig,
				worldBounds, scale);
		List<Tile> tiles = new ArrayList<Tile>();
		if (!codes.isEmpty()) {
			double actualResolution = viewPort.getResolution(codes.get(0).getTileLevel());
			for (TileCode code : codes) {
				Bbox bounds = TileServiceImpl.getInstance().getWorldBoundsForTile(viewPort, tileConfig, code);
				Tile tile = new Tile(getScreenBounds(actualScale, bounds));
				tile.setCode(code);
				tile.setUrl(WmsClient.getInstance().getWmsService().getMapUrl(getConfig(), viewPort.getCrs(), bounds,
						tileConfig.getTileWidth(), tileConfig.getTileHeight()));
				tiles.add(tile);
			}
		}
		return tiles;
	}

	// ------------------------------------------------------------------------
	// LegendUrlSupported implementation:
	// ------------------------------------------------------------------------

	@Override
	public String getLegendImageUrl() {
		return WmsClient.getInstance().getWmsService().getLegendGraphicUrl(wmsConfig);
	}

	@Override
	public String getLegendImageUrl(LegendConfig legendConfig) {
		return WmsClient.getInstance().getWmsService().getLegendGraphicUrl(wmsConfig, legendConfig);
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private Bbox getScreenBounds(double resolution, Bbox worldBounds) {
		return new Bbox(Math.round(worldBounds.getX() / resolution), -Math.round(worldBounds.getMaxY() / resolution),
				Math.round(worldBounds.getMaxX() / resolution) - Math.round(worldBounds.getX() / resolution),
				Math.round(worldBounds.getMaxY() / resolution) - Math.round(worldBounds.getY() / resolution));
	}
}
