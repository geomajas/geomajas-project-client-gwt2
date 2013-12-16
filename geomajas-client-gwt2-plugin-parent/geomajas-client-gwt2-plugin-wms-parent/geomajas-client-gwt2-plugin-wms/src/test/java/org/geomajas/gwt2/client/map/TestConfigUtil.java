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

import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.map.MapOptions.CrsType;

/**
 * Test utility.
 * 
 * @author Pieter De Graef
 */
public class TestConfigUtil {

	public static MapConfiguration getMapConfig() {
		MapOptions options = new MapOptions();
		options.setCrs("EPSG:4326", CrsType.DEGREES);
		options.setInitialBounds(new Bbox(-100, -100, 200, 200));
		options.setMaxBounds(new Bbox(-100, -100, 200, 200));
		List<Double> resolutions = new ArrayList<Double>();
		resolutions.add(1.0);
		resolutions.add(2.0);
		resolutions.add(4.0);
		resolutions.add(8.0);
		options.setResolutions(resolutions);

		MapConfigurationImpl config = new MapConfigurationImpl();
		config.setMapOptions(options);
		return config;
	}
}
