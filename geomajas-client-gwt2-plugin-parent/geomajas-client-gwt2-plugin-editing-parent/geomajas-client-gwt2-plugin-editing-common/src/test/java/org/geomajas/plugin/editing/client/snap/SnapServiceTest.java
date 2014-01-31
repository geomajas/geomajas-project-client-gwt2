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

package org.geomajas.plugin.editing.client.snap;

import junit.framework.Assert;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.snap.SnapAlgorithm;
import org.geomajas.plugin.editing.client.snap.SnapService;
import org.geomajas.plugin.editing.client.snap.SnapSourceProvider;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.type.GeometryType;

/**
 * Testcase for the methods of the {@link SnapService}.
 * 
 * @author Pieter De Graef
 * @author Jan Venstermans
 */
public class SnapServiceTest {

	private SnapService service = new SnapService();

	private SnapSourceProvider sourceProvider = new StaticSnapSourceProvider();

	private SnapAlgorithm algorithm = new FirstCoordinateSnapAlgorithm();

	private final int NUM_POINTS = 2;

	private SnapAlgorithm[] pointAlgorithms = new FirstCoordinateSnapAlgorithm[NUM_POINTS];

	private Coordinate[] pointCoordinates = new Coordinate[NUM_POINTS];

	@Before
	public void before() {
		//disable server side logging
		service.setLoggingActive(false);
		for (int i = 0 ; i < NUM_POINTS ; i++) {
			pointCoordinates[i] = new Coordinate();
			Coordinate[] coordArray = {pointCoordinates[i]};
			Geometry point = new Geometry();
			point.setGeometryType(Geometry.POINT);
			point.setCoordinates(coordArray);
			Geometry[] geomArray = {point};
			pointAlgorithms[i] = new FirstCoordinateSnapAlgorithm();
			pointAlgorithms[i].setGeometries(geomArray);
		}
	}

	/* basic tests using FirstCoordinateSnapAlgorithm */

	@Test
	public void testSnappingOneRule() {
		service.addSnappingRule(algorithm, sourceProvider, 10, true);
		service.update(null);
		Coordinate result = service.snap(new Coordinate(5, 5));
		Assert.assertEquals(0.0, result.getX());
		Assert.assertEquals(0.0, result.getY());
	}

	@Test
	public void testSnappingTwoRulesSecondHit() {
		setCoordinate(0,0,0);
		setCoordinate(1,15,15);
		service.addSnappingRule(pointAlgorithms[0], sourceProvider, 10, false);
		service.addSnappingRule(pointAlgorithms[1], sourceProvider, 20, false);
		Coordinate result = service.snap(new Coordinate(12, 12));
		assertEqualityCoordinates(result, pointCoordinates[1]);
	}

	@Test
	public void testSnappingTwoRulesBothHitFirstWins() {
		setCoordinate(0,0,0);
		setCoordinate(1,9,9);
		service.addSnappingRule(pointAlgorithms[0], sourceProvider, 10, false);
		service.addSnappingRule(pointAlgorithms[1], sourceProvider, 20, false);
		Coordinate result = service.snap(new Coordinate(5, 5));
		assertEqualityCoordinates(result, pointCoordinates[0]);
	}

	@Test
	public void testSnappingTwoRulesBothHitSecondHasPriority() {
		setCoordinate(0,0,0);
		setCoordinate(1,9,9);
		service.addSnappingRule(pointAlgorithms[0], sourceProvider, 10, false);
		service.addSnappingRule(pointAlgorithms[1], sourceProvider, 20, true);
		Coordinate result = service.snap(new Coordinate(5, 5));
		assertEqualityCoordinates(result, pointCoordinates[1]);
	}

	@Test
	public void testSnappingTwoRulesFirstHitSecondHasPriorityWithoutHit() {
		setCoordinate(0,0,0);
		setCoordinate(1,20,20);
		service.addSnappingRule(pointAlgorithms[0], sourceProvider, 10, false);
		service.addSnappingRule(pointAlgorithms[1], sourceProvider, 5, true);
		Coordinate result = service.snap(new Coordinate(5, 5));
		assertEqualityCoordinates(result, pointCoordinates[0]);
	}

	private void setCoordinate(int i, double x, double y) {
		pointCoordinates[i].setX(x);
		pointCoordinates[i].setY(x);
	}

	private void assertEqualityCoordinates(Coordinate result, Coordinate expected) {
		Assert.assertEquals(expected.getX(), result.getX());
		Assert.assertEquals(expected.getY(), result.getY());
	}
}