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

package org.geomajas.gwt2.plugin.print.wms.client;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.client.map.render.Tile;
import org.geomajas.gwt2.client.service.TileService;
import org.geomajas.gwt2.plugin.print.client.layerbuilder.PrintableLayersModelBuilder;
import org.geomajas.gwt2.plugin.print.wms.server.dto.WmsClientLayerInfo;
import org.geomajas.gwt2.plugin.wms.client.WmsClient;
import org.geomajas.gwt2.plugin.wms.client.layer.WmsLayer;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.plugin.rasterizing.command.dto.RasterLayerRasterizingInfo;

/**
 * Builder for WMS layer.
 *
 * @author Jan De Moerloose
 */
public class WmsLayerBuilder implements PrintableLayersModelBuilder {

	@Override
	public boolean supports(Layer object) {
		return object instanceof WmsLayer;
	}

	@Override
	public ClientLayerInfo build(MapPresenter mapPresenter, Layer layer, Bbox worldBounds, double resolution) {
		WmsLayer tileBasedLayer = (WmsLayer) layer;
		TileConfiguration tileConfig = tileBasedLayer.getTileConfiguration();

		WmsClientLayerInfo info = new WmsClientLayerInfo();
		List<RasterTile> tiles = new ArrayList<RasterTile>();
		for (Tile tile : getTiles(tileBasedLayer, mapPresenter.getViewPort().getCrs(), resolution, worldBounds)) {
			tiles.add(toRasterTile(tile));
		}
		info.setTiles(tiles);
		info.setTileHeight(tileBasedLayer.getTileConfiguration().getTileHeight());
		info.setTileWidth(tileBasedLayer.getTileConfiguration().getTileWidth());
		info.setScale(1 / getActualResolution(tileConfig, resolution));

		info.setId(tileBasedLayer.getId());
		RasterLayerRasterizingInfo rasterInfo = new RasterLayerRasterizingInfo();
		rasterInfo.setShowing(tileBasedLayer.isShowing());
		rasterInfo.setCssStyle(tileBasedLayer.getOpacity() + "");

		info.getWidgetInfo().put(RasterLayerRasterizingInfo.WIDGET_KEY, rasterInfo);
		return info;
	}

	private RasterTile toRasterTile(Tile tile) {
		RasterTile rTile = new RasterTile(tile.getBounds(), tile.getCode().toString());
		rTile.setCode(new TileCode(tile.getCode().getTileLevel(), tile.getCode().getX(), tile.getCode().getY()));
		rTile.setUrl(tile.getUrl());
		return rTile;
	}

	private List<Tile> getTiles(WmsLayer layer, String crs, double resolution, Bbox worldBounds) {
		TileConfiguration tileConfig = layer.getTileConfiguration();
		Bbox maxBounds = layer.getMaxBounds();
		worldBounds = BboxService.intersection(worldBounds, maxBounds);
		List<Tile> tiles = new ArrayList<Tile>();
		if (worldBounds != null) {
			List<org.geomajas.gwt2.client.map.render.TileCode> codes = TileService.getTileCodesForBounds(tileConfig,
					worldBounds, resolution);
			if (!codes.isEmpty()) {
				double actualResolution = tileConfig.getResolution(codes.get(0).getTileLevel());
				for (org.geomajas.gwt2.client.map.render.TileCode code : codes) {
					Bbox bounds = TileService.getWorldBoundsForTile(tileConfig, code);
					Tile tile = new Tile(getScreenBounds(actualResolution, bounds));
					tile.setCode(code);
					tile.setUrl(WmsClient.getInstance().getWmsService().getMapUrl(layer.getConfiguration(),
							bounds, tileConfig.getTileWidth(), tileConfig.getTileHeight()));
					tiles.add(tile);
				}
			}
		}
		return tiles;
	}

	private double getActualResolution(TileConfiguration tileConfig, double resolution) {
		int tileLevel = tileConfig.getResolutionIndex(resolution);
		double actualResolution = tileConfig.getResolution(tileLevel);
		return actualResolution;
	}

	private Bbox getScreenBounds(double resolution, Bbox worldBounds) {
		return new Bbox(Math.round(worldBounds.getX() / resolution), -Math.round(worldBounds.getMaxY() / resolution),
				Math.round(worldBounds.getMaxX() / resolution) - Math.round(worldBounds.getX() / resolution),
				Math.round(worldBounds.getMaxY() / resolution) - Math.round(worldBounds.getY() / resolution));
	}
}
