package org.geomajas.gwt2.plugin.wfs.server.command;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.global.GeomajasException;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureTypeInfo;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsGetCapabilitiesRequest;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsGetCapabilitiesResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "commandContext.xml" })
public class WfsGetCapabilitiesCommandTest {

	private Server server;

	@Autowired
	private WfsGetCapabilitiesCommand command;

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
		WfsGetCapabilitiesRequest request = new WfsGetCapabilitiesRequest("http://127.0.0.1:8080/wfs");
		WfsGetCapabilitiesResponse response = new WfsGetCapabilitiesResponse();
		command.execute(request, response);
		List<WfsFeatureTypeInfo> featureTypes = response.getGetCapabilitiesDto().getFeatureTypeList().getFeatureTypes();
		Assert.assertEquals(7, featureTypes.size());
		WfsFeatureTypeInfo type = featureTypes.get(0);
		Assert.assertEquals("dov-pub-bodem:Bodemkundig_erfgoed", type.getName());
		Assert.assertEquals("! URL gewijzigd. Bodemkundig erfgoed", type.getTitle());
		Assert.assertEquals("Nieuwe URL: http://127.0.0.1:8080/wfs/geoserver/bodem_varia/bodemkundig_erfgoed", type.getAbstract());
		Assert.assertEquals("EPSG:31370", type.getDefaultCrs());
		Assert.assertEquals(Arrays.asList("Soil", "bodem"), type.getKeywords());
		Assert.assertTrue(BboxService.equals(new Bbox(2.534012015078217, 50.694051222131925, 3.14929925489, 0.67934318463),
				type.getWGS84BoundingBox(), 0.00001));

	}

	@After
	public void after() throws Exception {
		server.stop();
	}

}
