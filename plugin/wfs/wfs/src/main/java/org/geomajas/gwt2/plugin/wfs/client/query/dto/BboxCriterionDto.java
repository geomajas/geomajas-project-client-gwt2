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

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;

/**
 * Bounding box criterion.
 * 
 * @author Jan De Moerloose
 * 
 */
public class BboxCriterionDto implements SpatialCriterionDto {

	private String attributeName;

	private Bbox bbox;

	private String crs;

	private BboxCriterionDto() {
	}
	
	public BboxCriterionDto(Coordinate center, double distance) {
		this(new Bbox(center.getX() - distance, center.getY() - distance, 2 * distance, 2 * distance));
	}

	public BboxCriterionDto(Bbox bbox) {
		this.bbox = bbox;
	}

	public BboxCriterionDto(String attributeName, Bbox bbox) {
		this.attributeName = attributeName;
		this.bbox = bbox;
	}

	@Override
	public void accept(CriterionDtoVisitor visitor, Object context) {
		visitor.visit(this, context);
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public Bbox getBbox() {
		return bbox;
	}

	public void setBbox(Bbox bbox) {
		this.bbox = bbox;
	}

	public String getCrs() {
		return crs;
	}

	public void setCrs(String crs) {
		this.crs = crs;
	}

}
