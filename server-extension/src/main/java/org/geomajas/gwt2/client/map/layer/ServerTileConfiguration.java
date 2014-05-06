package org.geomajas.gwt2.client.map.layer;

import java.util.ArrayList;

import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.client.map.render.TileCode;
import org.geomajas.gwt2.client.map.render.TileRenderer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;

/**
 * {@link TileConfiguration} for server layers.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ServerTileConfiguration extends TileConfiguration implements TileRenderer {

	private static final long serialVersionUID = 1L;

	private static final String RASTERIZING_PREFIX = "rasterizing/layer/";

	private String baseUrl;

	public ServerTileConfiguration(ClientMapInfo mapInfo, ClientLayerInfo layerInfo) {
		String layerId = layerInfo.getServerLayerId();
		ArrayList<Double> resolutions = new ArrayList<Double>();
		if (layerInfo instanceof ClientVectorLayerInfo) {
			ClientVectorLayerInfo vectorLayerInfo = (ClientVectorLayerInfo) layerInfo;
			baseUrl = GWT.getModuleBaseURL() + RASTERIZING_PREFIX + layerId + "@" + mapInfo.getCrs() + "/"
					+ vectorLayerInfo.getNamedStyleInfo().getName() + "/";
			setTileWidth(512);
			setTileHeight(512);
			for (int i = 0; i < 50; i++) {
				resolutions.add(layerInfo.getMaxExtent().getWidth() / (512 * Math.pow(2, i)));
			}
		} else if (layerInfo instanceof ClientRasterLayerInfo) {
			ClientRasterLayerInfo rasterLayerInfo = (ClientRasterLayerInfo) layerInfo;
			baseUrl = GWT.getModuleBaseURL() + RASTERIZING_PREFIX + layerId + "@" + mapInfo.getCrs() + "/";
			setTileWidth(((RasterLayerInfo) rasterLayerInfo.getLayerInfo()).getTileWidth());
			setTileHeight(((RasterLayerInfo) rasterLayerInfo.getLayerInfo()).getTileHeight());
			for (int i = 0; i < 50; i++) {
				resolutions.add(layerInfo.getMaxExtent().getWidth() / (getTileWidth() * Math.pow(2, i)));
			}
		}
		setTileOrigin(BboxService.getOrigin(layerInfo.getMaxExtent()));
		setResolutions(resolutions);
	}

	@Override
	public String getUrl(TileCode tileCode) {
		return URL.encode(baseUrl + tileCode.getTileLevel() + "/" + tileCode.getX() + "/" + tileCode.getY() + ".png");
	}
}
