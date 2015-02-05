/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.editing.client.service.validation;

import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.GeometryValidationState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;

/**
 * Validators validate the edited geometry on each operation and decide whether or not to roll back the operation.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface GeometryValidator {

	/**
	 * Validate the resulting geometry of the current operation.
	 * 
	 * @param geometry the geometry
	 * @param index the index of the geometry subpart that is affected by the operation
	 * @return the validation state
	 */
	GeometryValidationState validate(Geometry geometry, GeometryIndex index);

	/**
	 * Get the context object for this validator. Custom validators may return any object of choice, which will be
	 * passed down to the validation event.
	 * 
	 * @return the validation context object
	 */
	Object getValidationContext();

	/**
	 * Should the operation be rolled back ? This method will be called after each call to
	 * {@link #validate(Geometry, GeometryIndex)} and should return true if the operation is to be rolled back.
	 * 
	 * @return true if rolled back
	 */
	boolean isRollBack();

}
