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

package org.geomajas.gwt2.plugin.wfs.client.capabilities;

import java.io.Serializable;

import org.geomajas.geometry.Bbox;

/**
 * Feature type definition within WFS Capabilities.
 *
 * @author Jan De Moerloose
 */
public interface WfsFeatureTypeInfo extends Serializable {

	/**
	 * Get the layer name. This name is used in DescribeFeatureType requests.
	 *
	 * @return The layer name.
	 */
	String getName();

	/**
	 * Get the layer title. This is a nicely readable name for the layer.
	 *
	 * @return Get the layer title.
	 */
	String getTitle();

	/**
	 * Get a description for the layer.
	 *
	 * @return The layer description.
	 */
	String getAbstract();

	/**
	 * Get the keywords for this layer. The exact syntax is undefined in WFS 1.0.0;
	 *
	 * @return A list of keywords for this layer.
	 */
	String getKeywords();

	/**
	 * Get the default CRS of this layer.
	 *
	 * @return The list of supported coordinate systems.
	 */
	String getDefaultCrs();

	/**
	 * Get the extent for this layer in latlon coordinates.
	 *
	 * @return Get the extent for this layer in latlon coordinates.
	 */
	Bbox getWGS84BoundingBox();


}
