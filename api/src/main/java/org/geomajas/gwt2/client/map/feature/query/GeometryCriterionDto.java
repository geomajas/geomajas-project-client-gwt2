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
 * DTO object for geometry criteria.
 * 
 * @author Jan De Moerloose
 * 
 * @since 2.2.1
 */
@Api(allMethods = true)
public class GeometryCriterionDto implements SpatialCriterionDto {

	private static final long serialVersionUID = 221L;

	private String attributeName;

	private String operation; // contains, crosses, disjoint, equals, intersects, overlaps, touches, within

	private Geometry value;

	/**
	 * Contains relation (Polygons do not contain their boundary!).
	 */
	public static final String CONTAINS = "contains";

	/**
	 * Crosses relation.
	 */
	public static final String CROSSES = "crosses";

	/**
	 * Disjoint relation.
	 */
	public static final String DISJOINT = "disjoint";

	/**
	 * Equals relation.
	 */
	public static final String EQUALS = "equals";

	/**
	 * Intersects relation.
	 */
	public static final String INTERSECTS = "intersects";

	/**
	 * Overlaps relation.
	 */
	public static final String OVERLAPS = "overlaps";

	/**
	 * Touches relation.
	 */
	public static final String TOUCHES = "touches";

	/**
	 * Within relation (inverse of contains).
	 */
	public static final String WITHIN = "within";

	@SuppressWarnings("unused")
	private GeometryCriterionDto() {
	}

	/**
	 * Create a geometry criterion for this attribute, operation and geometry value.
	 * 
	 * @param attributeName
	 * @param operation
	 * @param value
	 */
	public GeometryCriterionDto(String attributeName, String operation, Geometry value) {
		this.attributeName = attributeName;
		this.operation = operation;
		this.value = value;
	}

	@Override
	public void accept(CriterionDtoVisitor visitor, Object context) {
		visitor.visit(this, context);
	}

	/**
	 * Get the attribute name.
	 * 
	 * @return
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * Set the attribute name.
	 * 
	 * @param attributeName
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	/**
	 * Get the geometry operation (see constants in this class).
	 * 
	 * @return
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * Set the geometry operation (see constants in this class).
	 * 
	 * @param operation
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * Get the geometry value to compare with.
	 * 
	 * @return
	 */
	public Geometry getValue() {
		return value;
	}

	/**
	 * Set the geometry value to compare with.
	 * 
	 * @param value
	 */
	public void setValue(Geometry value) {
		this.value = value;
	}

}
