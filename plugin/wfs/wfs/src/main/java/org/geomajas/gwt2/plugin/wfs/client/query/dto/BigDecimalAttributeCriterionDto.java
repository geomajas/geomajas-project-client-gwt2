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

import java.math.BigDecimal;

/**
 * {@link AttributeCriterionDto} of type {@link java.math.BigDecimal}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class BigDecimalAttributeCriterionDto extends AttributeCriterionDto<BigDecimal> {

	@Override
	public BigDecimalAttributeCriterionDto clone() {
		return new BigDecimalAttributeCriterionDto(getAttributeName(), getOperation(), getValue());
	}

	private BigDecimalAttributeCriterionDto() {
		super();
	}

	public BigDecimalAttributeCriterionDto(String attributeName, String operation, BigDecimal value) {
		super(attributeName, operation, value);
	}

}
