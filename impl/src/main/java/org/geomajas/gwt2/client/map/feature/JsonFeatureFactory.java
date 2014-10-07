package org.geomajas.gwt2.client.map.feature;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.geomajas.geometry.Geometry;
import org.geomajas.gwt2.client.map.attribute.Attribute;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.client.map.attribute.AttributeType;
import org.geomajas.gwt2.client.map.layer.FeaturesSupported;
import org.geomajas.gwt2.client.service.JsonService;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public class JsonFeatureFactory {

	public FeatureCollection createCollection(JSONObject jsonObject, FeaturesSupported layer) {
		FeatureCollection dto = new FeatureCollection(layer);
		String type = JsonService.getStringValue(jsonObject, "type");
		if ("FeatureCollection".equals(type)) {
			JSONArray features = JsonService.getChildArray(jsonObject, "features");
			for (int i = 0; i < features.size(); i++) {
				dto.getFeatures().add(createFeature((JSONObject) features.get(i), layer));
			}
		} else if (type.equals("Feature".equals(type))) {
			dto.getFeatures().add(createFeature(jsonObject, layer));
		}
		return dto;
	}

	private Feature createFeature(JSONObject jsonObject, FeaturesSupported layer) {
		String id = JsonService.getStringValue(jsonObject, "id");
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
			for (String  name : properties.keySet()) {
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
