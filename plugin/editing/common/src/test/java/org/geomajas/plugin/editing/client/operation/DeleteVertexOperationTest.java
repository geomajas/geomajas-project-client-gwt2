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

package org.geomajas.plugin.editing.client.operation;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditServiceImpl;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexService;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for deleting a vertex from any type of geometry.
 * 
 * @author Pieter De Graef
 */
public class DeleteVertexOperationTest {

	private static final double DELTA = 0.0001;

	private GeometryIndexService service = new GeometryIndexService();

	private GeometryEditService geometryEditService = new GeometryEditServiceImpl();

	private Geometry point = new Geometry(Geometry.POINT, 0, 0);

	private Geometry lineString = new Geometry(Geometry.LINE_STRING, 0, 0);

	private Geometry linearRing = new Geometry(Geometry.LINEAR_RING, 0, 0);

	private Geometry polygon = new Geometry(Geometry.POLYGON, 0, 0);

	private Geometry multiPoint = new Geometry(Geometry.MULTI_POINT, 0, 0);

	private Geometry multiLineString = new Geometry(Geometry.MULTI_LINE_STRING, 0, 0);

	private Geometry multiPolygon = new Geometry(Geometry.MULTI_POLYGON, 0, 0);

	// ------------------------------------------------------------------------
	// Constructor: initialize geometries.
	// ------------------------------------------------------------------------

	public DeleteVertexOperationTest() {
		point.setCoordinates(new Coordinate[] { new Coordinate(1, 1) });
		lineString
				.setCoordinates(new Coordinate[] { new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(3, 3) });
		linearRing.setCoordinates(new Coordinate[] { new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(3, 3),
				new Coordinate(1, 1) });

		Geometry shell = new Geometry(Geometry.LINEAR_RING, 0, 0);
		shell.setCoordinates(new Coordinate[] { new Coordinate(0, 0), new Coordinate(10, 0), new Coordinate(10, 10),
				new Coordinate(0, 10), new Coordinate(0, 0) });
		Geometry hole = new Geometry(Geometry.LINEAR_RING, 0, 0);
		hole.setCoordinates(new Coordinate[] { new Coordinate(4, 4), new Coordinate(6, 4), new Coordinate(6, 6),
				new Coordinate(4, 6), new Coordinate(4, 4) });
		polygon.setGeometries(new Geometry[] { shell, hole });

		Geometry point2 = new Geometry(Geometry.POINT, 0, 0);
		point2.setCoordinates(new Coordinate[] { new Coordinate(2, 2) });
		multiPoint.setGeometries(new Geometry[] { point, point2 });

		Geometry lineString2 = new Geometry(Geometry.LINE_STRING, 0, 0);
		lineString2.setCoordinates(new Coordinate[] { new Coordinate(4, 4), new Coordinate(5, 5), new Coordinate(6, 6),
				new Coordinate(7, 7) });
		multiLineString.setGeometries(new Geometry[] { lineString, lineString2 });

		Geometry shell2 = new Geometry(Geometry.LINEAR_RING, 0, 0);
		shell2.setCoordinates(new Coordinate[] { new Coordinate(0, 0), new Coordinate(10, 0), new Coordinate(10, 10),
				new Coordinate(0, 10), new Coordinate(0, 0) });
		Geometry hole2 = new Geometry(Geometry.LINEAR_RING, 0, 0);
		hole2.setCoordinates(new Coordinate[] { new Coordinate(3, 3), new Coordinate(7, 3), new Coordinate(7, 7),
				new Coordinate(3, 7), new Coordinate(3, 3) });
		Geometry polygon2 = new Geometry(Geometry.POLYGON, 0, 0);
		polygon2.setGeometries(new Geometry[] { shell2, hole2 });
		multiPolygon.setGeometries(new Geometry[] { polygon, polygon2 });
	}

	// ------------------------------------------------------------------------
	// Test the GeometryIndexDeleteVertexOperation for different geometry types
	// ------------------------------------------------------------------------

