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
import org.geomajas.geometry.Geometry;


/**
 * Geometry criterion.
 * 
 * @author Jan De Moerloose
 * 
 * @since 2.2.1
 */
@Api(allMethods = true)
public interface GeometryCriterion extends SpatialCriterion {

	/**
	 * Contains relation (Polygons do not contain their boundary!).
	 */
	String CONTAINS = "contains";

	/**
	 * Crosses relation.
	 */
	String CROSSES = "crosses";

	/**
	 * Disjoint relation.
	 */
	String DISJOINT = "disjoint";

	/**
	 * Equals relation.
	 */
	String EQUALS = "equals";

	/**
	 * Intersects relation.
	 */
	String INTERSECTS = "intersects";

	/**
	 * Overlaps relation.
	 */
	String OVERLAPS = "overlaps";

	/**
	 * Touches relation.
	 */
	String TOUCHES = "touches";

	/**
	 * Within relation (inverse of contains).
	 */
	String WITHIN = "within";
	

	/**
	 * Get the geometry value to compare with.
	 * 
	 * @return
	 */
	Geometry getValue();

	/**
	 * Get the geometry operation (see constants in this class).
	 * 
	 * @return
	 */
	String getOperation();

	/**
	 * Get the attribute name.
	 * 
	 * @return
	 */
	String getAttributeName();

}
