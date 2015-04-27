package org.geomajas.gwt2.plugin.wfs.server.command;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.geometry.service.WktService;
import org.geomajas.global.GeomajasException;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptorImpl;
import org.geomajas.gwt2.client.map.attribute.GeometryAttributeTypeImpl;
import org.geomajas.gwt2.client.map.attribute.GeometryType;
import org.geomajas.gwt2.client.map.attribute.PrimitiveAttributeTypeImpl;
import org.geomajas.gwt2.client.map.attribute.PrimitiveType;
import org.geomajas.gwt2.client.map.feature.query.Criterion;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsGetFeatureRequest;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsGetFeatureResponse;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.AttributeCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.BboxCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.DWithinCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.DoubleAttributeCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.FidCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.FullTextCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.GeometryCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.IntegerAttributeCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.LogicalCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.query.StringAttributeCriterionDto;
import org.geomajas.layer.feature.Feature;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.collection.CollectionFeatureSource;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.text.ecql.ECQL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "commandContext.xml" })
public class WfsGetFeatureCommandTest {

	private static final String WMS_BASE_URL = "http://apps.geomajas.org/geoserver/demo_world/ows";

	private static final String LAYER = "demo_world:simplified_country_borders";

	private Server server;

	@Autowired
	private WfsGetFeatureCommand command;

	private WfsServlet servlet;

	private SimpleFeatureSource featureSource;

	private SimpleFeature f1;

	private SimpleFeature f2;

	private SimpleFeature f3;

	@Before
	public void before() throws Exception {
		server = new Server(8080);
		servlet = new WfsServlet();
		ServletContextHandler servletContextHandler = new ServletContextHandler();
		servletContextHandler.setContextPath("/");
		servletContextHandler.addServlet(new ServletHolder(servlet),"/wfs/*");
		server.setHandler(servletContextHandler);
		server.start();
		StringBuilder sb = new StringBuilder();
		sb.append("myGeom:Point,");
		sb.append("myInt:Integer,");
		sb.append("myDouble:Double,");
		sb.append("myDate:Date,");
		sb.append("myBigDecimal:java.math.BigDecimal,");
		sb.append("myString:String,");
		sb.append("anotherString:String");
		SimpleFeatureType schema = DataUtilities.createType("testdata", sb.toString());
		f1 = DataUtilities.createFeature(schema, "fid1=POINT(1 1)|1|1.0|2014-01-01 02:24:56am|11111111.1111|a|cef");
		f2 = DataUtilities.createFeature(schema, "fid2=POINT(2 2)|2|2.0|2014-02-02 02:24:56am|22222222.2222|aa|ceef");
		f3 = DataUtilities.createFeature(schema, "fid3=POINT(3 3)|3|3.0|2014-03-03 02:24:56am|33333333.3333|ab|c");
		featureSource = new CollectionFeatureSource(DataUtilities.collection(new SimpleFeature[] { f1, f2, f3 }));
	}

	@Test
	public void integrationTest() throws Exception {
		WfsGetFeatureRequest request = new WfsGetFeatureRequest();
		request.setBaseUrl("http://127.0.0.1:8080/wfs");
		request.setTypeName("dov-pub-bodem:Bodemassociatiekaart");
		request.setCriterion(new FidCriterionDto(new String[] { "Bodemassociatiekaart.1", "Bodemassociatiekaart.2" }));
		request.setCrs("EPSG:31370");
		WfsGetFeatureResponse response = new WfsGetFeatureResponse();
		command.execute(request, response);
		Assert.assertEquals(2, response.getFeatureCollection().size());
		Assert.assertNotNull(servlet.getLastFilter());
		Assert.assertEquals(ECQL.toFilter("IN('Bodemassociatiekaart.1', 'Bodemassociatiekaart.2')"), servlet.getLastFilter());
	}
	
