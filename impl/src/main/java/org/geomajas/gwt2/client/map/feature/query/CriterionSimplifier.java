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

import org.geomajas.gwt2.client.map.feature.query.LogicalCriterionDto.Operator;

/**
 * Simplifies a complex {@link CriterionDto} by simplifying logical expressions, if possible.
 * 
 * @author Jan De Moerloose
 *
 */
public class CriterionSimplifier implements CriterionDtoVisitor {

	public CriterionDto simplify(CriterionDto criterionDto) {
		CriterionContext context = new CriterionContext();
		criterionDto.accept(this, context);
		return context.getCriterion();
	}

	@Override
	public void visit(LogicalCriterionDto criterion, Object context) {
		CriterionContext fc = (CriterionContext) context;
		Operator operator = criterion.getOperator();
		if (criterion.getChildren().size() == 0) {
			fc.setCriterion(CriterionDto.EXCLUDE);
		} else {
			CriterionDto simple = null;
			for (CriterionDto child : criterion.getChildren()) {
				child.accept(this, fc);
				if (fc.getCriterion().equals(CriterionDto.EXCLUDE) && operator == Operator.AND) {
					simple = CriterionDto.EXCLUDE;
					break;
				} else if (fc.getCriterion().equals(CriterionDto.INCLUDE) && operator == Operator.OR) {
					simple = CriterionDto.INCLUDE;
					break;
				} else if (simple == null) {
					simple = fc.getCriterion();
				} else if (simple.equals(CriterionDto.INCLUDE) && operator == Operator.AND) {
					simple = fc.getCriterion();
				} else if (simple.equals(CriterionDto.EXCLUDE) && operator == Operator.OR) {
					simple = fc.getCriterion();
				} else {
					LogicalCriterionDto tmp = new LogicalCriterionDto(criterion.getOperator());
					tmp.getChildren().add(simple);
					tmp.getChildren().add(fc.getCriterion());
					simple = tmp;
				}
			}
			fc.setCriterion(simple);
		}
	}

	@Override
	public void visit(AttributeCriterionDto<?> criterion, Object context) {
		CriterionContext fc = (CriterionContext) context;
		fc.setCriterion(criterion);
	}

	@Override
	public void visit(GeometryCriterionDto criterion, Object context) {
		CriterionContext fc = (CriterionContext) context;
		fc.setCriterion(criterion);
	}

	@Override
	public void visit(BboxCriterionDto criterion, Object context) {
		CriterionContext fc = (CriterionContext) context;
		fc.setCriterion(criterion);
	}

	@Override
	public void visit(FidCriterionDto criterion, Object context) {
		CriterionContext fc = (CriterionContext) context;
		fc.setCriterion(criterion);
	}

	@Override
	public void visit(DWithinCriterionDto criterion, Object context) {
		CriterionContext fc = (CriterionContext) context;
		fc.setCriterion(criterion);
	}

	@Override
	public void visit(FullTextCriterionDto criterion, Object context) {
		CriterionContext fc = (CriterionContext) context;
		fc.setCriterion(criterion);
	}

	@Override
	public void visit(IncludeCriterionDto criterion, Object context) {
		CriterionContext fc = (CriterionContext) context;
		fc.setCriterion(criterion);
	}

	@Override
	public void visit(ExcludeCriterionDto criterion, Object context) {
		CriterionContext fc = (CriterionContext) context;
		fc.setCriterion(criterion);
	}

	@Override
	public void visit(CriterionDto criterionDto, Object context) {
		CriterionContext fc = (CriterionContext) context;
		fc.setCriterion(criterionDto);
	}

	/**
	 * Context class to pass along the visitor.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	protected class CriterionContext {

		private CriterionDto criterion;

		public void setCriterion(CriterionDto criterion) {
			this.criterion = criterion;
		}

		public CriterionDto getCriterion() {
			return criterion;
		}

	}


}
