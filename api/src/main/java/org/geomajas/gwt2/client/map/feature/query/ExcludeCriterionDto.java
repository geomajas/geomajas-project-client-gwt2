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
 * DTO object for exclude all criterion.
 * 
 * @author Jan De Moerloose
 *
 * @since 2.2.1
 */
@Api(allMethods = true)
public class ExcludeCriterionDto implements CriterionDto {	

	private static final long serialVersionUID = 221L;
	
	/**
	 * Create an exclude-all criterion.
	 */
	public ExcludeCriterionDto() {
	}

	@Override
	public void accept(CriterionDtoVisitor visitor, Object context) {
		visitor.visit(this, context);		
	}

}

