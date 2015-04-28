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
import java.util.List;

import org.geomajas.geometry.service.GeometryService;
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

/**
 * Visitor implementation that creates a clone.
 *
 * @author Pieter De Graef
 * @author Jan De Moerloose added attribute support
 */
public class CriterionCloneVisitor implements CriterionVisitor {

	/**
	 * Create a clone of the given criterion.
	 * 
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
	public void visit(LogicalCriterion criterion, Object context) {
		LogicalCriterionDto clone = new LogicalCriterionDto(criterion.getOperator());
		List<Criterion> clonedChildren = new ArrayList<Criterion>(criterion.getChildren().size());
		for (Criterion criterionDto : criterion.getChildren()) {
			clonedChildren.add(clone((CriterionDto) criterionDto));
		}
		clone.setChildren(clonedChildren);
		((CriterionContext) context).setCriterion(clone);
	}

	@Override
	public void visit(AttributeCriterion<?> criterion, Object context) {
		((CriterionContext) context).setCriterion((CriterionDto) criterion.clone());
	}

	@Override
	public void visit(GeometryCriterion criterion, Object context) {
		GeometryCriterionDto clone = new GeometryCriterionDto(criterion.getAttributeName(), criterion.getOperation(),
				criterion.getValue());
		((CriterionContext) context).setCriterion(clone);
	}

	@Override
	public void visit(BboxCriterion criterion, Object context) {
		BboxCriterionDto clone = new BboxCriterionDto(criterion.getBbox());
		clone.setAttributeName(criterion.getAttributeName());
		clone.setCrs(criterion.getCrs());
		((CriterionContext) context).setCriterion(clone);
	}

	@Override
	public void visit(DWithinCriterion criterion, Object context) {
		DWithinCriterionDto clone = new DWithinCriterionDto(criterion.getDistance(), criterion.getUnits(),
				GeometryService.clone(criterion.getValue()));
		clone.setAttributeName(criterion.getAttributeName());
		((CriterionContext) context).setCriterion(clone);
	}

	@Override
	public void visit(FullTextCriterion criterion, Object context) {
		FullTextCriterionDto clone = new FullTextCriterionDto(criterion.getKey());
		((CriterionContext) context).setCriterion(clone);
	}

	@Override
	public void visit(IncludeCriterion criterion, Object context) {
		((CriterionContext) context).setCriterion(new IncludeCriterionDto());
	}

	@Override
	public void visit(ExcludeCriterion criterion, Object context) {
		((CriterionContext) context).setCriterion(new ExcludeCriterionDto());
	}

	@Override
	public void visit(FidCriterion criterion, Object context) {
		String[] fids = new String[criterion.getFids().length];
		for (int i = 0; i < criterion.getFids().length; i++) {
			fids[i] = criterion.getFids()[i];
		}
		FidCriterionDto clone = new FidCriterionDto(fids);
		((CriterionContext) context).setCriterion(clone);
	}

	@Override
	public void visit(Criterion criterionDto, Object context) {
	}

	/**
	 * Context class to pass along the visitor.
	 *
	 * @author Jan De Moerloose
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
