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

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;

/**
 * Builder for query objects.
 * 
 * @author Jan De Moerloose
 *
 * @since 2.2.1
 */
@Api(allMethods = true)
public interface QueryBuilder {

	/**
	 * Set the criterion.
	 * 
	 * @param criterion
	 * @return
	 */
	QueryBuilder criterion(Criterion criterion);

	/**
	 * Set the requested attribute names (optional).
	 * 
	 * @param criterion
	 * @return
	 */
	QueryBuilder requestedAttributeNames(String... requestedAttributeNames);

	/**
	 * Set the maximum number of features (optional).
	 * 
	 * @param criterion
	 * @return
	 */
	QueryBuilder maxFeatures(int maxFeatures);

	/**
	 * Set the maximum number of coordinates per feature (optional).
	 * 
	 * @param criterion
	 * @return
	 */
	QueryBuilder maxCoordinates(int maxCoordinates);

	/**
	 * Set the starting index.
	 * 
	 * @param startIndex
	 * @return
	 */
	QueryBuilder startIndex(int startIndex);

	/**
	 * Set the crs.
	 * 
	 * @param crs
	 * @return
	 */
	QueryBuilder crs(String crs);

	/**
	 * Set the attribute descriptors (optional).
	 * 
	 * @param attributeDescriptors
	 * @return
	 */
	QueryBuilder attributeDescriptors(List<AttributeDescriptor> attributeDescriptors);

	/**
	 * Build it!
	 * 
	 * @return
	 */
	Query build();

}
