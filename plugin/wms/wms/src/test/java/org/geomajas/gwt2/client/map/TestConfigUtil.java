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

package org.geomajas.gwt2.client.map;

import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.map.MapConfiguration.CrsType;

import java.util.ArrayList;
import java.util.List;

/**
 * Test utility.
 * 
 * @author Pieter De Graef
 */
public class TestConfigUtil {

	public static MapConfiguration getMapConfig() {
		MapConfiguration configuration = new MapConfigurationImpl();
		configuration.setCrs("EPSG:4326", CrsType.DEGREES);
		configuration.setMaxBounds(new Bbox(-100, -100, 200, 200));
		List<Double> resolutions = new ArrayList<Double>();
		resolutions.add(1.0);
		resolutions.add(0.5);
		resolutions.add(0.25);
		resolutions.add(0.125);
		configuration.setResolutions(resolutions);
		return configuration;
	}

	public static ViewPort createViewPort(MapEventBus eventBus, MapConfiguration configuration, int width,
			int height) {
		ViewPortImpl viewPort = new ViewPortImpl(eventBus);
		viewPort.initialize(configuration);
		viewPort.setMapSize(width, height);
		return viewPort;
	}
}