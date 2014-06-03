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

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;

/**
 * Interceptor for geometry operations. Interceptors are called before and after each operation. Interceptors are
 * chained and should call {@link GeometryIndexOperationInterceptorChain#proceed()} to call the next interceptor in the
 * chain or {@link GeometryIndexOperationInterceptorChain#rollback()} to roll back the operation.
 * 
 * @author Jan De Moerloose
 * @since 2.1.0
 * 
 */
@Api(allMethods = true)
@UserImplemented
public interface GeometryIndexOperationInterceptor {

	/**
	 * Intercept the operation for this chain. Implementors must call proceed() to invoke the next interceptor in the
	 * chain or rollback() to roll back the operation.
	 * 
	 * @param chain
	 * @throws GeometryOperationFailedException
	 */
	void intercept(GeometryIndexOperationInterceptorChain chain) throws GeometryOperationFailedException;
}
