package org.geomajas.gwt2.plugin.wfs.server.command;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.command.Command;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptorImpl;
import org.geomajas.gwt2.client.map.attribute.GeometryAttributeTypeImpl;
import org.geomajas.gwt2.client.map.attribute.GeometryType;
import org.geomajas.gwt2.client.map.attribute.PrimitiveAttributeTypeImpl;
import org.geomajas.gwt2.client.map.attribute.PrimitiveType;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsDataStoreFactory;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsDescribeFeatureTypeRequest;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsDescribeFeatureTypeResponse;
import org.geotools.data.DataStore;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.SimpleHttpClient;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.data.wfs.internal.WFSConfig;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.geotools.ows.ServiceException;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequenceFactory;

@Component
public class WfsDescribeFeatureTypeCommand implements
		Command<WfsDescribeFeatureTypeRequest, WfsDescribeFeatureTypeResponse> {

	private final Logger log = LoggerFactory.getLogger(WfsDescribeFeatureTypeCommand.class);

	public void execute(WfsDescribeFeatureTypeRequest request, WfsDescribeFeatureTypeResponse response)
			throws GeomajasException {
		// Create a WFS GetCapabilities URL:
		String capa = request.getBaseUrl() + "?service=wfs&version=1.0.0&request=GetCapabilities";

		Map<String, Serializable> connectionParameters = new HashMap<String, Serializable>();
		connectionParameters.put(WFSDataStoreFactory.URL.key, capa);
		connectionParameters.put(WFSDataStoreFactory.TIMEOUT.key, 10000);

		// Get the WFS feature source:
		SimpleFeatureType schema = null;
		try {
			WfsDataStoreFactory factory = new WfsDataStoreFactory();
			DataStore data = factory.createDataStore(connectionParameters, getClientForUrl(capa));
			schema = data.getSchema(request.getTypeName().replace(":", "_"));
		} catch (IOException e) {
			log.error("DescribeFeatureType failed for " + request.getTypeName(), e);
			throw new GeomajasException(ExceptionCode.UNEXPECTED_PROBLEM, e.getMessage());
		}

		if (schema != null) {
			List<org.geomajas.gwt2.client.map.attribute.AttributeDescriptor> descriptors = new ArrayList<org.geomajas.gwt2.client.map.attribute.AttributeDescriptor>();
			for (AttributeDescriptor attributeDescriptor : schema.getAttributeDescriptors()) {
				descriptors.add(createDescriptor(attributeDescriptor));
			}
			response.setAttributeDescriptors(descriptors);
		} else {
			log.error("Missing type name on server: " + request.getTypeName());
			throw new GeomajasException(ExceptionCode.PARAMETER_INVALID_VALUE, request.getTypeName());
		}
	}

	protected HTTPClient getClientForUrl(String url) {
		return new SimpleHttpClient();
	}

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
