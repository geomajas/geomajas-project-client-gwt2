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
 * DTO object for full text criterium. This criterion should be interpreted by the server. Typically used for search
 * engine alike behavior, where one does not specify an attribute.
 * 
 * @author Jan De Moerloose
 *
 * @since 2.2.1
 */
@Api(allMethods = true)
public class FullTextCriterionDto implements CriterionDto {

	private static final long serialVersionUID = 221L;

	private String key;

	@SuppressWarnings("unused")
	private FullTextCriterionDto() {
	}

	/**
	 * Create a full text criterion on a specific key.
	 * 
	 * @param key
	 */
	public FullTextCriterionDto(String key) {
		this.key = key;
	}

	@Override
	public void accept(CriterionDtoVisitor visitor, Object context) {
		visitor.visit(this, context);
	}

	/**
	 * Get the search key.
	 * 
	 * @return
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Set the search key to use.
	 * 
	 * @param key
	 */
	public void setKey(String key) {
		this.key = key;
	}

}
