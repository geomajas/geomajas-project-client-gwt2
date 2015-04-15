package org.geomajas.gwt2.plugin.wfs.server.command;

import junit.framework.Assert;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.geomajas.global.GeomajasException;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.client.map.attribute.GeometryAttributeType;
import org.geomajas.gwt2.client.map.attribute.GeometryType;
import org.geomajas.gwt2.client.map.attribute.PrimitiveAttributeType;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsDescribeFeatureTypeRequest;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsDescribeFeatureTypeResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "commandContext.xml" })
public class WfsDescribeFeatureTypeCommandTest {

	private Server server;

	@Autowired
	private WfsDescribeFeatureTypeCommand command;

	@Before
	public void before() throws Exception {
		server = new Server(8080);
		ContextHandler contextHandler = new ContextHandler();
		contextHandler.setContextPath("/wfs");
		Handler handler = new WfsHandler();
		contextHandler.setHandler(handler);
		server.setHandler(contextHandler);
		server.start();
	}

	@Test
	public void testBodem() throws GeomajasException {
		WfsDescribeFeatureTypeRequest request = new WfsDescribeFeatureTypeRequest("http://127.0.0.1:8080/wfs",
				"dov-pub-bodem:Bodemassociatiekaart");
		WfsDescribeFeatureTypeResponse response = new WfsDescribeFeatureTypeResponse();
		command.execute(request, response);
		Assert.assertEquals(5, response.getAttributeDescriptors().size());
		AttributeDescriptor descriptor = response.getAttributeDescriptors().get(0);
		Assert.assertEquals("uidn", descriptor.getName());
		Assert.assertTrue(descriptor.getType() instanceof PrimitiveAttributeType);
		Assert.assertEquals(Double.class, ((PrimitiveAttributeType) descriptor.getType()).getBinding());

		descriptor = response.getAttributeDescriptors().get(1);
		Assert.assertEquals("oidn", descriptor.getName());
		Assert.assertTrue(descriptor.getType() instanceof PrimitiveAttributeType);
		Assert.assertEquals(Double.class, ((PrimitiveAttributeType) descriptor.getType()).getBinding());

		descriptor = response.getAttributeDescriptors().get(2);
		Assert.assertEquals("associatie", descriptor.getName());
		Assert.assertTrue(descriptor.getType() instanceof PrimitiveAttributeType);
		Assert.assertEquals(Integer.class, ((PrimitiveAttributeType) descriptor.getType()).getBinding());

		descriptor = response.getAttributeDescriptors().get(3);
		Assert.assertEquals("omschr", descriptor.getName());
		Assert.assertTrue(descriptor.getType() instanceof PrimitiveAttributeType);
		Assert.assertEquals(String.class, ((PrimitiveAttributeType) descriptor.getType()).getBinding());

		descriptor = response.getAttributeDescriptors().get(4);
		Assert.assertEquals("shape", descriptor.getName());
		Assert.assertTrue(descriptor.getType() instanceof GeometryAttributeType);
		Assert.assertEquals(GeometryType.MULTIPOLYGON, ((GeometryAttributeType) descriptor.getType()).getGeometryType());

	}

	@After
	public void after() throws Exception {
		server.stop();
	}

}
