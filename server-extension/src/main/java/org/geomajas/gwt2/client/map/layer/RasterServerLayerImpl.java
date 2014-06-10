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
	public RasterServerLayerImpl(MapConfiguration mapConfiguration, ClientRasterLayerInfo layerInfo, final ViewPort viewPort, MapEventBus eventBus) {
		super(mapConfiguration, layerInfo, viewPort, eventBus);
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

	@Override
	protected void initLayerConfiguration() {
		String dispatcher = GeomajasServerExtension.getInstance().getEndPointService().getDispatcherUrl();
		String layerId = layerInfo.getServerLayerId();
		ArrayList<Double> resolutions = new ArrayList<Double>();
		RasterLayerInfo serverLayerInfo = (RasterLayerInfo) layerInfo.getLayerInfo();
		String baseUrl = dispatcher + TMS_PREFIX + layerId + "@" + getMapInfo().getCrs() + "/";
		getTileConfiguration().setTileWidth(serverLayerInfo.getTileWidth());
		getTileConfiguration().setTileHeight(serverLayerInfo.getTileHeight());
		for (int i = 0; i < 50; i++) {
			resolutions.add(layerInfo.getMaxExtent().getWidth() / (serverLayerInfo.getTileWidth() * Math.pow(2, i)));
		}
		getTileConfiguration().setResolutions(resolutions);
		getTileConfiguration().setTileOrigin(BboxService.getOrigin(layerInfo.getMaxExtent()));
		getTileConfiguration().setLimitXYByTileLevel(true);
		layerConfiguration = new ServerLayerConfiguration(baseUrl, ".png", getTileConfiguration());
	}

}