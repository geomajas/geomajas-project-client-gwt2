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

package org.geomajas.plugin.editing.client.event.state;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.event.AbstractGeometryEditEvent;
import org.geomajas.plugin.editing.client.service.GeometryIndex;

/**
 * Event which is passed when some part of a geometry has been marked for deletion during geometry editing. This event
 * does not signal the actual deletion though.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public class GeometryIndexMarkForDeletionBeginEvent
	extends AbstractGeometryEditEvent<GeometryIndexMarkForDeletionBeginHandler> {

	/**
	 * Create a new event.
	 * 
	 * @param geometry
	 *            The geometry being edited.
	 * @param indices
	 *            The indices that are marked for deletion.
	 */
	public GeometryIndexMarkForDeletionBeginEvent(Geometry geometry, List<GeometryIndex> indices) {
		super(geometry, indices);
	}

	@Override
	public Type<GeometryIndexMarkForDeletionBeginHandler> getAssociatedType() {
		return GeometryIndexMarkForDeletionBeginHandler.TYPE;
	}

	protected void dispatch(GeometryIndexMarkForDeletionBeginHandler geometryEditMarkForDeletionHandler) {
		geometryEditMarkForDeletionHandler.onGeometryIndexMarkForDeletionBegin(this);
	}
}