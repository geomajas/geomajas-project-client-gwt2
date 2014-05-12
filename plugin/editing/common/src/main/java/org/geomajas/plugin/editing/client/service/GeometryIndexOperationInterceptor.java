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

import org.geomajas.plugin.editing.client.operation.GeometryIndexOperation;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;

/**
 * Interceptor for geometry operations. Interceptors are called before and after each operation and may block the
 * operation by throwing an exception.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface GeometryIndexOperationInterceptor {

	/**
	 * Perform an action before the operation is executed. If the action fails, the complete operation will fail.
	 * 
	 * @param operation
	 * @param index
	 * @throws GeometryOperationFailedException
	 */
	void beforeExecute(GeometryIndexOperation operation, GeometryIndex index) throws GeometryOperationFailedException;

	/**
	 * Perform an action after the operation is executed. If the action fails, the complete operation will be inverted
	 * and will fail.
	 * 
	 * @param operation
	 * @param index
	 * @throws GeometryOperationFailedException
	 */
	void afterExecute(GeometryIndexOperation operation, GeometryIndex index) throws GeometryOperationFailedException;
}
