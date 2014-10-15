/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.gwt2.client.map.feature;

import com.google.gwt.dom.client.Document;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

import org.geomajas.geometry.Geometry;
import org.geomajas.gwt2.client.map.attribute.Attribute;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.client.map.attribute.AttributeType;
import org.geomajas.gwt2.client.map.layer.FeaturesSupported;
import org.geomajas.gwt2.client.service.JsonService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory that creates features from JSON objects.
 * 
 * @author Jan De Moerloose
 *
 */
public class JsonFeatureFactory {

	/**
	 * Create a feature collection for this layer.
	 * 
	 * @param jsonObject
	 * @param layer the layer (optional)
	 * @return the feature
	 */
	public FeatureCollection createCollection(JSONObject jsonObject, FeaturesSupported layer) {
		FeatureCollection dto = new FeatureCollection(layer);
		String type = JsonService.getStringValue(jsonObject, "type");
		if ("FeatureCollection".equals(type)) {
			JSONArray features = JsonService.getChildArray(jsonObject, "features");
			for (int i = 0; i < features.size(); i++) {
				dto.getFeatures().add(createFeature((JSONObject) features.get(i), layer));
			}
		} else if ("Feature".equals(type)) {
			dto.getFeatures().add(createFeature(jsonObject, layer));
		}
		return dto;
	}

	private Feature createFeature(JSONObject jsonObject, FeaturesSupported layer) {
		String id = null;
		// id is not mandatory !
		if (jsonObject.containsKey("id")) {
			id = JsonService.getStringValue(jsonObject, "id");
		} else {
			id = Document.get().createUniqueId();
		}
		JSONObject properties = JsonService.getChild(jsonObject, "properties");
		Map<String, Attribute<?>> attributes = new HashMap<String, Attribute<?>>();
		if (layer != null && layer.getAttributeDescriptors().size() > 0) {
			for (AttributeDescriptor descr : layer.getAttributeDescriptors()) {
				AttributeType type = descr.getType();
				String name = descr.getName();
				Attribute attribute;
				if (Boolean.class.equals(type.getBinding())) {
					attribute = new AttributeImpl(JsonService.getBooleanValue(properties, name));
				} else if (Short.class.equals(type.getBinding())) {
					attribute = new AttributeImpl(JsonService.getShortValue(properties, name));
				} else if (Integer.class.equals(type.getBinding())) {
					attribute = new AttributeImpl(JsonService.getIntValue(properties, name));
				} else if (Long.class.equals(type.getBinding())) {
					attribute = new AttributeImpl(JsonService.getLongValue(properties, name));
				} else if (Double.class.equals(type.getBinding())) {
					attribute = new AttributeImpl(JsonService.getDoubleValue(properties, name));
				} else if (Date.class.equals(type.getBinding())) {
					attribute = new AttributeImpl(JsonService.getDateValue(properties, name));
				} else if (Geometry.class.equals(type.getBinding())) {
					attribute = new AttributeImpl(JsonService.getGeometryValue(properties, name));
				} else {
					attribute = new AttributeImpl(JsonService.getStringValue(properties, name));
				}
				attributes.put(name, attribute);
			}
		} else {
			for (String name : properties.keySet()) {
				JSONValue value = properties.get(name);
				Attribute attribute;
				if (value.isBoolean() != null) {
					attribute = new AttributeImpl(JsonService.getBooleanValue(properties, name));
				} else if (value.isNumber() != null) {
					attribute = new AttributeImpl(JsonService.getDoubleValue(properties, name));
				} else if (value.isObject() != null) {
					attribute = new AttributeImpl(JsonService.getGeometryValue(properties, name));
				} else {
					attribute = new AttributeImpl(JsonService.getStringValue(properties, name));
				}
				attributes.put(name, attribute);
			}
		}
		Geometry geometry = JsonService.getGeometryValue(jsonObject, "geometry");
		return new FeatureImpl(layer, id, attributes, geometry, id);
	}

}
