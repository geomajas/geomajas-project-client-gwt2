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
package org.geomajas.gwt2.plugin.print.client.event;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.gwt2.plugin.print.client.util.PrintConfiguration;

/**
 * Info object, containing info of the successful end of a print request. This info consists of the HTTP url and request
 * parameters that should be called to actually download the print. In the case of a synchronous print service, the HTTP
 * method is POST (and the parameters contain the jsonized template), in the case of an asynchronous print service the
 * HTTP method is GET.
 * 
 * @author Jan Venstermans
 * @author Jan De Moerloose
 * 
 */
public class PrintFinishedInfo {

	/**
	 * HTTP method for the url.
	 * 
	 * @author Jan De Moerloose
	 *
	 */
	public enum HttpMethod {
		GET, POST
	}

	private String url;

	private HttpMethod method = HttpMethod.GET;

	private PrintConfiguration.PostPrintAction postPrintAction;

	/**
	 * A map of POST/GET parameters (NOT encoded yet !!!)
	 */
	private Map<String, String> params = new HashMap<String, String>();

	/**
	 * Get the encoded url.
	 * 
	 * @return the url
	 * @deprecated renamed to {@link #getUrl()}
	 */
	@Deprecated
	public String getEncodedUrl() {
		return url;
	}

	/**
	 * Set the encoded url.
	 * 
	 * @param the url
	 * 
	 * @deprecated renamed to {@link #setUrl(String)}
	 */
	@Deprecated
	public void setEncodedUrl(String url) {
		this.url = url;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void addParam(String name, String value) {
		params.put(name, value);
	}

	public PrintConfiguration.PostPrintAction getPostPrintAction() {
		return postPrintAction;
	}

	public void setPostPrintAction(PrintConfiguration.PostPrintAction postPrintAction) {
		this.postPrintAction = postPrintAction;
	}
}
