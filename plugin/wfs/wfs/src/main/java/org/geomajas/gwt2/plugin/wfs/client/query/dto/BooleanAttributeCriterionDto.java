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
 * {@link AttributeCriterionDto} of type {@link Boolean}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class BooleanAttributeCriterionDto extends AttributeCriterionDto<Boolean> {

	private BooleanAttributeCriterionDto() {
		super();
	}
	
	@Override
	public BooleanAttributeCriterionDto clone() {
		return new BooleanAttributeCriterionDto(getAttributeName(), getOperation(), getValue());
	}

	public BooleanAttributeCriterionDto(String attributeName, String operation, Boolean value) {
		super(attributeName, operation, value);
	}

}
