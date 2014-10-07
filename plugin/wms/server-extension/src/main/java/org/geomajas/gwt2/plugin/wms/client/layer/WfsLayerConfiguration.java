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

package org.geomajas.gwt2.plugin.wms.client.layer;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;

/**
 * General WFS configuration object.
 * 
 * @author Jan De Moerloose
 */

public class WfsLayerConfiguration {

	private final String baseUrl;

	private final String typeName;

	private final List<AttributeDescriptor> descriptors = new ArrayList<AttributeDescriptor>();

	public WfsLayerConfiguration(String baseUrl, String typeName) {
		this.baseUrl = baseUrl;
		this.typeName = typeName;
	}

	/**
	 * Get the base URL of the WFS service.
	 * 
	 * @return
	 */
	public String getBaseUrl() {
		return baseUrl;
	}


	/**
	 * Get the feature type name.
	 * 
	 * @return
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * Get the attribute descriptors.
	 * 
	 * @return
	 */
	public List<AttributeDescriptor> getDescriptors() {
		return descriptors;
	}

}
