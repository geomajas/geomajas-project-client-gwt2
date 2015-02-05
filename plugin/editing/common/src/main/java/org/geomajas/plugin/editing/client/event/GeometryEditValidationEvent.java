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

package org.geomajas.plugin.editing.client.event;

import java.util.Collections;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.GeometryValidationState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;

/**
 * Event which is passed when the geometry would become invalid during geometry editing. The operation is undone in this
 * case.
 * 
 * @author Jan De Moerloose
 * @since 2.1.0
 */
@Api(allMethods = true)
public class GeometryEditValidationEvent extends AbstractGeometryEditEvent<GeometryEditValidationHandler> {

	private GeometryValidationState state;
	
	private Object validationContext;

	/**
	 * Main constructor.
	 * 
	 * @param geometry geometry that is edited
	 * @param index index that is edited
	 * @param state state of editing
	 * @param validationContext validation context as received from 
	 *     {@link org.geomajas.plugin.editing.client.service.validation.GeometryValidator}
	 */
	public GeometryEditValidationEvent(Geometry geometry, GeometryIndex index, GeometryValidationState state,
			Object validationContext) {
		super(geometry, Collections.singletonList(index));
		this.state = state;
		this.validationContext = validationContext;
	}

	/**
	 * Get the current editing state.
	 * 
	 * @return Returns the current editing state.
	 */
	public Type<GeometryEditValidationHandler> getAssociatedType() {
		return GeometryEditValidationHandler.TYPE;
	}

	/**
	 * the handler.
	 * @param handler
	 */
	protected void dispatch(GeometryEditValidationHandler handler) {
		handler.onGeometryEditValidation(this);
	}

	/**
	 * Get the {@link GeometryIndex} that is changed in this event.
	 *
	 * @return index
	 */
	public GeometryIndex getIndex() {
		return getIndices().get(0);
	}

	/**
	 * Get the {@link GeometryValidationState} of current event.
	 *
	 * @return state
	 */
	public GeometryValidationState getValidationState() {
		return state;
	}

	/**
	 * Get the validation context.
	 * 
	 * @return validation context
	 */
	public Object getValidationContext() {
		return validationContext;
	}	
	
	
}