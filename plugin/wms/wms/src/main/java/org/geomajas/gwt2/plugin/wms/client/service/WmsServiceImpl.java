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

package org.geomajas.gwt2.plugin.wms.client.service;

import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.feature.FeatureCollection;
import org.geomajas.gwt2.client.map.feature.JsonFeatureFactory;
import org.geomajas.gwt2.client.map.layer.FeaturesSupported;
import org.geomajas.gwt2.client.map.layer.LegendConfig;
import org.geomajas.gwt2.client.map.render.TileCode;
import org.geomajas.gwt2.client.service.JsonService;
import org.geomajas.gwt2.client.service.TileService;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsGetCapabilitiesInfo;
import org.geomajas.gwt2.plugin.wms.client.capabilities.v1_1_1.WmsGetCapabilitiesInfo111;
import org.geomajas.gwt2.plugin.wms.client.capabilities.v1_3_0.WmsGetCapabilitiesInfo130;
import org.geomajas.gwt2.plugin.wms.client.describelayer.WmsDescribeLayerInfo;
import org.geomajas.gwt2.plugin.wms.client.describelayer.v1_1_1.WmsDescribeLayerInfo111;
import org.geomajas.gwt2.plugin.wms.client.layer.WmsLayer;
import org.geomajas.gwt2.plugin.wms.client.layer.WmsLayerConfiguration;
import org.geomajas.gwt2.plugin.wms.client.layer.WmsServiceVendor;