	@Test
	public void testEmptyPoint() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);
		Geometry point = new Geometry(Geometry.POINT, 0, 0);

		// Geometry index of wrong type:
		try {
			operation.execute(point, service.create(GeometryIndexType.TYPE_VERTEX, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testPoint() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);
		double value = point.getCoordinates()[0].getX();

		// Remove the single vertex:
		Geometry result = operation.execute(point, service.create(GeometryIndexType.TYPE_VERTEX, 0));
		Assert.assertNull(result.getCoordinates());

		// Undo the delete operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(value, undone.getCoordinates()[0].getX(), DELTA);
	}

	@Test
	public void testPointCornerCases() {
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);

		// Geometry index of wrong type:
		try {
			operation.execute(point, service.create(GeometryIndexType.TYPE_EDGE, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(point, service.create(GeometryIndexType.TYPE_GEOMETRY, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}

		// Non existing geometry index:
		try {
			operation.execute(point, service.create(GeometryIndexType.TYPE_VERTEX, -1));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(point, service.create(GeometryIndexType.TYPE_VERTEX, 1));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(point, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testErrorPoint() {
		Geometry point = new Geometry(Geometry.POINT, 0, 0);
		point.setCoordinates(new Coordinate[] { new Coordinate(1, 1), new Coordinate(2, 2) });
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);

		// Point with 2 coordinates...
		try {
			operation.execute(point, service.create(GeometryIndexType.TYPE_VERTEX, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testEmptyLineString() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);
		Geometry lineString = new Geometry(Geometry.LINE_STRING, 0, 0);

		// Insert the first point:
		try {
			operation.execute(lineString, service.create(GeometryIndexType.TYPE_VERTEX, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testLineStringVertexAtBegin() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);
		int count = lineString.getCoordinates().length;
		double x = lineString.getCoordinates()[1].getX();

		// Delete first vertex:
		Geometry result = operation.execute(lineString, service.create(GeometryIndexType.TYPE_VERTEX, 0));
		Assert.assertNotNull(result);
		Assert.assertEquals(x, result.getCoordinates()[0].getX(), DELTA);
		Assert.assertEquals(count - 1, result.getCoordinates().length);

		// Undo the delete operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(x, undone.getCoordinates()[1].getX(), DELTA);
	}

	@Test
	public void testLineStringVertexInMiddle() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);
		int count = lineString.getCoordinates().length;
		double x = lineString.getCoordinates()[2].getX();

		// Delete vertex in the middle:
		Geometry result = operation.execute(lineString, service.create(GeometryIndexType.TYPE_VERTEX, 1));
		Assert.assertNotNull(result);
		Assert.assertEquals(x, result.getCoordinates()[1].getX(), DELTA);
		Assert.assertEquals(count - 1, result.getCoordinates().length);

		// Undo the delete operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(x, undone.getCoordinates()[2].getX(), DELTA);
	}

	@Test
	public void testLineStringVertexAtEnd() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);
		int count = lineString.getCoordinates().length;
		double x = lineString.getCoordinates()[2].getX();

		// Delete last vertex:
		Geometry result = operation.execute(lineString, service.create(GeometryIndexType.TYPE_VERTEX, 2));
		Assert.assertNotNull(result);
		Assert.assertEquals(count - 1, result.getCoordinates().length);

		// Undo the delete operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(x, undone.getCoordinates()[2].getX(), DELTA);
	}

	@Test
	public void testLineStringCornerCases() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);

		// Geometry index of wrong type:
		try {
			operation.execute(lineString, service.create(GeometryIndexType.TYPE_GEOMETRY, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(lineString, service.create(GeometryIndexType.TYPE_EDGE, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}

		// Non existing geometry index:
		try {
			operation.execute(lineString, service.create(GeometryIndexType.TYPE_VERTEX, -1));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(lineString, service.create(GeometryIndexType.TYPE_VERTEX, 3));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(lineString, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testEmptyLinearRing() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);
		Geometry linearRing = new Geometry(Geometry.LINEAR_RING, 0, 0);

		// Insert the first point:
		try {
			operation.execute(linearRing, service.create(GeometryIndexType.TYPE_VERTEX, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testLinearRingVertexAtBegin() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);
		int count = linearRing.getCoordinates().length;
		double x = linearRing.getCoordinates()[1].getX();

		// Delete first vertex (check if closed):
		Geometry result = operation.execute(linearRing, service.create(GeometryIndexType.TYPE_VERTEX, 0));
		Assert.assertNotNull(result);
		Assert.assertEquals(x, result.getCoordinates()[0].getX(), DELTA);
		Assert.assertEquals(x, result.getCoordinates()[count - 2].getX(), DELTA);
		Assert.assertEquals(count - 1, result.getCoordinates().length);

		// Undo the delete operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(x, undone.getCoordinates()[1].getX(), DELTA);
	}

	@Test
	public void testLinearRingVertexInMiddle() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);
		int count = linearRing.getCoordinates().length;
		double x = linearRing.getCoordinates()[2].getX();

		// Delete second vertex:
		Geometry result = operation.execute(linearRing, service.create(GeometryIndexType.TYPE_VERTEX, 1));
		Assert.assertNotNull(result);
		Assert.assertEquals(x, result.getCoordinates()[1].getX(), DELTA);
		Assert.assertEquals(result.getCoordinates()[0].getX(), result.getCoordinates()[count - 2].getX(), DELTA);
		Assert.assertEquals(count - 1, result.getCoordinates().length);

		// Undo the delete operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(x, undone.getCoordinates()[2].getX(), DELTA);
	}

	@Test
	public void testLinearRingVertexAtEnd() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);
		int count = linearRing.getCoordinates().length;
		double x = linearRing.getCoordinates()[3].getX();

		// Delete the last vertex:
		Geometry result = operation.execute(linearRing, service.create(GeometryIndexType.TYPE_VERTEX, 2));
		Assert.assertNotNull(result);
		Assert.assertEquals(x, result.getCoordinates()[2].getX(), DELTA);
		Assert.assertEquals(result.getCoordinates()[0].getX(), result.getCoordinates()[count - 2].getX(), DELTA);
		Assert.assertEquals(count - 1, result.getCoordinates().length);

		// Undo the delete operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(x, undone.getCoordinates()[3].getX(), DELTA);
	}

	@Test
	public void testLinearRingCornerCases() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);

		// Geometry index of wrong type:
		try {
			operation.execute(linearRing, service.create(GeometryIndexType.TYPE_GEOMETRY, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(linearRing, service.create(GeometryIndexType.TYPE_EDGE, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}

		// Non existing geometry index:
		try {
			operation.execute(linearRing, service.create(GeometryIndexType.TYPE_VERTEX, -1));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(linearRing, service.create(GeometryIndexType.TYPE_VERTEX, 3));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(linearRing, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testEmptyPolygon() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);
		Geometry polygon = new Geometry(Geometry.POLYGON, 0, 0);

		try {
			operation.execute(polygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testPolygon() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);
		int count = polygon.getGeometries()[0].getCoordinates().length;
		double x = polygon.getGeometries()[0].getCoordinates()[1].getX();

		// Delete first vertex of the exterior ring:
		Geometry result = operation.execute(polygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0));
		Assert.assertEquals(x, result.getGeometries()[0].getCoordinates()[0].getX(), DELTA);
		Assert.assertEquals(count - 1, result.getGeometries()[0].getCoordinates().length);

		// Undo the delete operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(x, undone.getGeometries()[0].getCoordinates()[1].getX(), DELTA);
	}

	@Test
	public void testPolygonCornerCases() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);

		// Geometry index of wrong type:
		try {
			operation.execute(polygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(polygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}

		// Non existing geometry index:
		try {
			operation.execute(polygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 4));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(polygon, service.create(GeometryIndexType.TYPE_VERTEX, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(polygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testEmptyMultiPoint() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);
		Geometry multiPoint = new Geometry(Geometry.MULTI_POINT, 0, 0);

		// First a correct index. This should work:
		try {
			operation.execute(multiPoint, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testMultiPoint() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);
		double x = multiPoint.getGeometries()[0].getCoordinates()[0].getX();

		// Remove the single vertex:
		Geometry result = operation.execute(multiPoint, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0));
		Assert.assertNull(result.getGeometries()[0].getCoordinates());

		// Undo the delete operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(x, undone.getGeometries()[0].getCoordinates()[0].getX(), DELTA);
	}

	@Test
	public void testMultiPointCornerCases() {
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);

		// Geometry index of wrong type:
		try {
			operation.execute(multiPoint, service.create(GeometryIndexType.TYPE_EDGE, 2, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}

		// Non existing geometry index:
		try {
			operation.execute(multiPoint, service.create(GeometryIndexType.TYPE_VERTEX, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(multiPoint, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testEmptyMultiLineString() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);
		Geometry multiLineString = new Geometry(Geometry.MULTI_LINE_STRING, 0, 0);

		// First a correct index. This should work:
		try {
			operation.execute(multiLineString, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testMultiLineString() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);
		double x = multiLineString.getGeometries()[1].getCoordinates()[3].getX();

		// First a correct index. This should work:
		Geometry result = operation.execute(multiLineString, service.create(GeometryIndexType.TYPE_VERTEX, 1, 2));
		Assert.assertEquals(x, result.getGeometries()[1].getCoordinates()[2].getX(), DELTA);

		// Undo the delete operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(x, undone.getGeometries()[1].getCoordinates()[3].getX(), DELTA);
	}

	@Test
	public void testMultiLineStringCornerCases() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);

		// Geometry index of wrong type:
		try {
			operation.execute(multiLineString, service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}

		// Non existing geometry index:
		try {
			operation.execute(multiLineString, service.create(GeometryIndexType.TYPE_VERTEX, 0, 3));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(multiLineString, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(multiLineString, service.create(GeometryIndexType.TYPE_VERTEX, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testEmptyMultiPolygon() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);
		Geometry multiPolygon = new Geometry(Geometry.MULTI_POLYGON, 0, 0);

		// First a correct index. This should work:
		try {
			operation.execute(multiPolygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	@Test
	public void testMultiPolygon() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);
		double x = multiPolygon.getGeometries()[0].getGeometries()[0].getCoordinates()[2].getX();

		// First a correct index. This should work:
		Geometry result = operation.execute(multiPolygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 1));
		Assert.assertEquals(x, result.getGeometries()[0].getGeometries()[0].getCoordinates()[1].getX(), DELTA);

		// Undo the delete operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(x, undone.getGeometries()[0].getGeometries()[0].getCoordinates()[2].getX(), DELTA);
	}

	@Test
	public void testMultiPolygonCornerCases() throws GeometryOperationFailedException {
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);

		// Geometry index of wrong type:
		try {
			operation.execute(multiPolygon, service.create(GeometryIndexType.TYPE_GEOMETRY, 0, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}

		// Non existing geometry index:
		try {
			operation.execute(multiPolygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 4));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(multiPolygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
		try {
			operation.execute(multiPolygon, service.create(GeometryIndexType.TYPE_VERTEX, 0, 0, 0, 0));
			Assert.fail();
		} catch (GeometryOperationFailedException e) {
			// We expect an error...
		}
	}

	/* Look at GeometryEditService properties when inserting is true. */

	@Test
	public void testPointDeleteAndUndoGeometryEditService() throws GeometryOperationFailedException {
		GeometryIndex geometryIndexOfLastCoordinate = service.create(GeometryIndexType.TYPE_VERTEX, 0);
		geometryEditService.setEditingState(GeometryEditState.IDLE);

		// perform operation
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);
		Geometry result = operation.execute(point, geometryIndexOfLastCoordinate);
		Assert.assertNull(geometryEditService.getTentativeMoveOrigin());
		Assert.assertNull(geometryEditService.getInsertIndex());
		/*Assert.assertEquals(GeometryIndexType.TYPE_VERTEX, geometryEditService.getInsertIndex().getType());
		Assert.assertEquals(0, geometryEditService.getInsertIndex().getValue());
		Assert.assertNull(geometryEditService.getInsertIndex().getChild());*/

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertNull(geometryEditService.getTentativeMoveOrigin());
		Assert.assertNull(geometryEditService.getInsertIndex());
		Assert.assertFalse(geometryEditService.getEditingState().equals(GeometryEditState.INSERTING));
	}

	@Test
	public void testLineStringDeleteAndUndoGeometryEditService() throws GeometryOperationFailedException {
		GeometryIndex geometryIndexOfLastCoordinate = service.create(GeometryIndexType.TYPE_VERTEX, lineString.getCoordinates().length - 1);
		geometryEditService.setEditingState(GeometryEditState.INSERTING);

		// perform operation
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);
		Geometry result = operation.execute(lineString, geometryIndexOfLastCoordinate);
		Assert.assertEquals(result.getCoordinates()[result.getCoordinates().length - 1],
				geometryEditService.getTentativeMoveOrigin());
		Assert.assertEquals(geometryIndexOfLastCoordinate, geometryEditService.getInsertIndex());

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertTrue(geometryEditService.getEditingState().equals(GeometryEditState.INSERTING));
		Assert.assertEquals(undone.getCoordinates()[undone.getCoordinates().length - 1],
				geometryEditService.getTentativeMoveOrigin());
		Assert.assertEquals(GeometryIndexType.TYPE_VERTEX, geometryEditService.getInsertIndex().getType());
		Assert.assertEquals(geometryIndexOfLastCoordinate.getValue() + 1, geometryEditService.getInsertIndex().getValue());
		Assert.assertNull(geometryEditService.getInsertIndex().getChild());
	}

	@Test
	public void testLinearRingDeleteAndUndoGeometryEditService() throws GeometryOperationFailedException {
		GeometryIndex geometryIndexOfLastCoordinate = service.create(GeometryIndexType.TYPE_VERTEX, linearRing.getCoordinates().length - 2);
		geometryEditService.setEditingState(GeometryEditState.INSERTING);

		// perform operation
		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);
		Geometry result = operation.execute(linearRing, geometryIndexOfLastCoordinate);
		Assert.assertEquals(result.getCoordinates()[result.getCoordinates().length - 2],
				geometryEditService.getTentativeMoveOrigin());
		Assert.assertEquals(geometryIndexOfLastCoordinate, geometryEditService.getInsertIndex());

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(GeometryEditState.INSERTING, geometryEditService.getEditingState());
		Assert.assertEquals(undone.getCoordinates()[undone.getCoordinates().length - 2],
				geometryEditService.getTentativeMoveOrigin());
		Assert.assertEquals(GeometryIndexType.TYPE_VERTEX, geometryEditService.getInsertIndex().getType());
		Assert.assertEquals(null, geometryEditService.getInsertIndex().getChild());
		Assert.assertEquals(geometryIndexOfLastCoordinate.getValue() + 1, geometryEditService.getInsertIndex().getValue());
	}

	@Test
	public void testPolygonDeleteAndUndoGeometryEditService() throws GeometryOperationFailedException {
		Geometry polygonLinearRing = polygon.getGeometries()[0];
		GeometryIndex geometryIndexOfLastCoordinate = service.create(GeometryIndexType.TYPE_VERTEX, 0,
				polygonLinearRing.getCoordinates().length - 2);
		geometryEditService.setEditingState(GeometryEditState.INSERTING);

		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);
		Geometry result = operation.execute(polygon, geometryIndexOfLastCoordinate);
		Assert.assertEquals(result.getGeometries()[0].getCoordinates()[result.getGeometries()[0].getCoordinates().length - 2],
				geometryEditService.getTentativeMoveOrigin());
		Assert.assertEquals(geometryIndexOfLastCoordinate, geometryEditService.getInsertIndex());

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(GeometryEditState.INSERTING, geometryEditService.getEditingState());
		Assert.assertEquals(undone.getGeometries()[0].getCoordinates()[undone.getGeometries()[0].getCoordinates().length - 2],
				geometryEditService.getTentativeMoveOrigin());
		GeometryIndex indexAfterExecute = geometryEditService.getInsertIndex();
		Assert.assertEquals(GeometryIndexType.TYPE_GEOMETRY, indexAfterExecute.getType());
		Assert.assertNotNull(indexAfterExecute.getChild());
		Assert.assertEquals(GeometryIndexType.TYPE_VERTEX, indexAfterExecute.getChild().getType());
		Assert.assertEquals(geometryIndexOfLastCoordinate.getChild().getValue() + 1, indexAfterExecute.getChild().getValue());
	}

	@Test
	public void testMultiPointDeleteAndUndoGeometryEditService() throws GeometryOperationFailedException {
		int multiPointLastPointIndex = multiPoint.getGeometries().length - 2;
		GeometryIndex geometryIndexOfLastCoordinate = service.create(GeometryIndexType.TYPE_VERTEX, multiPointLastPointIndex, 0);
		geometryEditService.setEditingState(GeometryEditState.INSERTING);

		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);
		Geometry result = operation.execute(multiPoint, geometryIndexOfLastCoordinate);
		Assert.assertNull(geometryEditService.getTentativeMoveOrigin());
		Assert.assertEquals(geometryIndexOfLastCoordinate, geometryEditService.getInsertIndex());
		Assert.assertTrue(geometryEditService.getEditingState().equals(GeometryEditState.INSERTING));

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		GeometryIndex indexAfterUndo = geometryEditService.getInsertIndex();
		Assert.assertEquals(GeometryIndexType.TYPE_GEOMETRY, indexAfterUndo.getType());
		Assert.assertEquals(geometryIndexOfLastCoordinate.getValue() + 1, indexAfterUndo.getValue());
		Assert.assertNotNull(indexAfterUndo.getChild());
		Assert.assertEquals(GeometryIndexType.TYPE_VERTEX, indexAfterUndo.getChild().getType());
		Assert.assertEquals(0, indexAfterUndo.getChild().getValue());
	}

	@Test
	public void testMultiLineStringDeleteAndUndoGeometryEditService() throws GeometryOperationFailedException {
		int multiLineStringLastLineStringIndex = multiLineString.getGeometries().length - 1;
		Geometry lastLineString = multiLineString.getGeometries()[multiLineStringLastLineStringIndex];
		GeometryIndex geometryIndexOfLastCoordinate = service.create(GeometryIndexType.TYPE_VERTEX,
				multiLineStringLastLineStringIndex,
				lastLineString.getCoordinates().length - 1);
		geometryEditService.setEditingState(GeometryEditState.INSERTING);

		GeometryIndexOperation operation = new DeleteVertexOperation(service, geometryEditService);
		Geometry result = operation.execute(multiLineString, geometryIndexOfLastCoordinate);
		Assert.assertEquals(geometryIndexOfLastCoordinate, geometryEditService.getInsertIndex());
		Assert.assertEquals(lastLineString.getCoordinates()[lastLineString.getCoordinates().length - 1], geometryEditService.getTentativeMoveOrigin());

		// Undo the insert operation:
		Geometry undone = operation.getInverseOperation().execute(result, operation.getGeometryIndex());
		Assert.assertEquals(GeometryEditState.INSERTING, geometryEditService.getEditingState());
		Assert.assertEquals(lastLineString.getCoordinates()[lastLineString.getCoordinates().length - 1],
				geometryEditService.getTentativeMoveOrigin());
		GeometryIndex indexAfterUndo = geometryEditService.getInsertIndex();
		Assert.assertEquals(GeometryIndexType.TYPE_GEOMETRY, indexAfterUndo.getType());
		Assert.assertNotNull(indexAfterUndo.getChild());
		Assert.assertEquals(GeometryIndexType.TYPE_VERTEX, indexAfterUndo.getChild().getType());
		Assert.assertEquals(geometryIndexOfLastCoordinate.getChild().getValue() + 1, indexAfterUndo.getChild().getValue());

	}
}