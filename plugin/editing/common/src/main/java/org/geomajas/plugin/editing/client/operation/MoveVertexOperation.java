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

package org.geomajas.plugin.editing.client.operation;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;

/**
 * Geometry index operation that moves a single vertex from one location to another. This implementation does not create
 * a new geometry instance, but changes the given geometry.
 * 
 * @author Pieter De Graef
 * @author Jan Venstermans
 */
public class MoveVertexOperation extends AbstractGeometryIndexOperation {

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
		super(editService);
		this.newLocation = newLocation;
	}

	@Override
	public Geometry execute(Geometry geometry, GeometryIndex index) throws GeometryOperationFailedException {
		this.index = index;
		if (indexService.getType(index) != GeometryIndexType.TYPE_VERTEX) {
			throw new GeometryOperationFailedException("Index of wrong type. Must be TYPE_VERTEX.");
		}
		try {
			oldLocation = indexService.getVertex(geometry, index);
			setVertex(geometry, index, newLocation);
			revertInvalidAndThrow(geometry, index);
			return geometry;
		} catch (GeometryIndexNotFoundException e) {
			throw new GeometryOperationFailedException(e);
		}
	}

	@Override
	public AbstractGeometryIndexOperation getInverseOperation() {
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
			throws GeometryIndexNotFoundException, GeometryOperationInvalidException {
		if (index.hasChild() && geom.getGeometries() != null && geom.getGeometries().length > index.getValue()) {
			setVertex(geom.getGeometries()[index.getValue()], index.getChild(), coordinate);
		} else if (index.getType() == GeometryIndexType.TYPE_VERTEX && geom.getCoordinates() != null
				&& geom.getCoordinates().length > index.getValue()) {
			if (geom.getGeometryType().equals(Geometry.LINEAR_RING)) {
				// In case of a closed ring, the last vertex is not allowed to be moved:
				if ((geom.getCoordinates().length - 1) > index.getValue()) {
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
}