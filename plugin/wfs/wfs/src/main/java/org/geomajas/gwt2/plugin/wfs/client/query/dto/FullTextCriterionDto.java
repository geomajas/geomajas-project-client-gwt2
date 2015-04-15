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
 * DTO object for full text criterium.
 * 
 * @author Jan De Moerloose
 *
 */
public class FullTextCriterionDto implements CriterionDto {

	private static final long serialVersionUID = 1L;

	private String key;

	@SuppressWarnings("unused")
	private FullTextCriterionDto() {
	}

	/**
	 * Create a full text search on a specific key.
	 * @param key
	 */
	public FullTextCriterionDto(String key) {
		this.key = key;
	}

	@Override
	public void accept(CriterionDtoVisitor visitor, Object context) {
		visitor.visit(this, context);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
