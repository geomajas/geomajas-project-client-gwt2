package org.geomajas.gwt2.plugin.wfs.client.query.dto;

import java.util.List;

import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;

public class QueryDto {

	private CriterionDto criterion;

	private List<String> requestedAttributeNames; // if null, all attributes must be returned

	private int maxFeatures = Integer.MAX_VALUE;

	private int maxCoordsPerFeature = -1;

	private int startIndex;

	private String crs;

	private List<AttributeDescriptor> attributeDescriptors;

	public CriterionDto getCriterion() {
		return criterion;
	}

	public void setCriterion(CriterionDto criterion) {
		this.criterion = criterion;
	}

	public List<String> getRequestedAttributeNames() {
		return requestedAttributeNames;
	}

	public void setRequestedAttributeNames(List<String> requestedAttributeNames) {
		this.requestedAttributeNames = requestedAttributeNames;
	}

	public int getMaxFeatures() {
		return maxFeatures;
	}

	public void setMaxFeatures(int maxFeatures) {
		this.maxFeatures = maxFeatures;
	}

	public int getMaxCoordsPerFeature() {
		return maxCoordsPerFeature;
	}

	public void setMaxCoordsPerFeature(int maxCoordsPerFeature) {
		this.maxCoordsPerFeature = maxCoordsPerFeature;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public String getCrs() {
		return crs;
	}

	public void setCrs(String crs) {
		this.crs = crs;
	}

	public List<AttributeDescriptor> getAttributeDescriptors() {
		return attributeDescriptors;
	}

	public void setAttributeDescriptors(List<AttributeDescriptor> attributeDescriptors) {
		this.attributeDescriptors = attributeDescriptors;
	}

}