	@Test
	public void testGetCountries() throws Exception {
		WfsGetFeatureResponse response = command.getEmptyCommandResponse();
		Assert.assertNotNull(response);

		Bbox bounds = new Bbox(-180, -90, 360, 180);
		Geometry geometry = GeometryService.toPolygon(bounds);

		WfsGetFeatureRequest request = new WfsGetFeatureRequest();
		request.setBaseUrl(WMS_BASE_URL);
		request.setTypeName(LAYER);
		request.setCriterion(new GeometryCriterionDto("the_geom",GeometryCriterionDto.INTERSECTS,geometry));
		request.setCrs("EPSG:4326");		
		request.setMaxCoordsPerFeature(100);
		request.setMaxFeatures(100);

		command.execute(request, response);
		Assert.assertNotNull(response.getFeatureCollection());
		Assert.assertTrue(response.getFeatureCollection().getFeatures().size() > 0);
	}
	@Test
	public void EQCriterion() throws Exception {
		List<Feature> ff = queryFeatures(featureSource, new IntegerAttributeCriterionDto("myInt", "=", new Integer(1)),
				3, 0, null);
		Assert.assertEquals(1, ff.size());
		assertEquals(f1, ff.get(0));
	}

	@Test
	public void LTCriterion() throws Exception {
		List<Feature> ff = queryFeatures(featureSource, new IntegerAttributeCriterionDto("myInt", "<", new Integer(3)),
				3, 0, null);
		Assert.assertEquals(2, ff.size());
		assertEquals(f1, ff.get(0));
		assertEquals(f2, ff.get(1));
	}

	@Test
	public void GTCriterion() throws Exception {
		List<Feature> ff = queryFeatures(featureSource, new IntegerAttributeCriterionDto("myInt", ">", new Integer(1)),
				3, 0, null);
		Assert.assertEquals(2, ff.size());
		assertEquals(f2, ff.get(0));
		assertEquals(f3, ff.get(1));
	}

	@Test
	public void LTEQCriterion() throws Exception {
		List<Feature> ff = queryFeatures(featureSource,
				new IntegerAttributeCriterionDto("myInt", "<=", new Integer(2)), 3, 0, null);
		Assert.assertEquals(2, ff.size());
		assertEquals(f1, ff.get(0));
		assertEquals(f2, ff.get(1));
	}

	@Test
	public void GTEQCriterion() throws Exception {
		List<Feature> ff = queryFeatures(featureSource,
				new IntegerAttributeCriterionDto("myInt", ">=", new Integer(2)), 3, 0, null);
		Assert.assertEquals(2, ff.size());
		assertEquals(f2, ff.get(0));
		assertEquals(f3, ff.get(1));
	}

	@Test
	public void LikeCriterion() throws Exception {
		// single char
		List<Feature> ff = queryFeatures(featureSource, new StringAttributeCriterionDto("myString", "like", "a?"), 3,
				0, null);
		Assert.assertEquals(2, ff.size());
		assertEquals(f2, ff.get(0));
		assertEquals(f3, ff.get(1));
		// wildcard
		ff = queryFeatures(featureSource, new StringAttributeCriterionDto("myString", "like", "*a"), 3, 0, null);
		Assert.assertEquals(2, ff.size());
		assertEquals(f1, ff.get(0));
		assertEquals(f2, ff.get(1));
		// exact
		ff = queryFeatures(featureSource, new StringAttributeCriterionDto("myString", "like", "ab"), 3, 0, null);
		Assert.assertEquals(1, ff.size());
		assertEquals(f3, ff.get(0));
		// case insensitive (if supported by the datastore, but WFS 1.0.0 does not support it !!!)
		ff = queryFeatures(featureSource, new StringAttributeCriterionDto("myString", "like", "AB"), 3, 0, null);
		Assert.assertEquals(1, ff.size());
		assertEquals(f3, ff.get(0));
	}

