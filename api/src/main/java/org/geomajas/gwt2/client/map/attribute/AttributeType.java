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

/**
 * List of possible primitive types.
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public enum AttributeType {

	/**
	 * Boolean.
	 */
	BOOLEAN("boolean"),
	/**
	 * Short.
	 */
	SHORT("short"),
	/**
	 * Integer.
	 */
	INTEGER("integer"),
	/**
	 * Long.
	 */
	LONG("long"),
	/**
	 * Float.
	 */
	FLOAT("float"),
	/**
	 * Double.
	 */
	DOUBLE("double"),
	/**
	 * String.
	 */
	STRING("string"),
	/**
	 * Date.
	 */
	DATE("date");

	private final String value;

	/**
	 * Create primitive type.
	 *
	 * @param v value
	 */
	AttributeType(String v) {
		value = v;
	}

	/**
	 * Get enum value from.
	 *
	 * @param value string representation for enum
	 * @return enum value
	 */
	public static AttributeType fromValue(String value) {
		for (AttributeType c : AttributeType.values()) {
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
