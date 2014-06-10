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

package org.geomajas.plugin.editing.client.service.validation;

import java.util.List;

import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.geometry.service.GeometryValidationState;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.geomajas.plugin.editing.client.service.GeometryIndexService;

/**
 * {@link GeometryValidator} that uses the default simple features validation rules.
 * 
 * @author Jan De Moerloose
 * 
 */
public class DefaultGeometryValidator implements GeometryValidator {

	private GeometryEditService editService;

	private boolean rollBack;

	/**
	 * Creates a validator with optional roll back.
	 * 
	 * @param editService the edit service
	 * @param rollBack if true, the validator will roll back invalid geometries
	 */
	public DefaultGeometryValidator(GeometryEditService editService, boolean rollBack) {
		this.editService = editService;
		this.rollBack = rollBack;
	}

	@Override
	public GeometryValidationState validate(Geometry geometry, GeometryIndex index) {
		GeometryIndexService indexService = editService.getIndexService();
		if (editService.getEditingState() == GeometryEditState.INSERTING) {
			// for polygons, validate the previous edge (not the closing line edge !)
			if (indexService.isVertex(index)) {
				if (indexService.getSiblingCount(geometry, index) > 2) {
					try {
						List<GeometryIndex> edges = indexService.getAdjacentEdges(geometry, index);
						return indexService.validate(geometry, edges.get(0));
					} catch (GeometryIndexNotFoundException e) {
						// should never happen, must return something here
						return GeometryValidationState.VALID;
					}
				} else {
					// inserting 1st or 2nd point of new ring or geometry, evaluate the geometry
					return indexService.validate(geometry, indexService.getParent(index));
				}
			} else {
				// inserting ring or new geometry, evaluate normally
				return indexService.validate(geometry, index);
			}
		} else {
			if (indexService.isVertex(index)) {
				try {
					List<GeometryIndex> edges = indexService.getAdjacentEdges(geometry, index);
					for (GeometryIndex edge : edges) {
						GeometryValidationState state = indexService.validate(geometry, edge);
						if (!state.isValid()) {
							return state;
						}
					}
					return GeometryValidationState.VALID;
				} catch (GeometryIndexNotFoundException e) {
					// should never happen, must return something here
					return GeometryValidationState.VALID;
				}
			} else {
				return indexService.validate(geometry, index);
			}
		}
	}

	@Override
	public Object getValidationContext() {
		return GeometryService.getValidationContext();
	}

	@Override
	public boolean isRollBack() {
		return rollBack && !GeometryService.getValidationContext().isValid();
	}

}
