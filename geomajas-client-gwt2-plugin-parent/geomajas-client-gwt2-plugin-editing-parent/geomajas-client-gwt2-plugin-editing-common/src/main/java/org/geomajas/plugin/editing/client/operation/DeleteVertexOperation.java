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

package org.geomajas.plugin.editing.client.operation;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.geomajas.plugin.editing.client.service.GeometryIndexService;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;

/**
 * Geometry index operation that deletes a single vertex at the given index. This implementation does not create a new
 * geometry instance, but changes the given geometry.
 * 
 * @author Pieter De Graef
 */
public class DeleteVertexOperation implements GeometryIndexOperation {

	private final GeometryEditService editService;

	private final GeometryIndexService service;

	private GeometryIndex index;

	private Coordinate coordinate;

	/**
	 * Initialize this operation with an edit service.
	 * 
	 * @param editService
	 *            geometry edit service.
	 */
	public DeleteVertexOperation(GeometryEditService editService) {
		this.editService = editService;
		this.service = editService.getIndexService();
	}

	@Override
	public Geometry execute(Geometry geometry, GeometryIndex index) throws GeometryOperationFailedException {
		this.index = index;
		if (service.getType(index) != GeometryIndexType.TYPE_VERTEX) {
			throw new GeometryOperationFailedException("Index of wrong type. Must be TYPE_VERTEX.");
		}
		try {
			coordinate = service.getVertex(geometry, index);
			delete(geometry, index);
			return geometry;
		} catch (GeometryIndexNotFoundException e) {
			throw new GeometryOperationFailedException(e);
		}
	}

	@Override
	public GeometryIndexOperation getInverseOperation() {
		return new InsertVertexOperation(editService, coordinate);
	}

	@Override
	public GeometryIndex getGeometryIndex() {
		return index;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void delete(Geometry geom, GeometryIndex index) throws GeometryIndexNotFoundException,
			EdgesIntersectFailedException {
		if (index.hasChild() && geom.getGeometries() != null && geom.getGeometries().length > index.getValue()) {
			delete(geom.getGeometries()[index.getValue()], index.getChild());
		} else if (index.getType() == GeometryIndexType.TYPE_VERTEX) {
			deleteVertex(geom, index);
		} else {
			throw new GeometryIndexNotFoundException("Could not match index with given geometry");
		}
	}

	private void deleteVertex(Geometry geom, GeometryIndex index) throws GeometryIndexNotFoundException,
			EdgesIntersectFailedException {
		if (Geometry.POINT.equals(geom.getGeometryType())) {
			if (geom.getCoordinates() != null && geom.getCoordinates().length == 1) {
				geom.setCoordinates(null);
			} else {
				throw new GeometryIndexNotFoundException("Vertex index out of bounds.");
			}
		} else if (Geometry.LINE_STRING.equals(geom.getGeometryType())) {
			if (index.getValue() < 0 || geom.getCoordinates() == null || geom.getCoordinates().length == 0
					|| geom.getCoordinates().length <= index.getValue()) {
				throw new GeometryIndexNotFoundException("Vertex index out of bounds.");
			}
			if (geom.getCoordinates().length == 1) {
				geom.setCoordinates(null);
			} else {
				Coordinate[] result = new Coordinate[geom.getCoordinates().length - 1];
				int count = 0;
				for (int i = 0; i < geom.getCoordinates().length; i++) {
					if (i != index.getValue()) {
						result[count] = geom.getCoordinates()[i];
						count++;
					}
				}
				geom.setCoordinates(result);
			}
		} else if (Geometry.LINEAR_RING.equals(geom.getGeometryType())) {
			if (index.getValue() < 0 || geom.getCoordinates() == null || geom.getCoordinates().length == 0
					|| (geom.getCoordinates().length - 1) <= index.getValue()) {
				throw new GeometryIndexNotFoundException("Vertex index out of bounds.");
			}
			if (geom.getCoordinates().length == 2) {
				geom.setCoordinates(null);
			} else {
				if (isDeletedVertexCreatingIntersection(geom.getCoordinates(), index.getValue())) {
					throw new EdgesIntersectFailedException(EditingCommonCustomMessages.
							getInstance().getPolygonLinesCannotIntersectMessage());
				}
				Coordinate[] result = new Coordinate[geom.getCoordinates().length - 1];
				int count = 0;
				for (int i = 0; i < geom.getCoordinates().length; i++) {
					if (i != index.getValue()) {
						result[count] = geom.getCoordinates()[i];
						count++;
					}
				}
				result[result.length - 1] = new Coordinate(result[0]);
				geom.setCoordinates(result);
			}
		} else {
			throw new GeometryIndexNotFoundException("Could not match index with given geometry");
		}
	}

	private boolean isDeletedVertexCreatingIntersection(Coordinate[] currentGeometryCoordinates,
															 int vertexValueCoordinateToBeDeleted) {
		if (!editService.isPolygonEdgesCanIntersect() && currentGeometryCoordinates.length >= 6) {
			// coordinates of the (line) geometry without the selected point
			Coordinate[] geometryCoordinates = new Coordinate[currentGeometryCoordinates.length - 2];
			// fill the geometryCoordinates
			int count = 0;
			for (int i = vertexValueCoordinateToBeDeleted + 1 ;  i < currentGeometryCoordinates.length - 1; i++) {
				geometryCoordinates[count++] = currentGeometryCoordinates[i];
			}
			for (int j = 0 ; j < vertexValueCoordinateToBeDeleted; j++) {
				geometryCoordinates[count++] = currentGeometryCoordinates[j];
			}
			// check if there are enough points
			if (count != geometryCoordinates.length) {
				//something is wrong
				return false;
			}

			// construct geometries
			Geometry existingGeom = new Geometry();
			existingGeom.setCoordinates(geometryCoordinates);
			int leftIndex = vertexValueCoordinateToBeDeleted > 0 ? vertexValueCoordinateToBeDeleted - 1 :
					currentGeometryCoordinates.length - 2 ;
			int rightIndex = vertexValueCoordinateToBeDeleted < currentGeometryCoordinates.length - 1
					? vertexValueCoordinateToBeDeleted + 1 : 0 ;
			Coordinate[] lineCoordinates = {currentGeometryCoordinates[leftIndex],
					currentGeometryCoordinates[rightIndex]};
			Geometry lineGeom1 = new Geometry();
			lineGeom1.setCoordinates(lineCoordinates);

			// compare geometries for intersection
			if (GeometryService.intersects(existingGeom, lineGeom1)) {
				return true;
			}
		}
		return false;
	}
}