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
package org.geomajas.gwt2.plugin.wfs.client.query;

import java.util.Arrays;
import java.util.List;

import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.client.map.feature.query.Criterion;
import org.geomajas.gwt2.client.map.feature.query.Query;
import org.geomajas.gwt2.client.map.feature.query.QueryBuilder;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.QueryDto;

/**
 * Builds {@link Query} from DTO objects.
 * 
 * @author Jan De Moerloose
 *
 */
public class QueryDtoBuilder implements QueryBuilder {

	private Criterion criterion;

	private String[] requestedAttributeNames;

	private int maxFeatures = Integer.MAX_VALUE;

	private int maxCoordsPerFeature = -1;

	private String crs;

	private List<AttributeDescriptor> attributeDescriptors;

	private int startIndex;

	@Override
	public QueryBuilder criterion(Criterion criterion) {
		this.criterion = criterion;
		return this;
	}

	@Override
	public QueryBuilder requestedAttributeNames(String... requestedAttributeNames) {
		this.requestedAttributeNames = requestedAttributeNames;
		return this;
	}

	@Override
	public QueryBuilder maxFeatures(int maxFeatures) {
		this.maxFeatures = maxFeatures;
		return this;
	}

	@Override
	public QueryBuilder maxCoordinates(int maxCoordsPerFeature) {
		this.maxCoordsPerFeature = maxCoordsPerFeature;
		return this;
	}

	@Override
	public QueryBuilder startIndex(int startIndex) {
		this.startIndex = startIndex;
		return this;
	}

	@Override
	public QueryBuilder crs(String crs) {
		this.crs = crs;
		return this;
	}

	@Override
	public QueryBuilder attributeDescriptors(List<AttributeDescriptor> attributeDescriptors) {
		this.attributeDescriptors = attributeDescriptors;
		return this;
	}

	@Override
	public Query build() {
		QueryDto queryDto = new QueryDto();
		queryDto.setCriterion(criterion);
		queryDto.setCrs(crs);
		queryDto.setStartIndex(startIndex);
		if (requestedAttributeNames != null) {
			queryDto.setRequestedAttributeNames(Arrays.asList(requestedAttributeNames));
		}
		queryDto.setAttributeDescriptors(attributeDescriptors);
		queryDto.setMaxCoordsPerFeature(maxCoordsPerFeature);
		queryDto.setMaxFeatures(maxFeatures);
		return queryDto;
	}

}
