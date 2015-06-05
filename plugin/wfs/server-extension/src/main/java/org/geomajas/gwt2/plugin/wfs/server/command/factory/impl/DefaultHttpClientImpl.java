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
package org.geomajas.gwt2.plugin.wfs.server.command.factory.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.SystemDefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.CoreConnectionPNames;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.HTTPResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper of Apache 4 client for geotools.
 * 
 * @author Jan De Moerloose
 *
 */
public class DefaultHttpClientImpl implements HTTPClient {

	private final Logger log = LoggerFactory.getLogger(DefaultHttpClientImpl.class);

	private HttpClient client;

	private String user;

	private String password;

	private boolean tryGzip;

	private PoolingClientConnectionManager cm;

	private boolean readOnly;

	public DefaultHttpClientImpl(HttpClient client) {
		this.client = client;
		readOnly = true;
	}

	public DefaultHttpClientImpl() {
		cm = new PoolingClientConnectionManager();
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
	public HTTPResponse post(final URL url, final InputStream postContent, final String postContentType)
			throws IOException {

		HttpPost post = new HttpPost(url.toExternalForm());
		if (postContentType != null) {
			post.addHeader("Content-type", postContentType);
		}
		byte[] byteContent = IOUtils.toByteArray(postContent);
		if (log.isDebugEnabled()) {
			log.debug(url.toExternalForm());
			log.debug(new String(byteContent, "UTF-8"));
		}
		HttpEntity requestEntity = new ByteArrayEntity(byteContent);
		post.setEntity(requestEntity);

		HttpResponse response = client.execute(post);
		if (200 != response.getStatusLine().getStatusCode()) {
			post.releaseConnection();
			log.error("Server returned " + response.getStatusLine() + " for URL " + url.toExternalForm());
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
			log.error("Server returned " + response.getStatusLine() + " for URL " + url.toExternalForm());
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
		if (!isReadOnly()) {
			resetCredentials();
		}
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
		if (!isReadOnly()) {
			resetCredentials();
		}
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	private void resetCredentials() {
		((DefaultHttpClient) client).getCredentialsProvider().clear();
		HttpHost proxy = (HttpHost) client.getParams().getParameter(ConnRoutePNames.DEFAULT_PROXY);
		if (user != null && password != null && proxy != null) {
			((DefaultHttpClient) client).getCredentialsProvider().setCredentials(
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
		if (!isReadOnly()) {
			client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectTimeout * 1000);
		}
	}

	@Override
	public int getReadTimeout() {
		return client.getParams().getIntParameter(CoreConnectionPNames.SO_TIMEOUT, 0) / 1000;
	}

	@Override
	public void setReadTimeout(int readTimeout) {
		if (!isReadOnly()) {
			client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, readTimeout * 1000);
		}
	}

	/**
	 * Wraps the response.
	 * 
	 * @author Jan De Moerloose
	 *
	 */
	private class HttpMethodResponse implements HTTPResponse {

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