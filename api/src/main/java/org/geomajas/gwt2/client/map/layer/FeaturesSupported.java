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

package org.geomajas.gwt2.client.map.layer;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.client.map.feature.Feature;

import java.util.Collection;
import java.util.List;

/**
 * Extension for the layer interface which signifies that this particular layer has support for features. Features are
 * the individual objects that make up a layer. Most methods in this interface will revolve around filtering (usually
 * using the feature attributes) and feature selection.
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public interface FeaturesSupported extends Layer {

	/**
	 * Get the ordered list of attribute descriptors that describe the attributes of the features that belong to this
	 * layer.
	 *
	 * @return The list of attribute descriptors.
	 */
	List<AttributeDescriptor> getAttributeDescriptors();

	/**
	 * Is a certain feature selected or not?
	 *
	 * @param featureId The unique identifier of the feature within this layer.
	 * @return Returns true if the feature has been selected, false otherwise.
	 */
	boolean isFeatureSelected(String featureId);

	/**
	 * Select the given feature.
	 *
	 * @param feature The feature to select. Must be part of this layer.
	 * @return Return true if the selection was successful.
	 */
	boolean selectFeature(Feature feature);

	/**
	 * Deselect the given feature.
	 *
	 * @param feature The feature to deselect. Must be part of this layer.
	 * @return Return true if the deselection was successful.
	 */
	boolean deselectFeature(Feature feature);

	/**
	 * Deselect all features within this layer.
	 */
	void clearSelectedFeatures();

	/**
	 * Return a collection of all selected features within this layer.
	 *
	 * @return Returns the features.
	 */
	Collection<Feature> getSelectedFeatures();
}