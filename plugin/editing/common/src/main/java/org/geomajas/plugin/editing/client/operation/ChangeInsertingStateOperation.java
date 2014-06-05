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
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;

/**
 * Operation that changes the editing state from {@link GeometryEditState#IDLE} to {@link GeometryEditState#INSERTING}
 * and back. This operation is necessary to roll back or undo the last click when inserting.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ChangeInsertingStateOperation implements GeometryIndexOperation {

	private final GeometryEditService service;

	private final GeometryEditState previousState;

	private final GeometryEditState state;

	private GeometryIndex index;

	/**
	 * Initialize this operation with an indexing service.
	 * 
	 * @param service geometry index service.
	 * @param child The child geometry to insert.
	 */
	public ChangeInsertingStateOperation(GeometryEditService service, GeometryEditState state) {
		this.service = service;
		this.state = state;
		previousState = service.getEditingState();
	}

	@Override
	public Geometry execute(Geometry geometry, GeometryIndex index) throws GeometryOperationFailedException {
		this.index = index;
		if (state == GeometryEditState.INSERTING) {
			service.setInsertIndex(index);
		}
		service.setEditingState(state);
		return geometry;
	}

	@Override
	public GeometryIndexOperation getInverseOperation() {
		return new ChangeInsertingStateOperation(service, previousState);
	}

	@Override
	public GeometryIndex getGeometryIndex() {
		return index;
	}

}