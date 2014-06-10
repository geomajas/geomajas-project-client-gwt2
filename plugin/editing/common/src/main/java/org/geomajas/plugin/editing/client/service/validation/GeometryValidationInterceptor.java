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

import org.geomajas.geometry.service.GeometryValidationState;
import org.geomajas.plugin.editing.client.event.GeometryEditValidationEvent;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryIndexOperationInterceptor;
import org.geomajas.plugin.editing.client.service.GeometryIndexOperationInterceptorChain;

/**
 * Interceptor that validates the resulting geometry of each operation.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GeometryValidationInterceptor implements GeometryIndexOperationInterceptor {

	private GeometryValidator validator;

	/**
	 * Constructor that takes editing service.
	 * 
	 * @param editService
	 */
	public GeometryValidationInterceptor(GeometryEditService editService) {
		setValidator(new DefaultGeometryValidator(editService, true));
	}

	/**
	 * Set the validator for this interceptor.
	 * 
	 * @param validator
	 */
	public void setValidator(GeometryValidator validator) {
		this.validator = validator;
	}

	@Override
	public void intercept(GeometryIndexOperationInterceptorChain chain) throws GeometryOperationFailedException {
		chain.proceed();
		GeometryValidationState state = validator.validate(chain.getGeometry(), chain.getIndex());
		if (validator.isRollBack()) {
			chain.rollback();
		}
		// send an event with contextual info
		chain.getEventBus().fireEvent(
				new GeometryEditValidationEvent(chain.getGeometry(), chain.getIndex(), state, validator
						.getValidationContext()));
	}
}
