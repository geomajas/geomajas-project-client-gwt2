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

import java.io.Serializable;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Common interface for criterion DTO objects. Jackson-annotated for both versions.
 * 
 * @author Jan De Moerloose
 * 
 * @since 2.2.1
 */
@Api(allMethods = true)
@UserImplemented
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@org.codehaus.jackson.annotate.JsonTypeInfo(
		use = org.codehaus.jackson.annotate.JsonTypeInfo.Id.CLASS,
		include = org.codehaus.jackson.annotate.JsonTypeInfo.As.PROPERTY,
		property = "@class")
public interface CriterionDto extends Serializable {

	/**
	 * Exclude all criterion.
	 */
	CriterionDto EXCLUDE = new ExcludeCriterionDto();

	/**
	 * Include all criterion.
	 */
	CriterionDto INCLUDE = new IncludeCriterionDto();

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
	void accept(CriterionDtoVisitor visitor, Object context);

}
