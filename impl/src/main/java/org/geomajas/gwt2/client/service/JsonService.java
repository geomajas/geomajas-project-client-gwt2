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

package org.geomajas.gwt2.client.service;

import java.util.Date;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt2.client.map.attribute.GeometryType;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 * Utility class for JSON related functionality.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
public final class JsonService {

	// Specific date formatter for communication with back-end rest.
	// TODO provide a way to override this default.
	private static final DateTimeFormat DEFAULT_DATEFORMAT = DateTimeFormat.getFormat(PredefinedFormat.ISO_8601);

	private JsonService() {
		// No public constructors for final classes.
	}

	/**
	 * Format the given date to a string. The idea is that this string can be used in a JSON object.
	 * 
	 * @param date The date to format.
	 * @return The formatted date-string.
	 */
	public static String format(Date date) {
		return DEFAULT_DATEFORMAT.format(date);
	}

	/**
	 * Takes in a trusted JSON String and evals it.
	 * 
	 * @param jsonString JSON string that you trust.
	 * @return JavaScriptObject that you can cast to an Overlay Type
	 */
	public static JavaScriptObject eval(String jsonString) {
		return JsonUtils.safeEval(jsonString);
	}

	/**
	 * Parse the given JSON string into a {@link JSONObject}.
	 * 
	 * @param jsonString The trusted JSON string to parse.
	 * @return The JSON object that results from the parsed string.
	 * @throws JSONException Thrown in case something went wrong during parsing.
	 */
	public static JSONValue parse(String jsonString) throws JSONException {
		if (jsonString == null || "".equals(jsonString)) {
			return JSONNull.getInstance();
		}
		if (jsonString.charAt(0) == '[') {
			return new JSONArray(eval(jsonString));
		}
		return new JSONObject(eval(jsonString));
	}

	/**
	 * Get a child JSON object from a parent JSON object.
	 * 
	 * @param jsonObject The parent JSON object.
	 * @param key The name of the child object.
	 * @return Returns the child JSON object if it could be found, or null if the value was null.
	 * @throws JSONException In case something went wrong while searching for the child.
	 */
	public static JSONObject getChild(JSONObject jsonObject, String key) throws JSONException {
		checkArguments(jsonObject, key);
		JSONValue value = jsonObject.get(key);
		if (value != null) {
			if (value.isObject() != null) {
				return value.isObject();
			} else if (value.isNull() != null) {
				return null;
			}
			throw new JSONException("Child is not a JSONObject, but a: " + value.getClass());
		}
		return null;
	}

	/**
	 * Get a child JSON array from a parent JSON object.
	 * 
	 * @param jsonObject The parent JSON object.
	 * @param key The name of the child object.
	 * @return Returns the child JSON array if it could be found, or null if the value was null.
	 * @throws JSONException In case something went wrong while searching for the child.
	 */
	public static JSONArray getChildArray(JSONObject jsonObject, String key) throws JSONException {
		checkArguments(jsonObject, key);
		JSONValue value = jsonObject.get(key);
		if (value != null) {
			if (value.isArray() != null) {
				return value.isArray();
			} else if (value.isNull() != null) {
				return null;
			}
			throw new JSONException("Child is not a JSONArray, but a: " + value.getClass());
		}
		return null;
	}

	/**
	 * Get a string value from a {@link JSONObject}.
	 * 
	 * @param jsonObject The object to get the key value from.
	 * @param key The name of the key to search the value for.
	 * @return Returns the value for the key in the object or null.
	 * @throws JSONException Thrown in case the key could not be found in the JSON object.
	 */
	public static String getStringValue(JSONObject jsonObject, String key) throws JSONException {
		checkArguments(jsonObject, key);
		JSONValue value = jsonObject.get(key);
		if (value != null) {
			if (value.isString() != null) {
				return ((JSONString) value).stringValue();
			} else {
				String valueString = value.toString();
				if (!"null".equals(valueString)) {
					if (valueString.charAt(0) == '"') {
						return valueString.substring(1, valueString.length() - 1);
					}
					return valueString;
				}
			}
		}
		return null;
	}

	/**
	 * Get a character value from a {@link JSONObject}.
	 * 
	 * @param jsonObject The object to get the key value from.
	 * @param key The name of the key to search the value for.
	 * @return Returns the value for the key in the object or null.
	 * @throws JSONException Thrown in case the key could not be found in the JSON object.
	 */
	public static Character getCharValue(JSONObject jsonObject, String key) throws JSONException {
		String stringValue = getStringValue(jsonObject, key);
		if (stringValue != null) {
			return stringValue.charAt(0);
		}
		return null;
	}

