package org.geomajas.gwt2.plugin.wfs.server.dto;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureTypeDescriptionInfo;

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
