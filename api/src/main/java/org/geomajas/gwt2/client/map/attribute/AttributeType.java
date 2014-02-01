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

import org.geomajas.annotation.Api;

import java.io.Serializable;

/**
 * Definition of a type of attribute. This type is extended by more concrete forms of attribute types.
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public interface AttributeType extends Serializable {

	/**
	 * Get the name of the attribute type.
	 *
	 * @return The name.
	 */
	String getName();

	/**
	 * Get the Java classname that is used to represent attribute values of this type.
	 *
	 * @return The Java classname.
	 */
	Class<?> getBinding();
}
