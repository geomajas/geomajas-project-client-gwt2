package org.geomajas.gwt2.plugin.wfs.server.command.factory.impl;

import org.geomajas.gwt2.plugin.wfs.server.command.factory.HttpClientFactory;
import org.geotools.data.ows.HTTPClient;


public class DefaultHttpClientFactory implements HttpClientFactory {
	
	private HTTPClient client = new DefaultHTTPClient();

	@Override
	public HTTPClient getClientForUrl(String url) {
		return client;
	}

}
