/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.gwt2.plugin.wfs.server.command.factory;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.geomajas.gwt2.plugin.wfs.server.dto.WfsVersionDto;

/**
 * Utility class to create a WFS URL.
 * 
 * @author Jan De Moerloose
 *
 */
public final class URLBuilder {

	private URLBuilder() {

	}

	public static URL createWfsURL(URL baseUrl, WfsVersionDto version, String request) throws URISyntaxException,
			MalformedURLException {
		List<NameValuePair> params = new ArrayList<NameValuePair>(URLEncodedUtils.parse(baseUrl.toURI(), "UTF-8"));
		// we override some query params
		for (int i = params.size() - 1; i >= 0; i--) {
			NameValuePair pair = params.get(i);
			if ("service".equalsIgnoreCase(pair.getName()) || "version".equalsIgnoreCase(pair.getName())
					|| "request".equalsIgnoreCase(pair.getName())) {
				params.remove(i);
			}
		}
		params.add(new BasicNameValuePair("service", "WFS"));
		if(version != null) {
			params.add(new BasicNameValuePair("version", version.toString()));
		}
		params.add(new BasicNameValuePair("request", request));
		String protocol = baseUrl.getProtocol();
		String authority = baseUrl.getAuthority();
		String path = baseUrl.getPath();
		String query = URLEncodedUtils.format(params, "UTF-8");
		URL url = new URL(protocol + "://" + authority + path + "?" + query);
		return url;
	}
}
