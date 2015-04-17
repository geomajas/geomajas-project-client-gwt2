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
package org.geomajas.gwt2.plugin.wfs.server.dto;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureTypeDescriptionInfo;

/**
 * DTO class for WFS DescribeFeatureType result.
 * 
 * @author Jan De Moerloose
 *
 */
public class WfsFeatureTypeDescriptionDto implements WfsFeatureTypeDescriptionInfo {

	private static final long serialVersionUID = 100L;

	private String baseUrl;

	private String typeName;

	private List<AttributeDescriptor> descriptors = new ArrayList<AttributeDescriptor>();

	public WfsFeatureTypeDescriptionDto() {

	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public void setAttributeDescriptors(List<AttributeDescriptor> descriptors) {
		this.descriptors = descriptors;
	}

	/**
	 * Get the base URL of the WFS service.
	 * 
	 * @return
	 */
	@Override
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Get the feature type name.
	 * 
	 * @return
	 */
	@Override
	public String getTypeName() {
		return typeName;
	}

	/**
	 * Get the attribute descriptors.
	 * 
	 * @return
	 */
	@Override
	public List<AttributeDescriptor> getAttributeDescriptors() {
		return descriptors;
	}

}
