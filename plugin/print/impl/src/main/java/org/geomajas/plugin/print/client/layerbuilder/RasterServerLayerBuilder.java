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
package org.geomajas.plugin.print.client.layerbuilder;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.layer.RasterServerLayer;
import org.geomajas.plugin.rasterizing.command.dto.RasterLayerRasterizingInfo;

/**
 * {@link PrintableLayersModelBuilder} for {@link RasterServerLayer} instances.
 * 
 * @author Jan De Moerloose
 */
public class RasterServerLayerBuilder implements PrintableLayersModelBuilder {

	@Override
	public ClientLayerInfo build(MapPresenter mapPresenter, Layer layer, Bbox worldBounds, double resolution) {
		RasterServerLayer rasterServerLayer = (RasterServerLayer) layer;
		ClientRasterLayerInfo layerInfo = (ClientRasterLayerInfo) rasterServerLayer.getLayerInfo();
		RasterLayerRasterizingInfo rasterInfo = new RasterLayerRasterizingInfo();
		rasterInfo.setShowing(rasterServerLayer.isShowing());
		rasterInfo.setCssStyle(rasterServerLayer.getOpacity() + "");
		layerInfo.getWidgetInfo().put(RasterLayerRasterizingInfo.WIDGET_KEY, rasterInfo);
		return layerInfo;
	}

	@Override
	public boolean supports(Layer layer) {
		return layer instanceof RasterServerLayer;
	}
}