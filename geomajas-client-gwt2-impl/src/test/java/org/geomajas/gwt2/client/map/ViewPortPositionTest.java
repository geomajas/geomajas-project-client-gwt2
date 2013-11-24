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

import junit.framework.Assert;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test that checks if the ViewPortImpl positions correctly.
 * 
 * @author Pieter De Graef
 */
public class ViewPortPositionTest {

	private MapEventBus eventBus;

	private ViewPort viewPort;

	public ViewPortPositionTest() {
		eventBus = new MapEventBusImpl(this, GeomajasImpl.getInstance().getEventBus());
		viewPort = new ViewPortImpl(eventBus, getMapConfig());
		viewPort.setMapSize(200, 200);
	}

	@Before
	public void prepareTest() {
		viewPort.applyBounds(viewPort.getMaximumBounds());
	}

	@Test
	public void testOnMinimumScale() {
		Assert.assertEquals(0.0, viewPort.getPosition().getX());
		Assert.assertEquals(0.0, viewPort.getPosition().getY());

		viewPort.applyPosition(new Coordinate(10, 10));
		Assert.assertEquals(0.0, viewPort.getPosition().getX());
		Assert.assertEquals(0.0, viewPort.getPosition().getY());
	}

	@Test
	public void testOnAverageScale() {
		Assert.assertEquals(0.0, viewPort.getPosition().getX());
		Assert.assertEquals(0.0, viewPort.getPosition().getY());

		viewPort.applyScale(2.0); // Width now becomes 100, so max center = (50,50).
		Assert.assertEquals(0.0, viewPort.getPosition().getX());
		Assert.assertEquals(0.0, viewPort.getPosition().getY());

		viewPort.applyPosition(new Coordinate(10, 10));
		Assert.assertEquals(10.0, viewPort.getPosition().getX());
		Assert.assertEquals(10.0, viewPort.getPosition().getY());

		viewPort.applyPosition(new Coordinate(1000, 1000));
		Assert.assertEquals(50.0, viewPort.getPosition().getX());
		Assert.assertEquals(50.0, viewPort.getPosition().getY());
	}

	private MapConfiguration getMapConfig() {
		MapOptions options = new MapOptions();
		options.setCrs("EPSG:4326");
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