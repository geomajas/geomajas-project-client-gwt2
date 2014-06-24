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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.AbstractTileBasedLayer;
import org.geomajas.gwt2.client.map.layer.LegendConfig;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.client.map.render.Tile;
import org.geomajas.gwt2.client.map.render.TileCode;
import org.geomajas.gwt2.client.map.render.TileRenderer;
import org.geomajas.gwt2.client.service.TileService;
import org.geomajas.plugin.wms.client.WmsClient;
import org.geomajas.plugin.wms.client.capabilities.WmsLayerInfo;

/**
 * Default implementation of a {@link WmsLayer}.
 *
 * @author Pieter De Graef
 * @author An Buyle
 */
public class WmsLayerImpl extends AbstractTileBasedLayer implements WmsLayer {

	protected final WmsLayerConfiguration wmsConfig;

	protected final TileConfiguration tileConfig;

	protected final WmsLayerInfo layerCapabilities;

	protected TileRenderer tileRenderer;

	private double opacity = 1.0;

	public WmsLayerImpl(String title, MapConfiguration mapConfig, WmsLayerConfiguration wmsConfig,
			TileConfiguration tileConfig, WmsLayerInfo layerCapabilities) {
		super(wmsConfig.getLayers(), mapConfig, tileConfig);

		this.title = title;
		this.wmsConfig = wmsConfig;
		this.tileConfig = tileConfig;
		this.layerCapabilities = layerCapabilities;
		if (layerCapabilities != null) {
			Bbox maxBounds = layerCapabilities.getBoundingBox(mapConfig.getCrs());
			// we could transform if maxBounds = null, but that probably means the WMS is not configured correctly
			if (maxBounds != null) {
				setMaxBounds(maxBounds);
			}
		}
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
		this.tileRenderer = new WmsTileRenderer(wmsConfig, tileConfig, viewPort.getCrs());

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
	public WmsLayerConfiguration getConfiguration() {
		return wmsConfig;
	}

	@Override
	public TileConfiguration getTileConfiguration() {
		return tileConfig;
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
	
	

	// ------------------------------------------------------------------------
	// AbstractTileBasedLayer implementation:
	// ------------------------------------------------------------------------

	@Override
	public TileRenderer getTileRenderer() {
		return tileRenderer;
	}
	
	// ------------------------------------------------------------------------
	// HasMapScalesRenderer implementation:
	// ------------------------------------------------------------------------

	@Override
	public List<Tile> getTiles(double resolution, Bbox worldBounds) {
		List<TileCode> codes = TileService.getTileCodesForBounds(tileConfig, worldBounds, resolution);
		List<Tile> tiles = new ArrayList<Tile>();
		if (!codes.isEmpty()) {
			double actualResolution = tileConfig.getResolution(codes.get(0).getTileLevel());
			for (TileCode code : codes) {
				Bbox bounds = TileService.getWorldBoundsForTile(tileConfig, code);
				Tile tile = new Tile(getScreenBounds(actualResolution, bounds));
				tile.setCode(code);
				tile.setUrl(WmsClient.getInstance().getWmsService().getMapUrl(getConfiguration(), viewPort.getCrs(),
						bounds, tileConfig.getTileWidth(), tileConfig.getTileHeight()));
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
