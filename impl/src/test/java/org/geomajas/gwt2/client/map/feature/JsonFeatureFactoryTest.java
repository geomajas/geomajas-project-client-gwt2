package org.geomajas.gwt2.client.map.feature;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.geomajas.geometry.service.WktService;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptorImpl;
import org.geomajas.gwt2.client.map.attribute.PrimitiveAttributeTypeImpl;
import org.geomajas.gwt2.client.map.attribute.PrimitiveType;
import org.geomajas.gwt2.client.map.layer.FeaturesSupported;
import org.geomajas.gwt2.client.service.JsonTestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import com.google.gwtmockito.GwtMockitoTestRunner;

@RunWith(GwtMockitoTestRunner.class)
public class JsonFeatureFactoryTest {

	@Mock
	private FeaturesSupported layer;

	@Test
	public void testCreateCollection() throws Exception {
		List<AttributeDescriptor> l = new ArrayList<AttributeDescriptor>();
		l.add(new AttributeDescriptorImpl(new PrimitiveAttributeTypeImpl(PrimitiveType.INTEGER), "osm_id"));
		l.add(new AttributeDescriptorImpl(new PrimitiveAttributeTypeImpl(PrimitiveType.DATE), "lastchange"));
		l.add(new AttributeDescriptorImpl(new PrimitiveAttributeTypeImpl(PrimitiveType.STRING), "type"));
		l.add(new AttributeDescriptorImpl(new PrimitiveAttributeTypeImpl(PrimitiveType.STRING), "name"));
		l.add(new AttributeDescriptorImpl(new PrimitiveAttributeTypeImpl(PrimitiveType.INTEGER), "population"));
		when(layer.getAttributeDescriptors()).thenReturn(l);
		JsonFeatureFactory factory = new JsonFeatureFactory();
		FeatureCollection c = factory
				.createCollection(
						JsonTestUtil
								.parse("{\"type\":\"FeatureCollection\","
										+ "\"features\":[{\"type\":\"Feature\",\"id\":\"place.1\","
										+ "\"geometry\":{\"type\":\"Point\",\"coordinates\":[4.4436244,50.4120332]},"
										+ "\"geometry_name\":\"geom\",\"properties\":"
										+ "{\"osm_id\":9002746,\"lastchange\":\"2008-10-03T10:29:40.046-04:00\","
										+ "\"type\":\"city\",\"name\":\"Charleroi\",\"population\":203000}},"
										+ "{\"type\":\"Feature\",\"id\":\"place.2\",\"geometry\":"
										+ "{\"type\":\"Point\",\"coordinates\":[4.3487134,50.883392]},"
										+ "\"geometry_name\":\"geom\",\"properties\":{\"osm_id\":17428149,"
										+ "\"lastchange\":\"2008-10-03T10:29:40.046-04:00\",\"type\":\"village\",\"name\":\"Laken - Laeken\","
										+ "\"population\":null}}],\"crs\":{\"type\":\"EPSG\","
										+ "\"properties\":{\"code\":\"4326\"}}}"), layer);
		Assert.assertEquals(2, c.getFeatures().size());
		Feature f1 = c.getFeatures().get(0);
		Assert.assertEquals(9002746, f1.getAttributeValue("osm_id"));
		Assert.assertEquals(new Date(1223044180046L), (Date) f1.getAttributeValue("lastchange"));
		Assert.assertEquals("city", f1.getAttributeValue("type"));
		Assert.assertEquals("Charleroi", f1.getAttributeValue("name"));
		Assert.assertEquals(203000, f1.getAttributeValue("population"));
		Assert.assertEquals("POINT (4.4436244 50.4120332)", WktService.toWkt(f1.getGeometry()));
		Feature f2 = c.getFeatures().get(1);
		Assert.assertEquals(17428149, f2.getAttributeValue("osm_id"));
		Assert.assertEquals(new Date(1223044180046L), (Date) f2.getAttributeValue("lastchange"));
		Assert.assertEquals("village", f2.getAttributeValue("type"));
		Assert.assertEquals("Laken - Laeken", f2.getAttributeValue("name"));
		Assert.assertNull(f2.getAttributeValue("population"));
		Assert.assertEquals("POINT (4.3487134 50.883392)", WktService.toWkt(f2.getGeometry()));
	}

}
