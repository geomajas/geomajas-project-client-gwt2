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
 * Common interface for criterion objects.
 * 
 * @author Jan De Moerloose
 * 
 * @since 2.2.1
 */
@Api(allMethods = true)
public interface Criterion {

	/**
	 * Equals.
	 */
	String EQ = "=";

	/**
	 * Less than.
	 */
	String LT = "<";

	/**
	 * Less than or equals.
	 */
	String LTEQ = "<=";

	/**
	 * Greater than.
	 */
	String GT = ">";

	/**
	 * Greater than or equals.
	 */
	String GTEQ = ">=";

	/**
	 * Like (case insensitive).
	 */
	String LIKE = "like";

	/**
	 * Empty or null.
	 */
	String EMPTY = "empty";

	/**
	 * Not equals.
	 */
	String NE = "!=";

	/**
	 * Double dispatch. Implementors should call the right dispatch method on the visitor.
	 * 
	 * @param visitor the visitor
	 * @param context the context (anything the visitor wants to pass around).
	 */
	void accept(CriterionVisitor visitor, Object context);

}
