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

import java.util.List;

import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.client.map.feature.query.Criterion;
import org.geomajas.gwt2.client.map.feature.query.Query;

/**
 * Dto object for feature search.
 * 
 * @author Jan De Moerloose
 *
 */
public class QueryDto implements Query {

	private Criterion criterion;

	private List<String> requestedAttributeNames; // if null, all attributes must be returned

	private int maxFeatures = Integer.MAX_VALUE;

	private int maxCoordsPerFeature = -1;

	private int startIndex;

	private String crs;

	private List<AttributeDescriptor> attributeDescriptors;

	/**
	 * Get the criterion/filter of the query.
	 * 
	 * @return
	 */
	@Override
	public Criterion getCriterion() {
		return criterion;
	}

	/**
	 * Set the criterion/filter of the query.
	 * 
	 * @param criterion
	 */
	public void setCriterion(Criterion criterion) {
		this.criterion = criterion;
	}

	/**
	 * Get the requested attribute names. If null, all attributes will be returned.
	 * 
	 * @return
	 */
	@Override
	public List<String> getRequestedAttributeNames() {
		return requestedAttributeNames;
	}

	/**
	 * Set the requested attribute names. If null, all attributes will be returned.
	 * 
	 * @param requestedAttributeNames
	 */
	public void setRequestedAttributeNames(List<String> requestedAttributeNames) {
		this.requestedAttributeNames = requestedAttributeNames;
	}

	/**
	 * Get the maximum number of features to return.
	 * 
	 * @return
	 */
	@Override
	public int getMaxFeatures() {
		return maxFeatures;
	}

	/**
	 * Set the maximum number of features to return.
	 * 
	 * @param maxFeatures
	 */
	public void setMaxFeatures(int maxFeatures) {
		this.maxFeatures = maxFeatures;
	}

	/**
	 * Get the maximum number of coordinates per geometry for each features. If this maximum is exceeded, the geometry
	 * will be simplified.
	 * 
	 * @return
	 */
	@Override
	public int getMaxCoordsPerFeature() {
		return maxCoordsPerFeature;
	}

	/**
	 * Set the maximum number of coordinates per geometry for each features. If this maximum is exceeded, the geometry
	 * will be simplified.
	 * 
	 * @return
	 */
	public void setMaxCoordsPerFeature(int maxCoordsPerFeature) {
		this.maxCoordsPerFeature = maxCoordsPerFeature;
	}

	/**
	 * Get the start index of this query. Can be used for paging.
	 * 
	 * @return
	 */
	@Override
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * Set the start index of this query.
	 * 
	 * @param startIndex
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * Get the crs for the returned feature collection.
	 * 
	 * @return
	 */
	@Override
	public String getCrs() {
		return crs;
	}

	/**
	 * Set the crs for the returned feature collection.
	 * 
	 * @param crs
	 */
	public void setCrs(String crs) {
		this.crs = crs;
	}

	/**
	 * Get the list of attribute descriptors.
	 * 
	 * @return
	 */
	@Override
	public List<AttributeDescriptor> getAttributeDescriptors() {
		return attributeDescriptors;
	}

	/**
	 * Set the list of attribute descriptors.
	 * 
	 * @param attributeDescriptors
	 */
	public void setAttributeDescriptors(List<AttributeDescriptor> attributeDescriptors) {
		this.attributeDescriptors = attributeDescriptors;
	}

}
