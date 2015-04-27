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
public interface DWithinCriterion extends SpatialCriterion {

	/**
	 * Get the attribute name.
	 * 
	 * @return
	 */
	String getAttributeName();

	/**
	 * Get the distance to the geometry.
	 * 
	 * @return
	 */
	double getDistance();

	/**
	 * Get the unit type (e.g. 'm').
	 * 
	 * @return
	 */
	String getUnits();

	/**
	 * Get the geometry to compare with.
	 * 
	 * @return
	 */
	Geometry getValue();

}
