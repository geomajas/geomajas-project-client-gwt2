package org.geomajas.gwt2.plugin.print.tilebasedlayer.client;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.layer.AbstractTileBasedLayer;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.client.map.render.Tile;
import org.geomajas.gwt2.client.map.render.TileRenderer;
import org.geomajas.gwt2.client.service.TileService;
import org.geomajas.gwt2.plugin.print.client.layerbuilder.PrintableLayersModelBuilder;
import org.geomajas.gwt2.plugin.print.tilebasedlayer.server.dto.TilebasedClientLayerInfo;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.plugin.rasterizing.command.dto.RasterLayerRasterizingInfo;


public class TileBasedLayerBuilder implements PrintableLayersModelBuilder {

	@Override
	public boolean supports(Layer object) {
		return object instanceof AbstractTileBasedLayer;
	}

	@Override
	public ClientLayerInfo build(MapPresenter mapPresenter, Layer layer, Bbox worldBounds, double resolution) {
		AbstractTileBasedLayer tileBasedLayer = (AbstractTileBasedLayer) layer;

		TilebasedClientLayerInfo info = new TilebasedClientLayerInfo();
		List<RasterTile> tiles = new ArrayList<RasterTile>();
		for (Tile tile : getTiles(tileBasedLayer, mapPresenter.getViewPort().getCrs(), resolution, worldBounds)) {
			tiles.add(toRasterTile(tile));
		}
		info.setTiles(tiles);
		info.setTileHeight(tileBasedLayer.getTileConfiguration().getTileHeight());
		info.setTileWidth(tileBasedLayer.getTileConfiguration().getTileWidth());
		info.setScale(1/resolution);

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

	private List<Tile> getTiles(AbstractTileBasedLayer tileBasedLayer, String crs, double resolution, Bbox worldBounds) {
		TileConfiguration tileConfig = tileBasedLayer.getTileConfiguration();
		List<org.geomajas.gwt2.client.map.render.TileCode> codes = TileService.getTileCodesForBounds(tileConfig,
				worldBounds, resolution);
		List<Tile> tiles = new ArrayList<Tile>();
		if (!codes.isEmpty()) {
			double actualResolution = tileConfig.getResolution(codes.get(0).getTileLevel());
			for (org.geomajas.gwt2.client.map.render.TileCode code : codes) {
				Bbox bounds = TileService.getWorldBoundsForTile(tileConfig, code);
				Tile tile = new Tile(getScreenBounds(actualResolution, bounds));
				tile.setCode(code);
				TileRenderer tileRenderer = tileBasedLayer.getTileRenderer();
				tile.setUrl(tileRenderer.getUrl(code));
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
