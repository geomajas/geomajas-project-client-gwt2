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

import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;

/**
 * Geometry index operation that deletes a single sub-geometry at the given index. This implementation does not create a
 * new geometry instance, but changes the given geometry. Only indices of type TYPE_GEOMETRY are supported.
 * 
 * @author Pieter De Graef
 */
public class DeleteGeometryOperation extends AbstractGeometryIndexOperation {

	private GeometryIndex index;

	private Geometry deleted;

	/**
	 * Initialize this operation with an editing service.
	 * 
	 * @param editService geometry edit service.
	 */
	public DeleteGeometryOperation(GeometryEditService editService) {
		super(editService);
	}

	@Override
	public Geometry execute(Geometry geometry, GeometryIndex index) throws GeometryOperationFailedException {
		this.index = index;
		if (indexService.getType(index) != GeometryIndexType.TYPE_GEOMETRY) {
			throw new GeometryOperationFailedException("Index of wrong type. Must be TYPE_GEOMETRY.");
		}
		try {
			deleted = indexService.getGeometry(geometry, index);
			delete(geometry, index);
			return geometry;
		} catch (GeometryIndexNotFoundException e) {
			throw new GeometryOperationFailedException(e);
		}
	}

	@Override
	public AbstractGeometryIndexOperation getInverseOperation() {
		return new InsertGeometryOperation(editService, deleted);
	}

	@Override
	public GeometryIndex getGeometryIndex() {
		return index;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void delete(Geometry geometry, GeometryIndex index) throws GeometryIndexNotFoundException {
		if (index.hasChild() && geometry.getGeometries() != null && geometry.getGeometries().length > index.getValue()
				&& index.getValue() >= 0) {
			delete(geometry.getGeometries()[index.getValue()], index.getChild());
		} else if (index.getType() == GeometryIndexType.TYPE_GEOMETRY) {
			if (geometry.getGeometries().length == 1) {
				geometry.setGeometries(null);
			} else {
				Geometry[] result = new Geometry[geometry.getGeometries().length - 1];
				int count = 0;
				for (int i = 0; i < geometry.getGeometries().length; i++) {
					if (i != index.getValue()) {
						result[count] = geometry.getGeometries()[i];
						count++;
					}
				}
				geometry.setGeometries(result);
			}

		} else {
			throw new GeometryIndexNotFoundException("Could not match index with given geometry");
		}
	}
}