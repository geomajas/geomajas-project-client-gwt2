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

import com.google.gwt.core.client.Callback;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.layer.LegendConfig;
import org.geomajas.gwt2.client.map.render.TileCode;
import org.geomajas.gwt2.client.service.TileService;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsGetCapabilitiesInfo;
import org.geomajas.gwt2.plugin.wms.client.describelayer.WmsDescribeLayerInfo;
import org.geomajas.gwt2.plugin.wms.client.layer.WmsLayer;
import org.geomajas.gwt2.plugin.wms.client.layer.WmsLayerConfiguration;
import org.geomajas.gwt2.plugin.wms.client.service.WmsService.GetFeatureInfoFormat;

import java.util.List;

/**
 * Client service that assists in performing requests to the WMS server.
 *
 * @author Pieter De Graef
 * @author An Buyle (getLegendGraphicUrl)
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface WmsService {

	/**
	 * Supported format for the WMS GetFeatureInfo request.
	 *
	 * @author Pieter De Graef
	 */
	public enum GetFeatureInfoFormat {
		GML2("application/vnd.ogc.gml"), GML3("application/vnd.ogc.gml/3.1.1"), HTML("text/html"), TEXT("text/plain"),
		JSON("application/json");

		private String format;

		private GetFeatureInfoFormat(String format) {
			this.format = format;
		}

		public String toString() {
			return format;
		}
	}

	/**
	 * WMS version enumeration.
	 *
	 * @author Pieter De Graef
	 */
	public enum WmsVersion {
		V1_1_1("1.1.1"), V1_3_0("1.3.0");

		private String version;

		private WmsVersion(String version) {
			this.version = version;
		}

		public String toString() {
			return version;
		}
	}

	/**
	 * Supported WMS requests.
	 *
	 * @author Pieter De Graef
	 */
	public enum WmsRequest {
		GETMAP("GetMap"), GETCAPABILITIES("GetCapabilities"), GETFEATUREINFO("GetFeatureInfo"), GETLEGENDGRAPHIC(
				"GetLegendGraphic"), DESCRIBELAYER("DescribeLayer");

		private String request;

		private WmsRequest(String request) {
			this.request = request;
		}

		public String toString() {
			return request;
		}

		public static WmsRequest fromString(String text) {
			if (text != null) {
				for (WmsRequest wmsRequest : WmsRequest.values()) {
					if (text.equalsIgnoreCase(wmsRequest.request)) {
						return wmsRequest;
					}
				}
			}
			return null;
		}
	}

	/**
	 * Transforms the given URL. This interface can, for example, be used to make sure requests make use of a proxy
	 * servlet. A possible implementation could be: <code>return "/proxy?url=" + url;</code>
	 *
	 * @author Pieter De Graef
	 */
	public interface WmsUrlTransformer {

		/**
		 * Transform the given URL.
		 *
		 * @param request The WMS request that is used in the URL. It may be that you wish to add a proxy servlet to the
		 *                URL for some requests but not all.
		 * @param url     The URL to transform.
		 * @return Returns the transformed URL.
		 */
		String transform(WmsRequest request, String url);
	}

	// ------------------------------------------------------------------------
	// WMS GetCapabilities methods:
	// ------------------------------------------------------------------------

	/**
	 * Get the capabilities information of a WMS service.
	 *
	 * @param baseUrl  The WMS base URL (without any WMS parameters).
	 * @param version  The preferred WMS version.
	 * @param callback Callback that returns a {@link WmsGetCapabilitiesInfo} instance on success. From here, you can
	 *                 extract all the information or layers defined in the capabilities file.
	 */
	void getCapabilities(String baseUrl, WmsVersion version, Callback<WmsGetCapabilitiesInfo, String> callback);

	/**
	 * Get the layer description of one (or more) WMS layers.
	 * 
	 * @param baseUrl The WMS base URL (without any WMS parameters).
	 * @param layers The layer name(s).
	 * @param version The preferred WMS version.
	 * @param callback Callback that returns a {@link WmsDescribeLayerInfo} instance on success. From here, you can
	 *        extract all the information defined in the layer description response file (associated WFS/WCS).
	 */
	void describeLayer(String baseUrl, String layers, WmsVersion version,
			Callback<WmsDescribeLayerInfo, String> callback);

	// ------------------------------------------------------------------------
	// WMS GetMap methods:
	// ------------------------------------------------------------------------

	/**
	 * Get the URL that retrieves the requested bounds for the requested layer through a GetMap request.
	 *
	 * @param wmsConfig   The configuration object that points to some WMS layer.
	 * @param worldBounds The bounds to retrieve through the GetMap request.
	 * @param imageWidth  The image width.
	 * @param imageHeight The image height.
	 * @return URL to the image.
	 */
	String getMapUrl(WmsLayerConfiguration wmsConfig, Bbox worldBounds, int imageWidth, int imageHeight);

	// ------------------------------------------------------------------------
	// WMS GetLegendGraphic methods:
	// ------------------------------------------------------------------------

	/**
	 * Get the URL that points to the legend graphic of a WMS layer. (Usually through a WMS GetLegendGraphic request)
	 *
	 * @param wmsConfig The configuration object that points to some WMS layer.
	 * @return Returns the URL that points to the legend image.
	 */

	String getLegendGraphicUrl(WmsLayerConfiguration wmsConfig);

	/**
	 * Get the URL that points to the legend graphic of a WMS layer. (usually through a WMS GetLegendGraphic request)
	 *
	 * @param wmsConfig The configuration object that points to some WMS layer.
	 * 
	 * @param legendConfig Specific legend configuration that overrides the default legend configuration from within the
	 *        wmsConfig object. Note that by default WMS does not support these options, although some vendors have
	 *        added extra options to allows for this configuration (such as GeoServer, see
	 *        {@link org.geomajas.gwt2.plugin.wms.client.layer.WmsServiceVendor}).
	 * @return Returns the URL that points to the legend image.
	 */
	String getLegendGraphicUrl(WmsLayerConfiguration wmsConfig, LegendConfig legendConfig);
	
	/**
	 * Get the URL for a WMS GetFeatureInfo request.
	 * 
	 * @param layer the layer
	 * @param location location of the point
	 * @param worldBounds world bounds of the image
	 * @param resolution resolution of the image
	 * @param format format of the response
	 * @param maxFeatures maximum number of features to return
	 * @return the URL of the GetFeatureInfo request.
	 */
	String getFeatureInfoUrl(WmsLayer layer, Coordinate location, Bbox worldBounds, double resolution,
			String format, int maxFeatures);
	
	/**
	 * Get the URL for a WMS GetFeatureInfo request.
	 * 
	 * @param viewPort the viewPort
	 * @param layer the layer
	 * @param location location of the point
	 * @param format format of the response
	 * @return the URL of the GetFeatureInfo request.
	 */
	String getFeatureInfoUrl(ViewPort viewPort, final WmsLayer wmsLayer, Coordinate location,
			String format);


	// ------------------------------------------------------------------------
	// URL transformation options (for proxy):
	// ------------------------------------------------------------------------

	/**
	 * Apply a transformer to transform any URL that is generated within this service. This transformer can, for
	 * example, be used to add a proxy servlet to any URL.
	 *
	 * @param urlTransformer The URL transformer to use.
	 */
	void setWmsUrlTransformer(WmsUrlTransformer urlTransformer);

	/**
	 * Return the current URL transformer, or null if no transformer has been set yet.
	 *
	 * @return The current WMS URL transformer.
	 */
	WmsUrlTransformer getWmsUrlTransformer();

	/**
	 * Execute a WMS GetFeatureInfo request.
	 * 
	 * @param location The location in world space to get information for.
	 * @param callback The callback that is executed when the response returns. If features are found at the requested
	 *        location, they will be returned here.
	 */
	void getFeatureInfo(ViewPort viewPort, WmsLayer wmsLayer, Coordinate location,
			Callback<List<Feature>, String> callback);

	/**
	 * Execute a WMS GetFeatureInfo request.
	 * 
	 * @param location The location in world space to get information for.
	 * @param format The GetFeatureInfo format for the response.
	 * @param callback The callback that is executed when the response returns. If features are found at the requested
	 *        location, they will be returned here. Note that the callback returns a string on success. It is up to you
	 *        to parse this.
	 */
	void getFeatureInfo(ViewPort viewPort, WmsLayer wmsLayer, Coordinate location, String format,
			Callback<List<Feature>, String> callback);
}
