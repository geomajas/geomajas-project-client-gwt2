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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.geometry.service.GeometryValidationContext;
import org.geomajas.geometry.service.GeometryValidationState;
import org.geomajas.geometry.service.validation.ValidationViolation;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.geomajas.plugin.editing.client.service.GeometryIndexService;

/**
 * Some common validation logic for {@link GeometryIndexOperation}.
 * 
 * @author Jan De Moerloose
 * 
 */
public abstract class AbstractGeometryIndexOperation implements GeometryIndexOperation {

	protected final GeometryEditService editService;

	protected final GeometryIndexService indexService;

	protected boolean allowValidation = true;

	public AbstractGeometryIndexOperation(GeometryEditService editService) {
		this.editService = editService;
		this.indexService = editService.getIndexService();
	}

	@Override
	public abstract AbstractGeometryIndexOperation getInverseOperation();

	public boolean isAllowValidation() {
		return allowValidation;
	}

	public void setAllowValidation(boolean allowValidation) {
		this.allowValidation = allowValidation;
	}

	/**
	 * Revert the operation if the geometry is invalid and throw an {@link GeometryOperationInvalidException}.
	 * 
	 * @param geometry
	 * @param index
	 * @throws GeometryOperationInvalidException
	 */
	protected void revertInvalidAndThrow(Geometry geometry, GeometryIndex index)
			throws GeometryOperationInvalidException {
		if (editService.isValidating() && isAllowValidation()) {
			GeometryValidationState state = validate(geometry, index);
			if (state.isValid()) {
				return;
			} else {
				try {
					AbstractGeometryIndexOperation inverseOperation = getInverseOperation();
					inverseOperation.setAllowValidation(false);
					getInverseOperation().execute(geometry, index);
				} catch (GeometryOperationFailedException e) {
					// should not fail, ignore !!!
				}
				throw new GeometryOperationInvalidException(state);
			}
		}
	}

	protected GeometryValidationState validate(Geometry geom, GeometryIndex index) {
		if (editService.getEditingState() == GeometryEditState.INSERTING) {
			// for polygons, validate the previous edge (not the closing line edge !)
			if (indexService.isVertex(index)) {
				if(indexService.getSiblingCount(geom, index) > 2) {
					try {
						List<GeometryIndex> edges = indexService.getAdjacentEdges(geom, index);
						return GeometryService.validate(geom, toArray(edges.get(0)));
					} catch (GeometryIndexNotFoundException e) {
						// should never happen, must return something here
						return GeometryValidationState.VALID;
					}
				} else {
					// inserting 1st or 2nd point of new ring or geometry, evaluate the geometry
					return GeometryService.validate(geom, toArray(indexService.getParent(index)));
				}
			} else {
				// inserting ring or new geometry, evaluate normally
				return GeometryService.validate(geom, toArray(index));
			}
		} else {
			if (indexService.isVertex(index)) {
				try {
					List<GeometryIndex> edges = indexService.getAdjacentEdges(geom, index);
					List<ValidationViolation> violations = new ArrayList<ValidationViolation>();
					for (GeometryIndex edge : edges) {
						GeometryService.validate(geom, toArray(edge));
						violations.addAll(GeometryService.getValidationContext().getViolations());
					}
					return violations.size() == 0 ? GeometryValidationState.VALID : violations.get(0).getState();
				} catch (GeometryIndexNotFoundException e) {
					// should never happen, must return something here
					return GeometryValidationState.VALID;
				}
			} else {
				return GeometryService.validate(geom, toArray(index));
			}
		}
	}

	/**
	 * Get the index values of all levels.
	 * 
	 * @return array of indexes, one per level
	 */
	public int[] toArray(GeometryIndex index) {
		int depth = 0;
		GeometryIndex child = index;
		while (child != null) {
			depth++;
			child = child.getChild();
		}
		int[] values = new int[depth];
		child = index;
		int i = 0;
		while (child != null) {
			values[i++] = child.getValue();
			child = child.getChild();
		}
		return values;
	}

}
