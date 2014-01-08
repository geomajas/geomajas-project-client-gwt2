/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.client.map;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.gwt2.client.map.MapOptions.CrsType;

/**
 * Test utility.
 * 
 * @author Pieter De Graef
 */
public class TestConfigUtil {

	public static MapConfiguration create(ClientMapInfo mapInfo) {
		MapConfigurationImpl mapConfig = new MapConfigurationImpl();
		MapOptions mapOptions = new MapOptions();
		mapOptions.setCrs(mapInfo.getCrs(), CrsType.DEGREES);
		mapOptions.setInitialBounds(mapInfo.getInitialBounds());
		mapOptions.setMaxBounds(mapInfo.getMaxBounds());
		mapOptions.setMaximumScale(mapInfo.getScaleConfiguration().getMaximumScale().getPixelPerUnit());
		List<Double> resolutions = new ArrayList<Double>();
		for (ScaleInfo scale : mapInfo.getScaleConfiguration().getZoomLevels()) {
			resolutions.add(scale.getPixelPerUnit());
		}
		mapOptions.setResolutions(resolutions);
		mapConfig.setMapOptions(mapOptions);
		return mapConfig;
	}
}