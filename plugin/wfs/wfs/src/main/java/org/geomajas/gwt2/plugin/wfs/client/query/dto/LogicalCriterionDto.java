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


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DTO object for logical criteria.
 * 
 * @author Jan De Moerloose
 * 
 */
public class LogicalCriterionDto implements CriterionDto {

	private static final long serialVersionUID = 1L;

	private List<CriterionDto> children = new ArrayList<CriterionDto>();

	private Operator operator;

	/**
	 * Logical operator.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public enum Operator {
		AND, OR
	}

	private LogicalCriterionDto() {
	}

	public LogicalCriterionDto(Operator operator) {
		this.operator = operator;
	}

	public LogicalCriterionDto(Operator operator, CriterionDto... criteria) {
		this.operator = operator;
		children = Arrays.asList(criteria);
	}

	@Override
	public void accept(CriterionDtoVisitor visitor, Object context) {
		visitor.visit(this, context);
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public List<CriterionDto> getChildren() {
		return children;
	}

	public void setChildren(List<CriterionDto> children) {
		this.children = children;
	}

}
