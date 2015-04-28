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
package org.geomajas.gwt2.client.map.feature.query;

import org.geomajas.annotation.Api;

/**
 * Attribute criterion.
 * 
 * @author Jan De Moerloose
 *
 * @param <T> value type
 * @since 2.2.1
 */
@Api(allMethods = true)
public interface AttributeCriterion<T> extends Criterion {

	/**
	 * Get the attribute name.
	 * 
	 * @return
	 */
	String getAttributeName();

	/**
	 * Get the operation (see implementations for allowed values).
	 * 
	 * @return
	 */
	String getOperation();

	/**
	 * Get the value.
	 * 
	 * @return
	 */
	T getValue();

	/**
	 * Deep-clone this criterion.
	 * 
	 * @return
	 */
	AttributeCriterion<T> clone();

}
