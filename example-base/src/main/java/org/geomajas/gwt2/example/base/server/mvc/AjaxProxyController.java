/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.example.base.server.mvc;

import org.apache.commons.lang.NotImplementedException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Enumeration;

/**
 * Controller that proxies Ajax requests to a specified URL.
 * 
 * @author Pieter De Graef
 */
@Controller("/proxy/**")
public class AjaxProxyController {

	@RequestMapping(value = "/")
	public final void proxyAjaxCall(@RequestParam(required = true, value = "url") String url,
			HttpServletRequest request, HttpServletResponse response) throws IOException {

		// URL needs to be url decoded
		url = URLDecoder.decode(url, "utf-8");

		HttpClient client = new DefaultHttpClient();
		try {
			HttpRequestBase proxyRequest;

			// Split this according to the type of request
			if ("GET".equals(request.getMethod())) {
				Enumeration<?> paramNames = request.getParameterNames();
				while (paramNames.hasMoreElements()) {
					String paramName = (String) paramNames.nextElement();
					if (!paramName.equalsIgnoreCase("url")) {
						url = addParam(url, paramName, request.getParameter(paramName));
					}
				}

				proxyRequest = new HttpGet(url);
			} else if ("POST".equals(request.getMethod())) {
				proxyRequest = new HttpPost(url);

				// Set any eventual parameters that came with our original
				HttpParams params = new BasicHttpParams();
				Enumeration<?> paramNames = request.getParameterNames();
				while (paramNames.hasMoreElements()) {
					String paramName = (String) paramNames.nextElement();
					if (!"url".equalsIgnoreCase("url")) {
						params.setParameter(paramName, request.getParameter(paramName));
					}
				}
				proxyRequest.setParams(params);
			} else {
				throw new NotImplementedException("This proxy only supports GET and POST methods.");
			}

			// Execute the method
			HttpResponse proxyResponse = client.execute(proxyRequest);

			// Set the content type, as it comes from the server
			Header[] headers = proxyResponse.getAllHeaders();
			for (Header header : headers) {
				if ("Content-Type".equalsIgnoreCase(header.getName())) {
					response.setContentType(header.getValue());
				}
			}

			// Write the body, flush and close
			proxyResponse.getEntity().writeTo(response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	private String addParam(String url, String key, String value) {
		if (url.contains("?")) {
			url += "&";
		} else {
			url += "?";
		}
		url = url + key + "=" + value;
		return url;
	}
}