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
public class ViewPortTransformTest {

	private static final double DELTA = 0.00001;

	private static final int MAP_WIDTH = 640;

	private static final int MAP_HEIGHT = 480;

	private MapEventBus eventBus;

	private ViewPortImpl viewPort;

	public ViewPortTransformTest() {
		eventBus = new MapEventBusImpl(this, GeomajasImpl.getInstance().getEventBus());
		viewPort = new ViewPortImpl(eventBus);
		viewPort.initialize(getMapConfig());
		viewPort.setMapSize(MAP_WIDTH, MAP_HEIGHT);
	}

	@Before
	public void prepareTest() {
		viewPort.applyBounds(viewPort.getMaximumBounds());
	}

	@Test
	public void testTranslationMatrix() {
		Matrix matrix = viewPort.getTransformationService()
				.getTranslationMatrix(RenderSpace.SCREEN, RenderSpace.SCREEN);
		Assert.assertEquals(Matrix.IDENTITY, matrix);
		matrix = viewPort.getTransformationService().getTranslationMatrix(RenderSpace.WORLD, RenderSpace.WORLD);
		Assert.assertEquals(Matrix.IDENTITY, matrix);

		matrix = viewPort.getTransformationService().getTranslationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN);
		Assert.assertEquals(1.0, matrix.getXx(), DELTA);
		Assert.assertEquals(1.0, matrix.getYy(), DELTA);
		Assert.assertEquals(0.0, matrix.getXy(), DELTA);
		Assert.assertEquals(0.0, matrix.getYx(), DELTA);
		Assert.assertEquals(MAP_WIDTH / 2, matrix.getDx(), DELTA);
		Assert.assertEquals(MAP_HEIGHT / 2, matrix.getDy(), DELTA);
	}

	@Test
	public void testTransformationMatrix() {
		Matrix matrix = viewPort.getTransformationService().getTransformationMatrix(RenderSpace.SCREEN,
				RenderSpace.SCREEN);
		Assert.assertEquals(Matrix.IDENTITY, matrix);
		matrix = viewPort.getTransformationService().getTransformationMatrix(RenderSpace.WORLD, RenderSpace.WORLD);
		Assert.assertEquals(Matrix.IDENTITY, matrix);

		matrix = viewPort.getTransformationService().getTransformationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN);
		Assert.assertEquals(1 / viewPort.getResolution(), matrix.getXx(), DELTA);
		Assert.assertEquals(-1 / viewPort.getResolution(), matrix.getYy(), DELTA);
		Assert.assertEquals(0.0, matrix.getXy(), DELTA);
		Assert.assertEquals(0.0, matrix.getYx(), DELTA);
		Assert.assertEquals(MAP_WIDTH / 2, matrix.getDx(), DELTA);
		Assert.assertEquals(MAP_HEIGHT / 2, matrix.getDy(), DELTA);

		// Now move the map:
		viewPort.applyResolution(viewPort.getResolution() / 4);
		viewPort.applyPosition(new Coordinate(10, 10));
		matrix = viewPort.getTransformationService().getTransformationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN);
		Assert.assertEquals(1 / viewPort.getResolution(), matrix.getXx(), DELTA);
		Assert.assertEquals(-1 / viewPort.getResolution(), matrix.getYy(), DELTA);
		Assert.assertEquals(0.0, matrix.getXy(), DELTA);
		Assert.assertEquals(0.0, matrix.getYx(), DELTA);
		Assert.assertEquals((MAP_WIDTH / 2) - (10 / viewPort.getResolution()), matrix.getDx(), DELTA);
		Assert.assertEquals((MAP_HEIGHT / 2) + (10 / viewPort.getResolution()), matrix.getDy(), DELTA);
	}

	@Test
	public void testTransformCoordinate() {
		// Test identity transformations:
		Coordinate transformed = viewPort.getTransformationService().transform(new Coordinate(3, 42),
				RenderSpace.WORLD, RenderSpace.WORLD);
		Assert.assertEquals(3, transformed.getX(), DELTA);
		Assert.assertEquals(42, transformed.getY(), DELTA);
		transformed = viewPort.getTransformationService().transform(new Coordinate(3, 42), RenderSpace.SCREEN,
				RenderSpace.SCREEN);
		Assert.assertEquals(3, transformed.getX(), DELTA);
		Assert.assertEquals(42, transformed.getY(), DELTA);

		// Map should be centered around origin:
		transformed = viewPort.getTransformationService().transform(new Coordinate(0, 0), RenderSpace.WORLD,
				RenderSpace.SCREEN);
		Assert.assertEquals(MAP_WIDTH / 2, transformed.getX(), DELTA);
		Assert.assertEquals(MAP_HEIGHT / 2, transformed.getY(), DELTA);

		transformed = viewPort.getTransformationService().transform(transformed, RenderSpace.SCREEN, RenderSpace.WORLD);
		Assert.assertEquals(0, transformed.getX(), DELTA);
		Assert.assertEquals(0, transformed.getY(), DELTA);

		// None-origin coordinate:
		transformed = viewPort.getTransformationService().transform(new Coordinate(10, 10), RenderSpace.WORLD,
				RenderSpace.SCREEN);
		Assert.assertEquals((MAP_WIDTH / 2) + (10 / viewPort.getResolution()), transformed.getX(), DELTA);
		Assert.assertEquals((MAP_HEIGHT / 2) - (10 / viewPort.getResolution()), transformed.getY(), DELTA);

		transformed = viewPort.getTransformationService().transform(transformed, RenderSpace.SCREEN, RenderSpace.WORLD);
		Assert.assertEquals(10, transformed.getX(), DELTA);
		Assert.assertEquals(10, transformed.getY(), DELTA);
	}

	@Test
	public void testTransformBbox() {
		Bbox bbox = new Bbox(-10, -10, 20, 20);
		Bbox transformed = viewPort.getTransformationService().transform(bbox, RenderSpace.WORLD, RenderSpace.SCREEN);
		Assert.assertEquals((MAP_WIDTH / 2) - (10 / viewPort.getResolution()), transformed.getX(), DELTA);
		Assert.assertEquals((MAP_HEIGHT / 2) - (10 / viewPort.getResolution()), transformed.getY(), DELTA);
		Assert.assertEquals((MAP_WIDTH / 2) + (10 / viewPort.getResolution()), transformed.getMaxX(), DELTA);
		Assert.assertEquals((MAP_HEIGHT / 2) + (10 / viewPort.getResolution()), transformed.getMaxY(), DELTA);

		transformed = viewPort.getTransformationService().transform(transformed, RenderSpace.SCREEN, RenderSpace.WORLD);
		Assert.assertEquals(-10.0, transformed.getX(), DELTA);
		Assert.assertEquals(-10.0, transformed.getY(), DELTA);
		Assert.assertEquals(10.0, transformed.getMaxX(), DELTA);
		Assert.assertEquals(10.0, transformed.getMaxY(), DELTA);

		// Test identity transformations:
		transformed = viewPort.getTransformationService().transform(bbox, RenderSpace.WORLD, RenderSpace.WORLD);
		Assert.assertEquals(-10.0, transformed.getX(), DELTA);
		Assert.assertEquals(-10.0, transformed.getY(), DELTA);
		Assert.assertEquals(10.0, transformed.getMaxX(), DELTA);
		Assert.assertEquals(10.0, transformed.getMaxY(), DELTA);

		transformed = viewPort.getTransformationService().transform(bbox, RenderSpace.SCREEN, RenderSpace.SCREEN);
		Assert.assertEquals(-10.0, transformed.getX(), DELTA);
		Assert.assertEquals(-10.0, transformed.getY(), DELTA);
		Assert.assertEquals(10.0, transformed.getMaxX(), DELTA);
		Assert.assertEquals(10.0, transformed.getMaxY(), DELTA);
	}

	@Test
	public void testTransformPoint() {
		Geometry point = new Geometry(Geometry.POINT, 0, 0);
		point.setCoordinates(new Coordinate[] { new Coordinate(0, 0) });
		Geometry result = viewPort.getTransformationService().transform(point, RenderSpace.WORLD, RenderSpace.SCREEN);
		Assert.assertEquals(MAP_WIDTH / 2, result.getCoordinates()[0].getX(), DELTA);
		Assert.assertEquals(MAP_HEIGHT / 2, result.getCoordinates()[0].getY(), DELTA);

		result = viewPort.getTransformationService().transform(result, RenderSpace.SCREEN, RenderSpace.WORLD);
		Assert.assertEquals(0, result.getCoordinates()[0].getX(), DELTA);
		Assert.assertEquals(0, result.getCoordinates()[0].getY(), DELTA);
	}

	@Test
	public void testTransformLineString() {
		Geometry line = new Geometry(Geometry.LINE_STRING, 0, 0);
		line.setCoordinates(new Coordinate[] { new Coordinate(0, 0), new Coordinate(10, 10) });
		Geometry result = viewPort.getTransformationService().transform(line, RenderSpace.WORLD, RenderSpace.SCREEN);
		Assert.assertEquals(MAP_WIDTH / 2, result.getCoordinates()[0].getX(), DELTA);
		Assert.assertEquals(MAP_HEIGHT / 2, result.getCoordinates()[0].getY(), DELTA);
		Assert.assertEquals((MAP_WIDTH / 2) + (10 / viewPort.getResolution()), result.getCoordinates()[1].getX(),
				DELTA);
		Assert.assertEquals((MAP_HEIGHT / 2) - (10 / viewPort.getResolution()),
				result.getCoordinates()[1].getY(), DELTA);
	}

	@Test
	public void testTransformPolygon() {
		Geometry shell = new Geometry(Geometry.LINEAR_RING, 0, 0);
		shell.setCoordinates(new Coordinate[] { new Coordinate(-10, -10), new Coordinate(10, 0),
				new Coordinate(-10, 10), new Coordinate(-10, -10) });
		Geometry hole = new Geometry(Geometry.LINEAR_RING, 0, 0);
		hole.setCoordinates(new Coordinate[] { new Coordinate(-5, -5), new Coordinate(5, 0), new Coordinate(-5, 5),
				new Coordinate(-5, -5) });
		Geometry polygon = new Geometry(Geometry.POLYGON, 0, 0);
		polygon.setGeometries(new Geometry[] { shell, hole });

		// World to screen:
		Geometry result = viewPort.getTransformationService().transform(polygon, RenderSpace.WORLD, RenderSpace.SCREEN);
		Coordinate c = result.getGeometries()[0].getCoordinates()[0];
		Assert.assertEquals((MAP_WIDTH / 2) - (10 / viewPort.getResolution()), c.getX(), DELTA);
		Assert.assertEquals((MAP_HEIGHT / 2) + (10 / viewPort.getResolution()), c.getY(), DELTA);
		c = result.getGeometries()[0].getCoordinates()[1];
		Assert.assertEquals((MAP_WIDTH / 2) + (10 / viewPort.getResolution()), c.getX(), DELTA);
		Assert.assertEquals((MAP_HEIGHT / 2), c.getY(), DELTA);
		c = result.getGeometries()[1].getCoordinates()[2];
		Assert.assertEquals((MAP_WIDTH / 2) - (5 / viewPort.getResolution()), c.getX(), DELTA);
		Assert.assertEquals((MAP_HEIGHT / 2) - (5 / viewPort.getResolution()), c.getY(), DELTA);

		// Screen to world:
		result = viewPort.getTransformationService().transform(result, RenderSpace.SCREEN, RenderSpace.WORLD);
		c = result.getGeometries()[0].getCoordinates()[0];
		Assert.assertEquals(-10.0, c.getX(), DELTA);
		Assert.assertEquals(-10.0, c.getY(), DELTA);
		c = result.getGeometries()[0].getCoordinates()[1];
		Assert.assertEquals(10, c.getX(), DELTA);
		Assert.assertEquals(0, c.getY(), DELTA);
		c = result.getGeometries()[1].getCoordinates()[2];
		Assert.assertEquals(-5.0, c.getX(), DELTA);
		Assert.assertEquals(5.0, c.getY(), DELTA);
	}

	@Test
	public void testTransformMultiPoint() {
		Geometry point1 = new Geometry(Geometry.POINT, 0, 0);
		point1.setCoordinates(new Coordinate[] { new Coordinate(0, 0) });
		Geometry point2 = new Geometry(Geometry.POINT, 0, 0);
		point2.setCoordinates(new Coordinate[] { new Coordinate(5, 10) });
		Geometry multiPoint = new Geometry(Geometry.MULTI_POINT, 0, 0);
		multiPoint.setGeometries(new Geometry[] { point1, point2 });

		Geometry result = viewPort.getTransformationService().transform(multiPoint, RenderSpace.WORLD,
				RenderSpace.SCREEN);
		Coordinate coordinate = result.getGeometries()[0].getCoordinates()[0];
		Assert.assertEquals(MAP_WIDTH / 2, coordinate.getX(), DELTA);
		Assert.assertEquals(MAP_HEIGHT / 2, coordinate.getY(), DELTA);
		coordinate = result.getGeometries()[1].getCoordinates()[0];
		Assert.assertEquals((MAP_WIDTH / 2) + (5 / viewPort.getResolution()), coordinate.getX(), DELTA);
		Assert.assertEquals((MAP_HEIGHT / 2) - (10 / viewPort.getResolution()), coordinate.getY(), DELTA);

		result = viewPort.getTransformationService().transform(result, RenderSpace.SCREEN, RenderSpace.WORLD);
		coordinate = result.getGeometries()[0].getCoordinates()[0];
		Assert.assertEquals(0.0, coordinate.getX(), DELTA);
		Assert.assertEquals(0.0, coordinate.getY(), DELTA);
		coordinate = result.getGeometries()[1].getCoordinates()[0];
		Assert.assertEquals(5, coordinate.getX(), DELTA);
		Assert.assertEquals(10, coordinate.getY(), DELTA);
	}

	@Test
	public void testTransformMultiLineString() {
		Geometry ls1 = new Geometry(Geometry.LINE_STRING, 0, 0);
		ls1.setCoordinates(new Coordinate[] { new Coordinate(-5, 10), new Coordinate(10, 5) });
		Geometry ls2 = new Geometry(Geometry.LINE_STRING, 0, 0);
		ls2.setCoordinates(new Coordinate[] { new Coordinate(5, -10), new Coordinate(-10, -5) });
		Geometry mls = new Geometry(Geometry.MULTI_LINE_STRING, 0, 0);
		mls.setGeometries(new Geometry[] { ls1, ls2 });

		Geometry result = viewPort.getTransformationService().transform(mls, RenderSpace.WORLD, RenderSpace.SCREEN);
		Coordinate coordinate = result.getGeometries()[0].getCoordinates()[0];
		Assert.assertEquals((MAP_WIDTH / 2) - (5 / viewPort.getResolution()), coordinate.getX(), DELTA);
		Assert.assertEquals((MAP_HEIGHT / 2) - (10 / viewPort.getResolution()), coordinate.getY(), DELTA);
		coordinate = result.getGeometries()[1].getCoordinates()[1];
		Assert.assertEquals((MAP_WIDTH / 2) - (10 / viewPort.getResolution()), coordinate.getX(), DELTA);
		Assert.assertEquals((MAP_HEIGHT / 2) + (5 / viewPort.getResolution()), coordinate.getY(), DELTA);

		result = viewPort.getTransformationService().transform(result, RenderSpace.SCREEN, RenderSpace.WORLD);
		coordinate = result.getGeometries()[0].getCoordinates()[0];
		Assert.assertEquals(-5.0, coordinate.getX(), DELTA);
		Assert.assertEquals(10.0, coordinate.getY(), DELTA);
		coordinate = result.getGeometries()[1].getCoordinates()[1];
		Assert.assertEquals(-10.0, coordinate.getX(), DELTA);
		Assert.assertEquals(-5.0, coordinate.getY(), DELTA);
	}

	@Test
	public void testTransformMultiPolygon() {
		Geometry shell = new Geometry(Geometry.LINEAR_RING, 0, 0);
		shell.setCoordinates(new Coordinate[] { new Coordinate(-10, -10), new Coordinate(10, 0),
				new Coordinate(-10, 10), new Coordinate(-10, -10) });
		Geometry hole = new Geometry(Geometry.LINEAR_RING, 0, 0);
		hole.setCoordinates(new Coordinate[] { new Coordinate(-5, -5), new Coordinate(5, 0), new Coordinate(-5, 5),
				new Coordinate(-5, -5) });
		Geometry polygon = new Geometry(Geometry.POLYGON, 0, 0);
		polygon.setGeometries(new Geometry[] { shell, hole });
		Geometry mp = new Geometry(Geometry.POLYGON, 0, 0);
		mp.setGeometries(new Geometry[] { polygon });

		// World to screen:
		Geometry result = viewPort.getTransformationService().transform(mp, RenderSpace.WORLD, RenderSpace.SCREEN);
		Coordinate c = result.getGeometries()[0].getGeometries()[0].getCoordinates()[0];
		Assert.assertEquals((MAP_WIDTH / 2) - (10 / viewPort.getResolution()), c.getX(), DELTA);
		Assert.assertEquals((MAP_HEIGHT / 2) + (10 / viewPort.getResolution()), c.getY(), DELTA);
		c = result.getGeometries()[0].getGeometries()[0].getCoordinates()[1];
		Assert.assertEquals((MAP_WIDTH / 2) + (10 / viewPort.getResolution()), c.getX(), DELTA);
		Assert.assertEquals((MAP_HEIGHT / 2), c.getY(), DELTA);
		c = result.getGeometries()[0].getGeometries()[1].getCoordinates()[2];
		Assert.assertEquals((MAP_WIDTH / 2) - (5 / viewPort.getResolution()), c.getX(), DELTA);
		Assert.assertEquals((MAP_HEIGHT / 2) - (5 / viewPort.getResolution()), c.getY(), DELTA);

		// Screen to world:
		result = viewPort.getTransformationService().transform(result, RenderSpace.SCREEN, RenderSpace.WORLD);
		c = result.getGeometries()[0].getGeometries()[0].getCoordinates()[0];
		Assert.assertEquals(-10.0, c.getX(), DELTA);
		Assert.assertEquals(-10.0, c.getY(), DELTA);
		c = result.getGeometries()[0].getGeometries()[0].getCoordinates()[1];
		Assert.assertEquals(10, c.getX(), DELTA);
		Assert.assertEquals(0, c.getY(), DELTA);
		c = result.getGeometries()[0].getGeometries()[1].getCoordinates()[2];
		Assert.assertEquals(-5.0, c.getX(), DELTA);
		Assert.assertEquals(5.0, c.getY(), DELTA);
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