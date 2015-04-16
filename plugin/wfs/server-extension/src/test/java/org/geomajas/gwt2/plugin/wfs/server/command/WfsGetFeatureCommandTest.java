package org.geomajas.gwt2.plugin.wfs.server.command;

import junit.framework.Assert;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.geomajas.gwt2.plugin.wfs.client.query.dto.FidCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsGetFeatureRequest;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsGetFeatureResponse;
import org.geotools.filter.text.ecql.ECQL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "commandContext.xml" })
public class WfsGetFeatureCommandTest {

	private Server server;

	@Autowired
	private WfsGetFeatureCommand command;

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

	@After
	public void after() throws Exception {
		server.stop();
	}

}
