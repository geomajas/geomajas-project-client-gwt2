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

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.map.layer.LegendConfig;
import org.geomajas.plugin.wms.client.capabilities.WmsGetCapabilitiesInfo;
import org.geomajas.plugin.wms.client.layer.config.WmsLayerConfiguration;

import com.google.gwt.core.client.Callback;

/**
 * Client service that assists in performing requests to the WMS server.
 * 
 * @author Pieter De Graef
 * @author An Buyle (getLegendGraphicUrl)
 * @since 1.0.0
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
				"GetLegendGraphic");

		private String request;

		private WmsRequest(String request) {
			this.request = request;
		}

		public String toString() {
			return request;
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
		 * @param request
		 *            The WMS request that is used in the URL. It may be that you wish to add a proxy servlet to the URL
		 *            for some requests but not all.
		 * @param url
		 *            The URL to transform.
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
	 * @param baseUrl
	 *            The WMS base URL (without any WMS parameters).
	 * @param version
	 *            The preferred WMS version.
	 * @param callback
	 *            Callback that returns a {@link WmsGetCapabilitiesInfo} instance on success. From here, you can extract
	 *            all the information or layers defined in the capabilities file.
	 */
	void getCapabilities(String baseUrl, WmsVersion version, Callback<WmsGetCapabilitiesInfo, String> callback);

	// ------------------------------------------------------------------------
	// WMS GetMap methods:
	// ------------------------------------------------------------------------

	/**
	 * Get the URL that retrieves the requested bounds for the requested layer through a GetMap request.
	 * 
	 * @param wmsConfig
	 *            The configuration object that points to some WMS layer.
	 * @param crs
	 *            The preferred coordinate reference system.
	 * @param worldBounds
	 *            The bounds to retrieve through the GetMap request.
	 * @param imageWidth
	 *            The image width.
	 * @param imageHeight
	 *            The image height.
	 * @return URL to the image.
	 */
	String getMapUrl(WmsLayerConfiguration wmsConfig, String crs, Bbox worldBounds, int imageWidth, int imageHeight);

	// ------------------------------------------------------------------------
	// WMS GetLegendGraphic methods:
	// ------------------------------------------------------------------------

	/**
	 * Get the URL that points to the legend graphic of a WMS layer. (Usually through a WMS GetLegendGraphic request)
	 * 
	 * @param wmsConfig
	 *            The configuration object that points to some WMS layer.
	 * 
	 * @return Returns the URL that points to the legend image.
	 */

	String getLegendGraphicUrl(WmsLayerConfiguration wmsConfig);

	/**
	 * Get the URL that points to the legend graphic of a WMS layer. (usually through a WMS GetLegendGraphic request)
	 * 
	 * @param wmsConfig
	 *            The configuration object that points to some WMS layer.
	 ** @param legendConfig
	 *            Specific legend configuration that overrides the default legend configuration from within the
	 *            wmsConfig object. Note that by default WMS does not support these options, although some vendors have
	 *            added extra options to allows for this configuration (such as GeoServer, see
	 *            {@link org.geomajas.plugin.wms.client.layer.config.WmsServiceVendor}).
	 * 
	 * @return Returns the URL that points to the legend image.
	 */
	String getLegendGraphicUrl(WmsLayerConfiguration wmsConfig, LegendConfig legendConfig);

	// ------------------------------------------------------------------------
	// URL transformation options (for proxy):
	// ------------------------------------------------------------------------

	/**
	 * Apply a transformer to transform any URL that is generated within this service. This transformer can, for
	 * example, be used to add a proxy servlet to any URL.
	 * 
	 * @param urlTransformer
	 *            The URL transformer to use.
	 */
	void setWmsUrlTransformer(WmsUrlTransformer urlTransformer);

	/**
	 * Return the current URL transformer, or null if no transformer has been set yet.
	 * 
	 * @return The current WMS URL transformer.
	 */
	WmsUrlTransformer getWmsUrlTransformer();
}