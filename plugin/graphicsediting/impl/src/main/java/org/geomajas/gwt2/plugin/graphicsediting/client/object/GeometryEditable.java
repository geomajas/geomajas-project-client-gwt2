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
package org.geomajas.gwt2.plugin.graphicsediting.client.object;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;
import org.geomajas.graphics.client.object.role.RoleType;

/**
 * Implemented by graphics objects that have a {@link Geometry} representation.
 * 
 * @author Jan De Moerloose
 * @since 2.5.0
 * 
 */
@Api(allMethods = true)
public interface GeometryEditable {

	/**
	 * The name for the {@link org.geomajas.graphics.client.object.role.RoleType}.
	 */
	String TYPE_NAME = "GeometryEditable";

	/**
	 * The {@link org.geomajas.graphics.client.object.role.RoleType} for this role.
	 */
	RoleType<GeometryEditable> TYPE = new RoleType<GeometryEditable>(TYPE_NAME);

	/**
	 * Set the geometry.
	 * @param geometry
	 */
	void setGeometry(Geometry geometry);

	/**
	 * Get the geometry.
	 * @return
	 */
	Geometry getGeometry();

}
