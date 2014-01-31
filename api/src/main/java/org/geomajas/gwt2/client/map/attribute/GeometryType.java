/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.client.map.attribute;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;

/**
 * List of possible geometry types used to represent a {@link GeometryAttributeType}.
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public enum GeometryType {

	/**
	 * Point type geometry.
	 */
	POINT(Geometry.POINT),

	/**
	 * LineString type geometry.
	 */
	LINESTRING(Geometry.LINE_STRING),

	/**
	 * LinearRing type geometry.
	 */
	LINEARRING(Geometry.LINEAR_RING),

	/**
	 * Polygon type geometry.
	 */
	POLYGON(Geometry.POLYGON),

	/**
	 * MultiPoint type geometry.
	 */
	MULTIPOINT(Geometry.MULTI_POINT),

	/**
	 * MultiLineString type geometry.
	 */
	MULTILINESTRING(Geometry.MULTI_LINE_STRING),

	/**
	 * MultiPolygon type geometry.
	 */
	MULTIPOLYGON(Geometry.MULTI_POLYGON);

	private final String value;

	/**
	 * Create primitive type.
	 *
	 * @param v value
	 */
	GeometryType(String v) {
		value = v;
	}

	/**
	 * Get enum value from.
	 *
	 * @param value string representation for enum
	 * @return enum value
	 */
	public static GeometryType fromValue(String value) {
		for (GeometryType c : GeometryType.values()) {
			if (c.value.equals(value)) {
				return c;
			}
		}
		throw new IllegalArgumentException(value);
	}

	/**
	 * Get string representation of enum.
	 *
	 * @return string representation
	 */
	@Override
	public String toString() {
		return value;
	}
}
