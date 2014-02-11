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

import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.gwt.client.util.Log;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
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

	public AbstractGeometryIndexOperation(GeometryEditService editService) {
		this.editService = editService;
		this.indexService = editService.getIndexService();
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
		if (!editService.isValidating() || isValid(geometry, index)) {
			return;
		} else {
			try {
				getInverseOperation().execute(geometry, index);
			} catch (GeometryOperationFailedException e) {
				// should not fail, log anyhow !!!
				Log.logWarn("Could not invert operation on " + geometry);
			}
			throw new GeometryOperationInvalidException();
		}
	}

	protected boolean isValid(Geometry geom, GeometryIndex index) {
		return GeometryService.isValid(geom, index.getAllValues());
	}

}
