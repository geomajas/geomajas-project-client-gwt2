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
 * Geometry index operation that moves a single vertex from one location to another. This implementation does not create
 * a new geometry instance, but changes the given geometry.
 * 
 * @author Pieter De Graef
 * @author Jan Venstermans
 */
public class MoveVertexOperation implements GeometryIndexOperation {

	private final GeometryEditService editService;

	private final GeometryIndexService service;

	private final Coordinate newLocation;

	private Coordinate oldLocation;

	private GeometryIndex index;

	/**
	 * Initialize this operation with an edit service.
	 * 
	 * @param editService
	 *            geometry edit service.
	 */
	public MoveVertexOperation(GeometryEditService editService, Coordinate newLocation) {
		this.editService = editService;
		this.service = editService.getIndexService();
		this.newLocation = newLocation;
	}

	@Override
	public Geometry execute(Geometry geometry, GeometryIndex index) throws GeometryOperationFailedException {
		this.index = index;
		if (service.getType(index) != GeometryIndexType.TYPE_VERTEX) {
			throw new GeometryOperationFailedException("Index of wrong type. Must be TYPE_VERTEX.");
		}
		try {
			oldLocation = service.getVertex(geometry, index);
			setVertex(geometry, index, newLocation);
			return geometry;
		} catch (GeometryIndexNotFoundException e) {
			throw new GeometryOperationFailedException(e);
		}
	}

	@Override
	public GeometryIndexOperation getInverseOperation() {
		return new MoveVertexOperation(editService, oldLocation);
	}

	@Override
	public GeometryIndex getGeometryIndex() {
		return index;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void setVertex(Geometry geom, GeometryIndex index, Coordinate coordinate)
			throws GeometryIndexNotFoundException, GeometryOperationFailedException {
		if (index.hasChild() && geom.getGeometries() != null && geom.getGeometries().length > index.getValue()) {
			setVertex(geom.getGeometries()[index.getValue()], index.getChild(), coordinate);
		} else if (index.getType() == GeometryIndexType.TYPE_VERTEX && geom.getCoordinates() != null
				&& geom.getCoordinates().length > index.getValue()) {
			if (geom.getGeometryType().equals(Geometry.LINEAR_RING)) {
				// In case of a closed ring, the last vertex is not allowed to be moved:
				if ((geom.getCoordinates().length - 1) > index.getValue()) {
					if (isMovedVertexIntersectsWithExistingLines(geom.getCoordinates(), index.getValue(), coordinate)) {
						throw new GeometryOperationFailedException(EditingCommonCustomMessages.
								getInstance().getPolygonLinesCannotIntersectMessage());
					}
					geom.getCoordinates()[index.getValue()] = coordinate;
					if (index.getValue() == 0) {
						// In case of closed ring, keep last coordinate equal to the first:
						geom.getCoordinates()[geom.getCoordinates().length - 1] = new Coordinate(coordinate);
					}
				} else {
					throw new GeometryIndexNotFoundException("Can't move closing vertex of a LinearRing, "
							+ "move index=0 instead.");
				}
			} else {
				geom.getCoordinates()[index.getValue()] = coordinate;
			}
		} else {
			throw new GeometryIndexNotFoundException("Could not match index with given geometry");
		}
	}

	private boolean isMovedVertexIntersectsWithExistingLines(Coordinate[] currentGeometryCoordinates,
							int vertexValueCoordinateToBeMoved, Coordinate newCoordinate) {
		if (!editService.isPolygonEdgesCanIntersect() && currentGeometryCoordinates.length >= 5) {
			// coordinates of the (line) geometry without the selected point
			Coordinate[] geometryCoordinates = new Coordinate[currentGeometryCoordinates.length - 2];
			// fill the geometryCoordinates
			int count = 0;
			for (int i = vertexValueCoordinateToBeMoved + 1 ;  i < currentGeometryCoordinates.length - 1; i++) {
				geometryCoordinates[count++] = currentGeometryCoordinates[i];
			}
			for (int j = 0 ; j < vertexValueCoordinateToBeMoved; j++) {
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
			Coordinate[] lineCoordinates1 = {geometryCoordinates[0], newCoordinate};
			Coordinate[] lineCoordinates2 = {geometryCoordinates[geometryCoordinates.length - 1], newCoordinate};
			Geometry lineGeom1 = new Geometry();
			lineGeom1.setCoordinates(lineCoordinates1);
			Geometry lineGeom2 = new Geometry();
			lineGeom2.setCoordinates(lineCoordinates2);

			// compare geometries for intersection
			if (GeometryService.intersects(existingGeom, lineGeom1)  ||
					GeometryService.intersects(existingGeom, lineGeom2)) {
				return true;
			}
		}
		return false;
	}
}