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

package org.geomajas.gwt2.client.map.attribute;

import org.geomajas.annotation.Api;

/**
 * Definition of a type of attribute. This type is extended by more concrete forms of attribute types.
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public interface PrimitiveAttributeType extends AttributeType {

	/**
	 * Get the primitive type associated with this attribute type.
	 *
	 * @return The primitive type.
	 */
	PrimitiveType getPrimitiveType();
}
