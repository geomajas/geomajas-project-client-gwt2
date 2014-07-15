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

package org.geomajas.plugin.print.wms.client;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.client.map.render.Tile;
import org.geomajas.gwt2.client.service.TileService;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.plugin.print.client.layerbuilder.PrintableLayersModelBuilder;
import org.geomajas.plugin.print.wms.server.dto.WmsClientLayerInfo;
import org.geomajas.plugin.rasterizing.command.dto.RasterLayerRasterizingInfo;
import org.geomajas.plugin.wms.client.WmsClient;
import org.geomajas.plugin.wms.client.layer.WmsLayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for WMS layer.
 *
 * @author Jan De Moerloose
 */
public class WmsLayerBuilder implements PrintableLayersModelBuilder {

	@Override
	public ClientLayerInfo build(MapPresenter mapPresenter, Layer layer, Bbox worldBounds, double resolution) {
		WmsLayer wmsLayer = (WmsLayer) layer;

		WmsClientLayerInfo info = new WmsClientLayerInfo();
		List<RasterTile> tiles = new ArrayList<RasterTile>();
		for (Tile tile : getTiles(wmsLayer, mapPresenter.getViewPort().getCrs(), resolution, worldBounds)) {
			tiles.add(toRasterTile(tile));
		}
		info.setTiles(tiles);
		info.setTileHeight(wmsLayer.getTileConfiguration().getTileHeight());
		info.setTileWidth(wmsLayer.getTileConfiguration().getTileWidth());
		info.setScale(resolution);

		info.setId(wmsLayer.getId());
		RasterLayerRasterizingInfo rasterInfo = new RasterLayerRasterizingInfo();
		rasterInfo.setShowing(wmsLayer.isShowing());
		rasterInfo.setCssStyle(wmsLayer.getOpacity() + "");

		info.getWidgetInfo().put(RasterLayerRasterizingInfo.WIDGET_KEY, rasterInfo);
		return info;
	}

	@Override
	public boolean supports(Layer layer) {
		return layer instanceof WmsLayer;
	}

	private RasterTile toRasterTile(Tile tile) {
		RasterTile rTile = new RasterTile(tile.getBounds(), tile.getCode().toString());
		rTile.setCode(new TileCode(tile.getCode().getTileLevel(), tile.getCode().getX(), tile.getCode().getY()));
		rTile.setUrl(tile.getUrl());
		return rTile;
	}

	private List<Tile> getTiles(WmsLayer wmsLayer, String crs, double resolution, Bbox worldBounds) {
		TileConfiguration tileConfig = wmsLayer.getTileConfiguration();
		List<org.geomajas.gwt2.client.map.render.TileCode> codes = TileService.getTileCodesForBounds(tileConfig,
				worldBounds, resolution);
		List<Tile> tiles = new ArrayList<Tile>();
		if (!codes.isEmpty()) {
			double actualResolution = tileConfig.getResolution(codes.get(0).getTileLevel());
			for (org.geomajas.gwt2.client.map.render.TileCode code : codes) {
				Bbox bounds = TileService.getWorldBoundsForTile(tileConfig, code);
				Tile tile = new Tile(getScreenBounds(actualResolution, bounds));
				tile.setCode(code);
				tile.setUrl(WmsClient.getInstance().getWmsService().getMapUrl(wmsLayer.getConfiguration(),
						bounds, tileConfig.getTileWidth(), tileConfig.getTileHeight()));
				tiles.add(tile);
			}
		}
		return tiles;
	}

	private Bbox getScreenBounds(double resolution, Bbox worldBounds) {
		return new Bbox(Math.round(worldBounds.getX() / resolution), -Math.round(worldBounds.getMaxY() / resolution),
				Math.round(worldBounds.getMaxX() / resolution) - Math.round(worldBounds.getX() / resolution),
				Math.round(worldBounds.getMaxY() / resolution) - Math.round(worldBounds.getY() / resolution));
	}
}
