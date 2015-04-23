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

import java.math.BigDecimal;

import org.geomajas.annotation.Api;

/**
 * {@link AttributeCriterionDto} of type {@link java.math.BigDecimal}.
 * 
 * @author Jan De Moerloose
 * 
 * @since 2.2.1 
 */
@Api(allMethods = true)
public class BigDecimalAttributeCriterionDto extends AttributeCriterionDto<BigDecimal> {

	private static final long serialVersionUID = 221L;

	@Override
	public BigDecimalAttributeCriterionDto clone() {
		return new BigDecimalAttributeCriterionDto(getAttributeName(), getOperation(), getValue());
	}

	@SuppressWarnings("unused")
	private BigDecimalAttributeCriterionDto() {
		super();
	}

	/**
	 * Create a big decimal criterion for this attribute, operation and value.
	 * 
	 * @param attributeName
	 * @param operation
	 * @param value
	 */
	public BigDecimalAttributeCriterionDto(String attributeName, String operation, BigDecimal value) {
		super(attributeName, operation, value);
	}

}
