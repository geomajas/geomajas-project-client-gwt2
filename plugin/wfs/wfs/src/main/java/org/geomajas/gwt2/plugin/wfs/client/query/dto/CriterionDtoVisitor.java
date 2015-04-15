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

/**
 * Visitor pattern for visiting a criteria tree.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface CriterionDtoVisitor {

	void visit(LogicalCriterionDto criterion, Object context);

	void visit(AttributeCriterionDto<?> criterion, Object context);

	void visit(GeometryCriterionDto criterion, Object context);

	void visit(BboxCriterionDto criterion, Object context);

	void visit(DWithinCriterionDto criterion, Object context);

	void visit(FullTextCriterionDto criterion, Object context);	
	
	void visit(IncludeCriterionDto criterion, Object context);
	
	void visit(ExcludeCriterionDto criterion, Object context);

	void visit(FidCriterionDto fidCriterionDto, Object context);

}
