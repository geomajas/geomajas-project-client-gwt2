package org.geomajas.gwt2.plugin.wfs.server.command;

import junit.framework.Assert;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.geomajas.global.GeomajasException;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.client.map.attribute.GeometryAttributeType;
import org.geomajas.gwt2.client.map.attribute.GeometryType;
import org.geomajas.gwt2.client.map.attribute.PrimitiveAttributeType;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsDescribeFeatureTypeRequest;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsDescribeFeatureTypeResponse;
import org.geomajas.gwt2.plugin.wfs.server.dto.WfsFeatureTypeDescriptionDto;
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

	private WfsServlet servlet;

	@Before
	public void before() throws Exception {
		server = new Server(8080);
		servlet = new WfsServlet();
		ServletContextHandler servletContextHandler = new ServletContextHandler();
		servletContextHandler.setContextPath("/");
		servletContextHandler.addServlet(new ServletHolder(servlet),"/wfs/*");
		server.setHandler(servletContextHandler);
		server.start();
	}

	@Test
	public void integrationTest() throws GeomajasException {
		WfsDescribeFeatureTypeRequest request = new WfsDescribeFeatureTypeRequest("http://127.0.0.1:8080/wfs",
				"dov-pub-bodem:Bodemassociatiekaart");
		WfsDescribeFeatureTypeResponse response = new WfsDescribeFeatureTypeResponse();
		command.execute(request, response);
		WfsFeatureTypeDescriptionDto type = response.getFeatureTypeDescription();
		Assert.assertEquals(5, type.getAttributeDescriptors().size());
		AttributeDescriptor descriptor = type.getAttributeDescriptors().get(0);
		Assert.assertEquals("uidn", descriptor.getName());
		Assert.assertTrue(descriptor.getType() instanceof PrimitiveAttributeType);
		Assert.assertEquals(Double.class, ((PrimitiveAttributeType) descriptor.getType()).getBinding());

		descriptor = type.getAttributeDescriptors().get(1);
		Assert.assertEquals("oidn", descriptor.getName());
		Assert.assertTrue(descriptor.getType() instanceof PrimitiveAttributeType);
		Assert.assertEquals(Double.class, ((PrimitiveAttributeType) descriptor.getType()).getBinding());

		descriptor = type.getAttributeDescriptors().get(2);
		Assert.assertEquals("associatie", descriptor.getName());
		Assert.assertTrue(descriptor.getType() instanceof PrimitiveAttributeType);
		Assert.assertEquals(Integer.class, ((PrimitiveAttributeType) descriptor.getType()).getBinding());

		descriptor = type.getAttributeDescriptors().get(3);
		Assert.assertEquals("omschr", descriptor.getName());
		Assert.assertTrue(descriptor.getType() instanceof PrimitiveAttributeType);
		Assert.assertEquals(String.class, ((PrimitiveAttributeType) descriptor.getType()).getBinding());

		descriptor = type.getAttributeDescriptors().get(4);
		Assert.assertEquals("shape", descriptor.getName());
		Assert.assertTrue(descriptor.getType() instanceof GeometryAttributeType);
		Assert.assertEquals(GeometryType.MULTIPOLYGON, ((GeometryAttributeType) descriptor.getType()).getGeometryType());

	}

	@After
	public void after() throws Exception {
		server.stop();
	}

}
