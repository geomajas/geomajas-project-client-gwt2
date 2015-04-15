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
package org.geomajas.gwt2.plugin.wfs.client.query.dto;

import org.geomajas.geometry.Geometry;

/**
 * DTO object for geometry criteria.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GeometryCriterionDto implements SpatialCriterionDto {

	private String attributeName;

	private String operation; // contains, crosses, disjoint, equals, intersects, overlaps, touches, within

	private Geometry value;
	
	public static final String CONTAINS = "contains";
	
	public static final String CROSSES = "crosses";
	
	public static final String DISJOINT = "disjoint";
	
	public static final String EQUALS = "equals";
	
	public static final String INTERSECTS = "intersects";
	
	public static final String OVERLAPS = "overlaps";
	
	public static final String TOUCHES = "touches";
	
	public static final String WITHIN = "within";

	private GeometryCriterionDto() {
	}

	public GeometryCriterionDto(String attributeName, String operation, Geometry value) {
		this.attributeName = attributeName;
		this.operation = operation;
		this.value = value;
	}

	@Override
	public void accept(CriterionDtoVisitor visitor, Object context) {
		visitor.visit(this, context);
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Geometry getValue() {
		return value;
	}

	public void setValue(Geometry value) {
		this.value = value;
	}

}
