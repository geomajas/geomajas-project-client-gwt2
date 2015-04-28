package org.geomajas.gwt2.plugin.wfs.server.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.opengis.filter.Filter;

public class WfsServlet extends HttpServlet {

	private Filter lastFilter;
	private int port;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		handle(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		handle(req, resp);
	}

	public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String requestName = request.getParameter("REQUEST");
		if ("GetCapabilities".equalsIgnoreCase(requestName)) {
			response.setContentType("application/xml");
			response.setStatus(HttpServletResponse.SC_OK);
			filterPortAndCopyToResponse(getClass().getResourceAsStream("capabilities_1_0_0.xml"), response);
		} else if ("DescribeFeatureType".equalsIgnoreCase(requestName)) {
			response.setContentType("text/xml");
			response.setStatus(HttpServletResponse.SC_OK);
			filterPortAndCopyToResponse(getClass().getResourceAsStream("describeFeatureType.xml"), response);
		} else if ("GetFeature".equalsIgnoreCase(requestName)) {
			response.setContentType("text/xml; subtype=gml/2.1.2; charset=UTF-8");
			response.setStatus(HttpServletResponse.SC_OK);
			// create the parser with the filter 1.0 configuration
			Configuration configuration = new org.geotools.filter.v1_0.OGCConfiguration();
			Parser parser = new Parser(configuration);
			try {
				Map map = (Map) parser.parse(request.getInputStream());
				lastFilter = (Filter) ((Map) map.get("Query")).get("Filter");
			} catch (Exception e) {
				e.printStackTrace();
			}
			filterPortAndCopyToResponse(getClass().getResourceAsStream("getFeature.xml"), response);
		}
	}

	private void filterPortAndCopyToResponse(InputStream is, HttpServletResponse response) throws IOException {
		StringWriter sw = new StringWriter();
		IOUtils.copy(is , sw);
		String result = sw.getBuffer().toString().replaceAll("__port__", port+"");
		IOUtils.copy(new StringReader(result), response.getWriter());
	}

	public Filter getLastFilter() {
		return lastFilter;
	}

	public void setPort(int port) {
		this.port = port;
	}

}