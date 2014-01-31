/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.wms.client.service;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.AbstractReadOnlyAttributeInfo;
import org.geomajas.gwt2.client.map.feature.Feature;

/**
 * DTO containing a list of features + the attribute descriptors for all attributes that can be in use by the features.
 *
 * @author An Buyle
 * @since 1.0.0
 */
@Api(allMethods = true)
public class FeatureCollection {

	private final List<Feature> features;

	private final List<AbstractReadOnlyAttributeInfo> attributeDescriptors;

	protected FeatureCollection() {
		features = new ArrayList<Feature>();
		attributeDescriptors = new ArrayList<AbstractReadOnlyAttributeInfo>();
	}

	protected FeatureCollection(List<Feature> features, List<AbstractReadOnlyAttributeInfo> attributeDescriptors) {
		this.features = features;
		this.attributeDescriptors = attributeDescriptors;
	}

	/**
	 * Get the list of features within this collection.
	 *
	 * @return The list of features.
	 */
	public List<Feature> getFeatures() {
		return features;
	}

	/**
	 * Get an ordered list of attribute descriptors for the features within this collection.
	 *
	 * @return List of attribute descriptors.
	 */
	public List<AbstractReadOnlyAttributeInfo> getAttributeDescriptors() {
		return attributeDescriptors;
	}

	protected void setFeatures(List<Feature> features) {
		this.features.addAll(features);
	}

	protected void setAttributeDescriptors(List<AbstractReadOnlyAttributeInfo> attributeDescriptors) {
		this.attributeDescriptors.addAll(attributeDescriptors);
	}
}
