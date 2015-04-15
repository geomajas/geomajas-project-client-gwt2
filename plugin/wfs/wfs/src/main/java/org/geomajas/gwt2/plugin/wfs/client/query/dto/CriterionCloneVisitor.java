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

import org.geomajas.geometry.service.GeometryService;

import java.util.ArrayList;
import java.util.List;

/**
 * Visitor implementation that creates a clone.
 *
 * @author Pieter De Graef
 * @author Jan De Moerloose added attribute support
 */
public class CriterionCloneVisitor implements CriterionDtoVisitor {

	/**
	 * Create a clone of the given criterion.
	 * @param criterionDto The original criterion to clone.
	 * @return The clone.
	 */
	public CriterionDto clone(CriterionDto criterionDto) {
		CriterionContext context = new CriterionContext();
		criterionDto.accept(this, context);
		return context.getCriterion();
	}

	// ------------------------------------------------------------------------
	// CriterionDtoVisitor implementation:
	// ------------------------------------------------------------------------

	@Override
	public void visit(LogicalCriterionDto criterion, Object context) {
		LogicalCriterionDto clone = new LogicalCriterionDto(criterion.getOperator());
		List<CriterionDto> clonedChildren = new ArrayList<CriterionDto>(criterion.getChildren().size());
		for (CriterionDto criterionDto : criterion.getChildren()) {
			clonedChildren.add(clone(criterionDto));
		}
		clone.setChildren(clonedChildren);
		((CriterionContext) context).setCriterion(clone);
	}

	@Override
	public void visit(AttributeCriterionDto<?> criterion, Object context) {
		((CriterionContext) context).setCriterion(criterion.clone());
	}

	@Override
	public void visit(GeometryCriterionDto criterion, Object context) {
		GeometryCriterionDto clone = new GeometryCriterionDto(criterion.getAttributeName(), criterion.getOperation(),
				criterion.getValue());
		((CriterionContext) context).setCriterion(clone);
	}

	@Override
	public void visit(BboxCriterionDto criterion, Object context) {
		BboxCriterionDto clone = new BboxCriterionDto(criterion.getBbox());
		clone.setAttributeName(criterion.getAttributeName());
		clone.setCrs(criterion.getCrs());
		((CriterionContext) context).setCriterion(clone);
	}

	@Override
	public void visit(DWithinCriterionDto criterion, Object context) {
		DWithinCriterionDto clone = new DWithinCriterionDto(criterion.getDistance(), criterion.getUnits(),
				GeometryService.clone(criterion.getValue()));
		clone.setAttributeName(criterion.getAttributeName());
		((CriterionContext) context).setCriterion(clone);
	}

	@Override
	public void visit(FullTextCriterionDto criterion, Object context) {
		FullTextCriterionDto clone = new FullTextCriterionDto(criterion.getKey());
		((CriterionContext) context).setCriterion(clone);
	}

	@Override
	public void visit(IncludeCriterionDto criterion, Object context) {
		((CriterionContext) context).setCriterion(new IncludeCriterionDto());
	}

	@Override
	public void visit(ExcludeCriterionDto criterion, Object context) {
		((CriterionContext) context).setCriterion(new ExcludeCriterionDto());
	}

	@Override
	public void visit(FidCriterionDto criterion, Object context) {
		String[] fids = new String[criterion.getFids().length];
		for (int i = 0; i < criterion.getFids().length; i++) {
			fids[i] = criterion.getFids()[i];
		}
		FidCriterionDto clone = new FidCriterionDto(fids);
		((CriterionContext) context).setCriterion(clone);
	}

	/**
	 * Context class to pass along the visitor.
	 *
	 * @author Jan De Moerloose
	 */
	class CriterionContext {

		private CriterionDto criterion;

		public void setCriterion(CriterionDto criterion) {
			this.criterion = criterion;
		}

		public CriterionDto getCriterion() {
			return criterion;
		}

	}
}
