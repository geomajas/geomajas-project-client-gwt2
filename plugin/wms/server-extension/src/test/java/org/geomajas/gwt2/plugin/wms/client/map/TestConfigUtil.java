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

package org.geomajas.gwt2.plugin.wms.client.map;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapConfiguration.CrsType;
import org.geomajas.gwt2.client.map.MapConfigurationImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Test utility.
 * 
 * @author Pieter De Graef
 */
public class TestConfigUtil {

	public static MapConfiguration create(ClientMapInfo mapInfo) {
		MapConfigurationImpl mapConfig = new MapConfigurationImpl();
		mapConfig.setCrs(mapInfo.getCrs(), CrsType.DEGREES);
		mapConfig.setHintValue(MapConfiguration.INITIAL_BOUNDS, mapInfo.getInitialBounds());
		mapConfig.setMaxBounds(mapInfo.getMaxBounds());
		mapConfig.setMinimumResolution(1 / mapInfo.getScaleConfiguration().getMaximumScale().getPixelPerUnit());
		List<Double> resolutions = new ArrayList<Double>();
		for (ScaleInfo scale : mapInfo.getScaleConfiguration().getZoomLevels()) {
			resolutions.add(scale.getPixelPerUnit());
		}
		mapConfig.setResolutions(resolutions);
		return mapConfig;
	}
}
