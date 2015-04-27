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

import org.geomajas.gwt2.client.map.feature.query.CriterionVisitor;
import org.geomajas.gwt2.client.map.feature.query.ExcludeCriterion;

/**
 * DTO object for exclude all criterion.
 * 
 * @author Jan De Moerloose
 *
 */
public class ExcludeCriterionDto implements ExcludeCriterion, CriterionDto {	

	private static final long serialVersionUID = 221L;
	
	/**
	 * Create an exclude-all criterion.
	 */
	ExcludeCriterionDto() {
	}

	@Override
	public void accept(CriterionVisitor visitor, Object context) {
		visitor.visit(this, context);		
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ExcludeCriterion;
	}	

}

