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
import org.geomajas.gwt2.client.event.LayerStyleChangedEvent;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.View;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.AbstractLayer;
import org.geomajas.gwt2.client.map.layer.LegendConfig;
import org.geomajas.gwt2.client.map.render.FixedScaleLayerRenderer;
import org.geomajas.gwt2.client.map.render.FixedScaleRenderer;
import org.geomajas.gwt2.client.map.render.LayerRenderer;
import org.geomajas.gwt2.client.map.render.Tile;
import org.geomajas.gwt2.client.map.render.TileCode;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlContainer;
import org.geomajas.plugin.wms.client.WmsClient;
import org.geomajas.plugin.wms.client.capabilities.WmsLayerInfo;
import org.geomajas.plugin.wms.client.service.WmsTileServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of a {@link WmsLayer}.
 *
 * @author Pieter De Graef
 * @author An Buyle
 */
public class WmsLayerImpl extends AbstractLayer implements WmsLayer {

	public static final String DEFAULT_LEGEND_FONT_FAMILY = "Arial";

	public static final int DEFAULT_LEGEND_FONT_SIZE = 13;

	protected final WmsLayerConfiguration wmsConfig;

	protected final WmsTileConfiguration tileConfig;

	protected final WmsLayerInfo layerCapabilities;

	protected LayerRenderer renderer;

	private double opacity = 1.0;

	public WmsLayerImpl(String title, WmsLayerConfiguration wmsConfig, WmsTileConfiguration tileConfig,
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
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	@Override
	public WmsLayerConfiguration getConfig() {
		return wmsConfig;
	}

	@Override
	public WmsTileConfiguration getTileConfig() {
		return tileConfig;
	}

	@Override
	public WmsLayerInfo getCapabilities() {
		return layerCapabilities;
	}

	/**
	 * Returns the view port CRS. This layer should always have the same CRS as the map!
	 *
	 * @return The layer CRS (=map CRS).
	 */
	public String getCrs() {
		return viewPort.getCrs();
	}

	@Override
	public ViewPort getViewPort() {
		return viewPort;
	}

	// ------------------------------------------------------------------------
	// OpacitySupported implementation:
	// ------------------------------------------------------------------------

	@Override
	public void setOpacity(double opacity) {
		this.opacity = opacity;
		// TODO fix this...
		// if (null != renderer) {
		// renderer.getHtmlContainer().setOpacity(opacity);
		eventBus.fireEvent(new LayerStyleChangedEvent(this));
		// }
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
			if (viewPort.getScale() >= wmsConfig.getMinimumScale() && viewPort.getScale() <
					wmsConfig.getMaximumScale()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public LayerRenderer getRenderer() {
		if (renderer == null) {
			renderer = new FixedScaleLayerRenderer(viewPort, this, eventBus) {

				@Override
				public FixedScaleRenderer createNewScaleRenderer(int tileLevel, View view, HtmlContainer container) {
					return new WmsTileLevelRenderer(WmsLayerImpl.this, tileLevel, viewPort, container);
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
		List<TileCode> codes = WmsTileServiceImpl.getInstance().getTileCodesForBounds(getViewPort(), tileConfig,
				worldBounds, scale);
		List<Tile> tiles = new ArrayList<Tile>();
		if (!codes.isEmpty()) {
			double actualScale = viewPort.getFixedScale(codes.get(0).getTileLevel());
			for (TileCode code : codes) {
				Bbox bounds = WmsTileServiceImpl.getInstance().getWorldBoundsForTile(getViewPort(), tileConfig, code);
				Tile tile = new Tile(getScreenBounds(actualScale, bounds));
				tile.setCode(code);
				tile.setUrl(WmsClient.getInstance().getWmsService().getMapUrl(getConfig(), getCrs(), bounds,
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

	private Bbox getScreenBounds(double scale, Bbox worldBounds) {
		return new Bbox(Math.round(scale * worldBounds.getX()), -Math.round(scale * worldBounds.getMaxY()),
				Math.round(scale * worldBounds.getMaxX()) - Math.round(scale * worldBounds.getX()), Math.round(scale
				* worldBounds.getMaxY())
				- Math.round(scale * worldBounds.getY()));
	}
}
