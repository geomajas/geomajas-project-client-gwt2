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

import java.util.Date;

/**
 * Default implementation of the {@link PrimitiveAttributeType}.
 *
 * @author Pieter De Graef
 */
public class PrimitiveAttributeTypeImpl implements PrimitiveAttributeType {

	private PrimitiveType type;

	public PrimitiveAttributeTypeImpl() {
		this.type = type;
	}

	public PrimitiveAttributeTypeImpl(PrimitiveType type) {
		this.type = type;
	}

	@Override
	public PrimitiveType getPrimitiveType() {
		return type;
	}

	public void setType(PrimitiveType type) {
		this.type = type;
	}

	@Override
	public String getName() {
		return type.toString();
	}

	@Override
	public Class<?> getBinding() {
		switch (type) {
			case BOOLEAN:
				return boolean.class;
			case DATE:
				return Date.class;
			case DOUBLE:
				return double.class;
			case SHORT:
				return short.class;
			case INTEGER:
				return int.class;
			case LONG:
				return long.class;
			case FLOAT:
				return float.class;
			case STRING:
			default:
				return String.class;
		}
	}
}
