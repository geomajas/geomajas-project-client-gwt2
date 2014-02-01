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


package org.geomajas.gwt2.client.map.attribute;

import org.geomajas.geometry.Geometry;

/**
 * Default implementation of the {@link GeometryAttributeType}.
 *
 * @author Pieter De Graef
 */
public class GeometryAttributeTypeImpl implements GeometryAttributeType {

	private GeometryType type;

	public GeometryAttributeTypeImpl() {
	}

	public GeometryAttributeTypeImpl(GeometryType type) {
		this.type = type;
	}

	@Override
	public GeometryType getGeometryType() {
		return type;
	}

	public void setType(GeometryType type) {
		this.type = type;
	}

	@Override
	public String getName() {
		return type.toString();
	}

	@Override
	public Class<?> getBinding() {
		return Geometry.class;
	}
}
