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
 * {@link AttributeCriterionDto} of type {@link Short}.
 * 
 * @author Jan De Moerloose
 * 
 * @since 2.2.1
 */
@Api(allMethods = true)
public class ShortAttributeCriterionDto extends AttributeCriterionDto<Short> {

	private static final long serialVersionUID = 221L;

	@SuppressWarnings("unused")
	private ShortAttributeCriterionDto() {
		super();
	}

	@Override
	public ShortAttributeCriterionDto clone() {
		return new ShortAttributeCriterionDto(getAttributeName(), getOperation(), getValue());
	}

	/**
	 * Create a short criterion for this attribute, operation and value.
	 * 
	 * @param attributeName
	 * @param operation
	 * @param value
	 */
	public ShortAttributeCriterionDto(String attributeName, String operation, Short value) {
		super(attributeName, operation, value);
	}

}
