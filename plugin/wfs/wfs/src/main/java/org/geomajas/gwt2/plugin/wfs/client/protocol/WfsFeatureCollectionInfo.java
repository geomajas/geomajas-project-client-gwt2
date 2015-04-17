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
package org.geomajas.gwt2.plugin.wfs.client.protocol;

import java.io.Serializable;
import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.map.feature.Feature;

/**
 * Feature collection definition within WFS GetFeature.
 *
 * @author Jan De Moerloose
 */
public interface WfsFeatureCollectionInfo extends Serializable {

	/**
	 * Get the base url that was used to get the features.
	 * 
	 * @return
	 */
	String getBaseUrl();

	/**
	 * Get the type name.
	 * 
	 * @return
	 */
	String getTypeName();

	/**
	 * Get the features.
	 * 
	 * @return
	 */
	List<Feature> getFeatures();

	/**
	 * Get the bounding box of the features in the CRS that was used to request the features.
	 * 
	 * @return
	 */
	Bbox getBoundingBox();
}