	/**
	 * Get a date value from a {@link JSONObject}.
	 * 
	 * @param jsonObject The object to get the key value from.
	 * @param key The name of the key to search the value for.
	 * @return Returns the value for the key in the object or null.
	 * @throws JSONException Thrown in case the key could not be found in the JSON object, or if the date could not be
	 *         parsed correctly.
	 */
	public static Date getDateValue(JSONObject jsonObject, String key) throws JSONException {
		checkArguments(jsonObject, key);
		JSONValue value = jsonObject.get(key);
		if (value != null && value.isString() != null) {
			String dateString = getStringValue(jsonObject, key);
			if (dateString.charAt(0) == '"') {
				dateString = dateString.substring(1);
			}
			if (dateString.endsWith("\"")) {
				dateString = dateString.substring(0, dateString.length() - 1);
			}
			try {
				return DEFAULT_DATEFORMAT.parse(dateString);
			} catch (Exception e) {
				try {
					long millis = Long.parseLong(dateString);
					return new Date(millis);
				} catch (Exception e2) {
					throw new JSONException("Could not parse the date for key '" + key + "'", e);
				}
			}
		}
		return null;
	}

	/**
	 * Get a geometry value from a {@link JSONObject}.
	 * 
	 * @param jsonObject The object to get the key value from.
	 * @param key The name of the key to search the value for.
	 * @return Returns the value for the key in the object or null.
	 * @throws JSONException Thrown in case the key could not be found in the JSON object, or if the date could not be
	 *         parsed correctly.
	 */
	public static Geometry getGeometryValue(JSONObject jsonObject, String key) throws JSONException {
		checkArguments(jsonObject, key);
		JSONValue value = jsonObject.get(key);
		if (value != null && value.isObject() != null) {
			JSONObject obj = value.isObject();
			String type = JsonService.getStringValue(obj, "type");
			Geometry geometry = new Geometry(type, 0, 5);
			JSONArray array = JsonService.getChildArray(obj, "coordinates");
			switch (GeometryType.fromValue(type)) {
				case POINT:
					parseSimple(GeometryType.POINT, geometry, array);
					break;
				case LINESTRING:
					parseSimple(GeometryType.LINEARRING, geometry, array);
					break;
				case MULTILINESTRING:
					parseCollection(GeometryType.LINESTRING, geometry, array);
					break;
				case MULTIPOINT:
					parseCollection(GeometryType.POINT, geometry, array);
					break;
				case MULTIPOLYGON:
					parseCollection(GeometryType.POLYGON, geometry, array);
					break;
				case POLYGON:
					parseCollection(GeometryType.LINEARRING, geometry, array);
					break;
				default:
					break;
			}
			return geometry;
		}
		return null;
	}

	/**
	 * Get a integer value from a {@link JSONObject}.
	 * 
	 * @param jsonObject The object to get the key value from.
	 * @param key The name of the key to search the value for.
	 * @return Returns the value for the key in the object .
	 * @throws JSONException Thrown in case the key could not be found in the JSON object.
	 */
	public static Integer getIntValue(JSONObject jsonObject, String key) throws JSONException {
		checkArguments(jsonObject, key);
		JSONValue value = jsonObject.get(key);
		if (value != null && value.isNumber() != null) {
			double number = ((JSONNumber) value).doubleValue();
			return new Integer((int) number);
		}
		return null;
	}

	/**
	 * Get a double value from a {@link JSONObject}.
	 * 
	 * @param jsonObject The object to get the key value from.
	 * @param key The name of the key to search the value for.
	 * @return Returns the value for the key in the object .
	 * @throws JSONException Thrown in case the key could not be found in the JSON object.
	 */
	public static Double getDoubleValue(JSONObject jsonObject, String key) throws JSONException {
		checkArguments(jsonObject, key);
		JSONValue value = jsonObject.get(key);
		if (value != null && value.isNumber() != null) {
			double number = ((JSONNumber) value).doubleValue();
			return number;
		}
		return null;
	}

	/**
	 * Get a long value from a {@link JSONObject}.
	 * 
	 * @param jsonObject The object to get the key value from.
	 * @param key The name of the key to search the value for.
	 * @return Returns the value for the key in the object .
	 * @throws JSONException Thrown in case the key could not be found in the JSON object.
	 */
	public static Long getLongValue(JSONObject jsonObject, String key) throws JSONException {
		checkArguments(jsonObject, key);
		JSONValue value = jsonObject.get(key);
		if (value != null && value.isNumber() != null) {
			double number = ((JSONNumber) value).doubleValue();
			return new Long((long) number);
		}
		return null;
	}

	/**
	 * Get a short value from a {@link JSONObject}.
	 * 
	 * @param jsonObject The object to get the key value from.
	 * @param key The name of the key to search the value for.
	 * @return Returns the value for the key in the object .
	 * @throws JSONException Thrown in case the key could not be found in the JSON object.
	 */
	public static Short getShortValue(JSONObject jsonObject, String key) throws JSONException {
		checkArguments(jsonObject, key);
		JSONValue value = jsonObject.get(key);
		if (value != null && value.isNumber() != null) {
			double number = ((JSONNumber) value).doubleValue();
			return new Short((short) number);
		}
		return null;
	}

