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
package org.geomajas.plugin.jsapi.gwt2.client.exporter.map.feature;

import org.geomajas.annotation.FutureApi;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.jsapi.client.map.feature.Feature;
import org.geomajas.plugin.jsapi.client.map.layer.FeaturesSupported;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Definition of a feature within a layer.
 * 
 * @author Oliver May
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export("Feature")
@ExportPackage("org.geomajas.jsapi.map")
@FutureApi(allMethods = true)
public class FeatureImpl implements Exportable, Feature {

	private org.geomajas.gwt2.client.map.feature.Feature feature;

	private FeaturesSupported layer;

	public FeatureImpl() {
	}

	public FeatureImpl(org.geomajas.gwt2.client.map.feature.Feature feature, FeaturesSupported layer) {
		this.feature = feature;
		this.layer = layer;
	}

	/**
	 * Return the features unique identifier.
	 * 
	 * @return Returns the ID as a string.
	 */
	public String getId() {
		return feature.getId();
	}

	/**
	 * Get the value of a certain attribute.
	 * 
	 * @param attributeName The name of the attribute. These names are configured within a layer.
	 * @return Returns the value of the attribute, or null. If the attribute does not exist, this method will also
	 *         return null.
	 */
	public String getAttributeValue(String attributeName) {
		return feature.getAttributeValue(attributeName).toString();
	}

	/**
	 * Return the title of this feature. This is usually the value of one of the attributes (or derived from it).
	 * 
	 * @return Returns a readable label.
	 */
	public String getLabel() {
		return feature.getLabel();
	}

	/**
	 * Get the feature's geometry, , null when it needs to be lazy loaded.
	 * 
	 * @return geometry
	 */
	public Geometry getGeometry() {
		return feature.getGeometry();
	}

	/**
	 * Has this feature been selected or not?
	 * 
	 * @return Returns true or false.
	 */
	public boolean isSelected() {
		return layer.isFeatureSelected(feature.getId());
	}

	/**
	 * Return the layer instance associated with this feature. Every feature can belong to only one layer.
	 * 
	 * @return Returns the layer. This value can never be null.
	 */
	public FeaturesSupported getLayer() {
		return layer;
	}

	/**
	 * Get the attribute keys, null when it needs to be lazy loaded.
	 * 
	 * @return attribute keys
	 */
	public String[] getAttributes() {
		return feature.getAttributes().keySet().toArray(new String[feature.getAttributes().size()]);
	}

	public static org.geomajas.gwt2.client.map.feature.Feature toGwt(Feature feature) {
		return ((FeatureImpl) feature).feature;
	}
}