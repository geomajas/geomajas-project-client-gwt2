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
package org.geomajas.gwt2.plugin.wfs.server.dto.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geomajas.gwt2.client.map.feature.query.Criterion;
import org.geomajas.gwt2.client.map.feature.query.CriterionVisitor;
import org.geomajas.gwt2.client.map.feature.query.LogicalCriterion;

/**
 * DTO object for logical criteria.
 * 
 * @author Jan De Moerloose
 * 
 */
public class LogicalCriterionDto implements LogicalCriterion, CriterionDto {

	private static final long serialVersionUID = 221L;

	private List<Criterion> children = new ArrayList<Criterion>();

	private Operator operator;


	@SuppressWarnings("unused")
	private LogicalCriterionDto() {
	}

	/**
	 * Create a logical criterion for this operator.
	 * 
	 * @param operator
	 */
	public LogicalCriterionDto(Operator operator) {
		this.operator = operator;
	}

	/**
	 * Create a logical criterion for this operator and child criteria.
	 * 
	 * @param operator
	 * @param criteria
	 */
	public LogicalCriterionDto(Operator operator, Criterion... criteria) {
		this.operator = operator;
		children = Arrays.asList(criteria);
	}

	@Override
	public void accept(CriterionVisitor visitor, Object context) {
		visitor.visit(this, context);
	}

	/**
	 * Get the logical operator.
	 * 
	 * @return
	 */
	@Override
	public Operator getOperator() {
		return operator;
	}

	/**
	 * Set the logical operator.
	 * 
	 * @param operator
	 */
	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	/**
	 * Get the child criteria.
	 * 
	 * @return
	 */
	@Override
	public List<Criterion> getChildren() {
		return children;
	}

	/**
	 * Set the child criteria.
	 * 
	 * @param children
	 */
	public void setChildren(List<Criterion> children) {
		this.children = children;
	}

}
