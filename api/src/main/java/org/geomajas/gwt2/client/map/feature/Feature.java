/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.client.map.feature;

import java.util.Map;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt2.client.map.attribute.Attribute;
import org.geomajas.gwt2.client.map.layer.FeaturesSupported;

/**
 * Client side feature definition. It defines the features of
 * {@link org.geomajas.gwt2.client.map.layer.FeaturesSupported} layers.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @since 2.0.0
 */
@Api(allMethods = true)
public interface Feature {

	/**
	 * Return the features unique identifier.
	 * 
	 * @return Returns the ID as a string.
	 */
	String getId();

	/**
	 * Get the value of a certain attribute.
	 * 
	 * @param attributeName
	 *            The name of the attribute. These names are configured within a layer.
	 * @return Returns the value of the attribute, or null. If the attribute does not exist, this method will also
	 *         return null.
	 */
	Object getAttributeValue(String attributeName);

	/**
	 * Return the title of this feature. This is usually the value of one of the attributes (or derived from it).
	 * 
	 * @return Returns a readable label.
	 */
	String getLabel();

	/**
	 * Get the attributes map, null when it needs to be lazy loaded.
	 * 
	 * @return attributes map
	 */
	Map<String, Attribute<?>> getAttributes();

	/**
	 * Get the feature's geometry, , null when it needs to be lazy loaded.
	 * 
	 * @return geometry
	 */
	Geometry getGeometry();

	/**
	 * Has this feature been selected or not?
	 * 
	 * @return Returns true or false.
	 */
	boolean isSelected();

	/**
	 * Return the layer instance associated with this feature. Every feature can belong to only one layer.
	 * 
	 * @return Returns the layer. This value can never be null.
	 */
	FeaturesSupported getLayer();
}