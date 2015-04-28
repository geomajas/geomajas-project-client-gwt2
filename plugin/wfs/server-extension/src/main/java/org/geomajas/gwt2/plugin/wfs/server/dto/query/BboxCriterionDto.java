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

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.feature.query.BboxCriterion;
import org.geomajas.gwt2.client.map.feature.query.CriterionVisitor;

/**
 * Bounding box criterion.
 * 
 * @author Jan De Moerloose
 * 
 */
public class BboxCriterionDto implements BboxCriterion, CriterionDto {

	private static final long serialVersionUID = 221L;

	private String attributeName;

	private Bbox bbox;

	private String crs;

	@SuppressWarnings("unused")
	private BboxCriterionDto() {
	}

	/**
	 * Create a bbox criterion for a square region with this center and half-width and the default geometry attribute.
	 * 
	 * @param center
	 * @param distance
	 */
	public BboxCriterionDto(Coordinate center, double distance) {
		this(new Bbox(center.getX() - distance, center.getY() - distance, 2 * distance, 2 * distance));
	}

	/**
	 * Create bbox criterion for this bbox and the default geometry attribute.
	 * 
	 * @param bbox
	 */
	public BboxCriterionDto(Bbox bbox) {
		this.bbox = bbox;
	}

	/**
	 * Create bbox criterion for this bbox and geometry attribute name.
	 * 
	 * @param bbox
	 */
	public BboxCriterionDto(String attributeName, Bbox bbox) {
		this.attributeName = attributeName;
		this.bbox = bbox;
	}

	@Override
	public void accept(CriterionVisitor visitor, Object context) {
		visitor.visit(this, context);
	}

	/**
	 * Get the attribute name.
	 * 
	 * @return
	 */
	@Override
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * Set the geometry attribute name.
	 * 
	 * @param attributeName
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	/**
	 * Get the bounding box.
	 * 
	 * @return
	 */
	@Override
	public Bbox getBbox() {
		return bbox;
	}

	/**
	 * Set the bounding box.
	 * 
	 * @param bbox
	 */
	public void setBbox(Bbox bbox) {
		this.bbox = bbox;
	}

	/**
	 * Get the crs of the bounding box.
	 * 
	 * @return
	 */
	@Override
	public String getCrs() {
		return crs;
	}

	/**
	 * Set the crs of the bounding box.
	 * 
	 * @param crs
	 */
	public void setCrs(String crs) {
		this.crs = crs;
	}

}
