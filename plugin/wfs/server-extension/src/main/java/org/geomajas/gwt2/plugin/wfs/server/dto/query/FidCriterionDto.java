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

import org.geomajas.gwt2.client.map.feature.query.CriterionVisitor;
import org.geomajas.gwt2.client.map.feature.query.FidCriterion;

/**
 * Feature ID criterion.
 *
 * @author An Buyle
 *
 */
public class FidCriterionDto implements FidCriterion, CriterionDto {

	private static final long serialVersionUID = 221L;

	private String[] fids;

	@SuppressWarnings("unused")
	private FidCriterionDto() {
	}

	/**
	 * Create a criterion for the specified feature id list.
	 * 
	 * @param fids
	 */
	public FidCriterionDto(String[] fids) {
		this.fids = fids;
	}

	@Override
	public void accept(CriterionVisitor visitor, Object context) {
		visitor.visit(this, context);
	}

	/**
	 * Get the feature ids.
	 * 
	 * @return
	 */
	@Override
	public String[] getFids() {
		return fids;
	}

	/**
	 * Set the feature ids.
	 * 
	 * @param fids
	 */
	public void setFids(String[] fids) {
		this.fids = fids;
	}

}
