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
package org.geomajas.gwt2.plugin.wms.server.command.factory.impl;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.SystemDefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.CoreConnectionPNames;
import org.geomajas.gwt2.plugin.wms.server.command.factory.WmsHttpClientFactory;

/**
 * Default implementation of {@link WmsHttpClientFactory}.
 * 
 * @author Jan De Moerloose
 *
 */
public class DefaultWmsHttpClientFactory implements WmsHttpClientFactory {

	private DefaultHttpClient client;

	public DefaultWmsHttpClientFactory() {
		final PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
		cm.setMaxTotal(100);
		cm.setDefaultMaxPerRoute(100);
		client = new SystemDefaultHttpClient() {

			@Override
			protected ClientConnectionManager createClientConnectionManager() {
				return cm;
			}

		};
		client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
		client.getParams().setParameter(CoreConnectionPNames.TCP_NODELAY, true);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
	}

	@Override
	public HttpClient create(String sourceUrl) {
		return client;
	}

	@Override
	public URL getTargetUrl(String sourceUrl) {
		try {
			return new URL(sourceUrl);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Malformed WFS source url", e);
		}
	}

}
