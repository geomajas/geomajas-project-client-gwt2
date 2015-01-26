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

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that reports the editing of a geometry has resumed.
 * 
 * @author Jan De Moerloose
 * @since 2.1.0
 */
@Api(allMethods = true)
public class GeometryEditResumeEvent extends GwtEvent<GeometryEditResumeHandler> {

	private final Geometry geometry;

	/**
	 * Main constructor.
	 * 
	 * @param geometry geometry
	 */
	public GeometryEditResumeEvent(Geometry geometry) {
		this.geometry = geometry;
	}

	@Override
	public Type<GeometryEditResumeHandler> getAssociatedType() {
		return GeometryEditResumeHandler.TYPE;
	}

	@Override
	protected void dispatch(GeometryEditResumeHandler geometryEditWorkflowHandler) {
		geometryEditWorkflowHandler.onGeometryEditResume(this);
	}

	/**
	 * Get the geometry that will be edited.
	 * @return The geometry that is to be edited.
	 */
	public Geometry getGeometry() {
		return geometry;
	}
}
