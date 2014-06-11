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

import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.operation.GeometryIndexOperation;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;

import com.google.web.bindery.event.shared.EventBus;

/**
 * Chain of interceptors pattern for {@link GeometryIndexOperationInterceptor}.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface GeometryIndexOperationInterceptorChain {

	/**
	 * Proceed to the next interceptor. If this is the last interceptor in the chain, execute or roll back the
	 * operation.
	 * 
	 * @throws GeometryOperationFailedException
	 */
	void proceed() throws GeometryOperationFailedException;

	/**
	 * Schedule rollback of the operation.
	 */
	void rollback();

	/**
	 * Get the current operation.
	 * 
	 * @return
	 */
	GeometryIndexOperation getOperation();

	/**
	 * Get the current index.
	 * 
	 * @return
	 */
	GeometryIndex getIndex();

	/**
	 * Get the current geometry.
	 * 
	 * @return
	 */
	Geometry getGeometry();

	/**
	 * Get the event bus of the editor.
	 * 
	 * @return
	 */
	EventBus getEventBus();
}
