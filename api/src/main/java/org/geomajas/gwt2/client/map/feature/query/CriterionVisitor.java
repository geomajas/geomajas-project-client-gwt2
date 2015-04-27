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

/**
 * Visitor pattern for visiting a criteria tree.
 * 
 * @author Jan De Moerloose
 * 
 * @since 2.2.1
 */
@Api(allMethods = true)
public interface CriterionVisitor {

	/**
	 * Visit the {@link LogicalCriterion}.
	 * 
	 * @param criterion
	 * @param context
	 */
	void visit(LogicalCriterion criterion, Object context);

	/**
	 * Visit the {@link AttributeCriterion}.
	 * 
	 * @param criterion
	 * @param context
	 */
	void visit(AttributeCriterion<?> criterion, Object context);

	/**
	 * Visit the {@link GeometryCriterion}.
	 * 
	 * @param criterion
	 * @param context
	 */
	void visit(GeometryCriterion criterion, Object context);

	/**
	 * Visit the {@link BboxCriterion}.
	 * 
	 * @param criterion
	 * @param context
	 */
	void visit(BboxCriterion criterion, Object context);

	/**
	 * Visit the {@link LogicalCriterion}.
	 * 
	 * @param criterion
	 * @param context
	 */
	void visit(DWithinCriterion criterion, Object context);

	/**
	 * Visit the {@link FullTextCriterion}.
	 * 
	 * @param criterion
	 * @param context
	 */
	void visit(FullTextCriterion criterion, Object context);

	/**
	 * Visit the {@link IncludeCriterion}.
	 * 
	 * @param criterion
	 * @param context
	 */
	void visit(IncludeCriterion criterion, Object context);

	/**
	 * Visit the {@link ExcludeCriterion}.
	 * 
	 * @param criterion
	 * @param context
	 */
	void visit(ExcludeCriterion criterion, Object context);

	/**
	 * Visit the {@link FidCriterion}.
	 * 
	 * @param criterion
	 * @param context
	 */
	void visit(FidCriterion fidCriterion, Object context);

	/**
	 * Catch-all visit-method for custom {@link Criterion} implementations. Use reflection to handle different
	 * implementations.
	 * 
	 * @param criterionDto
	 * @param context
	 */
	void visit(Criterion criterion, Object context);

}
