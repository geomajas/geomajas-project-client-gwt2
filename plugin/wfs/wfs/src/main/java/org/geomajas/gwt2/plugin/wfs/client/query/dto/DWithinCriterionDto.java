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

import org.geomajas.geometry.Geometry;

/**
 * Distance within criterion.
 * 
 * @author Jan De Moerloose
 * 
 */
public class DWithinCriterionDto implements SpatialCriterionDto {

	private String attributeName;

	private double distance;

	private String units;

	private Geometry value;

	private DWithinCriterionDto() {
	}
	
	public DWithinCriterionDto(double distance, String units, Geometry value) {
		this(null, distance, units, value);
	}

	public DWithinCriterionDto(String attributeName, double distance, String units, Geometry value) {
		this.attributeName = attributeName;
		this.distance = distance;
		this.units = units;
		this.value = value;
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

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public Geometry getValue() {
		return value;
	}

	public void setValue(Geometry value) {
		this.value = value;
	}

}
