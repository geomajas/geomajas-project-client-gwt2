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
 * Query for feature search.
 * 
 * @author Jan De Moerloose
 *
 * @since 2.2.1
 */
@Api(allMethods = true)
public interface Query {

	/**
	 * Get the criterion/filter of the query.
	 * 
	 * @return
	 */
	Criterion getCriterion();

	/**
	 * Get the list of attribute descriptors.
	 * 
	 * @return
	 */
	List<AttributeDescriptor> getAttributeDescriptors();

	/**
	 * Get the crs for the returned feature collection.
	 * 
	 * @return
	 */
	String getCrs();

	/**
	 * Get the maximum number of coordinates per geometry for each features. If this maximum is exceeded, the geometry
	 * will be simplified.
	 * 
	 * @return
	 */
	int getMaxCoordsPerFeature();

	/**
	 * Get the requested attribute names. If null, all attributes will be returned.
	 * 
	 * @return
	 */
	List<String> getRequestedAttributeNames();

	/**
	 * Get the start index of this query. Can be used for paging.
	 * 
	 * @return
	 */
	int getStartIndex();

	/**
	 * Get the maximum number of features to return.
	 * 
	 * @return
	 */
	int getMaxFeatures();

}
