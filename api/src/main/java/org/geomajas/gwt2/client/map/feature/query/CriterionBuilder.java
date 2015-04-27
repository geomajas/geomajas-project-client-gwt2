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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Geometry;

/**
 * Builder for criteria.
 * 
 * @author Jan De Moerloose
 *
 * @since 2.2.1
 */
@Api(allMethods = true)
public interface CriterionBuilder {

	/**
	 * Set the attribute name.
	 * 
	 * @param attributeName
	 * @return
	 */
	CriterionBuilder attribute(String attributeName);

	/**
	 * Set the operation.
	 * 
	 * @param operation
	 * @see Criterion for allowed operations
	 * @return
	 */
	CriterionBuilder operation(String operation);

	/**
	 * Set the value.
	 * 
	 * @param operation
	 * @return
	 */
	CriterionBuilder value(Double value);

	/**
	 * Set the value.
	 * 
	 * @param operation
	 * @return
	 */
	CriterionBuilder value(Integer value);

	/**
	 * Set the value.
	 * 
	 * @param operation
	 * @return
	 */
	CriterionBuilder value(Long value);

	/**
	 * Set the value.
	 * 
	 * @param operation
	 * @return
	 */
	CriterionBuilder value(Float value);

	/**
	 * Set the value.
	 * 
	 * @param operation
	 * @return
	 */
	CriterionBuilder value(Short value);

	/**
	 * Set the value.
	 * 
	 * @param operation
	 * @return
	 */
	CriterionBuilder value(BigDecimal value);

	/**
	 * Set the value.
	 * 
	 * @param operation
	 * @return
	 */
	CriterionBuilder value(String value);

	/**
	 * Set the value.
	 * 
	 * @param operation
	 * @return
	 */
	CriterionBuilder value(Boolean value);

	/**
	 * Set the value.
	 * 
	 * @param operation
	 * @return
	 */
	CriterionBuilder value(Date value);

	/**
	 * Set the value.
	 * 
	 * @param operation
	 * @return
	 */
	CriterionBuilder value(Geometry value);

	/**
	 * SSet Dwithin parameters.
	 * 
	 * @param operation
	 * @return
	 */
	CriterionBuilder within(double distance, String units);

	/**
	 * Set feature ids.
	 * 
	 * @param fid
	 * @return
	 */
	CriterionBuilder fid(String... fid);

	/**
	 * Set full text key.
	 * 
	 * @param key
	 * @return
	 */
	CriterionBuilder fullText(String key);

	/**
	 * Set bounding box.
	 * 
	 * @param xmin
	 * @param ymin
	 * @param xmax
	 * @param ymax
	 * @return
	 */
	CriterionBuilder bbox(double xmin, double ymin, double xmax, double ymax);

	/**
	 * Set bounding box.
	 * 
	 * @param bbox
	 * @return
	 */
	CriterionBuilder bbox(Bbox bbox);

	/**
	 * Set CRS.
	 * 
	 * @param crs
	 * @return
	 */
	CriterionBuilder crs(String crs);

	/**
	 * Builds include.
	 * 
	 * @return
	 */
	CriterionBuilder include();

	/**
	 * Builds exclude.
	 * 
	 * @return
	 */
	CriterionBuilder exclude();

	/**
	 * Builds logical OR.
	 * 
	 * @param criteria
	 * @return
	 */
	CriterionBuilder or(List<Criterion> criteria);

	/**
	 * Builds logical AND.
	 * 
	 * @param criteria
	 * @return
	 */
	CriterionBuilder and(List<Criterion> criteria);

	/**
	 * Build it !
	 * @return
	 */
	Criterion build();
}
