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

import java.io.Serializable;

import org.geomajas.annotation.Api;

/**
 * Attribute definition. Holds an attribute value.
 * 
 * @param <VALUE_TYPE>
 *            type for the attribute value
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public interface Attribute<VALUE_TYPE> extends Serializable {

	/**
	 * Get the value for this attribute.
	 * 
	 * @return attribute value
	 */
	VALUE_TYPE getValue();
}