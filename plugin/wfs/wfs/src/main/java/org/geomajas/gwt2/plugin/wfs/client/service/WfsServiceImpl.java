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

package org.geomajas.gwt2.plugin.wfs.client.service;

import org.apache.xml.utils.UnImplNode;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureCollectionInfo;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureTypeDescriptionInfo;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsGetCapabilitiesInfo;
import org.geomajas.gwt2.plugin.wfs.client.protocol.v_1_0_0.WfsGetCapabilitiesInfo100;
import org.geomajas.gwt2.plugin.wfs.client.query.dto.QueryDto;
import org.geomajas.gwt2.plugin.wfs.client.service.WfsService.WfsVersion;

import com.google.gwt.core.client.Callback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

/**
 * Default implementation of the {@link WfsService}.
 * 
 * @author Jan De Moerloose
 */
public class WfsServiceImpl implements WfsService {

	protected WfsUrlTransformer urlTransformer;

	protected RequestBuilderFactory requestBuilderFactory;

	protected UrlEncoder urlEncoder;

	// ------------------------------------------------------------------------
	// WmsService implementation:
	// ------------------------------------------------------------------------

	public WfsServiceImpl() {
		setRequestBuilderFactory(new RequestBuilderFactory() {

			@Override
			public RequestBuilder create(Method method, String url) {
				return new RequestBuilder(method, url);
			}
		});
		setUrlEncoder(new UrlEncoder() {

			@Override
			public String encodeUrl(String url) {
				return URL.encode(url);
			}
		});
	}

	@Override
	public void getCapabilities(final WfsVersion version, String baseUrl,
			final Callback<WfsGetCapabilitiesInfo, String> callback) {
		String url = getCapabilitiesUrl(baseUrl, version);
		RequestBuilder builder = requestBuilderFactory.create(RequestBuilder.GET, url);
		builder.setHeader("Cache-Control", "no-cache");
		builder.setHeader("Pragma", "no-cache");
		try {
			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable e) {
					callback.onFailure(e.getMessage());
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						try {
							Document messageDom = XMLParser.parse(response.getText());
							WfsGetCapabilitiesInfo capabilities;
							switch (version) {
								case V1_0_0:
									capabilities = new WfsGetCapabilitiesInfo100(messageDom.getDocumentElement());
									break;
								default:
									callback.onFailure("Unsupported version");
									return;
							}
							callback.onSuccess(capabilities);
						} catch (Throwable t) {
							t.printStackTrace();
							callback.onFailure(t.getMessage());
						}
					} else {
						callback.onFailure(response.getText());
					}
				}
			});
		} catch (RequestException e) {
			// Couldn't connect to server
			callback.onFailure(e.getMessage());
		}
	}
	

	@Override
	public void describeFeatureType(WfsVersion version, String baseUrl, String typeName,
			Callback<WfsFeatureTypeDescriptionInfo, String> callback) {
		throw new UnsupportedOperationException("This method is only supported by the WFS server extension");		
	}

	// ------------------------------------------------------------------------
	// Proxy options:
	// ------------------------------------------------------------------------

	@Override
	public void getFeatures(WfsVersion version, String baseUrl, String typeName, QueryDto query,
			Callback<WfsFeatureCollectionInfo, String> callback) {
		throw new UnsupportedOperationException("This method is only supported by the WFS server extension");		
	}

	@Override
	public void setWfsUrlTransformer(WfsUrlTransformer urlTransformer) {
		this.urlTransformer = urlTransformer;
	}

	@Override
	public WfsUrlTransformer getWfsUrlTransformer() {
		return urlTransformer;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------
	protected void setRequestBuilderFactory(RequestBuilderFactory requestBuilderFactory) {
		this.requestBuilderFactory = requestBuilderFactory;
	}

	protected void setUrlEncoder(UrlEncoder urlEncoder) {
		this.urlEncoder = urlEncoder;
	}

	protected String finishUrl(WfsRequest request, StringBuilder builder) {
		String url = builder.toString();
		if (urlTransformer != null) {
			url = urlTransformer.transform(request, url);
		}
		return urlEncoder.encodeUrl(url);
	}

	protected String getCapabilitiesUrl(String baseUrl, WfsVersion version) {
		StringBuilder url = new StringBuilder(baseUrl);

		// Parameter: Service
		int pos = url.lastIndexOf("?");
		if (pos > 0) {
			url.append("&service=WFS");
		} else {
			url.append("?service=WFS");
		}

		// Parameter: Version
		url.append("&version=");
		url.append(version.toString());

		// Parameter: request type
		url.append("&request=GetCapabilities");

		return finishUrl(WfsRequest.GETCAPABILITIES, url);
	}

	// ------------------------------------------------------------------------
	// Dependencies for unit testing without GWT:
	// ------------------------------------------------------------------------

	/**
	 * factory for {@link RequestBuilder}.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface RequestBuilderFactory {

		RequestBuilder create(Method method, String url);

	}

	/**
	 * Coordinate formatter for bbox.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface CoordinateFormatter {

		String format(double number);

	}

	/**
	 * URL encoder.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface UrlEncoder {

		String encodeUrl(String url);
	}

}
