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
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.client.map.render.TileServiceImpl;
import org.geomajas.gwt2.client.map.render.TileCode;
import org.geomajas.gwt2.client.map.render.TileRenderer;
import org.geomajas.plugin.wms.client.WmsClient;

/**
 * {@link org.geomajas.gwt2.client.map.render.TileRenderer} implementation that creates tiles for {@link WmsLayer}s. It
 * does this by creating WMS GetMap request URLs.
 *
 * @author Pieter De Graef
 */
public class WmsTileRenderer implements TileRenderer {

	private final WmsLayerConfiguration layerConfiguration;

	private final TileConfiguration tileConfiguration;

	private final ViewPort viewPort;

	protected WmsTileRenderer(WmsLayerConfiguration layerConfiguration, TileConfiguration tileConfiguration,
			ViewPort viewPort) {
		this.layerConfiguration = layerConfiguration;
		this.tileConfiguration = tileConfiguration;
		this.viewPort = viewPort;
	}

	@Override
	public String getUrl(TileCode tileCode) {
		Bbox bounds = TileServiceImpl.getInstance().getWorldBoundsForTile(viewPort, tileConfiguration, tileCode);
		return WmsClient.getInstance().getWmsService().getMapUrl(layerConfiguration, viewPort.getCrs(), bounds,
				tileConfiguration.getTileWidth(), tileConfiguration.getTileHeight());
	}
}
