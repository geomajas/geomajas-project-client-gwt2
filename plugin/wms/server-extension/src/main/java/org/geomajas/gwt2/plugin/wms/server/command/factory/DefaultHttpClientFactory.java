package org.geomajas.gwt2.plugin.wms.server.command.factory;

import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.CoreConnectionPNames;
import org.geomajas.gwt2.plugin.wms.server.command.WmsGetFeatureInfoCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link HttpClientFactory}.
 * 
 * @author Jan De Moerloose
 *
 */
public class DefaultHttpClientFactory implements HttpClientFactory {

	private final Logger log = LoggerFactory.getLogger(WmsGetFeatureInfoCommand.class);

	private DefaultHttpClient client;

	public DefaultHttpClientFactory() {
		PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
		cm.setMaxTotal(100);
		cm.setDefaultMaxPerRoute(100);
		client = new DefaultHttpClient(cm);
		client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
		client.getParams().setParameter(CoreConnectionPNames.TCP_NODELAY, true);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
		applySystemProxySettings();
	}

	private void applySystemProxySettings() {
		final String proxyHost = System.getProperty("http.proxyHost");
		final int proxyPort = Integer.parseInt(System.getProperty("http.proxyPort", "80"));
		// String nonProxyHost = System.getProperty("http.nonProxyHosts");

		if (proxyHost != null) {
			log.debug("Found 'http.proxyHost' Java System property. Using it as proxy server. Port: " + proxyPort);
			HttpHost proxy = new HttpHost(proxyHost, proxyPort);
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}

		final String proxyUser = System.getProperty("http.proxyUser");
		final String proxyPassword = System.getProperty("http.proxyPassword");
		if (proxyUser != null) {
			if (proxyPassword == null || proxyPassword.length() == 0) {
				log.warn("System property http.proxyUser provided but http.proxyPassword "
						+ "not provided or empty. Proxy auth credentials will be passed as is anyway.");
			} else {
				log.debug("System property http.proxyUser and http.proxyPassword found,"
						+ " setting proxy auth credentials");
			}

			client.getCredentialsProvider().setCredentials(new org.apache.http.auth.AuthScope(proxyHost, proxyPort),
					new UsernamePasswordCredentials(proxyUser, proxyPassword));

		}
	}

	@Override
	public HttpClient createClientForUrl(URL url) {
		return client;
	}

}
