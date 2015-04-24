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

import org.geomajas.gwt2.client.map.feature.query.AttributeCriterion;
import org.geomajas.gwt2.client.map.feature.query.BboxCriterion;
import org.geomajas.gwt2.client.map.feature.query.Criterion;
import org.geomajas.gwt2.client.map.feature.query.CriterionVisitor;
import org.geomajas.gwt2.client.map.feature.query.DWithinCriterion;
import org.geomajas.gwt2.client.map.feature.query.ExcludeCriterion;
import org.geomajas.gwt2.client.map.feature.query.FidCriterion;
import org.geomajas.gwt2.client.map.feature.query.FullTextCriterion;
import org.geomajas.gwt2.client.map.feature.query.GeometryCriterion;
import org.geomajas.gwt2.client.map.feature.query.IncludeCriterion;
import org.geomajas.gwt2.client.map.feature.query.LogicalCriterion;
import org.geomajas.gwt2.client.map.feature.query.LogicalCriterion.Operator;

/**
 * Simplifies a complex {@link CriterionDto} by simplifying logical expressions, if possible.
 * 
 * @author Jan De Moerloose
 *
 */
public class CriterionSimplifier implements CriterionVisitor {

	public CriterionDto simplify(CriterionDto criterionDto) {
		CriterionContext context = new CriterionContext();
		criterionDto.accept(this, context);
		return (CriterionDto) context.getCriterion();
	}

	@Override
	public void visit(LogicalCriterion criterion, Object context) {
		CriterionContext fc = (CriterionContext) context;
		Operator operator = criterion.getOperator();
		if (criterion.getChildren().size() == 0) {
			fc.setCriterion(CriterionDto.EXCLUDE);
		} else {
			Criterion simple = null;
			for (Criterion child : criterion.getChildren()) {
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
	public void visit(AttributeCriterion<?> criterion, Object context) {
		CriterionContext fc = (CriterionContext) context;
		fc.setCriterion(criterion);
	}

	@Override
	public void visit(GeometryCriterion criterion, Object context) {
		CriterionContext fc = (CriterionContext) context;
		fc.setCriterion(criterion);
	}

	@Override
	public void visit(BboxCriterion criterion, Object context) {
		CriterionContext fc = (CriterionContext) context;
		fc.setCriterion(criterion);
	}

	@Override
	public void visit(FidCriterion criterion, Object context) {
		CriterionContext fc = (CriterionContext) context;
		fc.setCriterion(criterion);
	}

	@Override
	public void visit(DWithinCriterion criterion, Object context) {
		CriterionContext fc = (CriterionContext) context;
		fc.setCriterion(criterion);
	}

	@Override
	public void visit(FullTextCriterion criterion, Object context) {
		CriterionContext fc = (CriterionContext) context;
		fc.setCriterion(criterion);
	}

	@Override
	public void visit(IncludeCriterion criterion, Object context) {
		CriterionContext fc = (CriterionContext) context;
		fc.setCriterion(criterion);
	}

	@Override
	public void visit(ExcludeCriterion criterion, Object context) {
		CriterionContext fc = (CriterionContext) context;
		fc.setCriterion(criterion);
	}

	@Override
	public void visit(Criterion criterionDto, Object context) {
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

		private Criterion criterion;

		public void setCriterion(Criterion criterion) {
			this.criterion = criterion;
		}

		public Criterion getCriterion() {
			return criterion;
		}

	}

}
