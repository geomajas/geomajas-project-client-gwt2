/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.plugin.wms.client.layer;

import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.plugin.wms.client.WmsServerExtension;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsLayerInfo;

import com.google.gwt.core.client.Callback;

/**
 * Server-based implementation of the {@link FeatureInfoSupportedWmsLayer}.
 * 
 * @author Jan De Moerloose
 */
public class FeatureInfoSupportedWmsServerLayer extends FeatureInfoSupportedWmsLayer {

	/**
	 * Create a WMS layer with feature info support.
	 * 
	 * @param title
	 * @param crs
	 * @param wmsLayerConfig
	 * @param wmsTileConfig
	 * @param layerInfo
	 */
	public FeatureInfoSupportedWmsServerLayer(String title, String crs, WmsLayerConfiguration wmsLayerConfig,
			TileConfiguration wmsTileConfig, WmsLayerInfo layerInfo) {
		super(title, crs, wmsLayerConfig, wmsTileConfig, layerInfo);
	}

	@Override
	public void getFeatureInfo(Coordinate location, Callback<List<Feature>, String> callback) {
		WmsServerExtension.getInstance().getWmsService().getFeatureInfo(viewPort, this, location, callback);
	}

	@Override
	public void getFeatureInfo(Coordinate location, String format, Callback<List<Feature>, String> callback) {
		WmsServerExtension.getInstance().getWmsService().getFeatureInfo(viewPort, this, location, format, callback);
	}

	@Override
	public String getFeatureInfoUrl(Coordinate location, String format) {
		return WmsServerExtension.getInstance().getWmsService()
				.getFeatureInfoUrl(viewPort, this, location, format.toString());
	}

}
