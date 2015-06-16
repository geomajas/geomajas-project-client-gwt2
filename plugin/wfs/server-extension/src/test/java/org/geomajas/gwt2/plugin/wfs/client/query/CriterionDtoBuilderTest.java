package org.geomajas.gwt2.plugin.wfs.client.query;

import org.junit.Test;

public class CriterionDtoBuilderTest {

	@Test
	public void test() {
		String test = "\"EPSG:4326\" = \"EPSG:4326\"";
		System.out.println(test);
		System.out.println(test.replaceAll("\"EPSG:4326\"", "\"urn:x-ogc:def:crs:EPSG:4326\""));
	}
}
