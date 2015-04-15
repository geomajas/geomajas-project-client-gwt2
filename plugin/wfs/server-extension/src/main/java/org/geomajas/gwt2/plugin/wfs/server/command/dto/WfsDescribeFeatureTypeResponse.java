package org.geomajas.gwt2.plugin.wfs.server.command.dto;

import java.util.List;

import org.geomajas.command.CommandResponse;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;

public class WfsDescribeFeatureTypeResponse extends CommandResponse {

	private static final long serialVersionUID = 1L;

	private List<AttributeDescriptor> attributeDescriptors;

	public WfsDescribeFeatureTypeResponse() {
	}

	public List<AttributeDescriptor> getAttributeDescriptors() {
		return this.attributeDescriptors;
	}

	public void setAttributeDescriptors(List<AttributeDescriptor> attributeDescriptors) {
		this.attributeDescriptors = attributeDescriptors;
	}

}
