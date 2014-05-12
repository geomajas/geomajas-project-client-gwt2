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

package org.geomajas.plugin.editing.client.service;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.geometry.service.GeometryValidationState;
import org.geomajas.geometry.service.validation.ValidationViolation;
import org.geomajas.plugin.editing.client.operation.GeometryIndexOperation;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;

/**
 * Interceptor that validates the resulting geometry of each operation.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GeometryValidationInterceptor implements GeometryIndexOperationInterceptor {

	private GeometryEditService editService;

	private boolean enabled;

	private boolean blocking;

	/**
	 * Constructor that takes editing service.
	 * 
	 * @param editService
	 */
	public GeometryValidationInterceptor(GeometryEditService editService) {
		this.editService = editService;
	}

	/**
	 * Is the interceptor enabled ?
	 * 
	 * @return
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Enable/disable the interceptor.
	 * 
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Is the interceptor blocking ?
	 * 
	 * @return
	 */
	public boolean isBlocking() {
		return blocking;
	}

	/**
	 * Make the interceptor blocking. If an interceptor is blocking, it will throw on an invalid operation.
	 * 
	 * @param blocking
	 */
	public void setBlocking(boolean blocking) {
		this.blocking = blocking;
	}

	/**
	 * Does nothing.
	 */
	@Override
	public void beforeExecute(GeometryIndexOperation operation, GeometryIndex index)
			throws GeometryOperationFailedException {
	}

	/**
	 * If enabled, the geometry is validated and if invalid and blocking, an {@link GeometryOperationFailedException} is
	 * thrown.
	 */
	@Override
	public void afterExecute(GeometryIndexOperation operation, GeometryIndex index)
			throws GeometryOperationFailedException {
		if (isEnabled()) {
			GeometryValidationState state = validate(editService.getGeometry(), index);
			if (!state.isValid() && isBlocking()) {
				throw new GeometryOperationFailedException("Invalid geometry : " + state);
			}
		}
	}

	protected GeometryValidationState validate(Geometry geom, GeometryIndex index) {
		GeometryIndexService indexService = editService.getIndexService();
		if (editService.getEditingState() == GeometryEditState.INSERTING) {
			// for polygons, validate the previous edge (not the closing line edge !)
			if (indexService.isVertex(index)) {
				if (indexService.getSiblingCount(geom, index) > 2) {
					try {
						List<GeometryIndex> edges = indexService.getAdjacentEdges(geom, index);
						return indexService.validate(geom, edges.get(0));
					} catch (GeometryIndexNotFoundException e) {
						// should never happen, must return something here
						return GeometryValidationState.VALID;
					}
				} else {
					// inserting 1st or 2nd point of new ring or geometry, evaluate the geometry
					return indexService.validate(geom, indexService.getParent(index));
				}
			} else {
				// inserting ring or new geometry, evaluate normally
				return indexService.validate(geom, index);
			}
		} else {
			if (indexService.isVertex(index)) {
				try {
					List<GeometryIndex> edges = indexService.getAdjacentEdges(geom, index);
					List<ValidationViolation> violations = new ArrayList<ValidationViolation>();
					for (GeometryIndex edge : edges) {
						indexService.validate(geom, edge);
						violations.addAll(GeometryService.getValidationContext().getViolations());
					}
					return violations.size() == 0 ? GeometryValidationState.VALID : violations.get(0).getState();
				} catch (GeometryIndexNotFoundException e) {
					// should never happen, must return something here
					return GeometryValidationState.VALID;
				}
			} else {
				return indexService.validate(geom, index);
			}
		}
	}

}
