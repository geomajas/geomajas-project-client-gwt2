/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.wms.client.service;

import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.map.layer.LegendConfig;
import org.geomajas.plugin.wms.client.capabilities.WmsGetCapabilitiesInfo;
import org.geomajas.plugin.wms.client.capabilities.v1_1_1.WmsGetCapabilitiesInfo111;
import org.geomajas.plugin.wms.client.capabilities.v1_3_0.WmsGetCapabilitiesInfo130;
import org.geomajas.plugin.wms.client.layer.config.WmsLayerConfiguration;
import org.geomajas.plugin.wms.client.layer.config.WmsServiceVendor;

import com.google.gwt.core.client.Callback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

/**
 * Default implementation of the {@link WmsService}.
 * 
 * @author Pieter De Graef
 * @author An Buyle
 */
public class WmsServiceImpl implements WmsService {

	private static final NumberFormat NUMBERFORMAT = NumberFormat.getFormat("#0.0#");

	private static final String WMS_LEGEND_OPTIONS_START = "&legend_options=";

	private static final int LEGEND_DPI = 91;

	protected WmsUrlTransformer urlTransformer;

	// ------------------------------------------------------------------------
	// WmsService implementation:
	// ------------------------------------------------------------------------

	@Override
	public void getCapabilities(String baseUrl, final WmsVersion version,
			final Callback<WmsGetCapabilitiesInfo, String> callback) {
		String url = getCapabilitiesUrl(baseUrl, version);
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		try {
			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable e) {
					callback.onFailure(e.getMessage());
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						Document messageDom = XMLParser.parse(response.getText());
						WmsGetCapabilitiesInfo capabilities;
						switch (version) {
							case V1_1_1:
								capabilities = new WmsGetCapabilitiesInfo111(messageDom.getDocumentElement());
								break;
							case V1_3_0:
								capabilities = new WmsGetCapabilitiesInfo130(messageDom.getDocumentElement());
								break;
							default:
								callback.onFailure("Unsupported version");
								return;
						}
						callback.onSuccess(capabilities);
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
	public String getMapUrl(WmsLayerConfiguration wmsConfig, String crs, Bbox worldBounds, int imageWidth,
			int imageHeight) {
		StringBuilder url = getBaseUrlBuilder(wmsConfig);

		// Add the base parameters needed for getMap:
		addBaseParameters(url, wmsConfig, crs, worldBounds, imageWidth, imageHeight);

		// Parameter: request type
		url.append("&request=GetMap");

		return finishUrl(WmsRequest.GETMAP, url);
	}

	// ------------------------------------------------------------------------
	// WMS GetLegendGraphic methods:
	// ------------------------------------------------------------------------

	@Override
	public String getLegendGraphicUrl(WmsLayerConfiguration wmsConfig, LegendConfig legendConfig) {
		StringBuilder url = getBaseUrlBuilder(wmsConfig);

		// Parameter: service
		int pos = url.lastIndexOf("?");
		if (pos > 0) {
			url.append("&service=WMS");
		} else {
			url.append("?service=WMS");
		}

		// Parameter: layers
		url.append("&layer=");
		url.append(wmsConfig.getLayers()); // No URL.encode here!

		// Parameter: request type
		url.append("&request=GetLegendGraphic");

		// Parameter: styles
		url.append("&STYLE=");
		url.append(wmsConfig.getStyles());

		// Parameter: format
		url.append("&format=");
		String format = legendConfig.getImageFormat();
		if (format == null) {
			url.append("image/png");
		} else if (!format.startsWith("image/")) {
			url.append("image/");
			url.append(format.toLowerCase());
		} else {
			url.append(format.toLowerCase());
		}

		// Parameter: width
		url.append("&width=");
		url.append(legendConfig.getIconWidth());

		// Parameter: height
		url.append("&height=");
		url.append(legendConfig.getIconHeight());

		// Parameter: transparent
		url.append("&transparent=true");

		// Check for specific vendor options:
		if (WmsServiceVendor.GEOSERVER_WMS.equals(wmsConfig.getWmsServiceVendor())) {
			url.append(WMS_LEGEND_OPTIONS_START);
			if (null != legendConfig.getFontStyle().getFamily()) {
				url.append("fontName:");
				url.append(legendConfig.getFontStyle().getFamily());
				url.append(";");
			}
			url.append("fontAntiAliasing:true;");

			if (null != legendConfig.getFontStyle().getColor()) {
				url.append("fontColor:");
				url.append(legendConfig.getFontStyle().getColor().replace("#", "0x"));
				url.append(";");
			}
			if (legendConfig.getFontStyle().getSize() > 0) {
				url.append("fontSize:");
				url.append(legendConfig.getFontStyle().getSize());
				url.append(";");
			}

			// TODO:
			// int dpi = legendConfig.getDpi();
			// if (dpi <= 0) {
			// dpi = LEGEND_DPI;
			// }
			int dpi = LEGEND_DPI;
			url.append("bgColor:0xFFFFFF;dpi:" + dpi);
		}

		return finishUrl(WmsRequest.GETLEGENDGRAPHIC, url);
	}

	@Override
	public String getLegendGraphicUrl(WmsLayerConfiguration wmsConfig) {
		return getLegendGraphicUrl(wmsConfig, wmsConfig.getLegendConfig());
	}

	// ------------------------------------------------------------------------
	// Proxy options:
	// ------------------------------------------------------------------------

	@Override
	public void setWmsUrlTransformer(WmsUrlTransformer urlTransformer) {
		this.urlTransformer = urlTransformer;
	}

	@Override
	public WmsUrlTransformer getWmsUrlTransformer() {
		return urlTransformer;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	protected StringBuilder getBaseUrlBuilder(WmsLayerConfiguration config) {
		return new StringBuilder(config.getBaseUrl());
	}

	protected String finishUrl(WmsRequest request, StringBuilder builder) {
		String url = builder.toString();
		if (urlTransformer != null) {
			url = urlTransformer.transform(request, url);
		}
		return URL.encode(url);
	}

	protected StringBuilder addBaseParameters(StringBuilder url, WmsLayerConfiguration config, String crs,
			Bbox worldBounds, int imageWidth, int imageHeight) {
		// Parameter: service
		int pos = url.lastIndexOf("?");
		if (pos > 0) {
			url.append("&service=WMS");
		} else {
			url.append("?service=WMS");
		}

		// Parameter: layers
		url.append("&layers=");
		url.append(config.getLayers()); // No URL.encode here, performed in finishUrl

		// Parameter: width
		url.append("&width=");
		url.append(Integer.toString(imageWidth));

		// Parameter: height
		url.append("&height=");
		url.append(Integer.toString(imageHeight));

		// Parameter: bbox
		url.append("&bbox=");
		if (useInvertedAxis(config.getVersion(), crs)) {
			// Replace
			url.append(floatToStringWithDecimalPoint((worldBounds.getY())));
			url.append(",");
			url.append(floatToStringWithDecimalPoint(worldBounds.getX()));
			url.append(",");
			url.append(floatToStringWithDecimalPoint(worldBounds.getMaxY()));
			url.append(",");
			url.append(floatToStringWithDecimalPoint(worldBounds.getMaxX()));
		} else {
			url.append(floatToStringWithDecimalPoint(worldBounds.getX()));
			url.append(",");
			url.append(floatToStringWithDecimalPoint(worldBounds.getY()));
			url.append(",");
			url.append(floatToStringWithDecimalPoint(worldBounds.getMaxX()));
			url.append(",");
			url.append(floatToStringWithDecimalPoint(worldBounds.getMaxY()));
		}

		// Parameter: format
		url.append("&format=");
		url.append(config.getFormat());

		// Parameter: version
		url.append("&version=");
		url.append(config.getVersion().toString());

		// Parameter: crs/srs
		switch (config.getVersion()) {
			case V1_1_1:
				url.append("&srs=");
				break;
			case V1_3_0:
			default:
				url.append("&crs=");
				break;
		}
		url.append(crs); // No URL.encode here, performed in finishUrl

		// Parameter: styles
		url.append("&styles=");
		url.append(config.getStyles());

		// Parameter: transparent
		if (config.isTransparent()) {
			url.append("&transparent=");
			url.append(config.isTransparent());
		}

		// Return the URL:
		return url;
	}

	protected static String floatToStringWithDecimalPoint(double number) {
		return NUMBERFORMAT.format(number).replace(",", ".");
	}

	protected String getCapabilitiesUrl(String baseUrl, WmsVersion version) {
		StringBuilder url = new StringBuilder(baseUrl);

		// Parameter: Service
		int pos = url.lastIndexOf("?");
		if (pos > 0) {
			url.append("&service=WMS");
		} else {
			url.append("?service=WMS");
		}

		// Parameter: Version
		url.append("&version=");
		url.append(version.toString());

		// Parameter: request type
		url.append("&request=GetCapabilities");

		return finishUrl(WmsRequest.GETCAPABILITIES, url);
	}

	protected boolean useInvertedAxis(WmsVersion version, String crs) {
		if (WmsVersion.V1_3_0.equals(version) && ("EPSG:4326".equalsIgnoreCase(crs) || 
				"WGS:84".equalsIgnoreCase(crs))) {
			return true;
		}
		return false;
	}
}