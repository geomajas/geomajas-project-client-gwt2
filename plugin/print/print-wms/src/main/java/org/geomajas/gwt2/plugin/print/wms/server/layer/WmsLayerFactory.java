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
package org.geomajas.gwt2.plugin.print.wms.server.layer;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.global.GeomajasException;
import org.geomajas.gwt2.plugin.print.wms.server.dto.WmsClientLayerInfo;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.common.proxy.LayerHttpService;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.plugin.rasterizing.api.LayerFactory;
import org.geomajas.plugin.rasterizing.command.dto.RasterLayerRasterizingInfo;
import org.geomajas.plugin.rasterizing.layer.RasterDirectLayer;
import org.geomajas.plugin.rasterizing.layer.RasterDirectLayer.UrlDownLoader;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.DispatcherUrlService;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This factory creates a GeoTools layer that is capable of rendering WMS layers.
 * 
 * @author Jan De Moerloose
 * @author An Buyle
 */
@Component
public class WmsLayerFactory implements LayerFactory {

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private DispatcherUrlService dispatcherUrlService;

	@Autowired
	private LayerHttpService httpService;


	public boolean canCreateLayer(MapContext mapContext, ClientLayerInfo clientLayerInfo) {
		return clientLayerInfo instanceof WmsClientLayerInfo;
	}

	public Layer createLayer(MapContext mapContext, ClientLayerInfo clientLayerInfo) throws GeomajasException {
		if (!(clientLayerInfo instanceof WmsClientLayerInfo)) {
			throw new IllegalArgumentException(
					"WmsLayerFactory.createLayer() should only be called using WmsClientLayerInfo");
		}
		WmsClientLayerInfo rasterInfo = (WmsClientLayerInfo) clientLayerInfo;
		RasterLayerRasterizingInfo extraInfo = (RasterLayerRasterizingInfo) rasterInfo
				.getWidgetInfo(RasterLayerRasterizingInfo.WIDGET_KEY);
		List<RasterTile> tiles = rasterInfo.getTiles();
		
		for (RasterTile rasterTile : tiles) {
			if (null != rasterTile.getUrl() && !rasterTile.getUrl().isEmpty()) {
				rasterTile.setUrl(calculateUrl(rasterTile.getUrl()));				
			}
		}

		final RasterLayer layer = configurationService.getRasterLayer(clientLayerInfo.getServerLayerId());
		RasterDirectLayer rasterLayer = new RasterDirectLayer(new UrlDownLoader() {

			@Override
			public InputStream getStream(String url) throws IOException {
				return httpService.getStream(url, layer);
			}
		}, tiles, rasterInfo.getTileHeight(), rasterInfo.getTileWidth(), rasterInfo.getScale(),
		extraInfo.getCssStyle());
		rasterLayer.setTitle(clientLayerInfo.getLabel());
		rasterLayer.getUserData().put(USERDATA_KEY_LAYER_ID, rasterInfo.getId());
		rasterLayer.getUserData().put(USERDATA_KEY_SHOWING, extraInfo.isShowing());
		return rasterLayer;
	}

	public Map<String, Object> getLayerUserData(MapContext mapContext, ClientLayerInfo clientLayerInfo) {
		Map<String, Object> userData = new HashMap<String, Object>();
		RasterLayerRasterizingInfo extraInfo = (RasterLayerRasterizingInfo) clientLayerInfo
				.getWidgetInfo(RasterLayerRasterizingInfo.WIDGET_KEY);
		userData.put(USERDATA_KEY_SHOWING, extraInfo.isShowing());
		return userData;
	}
	
	private String calculateUrl(String urlAsString) {
		URL absoluteUrl = null;

		if (!urlAsString.startsWith("http:") && !urlAsString.startsWith("https:")) {

			try {
				String baseUrlAsString = dispatcherUrlService.getLocalDispatcherUrl();
				
				URL baseUrl = new URL(baseUrlAsString);
				absoluteUrl = new URL(baseUrl, "../" + urlAsString);
				
			} catch (MalformedURLException e) {
				// Should never happen...
				//log.error("Error converting URL " + legendImageServiceUrlAsString + " to absolute URL", e);
				e.printStackTrace();
			}
		} else {
			try {
				absoluteUrl = new URL(urlAsString);
			} catch (MalformedURLException e) {
				// Should never happen...
				//log.error("Error converting URL " + legendImageServiceUrlAsString + " to absolute URL", e);
				e.printStackTrace();
			}
		}
		
		return absoluteUrl.toExternalForm();
	}
}
