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
import org.geomajas.geometry.Geometry;

/**
 * Distance within criterion.
 * 
 * @author Jan De Moerloose
 * 
 * @since 2.2.1
 */
@Api(allMethods = true)
public class DWithinCriterionDto implements SpatialCriterionDto {

	private static final long serialVersionUID = 221L;

	private String attributeName;

	private double distance;

	private String units;

	private Geometry value;

	@SuppressWarnings("unused")
	private DWithinCriterionDto() {
	}

	/**
	 * Create a criterion for this distance, unit type, and value. The default geometry attribute name is used.
	 * 
	 * @param distance
	 * @param units
	 * @param value
	 */
	public DWithinCriterionDto(double distance, String units, Geometry value) {
		this(null, distance, units, value);
	}

	/**
	 * Create a criterion for this attribute, distance, unit type and value.
	 * 
	 * @param attributeName
	 * @param distance
	 * @param units
	 * @param value
	 */
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

	/**
	 * Get the attribute name.
	 * 
	 * @return
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * Set the attribute name.
	 * 
	 * @param attributeName
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	/**
	 * Get the distance to the geometry.
	 * 
	 * @return
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * Set the distance to the geometry.
	 * 
	 * @param distance
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * Get the unit type (e.g. 'm').
	 * 
	 * @return
	 */
	public String getUnits() {
		return units;
	}

	/**
	 * Set the unit type (e.g. 'm').
	 * 
	 * @param units
	 */
	public void setUnits(String units) {
		this.units = units;
	}

	/**
	 * Get the geometry to compare with.
	 * 
	 * @return
	 */
	public Geometry getValue() {
		return value;
	}

	/**
	 * Set the geometry to compare with.
	 * 
	 * @param value
	 */
	public void setValue(Geometry value) {
		this.value = value;
	}

}
