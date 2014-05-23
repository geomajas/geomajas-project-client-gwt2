package org.geomajas.plugin.editing.client.operation;

import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;

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
		if(state == GeometryEditState.INSERTING) {
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