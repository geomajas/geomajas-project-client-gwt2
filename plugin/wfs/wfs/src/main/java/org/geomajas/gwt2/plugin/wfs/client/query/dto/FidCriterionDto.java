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
 * Feature ID criterion.
 *
 * @author An Buyle
 *
 */
public class FidCriterionDto implements CriterionDto {

	private static final long serialVersionUID = 100L;
	private String[] fids;


	@SuppressWarnings("unused")
	private FidCriterionDto() {
	}

	public FidCriterionDto(String[] fids) {
		this.fids = fids;
	}


	@Override
	public void accept(CriterionDtoVisitor visitor, Object context) {
		visitor.visit(this, context);
	}


	public String[] getFids() {
		return fids;
	}

	public void setFids(String[] fids) {
		this.fids = fids;
	}

}
