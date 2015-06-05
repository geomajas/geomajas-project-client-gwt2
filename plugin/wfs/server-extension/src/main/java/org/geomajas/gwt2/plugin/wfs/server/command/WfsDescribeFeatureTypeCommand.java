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
package org.geomajas.gwt2.plugin.wfs.server.command;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.command.CommandHasRequest;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptorImpl;
import org.geomajas.gwt2.client.map.attribute.GeometryAttributeTypeImpl;
import org.geomajas.gwt2.client.map.attribute.GeometryType;
import org.geomajas.gwt2.client.map.attribute.PrimitiveAttributeTypeImpl;
import org.geomajas.gwt2.client.map.attribute.PrimitiveType;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsDescribeFeatureTypeRequest;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsDescribeFeatureTypeResponse;
import org.geomajas.gwt2.plugin.wfs.server.command.factory.WfsHttpClientFactory;
import org.geomajas.gwt2.plugin.wfs.server.command.factory.URLBuilder;
import org.geomajas.gwt2.plugin.wfs.server.command.factory.WfsDataStoreFactory;
import org.geomajas.gwt2.plugin.wfs.server.command.factory.impl.DefaultWfsHttpClientFactory;
import org.geomajas.gwt2.plugin.wfs.server.command.factory.impl.DefaultWfsDataStoreFactory;
import org.geomajas.gwt2.plugin.wfs.server.dto.WfsFeatureTypeDescriptionDto;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Command that issues WFS DescribeFeatureType request.
 * 
 * @author Jan De Moerloose
 *
 */
@Component(WfsDescribeFeatureTypeRequest.COMMAND_NAME)
public class WfsDescribeFeatureTypeCommand implements
		CommandHasRequest<WfsDescribeFeatureTypeRequest, WfsDescribeFeatureTypeResponse> {

	private final Logger log = LoggerFactory.getLogger(WfsDescribeFeatureTypeCommand.class);

	private WfsDataStoreFactory dataStoreFactory;

	private WfsHttpClientFactory httpClientFactory;

	public WfsDescribeFeatureTypeCommand() {
		dataStoreFactory = new DefaultWfsDataStoreFactory();
		httpClientFactory = new DefaultWfsHttpClientFactory();
	}

	public void setDataStoreFactory(WfsDataStoreFactory dataStoreFactory) {
		this.dataStoreFactory = dataStoreFactory;
	}

	public void setHttpClientFactory(WfsHttpClientFactory httpClientFactory) {
		this.httpClientFactory = httpClientFactory;
	}

	@Override
	public void execute(WfsDescribeFeatureTypeRequest request, WfsDescribeFeatureTypeResponse response)
			throws GeomajasException {
		SimpleFeatureType schema = null;
		try {
			String sourceUrl = request.getBaseUrl();
			URL targetUrl = httpClientFactory.getTargetUrl(sourceUrl);
			
			// Create a WFS GetCapabilities URL:
			URL url = URLBuilder.createWfsURL(targetUrl, request.getVersion(), "GetCapabilities");
			String capa = url.toExternalForm();

			Map<String, Serializable> connectionParameters = new HashMap<String, Serializable>();
			connectionParameters.put(WFSDataStoreFactory.URL.key, capa);
			connectionParameters.put(WFSDataStoreFactory.TIMEOUT.key, 10000);
			if (request.getStrategy() != null) {
				connectionParameters.put(WFSDataStoreFactory.WFS_STRATEGY.key, request.getStrategy());
			}
			// Get the WFS feature source:
			WFSDataStore data = dataStoreFactory.createDataStore(connectionParameters,
					httpClientFactory.create(sourceUrl));
			schema = data.getSchema(request.getTypeName().replace(":", "_"));
		} catch (Exception e) {
			log.error("DescribeFeatureType failed for " + request.getTypeName(), e);
			throw new GeomajasException(ExceptionCode.UNEXPECTED_PROBLEM, e.getMessage());
		}

		if (schema != null) {
			List<org.geomajas.gwt2.client.map.attribute.AttributeDescriptor> descriptors = 
					new ArrayList<org.geomajas.gwt2.client.map.attribute.AttributeDescriptor>();
			for (AttributeDescriptor attributeDescriptor : schema.getAttributeDescriptors()) {
				descriptors.add(createDescriptor(attributeDescriptor));
			}
			WfsFeatureTypeDescriptionDto featureTypeDescriptionDto = new WfsFeatureTypeDescriptionDto();
			featureTypeDescriptionDto.setBaseUrl(request.getBaseUrl());
			featureTypeDescriptionDto.setTypeName(request.getTypeName());
			featureTypeDescriptionDto.setAttributeDescriptors(descriptors);
			response.setFeatureTypeDescription(featureTypeDescriptionDto);
		} else {
			log.error("Missing type name on server: " + request.getTypeName());
			throw new GeomajasException(ExceptionCode.PARAMETER_INVALID_VALUE, request.getTypeName());
		}
	}

	@Override
	public WfsDescribeFeatureTypeRequest getEmptyCommandRequest() {
		return new WfsDescribeFeatureTypeRequest(null, null);
	}

	@Override
	public WfsDescribeFeatureTypeResponse getEmptyCommandResponse() {
		return new WfsDescribeFeatureTypeResponse();
	}

	private org.geomajas.gwt2.client.map.attribute.AttributeDescriptor createDescriptor(
			AttributeDescriptor attributeDescriptor) {
		Class<?> binding = attributeDescriptor.getType().getBinding();
		String name = attributeDescriptor.getLocalName();
		AttributeDescriptorImpl attributeInfo;
		if (Integer.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(new PrimitiveAttributeTypeImpl(PrimitiveType.INTEGER), name);
		} else if (Float.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(new PrimitiveAttributeTypeImpl(PrimitiveType.FLOAT), name);
		} else if (Double.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(new PrimitiveAttributeTypeImpl(PrimitiveType.DOUBLE), name);
		} else if (BigDecimal.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(new PrimitiveAttributeTypeImpl(PrimitiveType.DOUBLE), name);
		} else if (Boolean.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(new PrimitiveAttributeTypeImpl(PrimitiveType.BOOLEAN), name);
		} else if (Date.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(new PrimitiveAttributeTypeImpl(PrimitiveType.DATE), name);
		} else if (Point.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(new GeometryAttributeTypeImpl(GeometryType.POINT), name);
		} else if (LineString.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(new GeometryAttributeTypeImpl(GeometryType.LINESTRING), name);
		} else if (LinearRing.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(new GeometryAttributeTypeImpl(GeometryType.LINEARRING), name);
		} else if (Polygon.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(new GeometryAttributeTypeImpl(GeometryType.POLYGON), name);
		} else if (MultiPoint.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(new GeometryAttributeTypeImpl(GeometryType.MULTIPOINT), name);
		} else if (MultiLineString.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(new GeometryAttributeTypeImpl(GeometryType.MULTILINESTRING),
					name);
		} else if (MultiPolygon.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(new GeometryAttributeTypeImpl(GeometryType.MULTIPOLYGON), name);
		} else {
			attributeInfo = new AttributeDescriptorImpl(new PrimitiveAttributeTypeImpl(PrimitiveType.STRING), name);
		}
		return attributeInfo;
	}
}
