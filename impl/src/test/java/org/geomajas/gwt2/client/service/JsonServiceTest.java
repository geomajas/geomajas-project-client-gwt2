package org.geomajas.gwt2.client.service;

import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.WktService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwtmockito.GwtMockitoTestRunner;

@RunWith(GwtMockitoTestRunner.class)
public class JsonServiceTest {

	@Test
	public void testAddToJson()  throws Exception{
		JSONObject object = JsonTestUtil.parse("{}");
		JsonService.addToJson(object, "number", new JSONNumber(123));
		Assert.assertEquals(123.0, object.get("number").isNumber().doubleValue(), 0.0001);
	}
	
	@Test
	public void testParsing() throws Exception {
		JSONObject o = JsonTestUtil.parse("{\"employees\":[\n" + 
				"    {\"firstName\":\"John\", \"lastName\":\"Doe\"},\n" + 
				"    {\"firstName\":\"Anna\", \"lastName\":\"Smith\"},\n" + 
				"    {\"firstName\":\"Peter\", \"lastName\":\"Jones\"}\n" + 
				"]}");
		JSONArray a = JsonService.getChildArray(o, "employees");
		Assert.assertNotNull(a);
		Assert.assertEquals(3, a.size());
	}
	
	@Test
	public void testPoint() throws Exception {
		JSONObject o = JsonTestUtil.parse("{\"geometry\":{\n" + 
				"    \"type\": \"Point\", \n" + 
				"    \"coordinates\": [30, 10]\n" + 
				"}}");
		Geometry g = JsonService.getGeometryValue(o, "geometry");
		Assert.assertEquals("POINT (30.0 10.0)",WktService.toWkt(g));
	}

	@Test
	public void testLineString() throws Exception {
		JSONObject o = JsonTestUtil.parse("{\"geometry\":{\n" + 
				"    \"type\": \"LineString\", \n" + 
				"    \"coordinates\": [\n" + 
				"        [30, 10], [10, 30], [40, 40]\n" + 
				"    ]\n" + 
				"}}");
		Geometry g = JsonService.getGeometryValue(o, "geometry");
		Assert.assertEquals("LINESTRING (30.0 10.0, 10.0 30.0, 40.0 40.0)",WktService.toWkt(g));
	}

	@Test
	public void testPolygon() throws Exception {
		JSONObject o = JsonTestUtil.parse("{\"geometry\":{\n" + 
				"    \"type\": \"Polygon\", \n" + 
				"    \"coordinates\": [\n" + 
				"        [[35, 10], [45, 45], [15, 40], [10, 20], [35, 10]], \n" + 
				"        [[20, 30], [35, 35], [30, 20], [20, 30]]\n" + 
				"    ]\n" + 
				"}}");
		Geometry g = JsonService.getGeometryValue(o, "geometry");
		Assert.assertEquals("POLYGON ((35.0 10.0, 45.0 45.0, 15.0 40.0, 10.0 20.0, 35.0 10.0),"
				+ " (20.0 30.0, 35.0 35.0, 30.0 20.0, 20.0 30.0))",WktService.toWkt(g));
	}

	@Test
	public void testMultiPoint() throws Exception {
		JSONObject o = JsonTestUtil.parse("{\"geometry\":{\n" + 
				"    \"type\": \"MultiPoint\", \n" + 
				"    \"coordinates\": [\n" + 
				"        [10, 40], [40, 30], [20, 20], [30, 10]\n" + 
				"    ]\n" + 
				"}}");
		Geometry g = JsonService.getGeometryValue(o, "geometry");
		Assert.assertEquals("MULTIPOINT ((10.0 40.0), (40.0 30.0), (20.0 20.0), (30.0 10.0))",WktService.toWkt(g));
	}
	
	@Test
	public void testMultiLineString() throws Exception {
		JSONObject o = JsonTestUtil.parse("{\"geometry\":{\n" + 
				"    \"type\": \"MultiLineString\", \n" + 
				"    \"coordinates\": [\n" + 
				"        [[10, 10], [20, 20], [10, 40]], \n" + 
				"        [[40, 40], [30, 30], [40, 20], [30, 10]]\n" + 
				"    ]\n" + 
				"}}");
		Geometry g = JsonService.getGeometryValue(o, "geometry");
		Assert.assertEquals("MULTILINESTRING ((10.0 10.0, 20.0 20.0, 10.0 40.0), "
				+ "(40.0 40.0, 30.0 30.0, 40.0 20.0, 30.0 10.0))",WktService.toWkt(g));
	}

	@Test
	public void testMultiPolygon() throws Exception {
		JSONObject o = JsonTestUtil.parse("{\"geometry\":{\n" + 
				"    \"type\": \"MultiPolygon\", \n" + 
				"    \"coordinates\": [\n" + 
				"        [\n" + 
				"            [[40, 40], [20, 45], [45, 30], [40, 40]]\n" + 
				"        ], \n" + 
				"        [\n" + 
				"            [[20, 35], [10, 30], [10, 10], [30, 5], [45, 20], [20, 35]], \n" + 
				"            [[30, 20], [20, 15], [20, 25], [30, 20]]\n" + 
				"        ]\n" + 
				"    ]\n" + 
				"}\n" + 
				"}");
		Geometry g = JsonService.getGeometryValue(o, "geometry");
		Assert.assertEquals("MULTIPOLYGON (((40.0 40.0, 20.0 45.0, 45.0 30.0, 40.0 40.0)), "
				+ "((20.0 35.0, 10.0 30.0, 10.0 10.0, 30.0 5.0, 45.0 20.0, 20.0 35.0), "
				+ "(30.0 20.0, 20.0 15.0, 20.0 25.0, 30.0 20.0)))",WktService.toWkt(g));
	}


}
