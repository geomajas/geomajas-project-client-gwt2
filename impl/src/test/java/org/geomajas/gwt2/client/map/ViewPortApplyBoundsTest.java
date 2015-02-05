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

package org.geomajas.gwt2.client.map;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.Matrix;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.map.MapConfiguration.CrsType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit test that checks if the correct events are fired by the ViewPortImpl.
 *
 * @author Pieter De Graef
 */
public class ViewPortApplyBoundsTest {

	private static final double DELTA = 0.00001;

	private static final int MAP_WIDTH = 640;

	private static final int MAP_HEIGHT = 480;

	private MapEventBus eventBus;

	private ViewPortImpl viewPort;

	public ViewPortApplyBoundsTest() {
		eventBus = new MapEventBusImpl(this, GeomajasImpl.getInstance().getEventBus());
		viewPort = new ViewPortImpl(eventBus);
		viewPort.initialize(getMapConfig());
		viewPort.setMapSize(MAP_WIDTH, MAP_HEIGHT);
	}

	@Test
	public void testApplyBoundsHighBox() {
		Bbox boxHigh = new Bbox(-1, -100, 2, 200);

		viewPort.applyBounds(boxHigh, ZoomOption.LEVEL_FIT);

		Bbox viewPortBounds = viewPort.getBounds();
		Coordinate coordinate = BboxService.getCenterPoint(boxHigh);
		Coordinate viewCentralPoint = BboxService.getCenterPoint(viewPortBounds);
		//central position
		Assert.assertEquals(coordinate.getX(), viewCentralPoint.getX(), DELTA);
		Assert.assertEquals(coordinate.getY(), viewCentralPoint.getY(), DELTA);
		// is start bounding box in the current viewport?
		Assert.assertTrue(boxHigh.getX() >= viewPortBounds.getX());
		Assert.assertTrue(boxHigh.getY() >= viewPortBounds.getY());
		Assert.assertTrue(boxHigh.getMaxX() <= viewPortBounds.getMaxY());
		Assert.assertTrue(boxHigh.getMaxY() <= viewPortBounds.getMaxY());
	}

	@Test
	public void testApplyBoundsWideBox() {
		Bbox boxWide = new Bbox(-100, -1, 200, 2);

		viewPort.applyBounds(boxWide, ZoomOption.LEVEL_FIT);

		Bbox viewPortBounds = viewPort.getBounds();
		Coordinate coordinate = BboxService.getCenterPoint(boxWide);
		Coordinate viewCentralPoint = BboxService.getCenterPoint(viewPortBounds);
		//central position
		Assert.assertEquals(coordinate.getX(), viewCentralPoint.getX(), DELTA);
		Assert.assertEquals(coordinate.getY(), viewCentralPoint.getY(), DELTA);
		// is start bounding box in the current viewport?
		Assert.assertTrue(boxWide.getX() >= viewPortBounds.getX());
		Assert.assertTrue(boxWide.getY() >= viewPortBounds.getY());
		Assert.assertTrue(boxWide.getMaxX() <= viewPortBounds.getMaxY());
		Assert.assertTrue(boxWide.getMaxY() <= viewPortBounds.getMaxY());
	}

	private MapConfiguration getMapConfig() {
		MapConfigurationImpl config = new MapConfigurationImpl();
		config.setCrs("EPSG:4326", CrsType.DEGREES);
		config.setMaxBounds(new Bbox(-100, -100, 200, 200));
		List<Double> resolutions = new ArrayList<Double>();
		resolutions.add(1.0);
		resolutions.add(0.5);
		resolutions.add(0.25);
		resolutions.add(0.125);
		config.setResolutions(resolutions);
		return config;
	}
}