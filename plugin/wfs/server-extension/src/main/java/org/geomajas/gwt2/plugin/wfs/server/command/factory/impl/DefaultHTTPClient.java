package org.geomajas.gwt2.plugin.wfs.server.command.factory.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.CoreConnectionPNames;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.wfs.MultithreadedHttpClient;
import org.geotools.util.logging.Logging;

public class DefaultHTTPClient implements HTTPClient {

	private static final Logger LOGGER = Logging.getLogger(MultithreadedHttpClient.class);

	private DefaultHttpClient client;

	private String user;

	private String password;

	private boolean tryGzip;

	private PoolingClientConnectionManager cm;

	public DefaultHTTPClient() {
		cm = new PoolingClientConnectionManager();
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
			LOGGER.fine("Found 'http.proxyHost' Java System property. Using it as proxy server. Port: " + proxyPort);
			HttpHost proxy = new HttpHost(proxyHost, proxyPort);
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}

		final String proxyUser = System.getProperty("http.proxyUser");
		final String proxyPassword = System.getProperty("http.proxyPassword");
		if (proxyUser != null) {
			if (proxyPassword == null || proxyPassword.length() == 0) {
				LOGGER.warning("System property http.proxyUser provided but http.proxyPassword "
						+ "not provided or empty. Proxy auth credentials will be passed as is anyway.");
			} else {
				LOGGER.fine("System property http.proxyUser and http.proxyPassword found,"
						+ " setting proxy auth credentials");
			}

			client.getCredentialsProvider().setCredentials(new org.apache.http.auth.AuthScope(proxyHost, proxyPort),
					new UsernamePasswordCredentials(proxyUser, proxyPassword));

		}
	}

	@Override
	public HTTPResponse post(final URL url, final InputStream postContent, final String postContentType)
			throws IOException {

		HttpPost post = new HttpPost(url.toExternalForm());
		if (postContentType != null) {
			post.addHeader("Content-type", postContentType);
		}
		byte[] byteContent = IOUtils.toByteArray(postContent);
		HttpEntity requestEntity = new ByteArrayEntity(byteContent);
		post.setEntity(requestEntity);

		HttpResponse response = client.execute(post);
		if (200 != response.getStatusLine().getStatusCode()) {
			post.releaseConnection();
			throw new IOException("Server returned " + response.getStatusLine() + " for URL " + url.toExternalForm());
		}

		return new HttpMethodResponse(post, response);
	}

	@Override
	public HTTPResponse get(final URL url) throws IOException {

		HttpGet get = new HttpGet(url.toExternalForm());

		HttpResponse response = client.execute(get);
		if (200 != response.getStatusLine().getStatusCode()) {
			get.releaseConnection();
			throw new IOException("Server returned " + response.getStatusLine() + " for URL " + url.toExternalForm());
		}

		return new HttpMethodResponse(get, response);
	}

	@Override
	public String getUser() {
		return user;
	}

	@Override
	public void setUser(String user) {
		this.user = user;
		resetCredentials();
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
		resetCredentials();
	}

	private void resetCredentials() {
		client.getCredentialsProvider().clear();
		HttpHost proxy = (HttpHost) client.getParams().getParameter(ConnRoutePNames.DEFAULT_PROXY);
		if (user != null && password != null && proxy != null) {
			client.getCredentialsProvider().setCredentials(
					new org.apache.http.auth.AuthScope(proxy.getHostName(), proxy.getPort()),
					new UsernamePasswordCredentials(user, password));
		}
	}

	@Override
	public int getConnectTimeout() {
		return client.getParams().getIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 0) / 1000;
	}

	@Override
	public void setConnectTimeout(int connectTimeout) {
		client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectTimeout * 1000);
	}

	@Override
	public int getReadTimeout() {
		return client.getParams().getIntParameter(CoreConnectionPNames.SO_TIMEOUT, 0) / 1000;
	}

	@Override
	public void setReadTimeout(int readTimeout) {
		client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, readTimeout * 1000);
	}

	public int getMaxConnections() {
		return cm.getMaxTotal();
	}

	public void setMaxConnections(final int maxConnections) {
		cm.setMaxTotal(maxConnections);
		cm.setDefaultMaxPerRoute(maxConnections);
	}

	private static class HttpMethodResponse implements HTTPResponse {

		private HttpRequestBase request;

		private HttpResponse response;

		private InputStream responseBodyAsStream;

		public HttpMethodResponse(final HttpRequestBase request, final HttpResponse response) {
			this.request = request;
			this.response = response;
		}

		@Override
		public void dispose() {
			if (responseBodyAsStream != null) {
				try {
					responseBodyAsStream.close();
				} catch (IOException e) {
					// ignore
				}
			}
			if (request != null) {
				request.releaseConnection();
				request = null;
			}
		}

		@Override
		public String getContentType() {
			return getResponseHeader("Content-Type");
		}

		@Override
		public String getResponseHeader(final String headerName) {
			Header responseHeader = response.getFirstHeader(headerName);
			return responseHeader == null ? null : responseHeader.getValue();
		}

		@Override
		public InputStream getResponseStream() throws IOException {
			if (responseBodyAsStream == null) {
				responseBodyAsStream = response.getEntity().getContent();
			}
			return responseBodyAsStream;
		}

		@Override
		public String getResponseCharset() {
			ContentType contentType = ContentType.getOrDefault(response.getEntity());
			Charset charSet = contentType.getCharset();
			return charSet.name();
		}

	}

	@Override
	public void setTryGzip(boolean tryGZIP) {
		this.tryGzip = tryGZIP;
	}

	@Override
	public boolean isTryGzip() {
		return tryGzip;
	}
}