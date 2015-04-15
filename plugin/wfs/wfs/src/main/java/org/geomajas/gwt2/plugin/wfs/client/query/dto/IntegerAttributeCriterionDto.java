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
 * {@link AttributeCriterionDto} of type {@link Integer}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class IntegerAttributeCriterionDto extends AttributeCriterionDto<Integer> {

	private IntegerAttributeCriterionDto() {
		super();
	}
	
	@Override
	public IntegerAttributeCriterionDto clone() {
		return new IntegerAttributeCriterionDto(getAttributeName(), getOperation(), getValue());
	}

	public IntegerAttributeCriterionDto(String attributeName, String operation, Integer value) {
		super(attributeName, operation, value);
	}

}
