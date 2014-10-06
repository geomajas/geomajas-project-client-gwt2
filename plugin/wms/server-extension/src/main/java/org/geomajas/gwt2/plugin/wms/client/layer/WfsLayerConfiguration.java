// Copyright (C) 2010-2011 DOV, http://dov.vlaanderen.be/
// All rights reserved

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
