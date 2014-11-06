package org.geomajas.gwt2.client.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;


public class JsonTestUtil {

	public static JSONObject parse(String json) throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(json);
		return (JSONObject) toGoogle(node);
	}

	private static JSONValue toGoogle(JsonNode node) {
		if (node.isObject()) {
			TestJSONObject object = new TestJSONObject();
			for (Iterator<String> it = node.fieldNames(); it.hasNext();) {
				String key = it.next();
				object.put(key, toGoogle(node.get(key)));
			}
			return object;
		} else if (node.isArray()) {
			JSONArray array = new TestJSONArray();
			for (int i = 0; i < node.size(); i++) {
				array.set(i, toGoogle(node.get(i)));
			}
			return array;
		} else if (node.isBoolean()) {
			JSONBoolean b = JSONBoolean.getInstance(node.booleanValue());
			return b;
		} else if (node.isNumber()) {
			JSONNumber n = new JSONNumber(node.doubleValue());
			return n;
		} else if (node.isTextual()) {
			JSONString s = new JSONString(node.textValue());
			return s;
		}
		return null;
	}

	static class TestJSONObject extends JSONObject {

		private Map<String, JSONValue> valueMap = new HashMap<String, JSONValue>();

		@Override
		public JSONValue get(String key) {
			return valueMap.get(key);
		}

		@Override
		public Set<String> keySet() {
			return valueMap.keySet();
		}

		@Override
		public JSONValue put(String key, JSONValue jsonValue) {
			return valueMap.put(key, jsonValue);
		}

		@Override
		public int size() {
			return valueMap.size();
		}

		@Override
		public boolean containsKey(String key) {
			return valueMap.containsKey(key);
		}
		
		

	}

	static class  TestJSONArray extends JSONArray {

		private List<JSONValue> values = new ArrayList<JSONValue>();

		@Override
		public JSONValue get(int index) {
			return values.get(index);
		}

		@Override
		public JSONValue set(int index, JSONValue value) {
			if(index == values.size()) {
				values.add(index, value);
			}
			return values.set(index, value);
		}

		@Override
		public int size() {
			return values.size();
		}

	}

}
