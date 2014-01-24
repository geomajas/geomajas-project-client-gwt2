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

import junit.framework.Assert;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.map.MapConfiguration.CrsType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ViewPortMaxBoundsTest {

	private MapConfiguration mapConfig;

	private MapEventBus eventBus;

	private ViewPortImpl viewPort;

	public ViewPortMaxBoundsTest() {
		mapConfig = getMapConfig();
		eventBus = new MapEventBusImpl(this, GeomajasImpl.getInstance().getEventBus());
		viewPort = new ViewPortImpl(eventBus);
		viewPort.initialize(mapConfig);
		viewPort.setMapSize(1000, 1000);
	}

	@Before
	public void prepareTest() {
		mapConfig = getMapConfig();
		viewPort = new ViewPortImpl(eventBus);
		((ViewPortImpl) viewPort).initialize(mapConfig);
	}

	@Test
	public void testInitialBounds() {
		Bbox maxBounds = viewPort.getMaximumBounds();
		Assert.assertEquals(-100.0, maxBounds.getX());
		Assert.assertEquals(-100.0, maxBounds.getY());
		Assert.assertEquals(100.0, maxBounds.getMaxX());
		Assert.assertEquals(100.0, maxBounds.getMaxY());
	}

	@Test
	public void testSetMaxBounds() {
		mapConfig.setMaxBounds(new org.geomajas.geometry.Bbox(0, 0, 10, 10));
		viewPort = new ViewPortImpl(eventBus);
		((ViewPortImpl) viewPort).initialize(mapConfig);

		Bbox maxBounds = viewPort.getMaximumBounds();
		Assert.assertEquals(0.0, maxBounds.getX());
		Assert.assertEquals(0.0, maxBounds.getY());
		Assert.assertEquals(10.0, maxBounds.getMaxX());
		Assert.assertEquals(10.0, maxBounds.getMaxY());
	}

	private MapConfiguration getMapConfig() {
		MapConfigurationImpl config = new MapConfigurationImpl();
		config.setCrs("EPSG:4326", CrsType.DEGREES);
		config.setMaxBounds(new Bbox(-100, -100, 200, 200));
		List<Double> resolutions = new ArrayList<Double>();
		resolutions.add(1.0);
		resolutions.add(2.0);
		resolutions.add(4.0);
		resolutions.add(8.0);
		config.setResolutions(resolutions);
		return config;
	}
}