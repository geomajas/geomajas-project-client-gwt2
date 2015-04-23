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
import org.geomajas.annotation.UserImplemented;

/**
 * Visitor pattern for visiting a criteria tree.
 * 
 * @author Jan De Moerloose
 * 
 * @since 2.2.1 
 */
@Api(allMethods = true)
@UserImplemented
public interface CriterionDtoVisitor {

	/**
	 * Visit the {@link LogicalCriterionDto}.
	 * 
	 * @param criterion
	 * @param context
	 */
	void visit(LogicalCriterionDto criterion, Object context);

	/**
	 * Visit the {@link AttributeCriterionDto}.
	 * 
	 * @param criterion
	 * @param context
	 */
	void visit(AttributeCriterionDto<?> criterion, Object context);

	/**
	 * Visit the {@link GeometryCriterionDto}.
	 * 
	 * @param criterion
	 * @param context
	 */
	void visit(GeometryCriterionDto criterion, Object context);

	/**
	 * Visit the {@link BboxCriterionDto}.
	 * 
	 * @param criterion
	 * @param context
	 */
	void visit(BboxCriterionDto criterion, Object context);

	/**
	 * Visit the {@link LogicalCriterionDto}.
	 * 
	 * @param criterion
	 * @param context
	 */
	void visit(DWithinCriterionDto criterion, Object context);

	/**
	 * Visit the {@link FullTextCriterionDto}.
	 * 
	 * @param criterion
	 * @param context
	 */
	void visit(FullTextCriterionDto criterion, Object context);

	/**
	 * Visit the {@link IncludeCriterionDto}.
	 * 
	 * @param criterion
	 * @param context
	 */
	void visit(IncludeCriterionDto criterion, Object context);

	/**
	 * Visit the {@link ExcludeCriterionDto}.
	 * 
	 * @param criterion
	 * @param context
	 */
	void visit(ExcludeCriterionDto criterion, Object context);

	/**
	 * Visit the {@link FidCriterionDto}.
	 * 
	 * @param criterion
	 * @param context
	 */
	void visit(FidCriterionDto fidCriterionDto, Object context);

	/**
	 * Catch-all visit-method for custom {@link CriterionDto} implementations. Use reflection to handle different
	 * implementations.
	 * 
	 * @param criterionDto
	 * @param context
	 */
	void visit(CriterionDto criterionDto, Object context);

}
