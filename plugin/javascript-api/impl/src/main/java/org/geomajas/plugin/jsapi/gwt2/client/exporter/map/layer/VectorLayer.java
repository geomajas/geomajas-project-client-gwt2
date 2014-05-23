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
package org.geomajas.plugin.jsapi.gwt2.client.exporter.map.layer;

import java.util.Collection;

import org.geomajas.gwt2.client.map.layer.VectorServerLayer;
import org.geomajas.plugin.jsapi.client.map.feature.Feature;
import org.geomajas.plugin.jsapi.client.map.layer.FeaturesSupported;
import org.geomajas.plugin.jsapi.client.map.layer.LabelsSupported;
import org.geomajas.plugin.jsapi.gwt2.client.exporter.map.feature.FeatureImpl;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * <p>
 * Definition of a vector layer for the JavaScript API.
 * </p>
 * <p>
 * A VectorLayer has support for features. Features are the individual objects that make up a layer. Most methods in
 * this interface will revolve around filtering (usually using the feature attributes) and feature selection.
 * </p>
 * <p>
 * A VectorLayer also has support for labelling of features. Of course, these labels can only be visible if the layer
 * itself is visible; but one can change the labels-setting nonetheless.
 * </p>
 * 
 * @author Oliver May
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export("VectorLayer")
@ExportPackage("org.geomajas.jsapi.map.layer")
public class VectorLayer extends LayerImpl implements Exportable, FeaturesSupported, LabelsSupported {

	public VectorLayer() {
	}

	/**
	 * Initialize this {@link FeaturesSupported} layer with a {@link VectorLayer} from the GWT face.
	 * 
	 * @param layer The {@link VectorLayer} to wrap.
	 * @since 1.0.0
	 */
	public VectorLayer(org.geomajas.gwt2.client.map.layer.FeaturesSupported layer) {
		super(layer);
	}

	// ------------------------------------------------------------------------
	// FeaturesSupported implementation:
	// ------------------------------------------------------------------------

	/**
	 * Apply a filter on the layer. Such a filter specifies which features are to be shown on the map, and which aren't.
	 * This filter is actually used on the back-end and therefore follows the CQL standards.
	 * 
	 * @param filter The CQL filter, based upon the layer attribute definitions. Use null to disable filtering.
	 */
	public void setFilter(String filter) {
		if (layer instanceof VectorServerLayer) {
			((VectorServerLayer) layer).setFilter(filter);
		}
	}

	/**
	 * Returns the filter that has been set onto this layer.
	 * 
	 * @return Returns the CQL filter.
	 */
	public String getFilter() {
		if (layer instanceof VectorServerLayer) {
			return ((VectorServerLayer) layer).getFilter();
		} else {
			return null;
		}
	}

	/**
	 * Is a certain feature selected or not?
	 * 
	 * @param featureId The unique identifier of the feature within this layer.
	 * @return Returns true if the feature has been selected, false otherwise.
	 */
	public boolean isFeatureSelected(String featureId) {
		return asFeaturesSupported().isFeatureSelected(featureId);
	}

	/**
	 * Select the given feature.
	 * 
	 * @param feature The feature to select. Must be part of this layer.
	 * @return Return true if the selection was successful.
	 */
	public boolean selectFeature(Feature feature) {
		return asFeaturesSupported().selectFeature(toGwt2(feature));
	}

	/**
	 * Deselect the given feature.
	 * 
	 * @param feature The feature to deselect. Must be part of this layer.
	 * @return Return true if the deselection was successful.
	 */
	public boolean deselectFeature(Feature feature) {
		return asFeaturesSupported().deselectFeature(toGwt2(feature));
	}

	/** Deselect all features within this layer. */
	public void clearSelectedFeatures() {
		asFeaturesSupported().clearSelectedFeatures();
	}

	/**
	 * Get the full list of currently selected features within this layer.
	 * 
	 * @return The list of selected features as an array.
	 */
	public Feature[] getSelectedFeatures() {
		Collection<org.geomajas.gwt2.client.map.feature.Feature> selection = asFeaturesSupported()
				.getSelectedFeatures();
		Feature[] features = new Feature[selection.size()];
		int count = 0;
		for (org.geomajas.gwt2.client.map.feature.Feature feature : selection) {
			features[count] = new FeatureImpl(feature, this);
			count++;
		}
		return features;
	}

	// ------------------------------------------------------------------------
	// LabelsSupported implementation:
	// ------------------------------------------------------------------------

	/**
	 * Make the feature labels visible or invisible on the map.
	 * 
	 * @param labeled Should the labels be shown or not?
	 */
	public void setLabeled(boolean labeled) {
		asLabelsSupported().setLabeled(labeled);
	}

	/**
	 * Are the feature labels currently visible or not?
	 * 
	 * @return Returns true or false.
	 */
	public boolean isLabeled() {
		return asLabelsSupported().isLabeled();
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private org.geomajas.gwt2.client.map.layer.FeaturesSupported asFeaturesSupported() {
		return (org.geomajas.gwt2.client.map.layer.FeaturesSupported) layer;
	}

	private org.geomajas.gwt2.client.map.layer.LabelsSupported asLabelsSupported() {
		return (org.geomajas.gwt2.client.map.layer.LabelsSupported) layer;
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	private org.geomajas.gwt2.client.map.feature.Feature toGwt2(Feature feature) {
		return FeatureImpl.toGwt(feature);
	}
}