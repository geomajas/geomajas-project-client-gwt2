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

package org.geomajas.plugin.editing.client.event;

import org.geomajas.geometry.Geometry;
import org.geomajas.annotation.FutureApi;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that reports the editing of a geometry has been suspended.
 * 
 * @author Jan De Moerloose
 * @since 2.1.0
 */
@FutureApi(allMethods = true)
public class GeometryEditSuspendEvent extends GwtEvent<GeometryEditSuspendHandler> {

	private final Geometry geometry;

	public GeometryEditSuspendEvent(Geometry geometry) {
		this.geometry = geometry;
	}

	@Override
	public Type<GeometryEditSuspendHandler> getAssociatedType() {
		return GeometryEditSuspendHandler.TYPE;
	}

	@Override
	protected void dispatch(GeometryEditSuspendHandler geometryEditWorkflowHandler) {
		geometryEditWorkflowHandler.onGeometryEditSuspend(this);
	}

	/**
	 * Get the geometry that will be edited.
	 * @return The geometry that is to be edited.
	 */
	public Geometry getGeometry() {
		return geometry;
	}
}
