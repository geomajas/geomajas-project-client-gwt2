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

package org.geomajas.gwt2.plugin.wfs.server.command.dto;

import java.util.List;

import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.client.map.feature.query.Criterion;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.CriterionDto;

/**
 * Request for the {@link org.geomajas.gwt2.plugin.wfs.server.command.WfsGetFeatureCommand}. Should contain all the info
 * needed to create an actual WFS request.
 *
 * @author Jan De Moerloose
 */
public class WfsGetFeatureRequest extends AbstractWfsRequest {

	private static final long serialVersionUID = 200L;

	public static final String COMMAND_NAME = "command.WfsGetFeature";

	private String typeName;

	private CriterionDto criterion;

	private List<String> requestedAttributeNames; // if null, all attributes must be returned

	private int maxFeatures = Integer.MAX_VALUE;

	private int maxCoordsPerFeature = -1;

	private int startIndex;

	private String crs;

	private List<AttributeDescriptor> schema;

	public WfsGetFeatureRequest() {
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Criterion getCriterion() {
		return criterion;
	}

	public void setCriterion(CriterionDto criterion) {
		this.criterion = criterion;
	}

	public int getMaxFeatures() {
		return maxFeatures;
	}

	public void setMaxFeatures(int maxFeatures) {
		this.maxFeatures = maxFeatures;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getMaxCoordsPerFeature() {
		return maxCoordsPerFeature;
	}

	public void setMaxCoordsPerFeature(int maxCoordsPerFeature) {
		this.maxCoordsPerFeature = maxCoordsPerFeature;
	}

	/**
	 * Get the list of attribute names that should be returned for each feature. If null, all attributes are returned.
	 * 
	 * @return
	 */
	public List<String> getRequestedAttributeNames() {
		return requestedAttributeNames;
	}

	/**
	 * Set the list of attribute names that should be returned for each feature. If null, all attributes are returned.
	 * 
	 * @param requestedAttributeNames
	 */
	public void setRequestedAttributeNames(List<String> requestedAttributeNames) {
		this.requestedAttributeNames = requestedAttributeNames;
	}

	public String getCrs() {
		return crs;
	}

	public void setCrs(String crs) {
		this.crs = crs;
	}

	public List<AttributeDescriptor> getSchema() {
		return schema;
	}

	public void setSchema(List<AttributeDescriptor> schema) {
		this.schema = schema;
	}
}