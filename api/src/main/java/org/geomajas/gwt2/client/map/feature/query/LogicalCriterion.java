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

import java.util.List;

import org.geomajas.annotation.Api;


/**
 * Logical criterion.
 * 
 * @author Jan De Moerloose
 * 
 * @since 2.2.1
 */
@Api(allMethods = true)
public interface LogicalCriterion extends Criterion {
	
	/**
	 * Logical operator.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public enum Operator {
		AND, OR
	}

	/**
	 * Get the logical operator.
	 * 
	 * @return
	 */
	Operator getOperator();

	/**
	 * Get the child criteria.
	 * 
	 * @return
	 */
	List<Criterion> getChildren();

}
