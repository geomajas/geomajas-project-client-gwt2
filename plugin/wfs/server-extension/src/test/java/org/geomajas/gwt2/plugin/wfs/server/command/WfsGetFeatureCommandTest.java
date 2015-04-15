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
import org.geomajas.gwt2.plugin.wfs.client.query.dto.FidCriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsGetFeatureRequest;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsGetFeatureResponse;
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
	public void testBodem() throws Exception {
		WfsGetFeatureRequest request = new WfsGetFeatureRequest();
		request.setBaseUrl("http://127.0.0.1:8080/wfs");
		request.setTypeName("dov-pub-bodem:Bodemassociatiekaart");
		request.setCriterion(new FidCriterionDto(new String[] { "Bodemassociatiekaart.1", "Bodemassociatiekaart.2" }));
		request.setCrs("EPSG:31370");
		WfsGetFeatureResponse response = new WfsGetFeatureResponse();
		command.execute(request, response);
		Assert.assertEquals(2, response.getFeatures().size());
	}

	@After
	public void after() throws Exception {
		server.stop();
	}

}
