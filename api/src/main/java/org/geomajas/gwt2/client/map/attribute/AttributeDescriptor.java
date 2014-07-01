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
 * Descriptor for an attribute.
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public interface AttributeDescriptor extends Serializable {

	/**
	 * Get the type of attribute this descriptor describes.
	 *
	 * @return The type of attribute.
	 */
	AttributeType getType();

	/**
	 * Get the name for this attribute.
	 *
	 * @return The attribute name.
	 */
	String getName();

	/**
	 * Get the label for this attribute.
	 *
	 * @since 2.1.0
	 * @return The attribute label.
	 */
	String getLabel();
}
