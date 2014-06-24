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
package org.geomajas.plugin.wmsclient.printing.client;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.render.Tile;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.plugin.print.client.layerbuilder.PrintableLayersModelBuilder;
import org.geomajas.plugin.rasterizing.command.dto.RasterLayerRasterizingInfo;
import org.geomajas.plugin.wms.client.layer.WmsLayer;
import org.geomajas.plugin.wmsclient.printing.server.dto.WmsClientLayerInfo;

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
		for (Tile tile : wmsLayer.getTiles(resolution, worldBounds)) {
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
}