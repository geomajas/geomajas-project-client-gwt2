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

import org.geomajas.geometry.service.GeometryValidationState;

/**
 * Exception that is thrown when an operation leads to intersecting edges and
 * the configuration does not allow this.
 * 
 * @author Jan Venstermans
 */
public class GeometryOperationInvalidException extends GeometryOperationFailedException {

	private static final long serialVersionUID = 100L;
	
	private GeometryValidationState validationState;

	public GeometryOperationInvalidException(GeometryValidationState validationState) {
		this.validationState = validationState;
	}
	
	public GeometryValidationState getValidationState() {
		return validationState;
	}
	
	
}