	/**
	 * Add a certain object value to the given JSON object.
	 * 
	 * @param jsonObject The JSON object to add the value to.
	 * @param key The key to be used when adding the value.
	 * @param value The value to attach to the key in the JSON object.
	 * @throws JSONException In case something went wrong while adding.
	 */
	public static void addToJson(JSONObject jsonObject, String key, Object value) throws JSONException {
		if (jsonObject == null) {
			throw new JSONException("Can't add key '" + key + "' to a null object.");
		}
		if (key == null) {
			throw new JSONException("Can't add null key.");
		}
		JSONValue jsonValue = null;
		if (value != null) {
			if (value instanceof Date) {
				jsonValue = new JSONString(JsonService.format((Date) value));
			} else if (value instanceof JSONValue) {
				jsonValue = (JSONValue) value;
			} else {
				jsonValue = new JSONString(value.toString());
			}
		}
		jsonObject.put(key, jsonValue);
	}

	/**
	 * checks if a certain object value is null.
	 * 
	 * @param jsonObject The object to get the key value from.
	 * @param key The name of the key to search the value for.
	 * @throws JSONException In case something went wrong while parsing.
	 */
	public static boolean isNullObject(JSONObject jsonObject, String key) throws JSONException {
		checkArguments(jsonObject, key);
		JSONValue value = jsonObject.get(key);

		boolean isNull = false;
		if (value == null || value.isNull() != null) {
			isNull = true;
		}
		return isNull;
	}

	/**
	 * Get a boolean value from a {@link JSONObject}.
	 * 
	 * @param jsonObject The object to get the key value from.
	 * @param key The name of the key to search the value for.
	 * @return Returns the value for the key in the object .
	 * @throws JSONException Thrown in case the key could not be found in the JSON object.
	 */
	public static Boolean getBooleanValue(JSONObject jsonObject, String key) throws JSONException {
		checkArguments(jsonObject, key);
		JSONValue value = jsonObject.get(key);
		Boolean result = null;
		if (value != null && value.isBoolean() != null) {
			return ((JSONBoolean) value).booleanValue();
		}
		return result;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private static void checkArguments(JSONObject jsonObject, String key) throws JSONException {
		if (key == null) {
			throw new IllegalArgumentException("Can't go searching for a null key.");
		}
		if (jsonObject == null) {
			throw new JSONException("Can't search for key '" + key + "' in a null object.");
		}
		if (!jsonObject.containsKey(key)) {
			throw new JSONException("Key '" + key + "' could not be found.");
		}
		// Please, proceed...
	}

	private static void parseCollection(GeometryType type, Geometry geometry, JSONArray array) {
		Geometry[] geometries = new Geometry[array.size()];
		geometry.setGeometries(geometries);
		for (int i = 0; i < array.size(); i++) {
			switch (type) {
				case POINT:
					geometries[i] = new Geometry(Geometry.POINT, 0, 5);
					parseSimple(GeometryType.POINT, geometries[i], array.get(i).isArray());
					break;
				case LINEARRING:
					geometries[i] = new Geometry(Geometry.LINEAR_RING, 0, 5);
					parseSimple(GeometryType.LINEARRING, geometries[i], array.get(i).isArray());
					break;
				case LINESTRING:
					geometries[i] = new Geometry(Geometry.LINE_STRING, 0, 5);
					parseSimple(GeometryType.LINESTRING, geometries[i], array.get(i).isArray());
					break;
				case POLYGON:
					geometries[i] = new Geometry(Geometry.POLYGON, 0, 5);
					parseCollection(GeometryType.LINEARRING, geometries[i], array.get(i).isArray());
					break;
				default:
					break;
			}
		}
	}

	private static void parseSimple(GeometryType type, Geometry geometry, JSONArray array) {
		Coordinate[] coords;
		switch (type) {
			case POINT:
				coords = new Coordinate[1];
				coords[0] = new Coordinate();
				coords[0].setX(array.get(0).isNumber().doubleValue());
				coords[0].setY(array.get(1).isNumber().doubleValue());
				geometry.setCoordinates(coords);
				break;
			case LINEARRING:
			case LINESTRING:
				coords = new Coordinate[array.size()];
				for (int i = 0; i < coords.length; i++) {
					coords[i] = new Coordinate();
					coords[i].setX(array.get(i).isArray().get(0).isNumber().doubleValue());
					coords[i].setY(array.get(i).isArray().get(1).isNumber().doubleValue());
				}
				geometry.setCoordinates(coords);
				break;
			default:
				break;
		}
	}

}