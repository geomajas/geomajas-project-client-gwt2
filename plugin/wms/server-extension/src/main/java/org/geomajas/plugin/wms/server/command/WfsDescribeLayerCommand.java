/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.wms.server.command;

import org.geomajas.command.Command;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptorImpl;
import org.geomajas.gwt2.client.map.attribute.AttributeType;
import org.geomajas.plugin.wms.server.command.dto.WfsDescribeLayerRequest;
import org.geomajas.plugin.wms.server.command.dto.WfsDescribeLayerResponse;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Command that executes a WFS Describe Layer request. It sends back the attribute descriptors to the client.
 *
 * @author Pieter De Graef
 */
@Component
public class WfsDescribeLayerCommand implements Command<WfsDescribeLayerRequest, WfsDescribeLayerResponse> {

	private final Logger log = LoggerFactory.getLogger(WfsDescribeLayerCommand.class);

	public void execute(WfsDescribeLayerRequest request, WfsDescribeLayerResponse response) throws Exception {
		// Create a WFS GetCapabilities URL:
		String capa = request.getBaseUrl() + "?service=wfs&version=1.0.0&request=GetCapabilities";

		Map<String, String> connectionParameters = new HashMap<String, String>();
		connectionParameters.put("WFSDataStoreFactory:GET_CAPABILITIES_URL", capa);
		connectionParameters.put("WFSDataStoreFactory:TIMEOUT", "10000");

		// Get the WFS feature source:
		DataStore data = DataStoreFinder.getDataStore(connectionParameters);

		SimpleFeatureType schema = null;
		try {
			schema = data.getSchema(request.getLayerName()); // forward all exceptions
		} catch (Throwable throwable) {
			log.warn(throwable.getMessage());
		}

		if (schema != null) {
			List<org.geomajas.gwt2.client.map.attribute.AttributeDescriptor> descriptors = new
					ArrayList<org.geomajas.gwt2.client.map.attribute.AttributeDescriptor>();
			for (AttributeDescriptor attributeDescriptor : schema.getAttributeDescriptors()) {
				descriptors.add(createDescriptor(attributeDescriptor));
			}
			response.setAttributeDescriptors(descriptors);
		}
	}

	public WfsDescribeLayerResponse getEmptyCommandResponse() {
		return new WfsDescribeLayerResponse();
	}

	private org.geomajas.gwt2.client.map.attribute.AttributeDescriptor createDescriptor(AttributeDescriptor
			attributeDescriptor) throws IOException {
		Class<?> binding = attributeDescriptor.getType().getBinding();
		if (binding == null) {
			log.warn("No attribute binding found from GeoTools AttributeDescriptor.");
			throw new IOException("No attribute binding found...");
		}
		String name = attributeDescriptor.getLocalName();
		AttributeDescriptorImpl attributeInfo;
		if (Integer.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(AttributeType.INTEGER,
					name);
		} else if (Float.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(AttributeType.FLOAT,
					name);
		} else if (Double.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(AttributeType.DOUBLE,
					name);
		} else if (BigDecimal.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(AttributeType.DOUBLE,
					name);
		} else if (Boolean.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(AttributeType.BOOLEAN,
					name);
		} else if (Date.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(AttributeType.DATE,
					name);
		} else {
			attributeInfo = new AttributeDescriptorImpl(AttributeType.STRING,
					name);
		}
		return attributeInfo;
	}
}