	@Test
	public void FullTextCriterion() throws Exception {
		// a
		List<Feature> ff = queryFeatures(featureSource, new FullTextCriterionDto("a"), 3,
				0, null);
		Assert.assertEquals(3, ff.size());
		// aa
		ff = queryFeatures(featureSource, new FullTextCriterionDto("aa"), 3,
				0, null);
		Assert.assertEquals(1, ff.size());
		// e
		ff = queryFeatures(featureSource, new FullTextCriterionDto("e"), 3,
				0, null);
		Assert.assertEquals(2, ff.size());
		// ee
		ff = queryFeatures(featureSource, new FullTextCriterionDto("ee"), 3,
				0, null);
		Assert.assertEquals(1, ff.size());
	}

	@Test
	public void AndCriterion() throws Exception {
		AttributeCriterionDto attr1 = new IntegerAttributeCriterionDto("myInt", "<=", new Integer(2));
		AttributeCriterionDto attr2 = new DoubleAttributeCriterionDto("myDouble", ">", new Double(1.5));
		LogicalCriterionDto and = new LogicalCriterionDto(LogicalCriterionDto.Operator.AND);
		and.getChildren().add(attr1);
		and.getChildren().add(attr2);
		List<Feature> ff = queryFeatures(featureSource, and, 3, 0, null);
		Assert.assertEquals(1, ff.size());
		assertEquals(f2, ff.get(0));
	}

	@Test
	public void OrCriterion() throws Exception {
		AttributeCriterionDto attr1 = new IntegerAttributeCriterionDto("myInt", "<=", new Integer(2));
		AttributeCriterionDto attr2 = new DoubleAttributeCriterionDto("myDouble", ">", new Double(1.5));
		LogicalCriterionDto or = new LogicalCriterionDto(LogicalCriterionDto.Operator.OR);
		or.getChildren().add(attr1);
		or.getChildren().add(attr2);
		List<Feature> ff = queryFeatures(featureSource, or, 3, 0, null);
		Assert.assertEquals(3, ff.size());
		assertEquals(f1, ff.get(0));
		assertEquals(f2, ff.get(1));
		assertEquals(f3, ff.get(2));
	}

	@Test
	public void BboxCriterion() throws Exception {
		BboxCriterionDto c = new BboxCriterionDto(new Bbox(0, 0, 1.5, 1.5));
		List<Feature> ff = queryFeatures(featureSource, c, 3, 0, null);
		Assert.assertEquals(1, ff.size());
		assertEquals(f1, ff.get(0));
	}

	@Test
	public void DWithinCriterion() throws Exception {
		Geometry distancePoint = new Geometry(Geometry.POINT, 0, 5);
		distancePoint.setCoordinates(new Coordinate[] {new Coordinate(1.5, 1.5)});
		DWithinCriterionDto c = new DWithinCriterionDto(0.71, "", distancePoint);
		List<Feature> ff = queryFeatures(featureSource, c, 3, 0, null);
		Assert.assertEquals(2, ff.size());
		assertEquals(f1, ff.get(0));
		assertEquals(f2, ff.get(1));
	}

	@Test
	public void totalBounds() throws Exception {
		List<Feature> ff = queryFeatures(featureSource,
				new IntegerAttributeCriterionDto("myInt", "<", new Integer(100)), 3, 0, null);
		Assert.assertEquals(3, ff.size());
		Bbox bounds = command.getTotalBounds(ff);
		Assert.assertTrue(BboxService.equals(bounds, new Bbox(1, 1, 2, 2), 0.001));
	}

	protected List<Feature> queryFeatures(FeatureSource<SimpleFeatureType, SimpleFeature> source,
			Criterion criterion, int maxFeatures, int startIndex, List<String> attributeNames) throws IOException,
			GeomajasException {
		List<org.geomajas.gwt2.client.map.attribute.AttributeDescriptor> descriptors = 
				new ArrayList<org.geomajas.gwt2.client.map.attribute.AttributeDescriptor>();
		for (AttributeDescriptor attributeDescriptor : featureSource.getSchema()
				.getAttributeDescriptors()) {
			descriptors.add(createDescriptor(attributeDescriptor));
		}
		Query query = command.createQuery(source.getName().getLocalPart(), descriptors, criterion, maxFeatures, startIndex, attributeNames, "EPSG:4326");
		return command.convertFeatures(featureSource.getFeatures(query), 1000, 100);
	}

