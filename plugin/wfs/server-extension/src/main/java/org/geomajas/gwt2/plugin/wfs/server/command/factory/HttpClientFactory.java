package org.geomajas.gwt2.plugin.wfs.server.command.factory;

import org.geotools.data.ows.HTTPClient;


public interface HttpClientFactory {
	
	HTTPClient getClientForUrl(String url);
}