import com.google.gwt.core.client.Callback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.json.client.JSONValue;
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

	private static final int DEFAULT_MAX_FEATURES = 20; // Default maximum number of feats returned by GetFeatureInfo

	protected WmsUrlTransformer urlTransformer;

	protected RequestBuilderFactory requestBuilderFactory;

	protected CoordinateFormatter coordinateFormatter;

	protected UrlEncoder urlEncoder;

	protected JsonFeatureFactory jsonFeatureFactory;

	// ------------------------------------------------------------------------
	// WmsService implementation:
	// ------------------------------------------------------------------------

	public WmsServiceImpl() {
		jsonFeatureFactory = new JsonFeatureFactory();
		setRequestBuilderFactory(new RequestBuilderFactory() {

			@Override
			public RequestBuilder create(Method method, String url) {
				return new RequestBuilder(method, url);
			}
		});
		setCoordinateFormatter(new CoordinateFormatter() {

			@Override
			public String format(double number) {
				return NUMBERFORMAT.format(number);
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
	public void getCapabilities(String baseUrl, final WmsVersion version,
			final Callback<WmsGetCapabilitiesInfo, String> callback) {
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
	public void describeLayer(String baseUrl, String layers, final WmsVersion version,
			final Callback<WmsDescribeLayerInfo, String> callback) {
		// looks like geoserver fails for versions other than 1.1.1 ?
		// force 1.1.1 until more info on https://jira.codehaus.org/browse/GEOS-5918
		final WmsVersion localVersion = WmsVersion.V1_1_1;
		String url = describeLayerUrl(baseUrl, layers, localVersion);
		RequestBuilder builder = requestBuilderFactory.create(RequestBuilder.GET, url);
		try {
			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable e) {
					callback.onFailure(e.getMessage());
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						try {
							Document messageDom = XMLParser.parse(response.getText());
							WmsDescribeLayerInfo describeLayerInfo = null;
							switch (localVersion) {
								case V1_1_1:
									describeLayerInfo = new WmsDescribeLayerInfo111(messageDom.getDocumentElement());
									break;
								case V1_3_0:
								default:
									callback.onFailure("Unsupported version");
									return;
							}
							callback.onSuccess(describeLayerInfo);
						} catch (Throwable t) {
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
	public void getFeatureInfo(ViewPort viewPort, WmsLayer wmsLayer, Coordinate location,
			final Callback<List<Feature>, String> callback) {
		getFeatureInfo(viewPort, wmsLayer, location, GetFeatureInfoFormat.JSON.toString(), callback);
	}

	@Override
	public void getFeatureInfo(ViewPort viewPort, final WmsLayer wmsLayer, Coordinate location, String format,
			final Callback<List<Feature>, String> callback) {
		final String url = getFeatureInfoUrl(viewPort, wmsLayer, location, format.toString());

		// we can only handle json for now
		if (!GetFeatureInfoFormat.JSON.toString().equals(format)) {
			callback.onFailure("Client does not support " + format + " format");
		}
		RequestBuilder builder = requestBuilderFactory.create(RequestBuilder.GET, url);
		try {
			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable e) {
					callback.onFailure(e.getMessage());
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						JSONValue jsonValue = JsonService.parse(response.getText());
						FeatureCollection featureCollection;
						if (jsonValue.isObject() != null) {
							if (wmsLayer instanceof FeaturesSupported) {
								FeaturesSupported featuresSupported = (FeaturesSupported) wmsLayer;
								featureCollection = jsonFeatureFactory.createCollection(jsonValue.isObject(),
										featuresSupported);
							} else {
								featureCollection = jsonFeatureFactory.createCollection(jsonValue.isObject(), null);
							}
							callback.onSuccess(featureCollection.getFeatures());
						} else if (jsonValue.isNull() != null) {
							callback.onFailure("Response was empty");
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
	public String getMapUrl(WmsLayerConfiguration wmsConfig, Bbox worldBounds, int imageWidth, int imageHeight) {
		StringBuilder url = getBaseUrlBuilder(wmsConfig);

		// Add the base parameters needed for getMap:
		addBaseParameters(url, wmsConfig, worldBounds, imageWidth, imageHeight);

		// Parameter: request type
		url.append("&request=GetMap");

		return finishUrl(WmsRequest.GETMAP, url);
	}

	@Override
	public String getFeatureInfoUrl(WmsLayer layer, Coordinate location, Bbox worldBounds, double resolution,
			String format, int maxFeatures) {
		StringBuilder url = getBaseUrlBuilder(layer.getConfiguration());
		int x = (int) (Math.round(location.getX() - worldBounds.getX()) / resolution);
		int y = (int) (Math.round(worldBounds.getMaxY() - location.getY()) / resolution);
		int width = (int) (Math.round(worldBounds.getWidth() / resolution));
		int height = (int) (Math.round(worldBounds.getHeight() / resolution));

		// Add the base parameters needed for getMap:
		addBaseParameters(url, layer.getConfiguration(), worldBounds, width, height);

		url.append("&QUERY_LAYERS=");
		url.append(layer.getConfiguration().getLayers()); // No URL.encode here!
		url.append("&request=GetFeatureInfo");
		switch (layer.getConfiguration().getVersion()) {
			case V1_3_0:
				url.append("&I=");
				url.append(x);
				url.append("&J=");
				url.append(y);
				break;
			case V1_1_1:
			default:
				url.append("&X=");
				url.append(x);
				url.append("&Y=");
				url.append(y);
		}
		url.append("&FEATURE_COUNT=");
		url.append(maxFeatures);
		url.append("&INFO_FORMAT=");
		url.append(format.toString());

		return finishUrl(WmsRequest.GETFEATUREINFO, url);
	}

	public String getFeatureInfoUrl(ViewPort viewPort, final WmsLayer wmsLayer, Coordinate location, String format) {
		TileCode tileCode = TileService.getTileCodeForLocation(wmsLayer.getTileConfiguration(), location,
				viewPort.getResolution());
		Bbox worldBounds = TileService.getWorldBoundsForTile(wmsLayer.getTileConfiguration(), tileCode);

		final String url = getFeatureInfoUrl(wmsLayer, location, worldBounds, viewPort.getResolution(), format,
				DEFAULT_MAX_FEATURES);
		return url;
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
		
		// Parameter: version
		url.append("&version=");
		url.append(wmsConfig.getVersion().toString());

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
		
		// Parameter: transparent
		url.append("&transparent=true");

		// Check for specific vendor options:
		if (WmsServiceVendor.GEOSERVER_WMS.equals(wmsConfig.getWmsServiceVendor())) {

			// Parameter: for geoserver, width = icon width
			url.append("&width=");
			url.append(legendConfig.getIconWidth());

			// Parameter: for geoserver, height = icon height
			url.append("&height=");
			url.append(legendConfig.getIconHeight());
			
			url.append("&legend_options=");
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
			
			// geoserver supports dpi directly, use calculated width/height for other servers
			if (legendConfig.getDpi() != null) {
				double dpi = legendConfig.getDpi();
				// default dpi is 90.
				url.append("bgColor:0xFFFFFF;dpi:" + (int) dpi);
			}
			
		} else {
			if (legendConfig.getWidth() != null) {
				// Parameter: width
				url.append("&width=");
				url.append(legendConfig.getWidth());
			}

			if (legendConfig.getHeight() != null) {
				// Parameter: width
				url.append("&height=");
				url.append(legendConfig.getHeight());
			}
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
	protected void setRequestBuilderFactory(RequestBuilderFactory requestBuilderFactory) {
		this.requestBuilderFactory = requestBuilderFactory;
	}

	protected void setCoordinateFormatter(CoordinateFormatter coordinateFormatter) {
		this.coordinateFormatter = coordinateFormatter;
	}

	protected void setUrlEncoder(UrlEncoder urlEncoder) {
		this.urlEncoder = urlEncoder;
	}

	protected StringBuilder getBaseUrlBuilder(WmsLayerConfiguration config) {
		return new StringBuilder(config.getBaseUrl());
	}

	protected String finishUrl(WmsRequest request, StringBuilder builder) {
		String url = builder.toString();
		if (urlTransformer != null) {
			url = urlTransformer.transform(request, url);
		}
		return urlEncoder.encodeUrl(url);
	}

	protected StringBuilder addBaseParameters(StringBuilder url, WmsLayerConfiguration config, Bbox worldBounds,
			int imageWidth, int imageHeight) {
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
		if (config.isUseInvertedAxis()) {
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
		url.append(config.getCrs()); // No URL.encode here, performed in finishUrl

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

	protected String floatToStringWithDecimalPoint(double number) {
		return coordinateFormatter.format(number).replace(",", ".");
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

	protected String describeLayerUrl(String baseUrl, String layers, WmsVersion version) {
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
		url.append("&request=DescribeLayer");

		// Parameter: layers
		url.append("&layers=");
		url.append(layers); // No URL.encode here, performed in finishUrl

		return finishUrl(WmsRequest.DESCRIBELAYER, url);
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