	public void assertEquals(SimpleFeature feature, Feature dto) {
		for (Property prop : feature.getProperties()) {
			String name = prop.getName().getLocalPart();
			if (name != feature.getDefaultGeometryProperty().getName().getLocalPart()) {
				Object value = prop.getValue();
				Object dtoValue = dto.getAttributes().get(name).getValue();
				if (value != null) {
					if (value instanceof Double) {
						Assert.assertEquals((Double) value, (Double) dtoValue, 0.0001);
					} else if (value instanceof BigDecimal) {
						// big decimal is handled as double (probably because Geomajas does not have primitive type for it ?)
						Assert.assertEquals(((BigDecimal) value).doubleValue(), (Double) dtoValue, 0.0001);
					} else {
						Assert.assertEquals(value, dtoValue);
					}
				} else {
					Assert.assertNull(dtoValue);
				}
			} else {
				Object value = prop.getValue();
				Object dtoValue = dto.getGeometry();
				if (value != null) {
					WKTReader reader = new WKTReader();
					try {
						com.vividsolutions.jts.geom.Geometry other = reader.read(WktService.toWkt((Geometry) dtoValue));
						Assert.assertTrue("geometries differ",
								other.equalsExact((com.vividsolutions.jts.geom.Geometry) value, 0.0001));
					} catch (Exception e) {
						Assert.fail();
					}
				} else {
					Assert.assertNull(dtoValue);
				}
			}
		}
	}
	
	private org.geomajas.gwt2.client.map.attribute.AttributeDescriptor createDescriptor(
			AttributeDescriptor attributeDescriptor) throws IOException {
		Class<?> binding = attributeDescriptor.getType().getBinding();
		if (binding == null) {
			throw new IOException("No attribute binding found...");
		}
		String name = attributeDescriptor.getLocalName();
		AttributeDescriptorImpl attributeInfo;
		if (Integer.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(
					new PrimitiveAttributeTypeImpl(PrimitiveType.INTEGER), name);
		} else if (Float.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(
					new PrimitiveAttributeTypeImpl(PrimitiveType.FLOAT), name);
		} else if (Double.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(
					new PrimitiveAttributeTypeImpl(PrimitiveType.DOUBLE), name);
		} else if (BigDecimal.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(
					new PrimitiveAttributeTypeImpl(PrimitiveType.DOUBLE), name);
		} else if (Boolean.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(
					new PrimitiveAttributeTypeImpl(PrimitiveType.BOOLEAN), name);
		} else if (Date.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(
					new PrimitiveAttributeTypeImpl(PrimitiveType.DATE), name);
		} else if (Point.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(
					new GeometryAttributeTypeImpl(GeometryType.POINT), name);
		} else if (LineString.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(
					new GeometryAttributeTypeImpl(GeometryType.LINESTRING),
					name);
		} else if (LinearRing.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(
					new GeometryAttributeTypeImpl(GeometryType.LINEARRING),
					name);
		} else if (Polygon.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(
					new GeometryAttributeTypeImpl(GeometryType.POLYGON), name);
		} else if (MultiPoint.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(
					new GeometryAttributeTypeImpl(GeometryType.MULTIPOINT),
					name);
		} else if (MultiLineString.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(
					new GeometryAttributeTypeImpl(GeometryType.MULTILINESTRING),
					name);
		} else if (MultiPolygon.class.equals(binding)) {
			attributeInfo = new AttributeDescriptorImpl(
					new GeometryAttributeTypeImpl(GeometryType.MULTIPOLYGON),
					name);
		} else {
			attributeInfo = new AttributeDescriptorImpl(
					new PrimitiveAttributeTypeImpl(PrimitiveType.STRING), name);
		}
		return attributeInfo;
	}

	@After
	public void after() throws Exception {
		server.stop();
	}

}
