package org.geomajas.gwt2.plugin.wfs.server.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;


public class WfsHandler extends AbstractHandler {

	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String requestName = request.getParameter("REQUEST");
		if("GetCapabilities".equalsIgnoreCase(requestName)) {
			System.out.println("GetCapabilities");
			response.setContentType("application/xml");
			response.setStatus(HttpServletResponse.SC_OK);
			baseRequest.setHandled(true);
			IOUtils.copy(getClass().getResourceAsStream("capabilities_1_0_0.xml"), response.getWriter());
		} else if("DescribeFeatureType".equalsIgnoreCase(requestName)) {
			System.out.println("DescribeFeatureType");
			response.setContentType("text/xml");
			response.setStatus(HttpServletResponse.SC_OK);
			baseRequest.setHandled(true);
			IOUtils.copy(getClass().getResourceAsStream("describeFeatureType.xml"), response.getWriter());
		} else if("GetFeature".equalsIgnoreCase(requestName)) {
			System.out.println("GetFeature");
			response.setContentType("text/xml; subtype=gml/2.1.2; charset=UTF-8");
			response.setStatus(HttpServletResponse.SC_OK);
			baseRequest.setHandled(true);
			IOUtils.copy(getClass().getResourceAsStream("getFeature.xml"), response.getWriter());
		} else {
			System.out.println("missing "+request.getPathInfo());
		}
	}

}