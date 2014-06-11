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

package org.geomajas.gwt2.client.map.layer;

import java.util.ArrayList;

import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.client.map.render.TileRenderer;

/**
 * The client side representation of a raster layer defined on the backend.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * 
 */
public class RasterServerLayerImpl extends AbstractServerLayer<ClientRasterLayerInfo> implements RasterServerLayer {

	private static final String TMS_PREFIX = "tms/";

	/** The only constructor. */
	public RasterServerLayerImpl(MapConfiguration mapConfiguration, ClientRasterLayerInfo layerInfo, ViewPort viewPort,
			MapEventBus eventBus) {
		super(mapConfiguration, layerInfo, createTileConfiguration(layerInfo), viewPort, eventBus);
	}

	@Override
	public TileRenderer getTileRenderer() {
		if (tileRenderer == null) {
			String dispatcher = GeomajasServerExtension.getInstance().getEndPointService().getDispatcherUrl();
			String layerId = layerInfo.getServerLayerId();
			String baseUrl = dispatcher + TMS_PREFIX + layerId + "@" + viewPort.getCrs() + "/";
			tileRenderer = new RasterServerTileRenderer(baseUrl, ".png");
		}
		return tileRenderer;
	}

	// ------------------------------------------------------------------------
	// Layer implementation:
	// ------------------------------------------------------------------------

	/**
	 * Apply a new opacity on the entire raster layer. Changing the opacity on a layer does NOT fire a layer style
	 * changed event.
	 * 
	 * @param opacity The new opacity value. Must be a value between 0 and 1, where 0 means invisible and 1 is totally
	 *        visible.
	 */
	public void setOpacity(double opacity) {
		getLayerInfo().setStyle(Double.toString(opacity));
		renderer.setOpacity(opacity);
	}

	public double getOpacity() {
		return Double.parseDouble(getLayerInfo().getStyle());
	}

	/**
	 * Create the tile configuration
	 */
	private static TileConfiguration createTileConfiguration(ClientRasterLayerInfo layerInfo) {
		RasterLayerInfo serverLayerInfo = (RasterLayerInfo) layerInfo.getLayerInfo();
		TileConfiguration tileConfig = new TileConfiguration();
		tileConfig.setTileWidth(serverLayerInfo.getTileWidth());
		tileConfig.setTileHeight(serverLayerInfo.getTileHeight());
		tileConfig.setMaxBounds(layerInfo.getMaxExtent());
		if (serverLayerInfo.getResolutions().size() > 0) {
			// use resolutions of server for raster layer (as yet not reprojectable)
			tileConfig.setResolutions(serverLayerInfo.getResolutions());
		} else {
			// if no resolutions, fall back to quad tree numbers
			ArrayList<Double> resolutions = new ArrayList<Double>();
			for (int i = 0; i < 50; i++) {
				resolutions
						.add(layerInfo.getMaxExtent().getWidth() / (serverLayerInfo.getTileWidth() * Math.pow(2, i)));
				tileConfig.setResolutions(resolutions);
			}
		}
		tileConfig.setTileOrigin(BboxService.getOrigin(layerInfo.getMaxExtent()));
		return tileConfig;
	}

}