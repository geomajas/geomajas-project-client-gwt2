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

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.feature.query.QueryDto;
import org.geomajas.gwt2.client.map.layer.FeaturesSupported;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureCollectionInfo;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureTypeDescriptionInfo;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsGetCapabilitiesInfo;

import com.google.gwt.core.client.Callback;

/**
 * Client service that assists in performing requests to a WFS server.
 *
 * @author Jan De Moerloose
 * @since 2.2.1
 */
@Api(allMethods = true)
public interface WfsService {

	/**
	 * WFS version enumeration.
	 *
	 * @author Jan De Moerloose
	 */
	public enum WfsVersion {
		V1_0_0("1.0.0"), V1_1_0("1.1.0"), V2_0_0("2.0.0");

		private String version;

		private WfsVersion(String version) {
			this.version = version;
		}

		public String toString() {
			return version;
		}
	}

	/**
	 * Supported WFS requests.
	 *
	 * @author Jan De Moerloose
	 */
	public enum WfsRequest {
		GETCAPABILITIES("GetCapabilities");

		private String request;

		private WfsRequest(String request) {
			this.request = request;
		}

		public String toString() {
			return request;
		}

		public static WfsRequest fromString(String text) {
			if (text != null) {
				for (WfsRequest wfsRequest : WfsRequest.values()) {
					if (text.equalsIgnoreCase(wfsRequest.request)) {
						return wfsRequest;
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
	 * @author Jan De Moerloose
	 */
	@Api(allMethods = true)
	public interface WfsUrlTransformer {

		/**
		 * Transform the given URL.
		 *
		 * @param request The WFS request that is used in the URL. It may be that you wish to add a proxy servlet to the
		 *        URL for some requests but not all.
		 * @param url The URL to transform.
		 * @return Returns the transformed URL.
		 */
		String transform(WfsRequest request, String url);
	}

	// ------------------------------------------------------------------------
	// WFS GetCapabilities methods:
	// ------------------------------------------------------------------------

	/**
	 * Get the capabilities information of a WFS service.
	 *
	 * @param version The preferred WFS version.
	 * @param baseUrl The WFS base URL (without any WFS parameters).
	 * @param callback Callback that returns a {@link WfsGetCapabilitiesInfo} instance on success. From here, you can
	 *        extract all the information or layers defined in the capabilities file.
	 */
	void getCapabilities(WfsVersion version, String baseUrl, Callback<WfsGetCapabilitiesInfo, String> callback);

	/**
	 * Get the feature type description from a WFS service.
	 * 
	 * @param version The preferred WFS version.
	 * @param baseUrl The WFS base URL (without any WFS parameters).
	 * @param typeName The feature type name
	 * @param callback Callback that returns a {@link WfsFeatureTypeDescriptionInfo} instance on success. From here, you
	 *        can extract all the attribute information.
	 */
	void describeFeatureType(WfsVersion version, String baseUrl, String typeName,
			Callback<WfsFeatureTypeDescriptionInfo, String> callback);

	/**
	 * Get features from a WFS service.
	 * 
	 * @param version The preferred WFS version.
	 * @param baseUrl The WFS base URL (without any WFS parameters).
	 * @param typeName The feature type name
	 * @param query The query
	 * @param callback Callback that returns a {@link WfsFeatureCollectionInfo} instance on success. From here, you
	 *        can extract all the attribute information.
	 */
	void getFeatures(WfsVersion version, String baseUrl, String typeName, QueryDto query,
			Callback<WfsFeatureCollectionInfo, String> callback);
	
	/**
	 * Get features from a WFS service for a specific layer.
	 * 
	 * @param layer The layer.
	 * @param version The preferred WFS version.
	 * @param baseUrl The WFS base URL (without any WFS parameters).
	 * @param typeName The feature type name
	 * @param query The query
	 * @param callback Callback that returns a {@link WfsFeatureCollectionInfo} instance on success. From here, you
	 *        can extract all the attribute information.
	 */
	void getFeatures(WfsVersion version, FeaturesSupported layer, String baseUrl, String typeName, QueryDto query,
			Callback<WfsFeatureCollectionInfo, String> callback);

	// ------------------------------------------------------------------------
	// URL transformation options (for proxy):
	// ------------------------------------------------------------------------

	/**
	 * Apply a transformer to transform any URL that is generated within this service. This transformer can, for
	 * example, be used to add a proxy servlet to any URL.
	 *
	 * @param urlTransformer The URL transformer to use.
	 */
	void setWfsUrlTransformer(WfsUrlTransformer urlTransformer);

	/**
	 * Return the current URL transformer, or null if no transformer has been set yet.
	 *
	 * @return The current WFS URL transformer.
	 */
	WfsUrlTransformer getWfsUrlTransformer();

}
