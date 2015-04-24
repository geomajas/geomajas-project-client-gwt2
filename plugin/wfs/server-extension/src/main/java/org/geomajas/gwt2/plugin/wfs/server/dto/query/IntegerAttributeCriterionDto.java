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


/**
 * {@link AttributeCriterionDto} of type {@link Integer}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class IntegerAttributeCriterionDto extends AttributeCriterionDto<Integer> {

	private static final long serialVersionUID = 221L;

	@SuppressWarnings("unused")
	private IntegerAttributeCriterionDto() {
		super();
	}

	@Override
	public IntegerAttributeCriterionDto clone() {
		return new IntegerAttributeCriterionDto(getAttributeName(), getOperation(), getValue());
	}

	/**
	 * Create a integer criterion for this attribute, operation and value.
	 * 
	 * @param attributeName
	 * @param operation
	 * @param value
	 */
	public IntegerAttributeCriterionDto(String attributeName, String operation, Integer value) {
		super(attributeName, operation, value);
	}

}
