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

package org.geomajas.gwt2.plugin.wms.server.command;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.geomajas.command.Command;
import org.geomajas.gwt2.plugin.wfs.server.command.converter.FeatureConverter;
import org.geomajas.gwt2.plugin.wms.client.service.WmsService.GetFeatureInfoFormat;
import org.geomajas.gwt2.plugin.wms.server.command.dto.WmsGetFeatureInfoRequest;
import org.geomajas.gwt2.plugin.wms.server.command.dto.WmsGetFeatureInfoResponse;
import org.geomajas.gwt2.plugin.wms.server.command.factory.DefaultHttpClientFactory;
import org.geomajas.gwt2.plugin.wms.server.command.factory.HttpClientFactory;
import org.geomajas.layer.feature.Feature;
import org.geotools.GML;
import org.geotools.GML.Version;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.feature.simple.SimpleFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

/**
 * Command that executes a WMS GetFeatureInfo request.
 * <p/>
 * This command is not part of the API and shouldn't be used directly.
 *
 * @author Pieter De Graef
 * @author An Buyle
 * @author Jan De Moerloose
 */
@Component
public class WmsGetFeatureInfoCommand implements Command<WmsGetFeatureInfoRequest, WmsGetFeatureInfoResponse> {

	private final Logger log = LoggerFactory.getLogger(WmsGetFeatureInfoCommand.class);

	private static final String PARAM_FORMAT = "info_format";
	
	private HttpClientFactory httpClientFactory = new DefaultHttpClientFactory();

	public void execute(WmsGetFeatureInfoRequest request, WmsGetFeatureInfoResponse response) throws Exception {
		URL url = new URL(request.getUrl());
		GML gml;

		GetFeatureInfoFormat format = getFormatFromUrl(request.getUrl());
		switch (format) {
			case GML2:
				gml = new GML(Version.GML2);
				response.setFeatures(getFeaturesFromUrl(url, gml, request.getMaxCoordsPerFeature()));
				break;
			case GML3:
				gml = new GML(Version.GML3);
				response.setFeatures(getFeaturesFromUrl(url, gml, request.getMaxCoordsPerFeature()));
				break;
			case JSON:
				response.setFeatures(getFeaturesFromJson(request, url, request.getMaxCoordsPerFeature()));
				break;
			default:
				String content = readUrl(url);
				response.setWmsResponse(content);
		}
	}
	
	public HttpClientFactory getHttpClientFactory() {
		return httpClientFactory;
	}
	
	public void setHttpClientFactory(HttpClientFactory httpClientFactory) {
		this.httpClientFactory = httpClientFactory;
	}

	public WmsGetFeatureInfoResponse getEmptyCommandResponse() {
		return new WmsGetFeatureInfoResponse();
	}

	private List<Feature> getFeaturesFromJson(WmsGetFeatureInfoRequest request, URL url, int maxCoordsPerFeature)
			throws IOException {
		FeatureConverter converter = new FeatureConverter();
		List<Feature> dtoFeatures = new ArrayList<Feature>();
		HttpClient client = httpClientFactory.createClientForUrl(url);
		HttpGet get = new HttpGet(url.toExternalForm());
		HttpResponse response = client.execute(get);
		if (200 != response.getStatusLine().getStatusCode()) {
			get.releaseConnection();
			throw new IOException("Server returned " + response.getStatusLine() + " for URL " + url.toExternalForm());
		}
		FeatureIterator<SimpleFeature> it = new FeatureJSON().streamFeatureCollection(response.getEntity().getContent());
		while (it.hasNext()) {
			SimpleFeature feature = it.next();
			try {
				dtoFeatures.add(converter.toDto(feature, maxCoordsPerFeature));
			} catch (Exception e) {
				log.error("Error parsing Feature information: " + e.getMessage());
			}
		}
		it.close();
		return dtoFeatures;
	}

	private List<Feature> getFeaturesFromUrl(URL url, GML gml, int maxCoordsPerFeature) throws IOException,
			SAXException, ParserConfigurationException {
		HttpClient client = httpClientFactory.createClientForUrl(url);
		HttpGet get = new HttpGet(url.toExternalForm());
		HttpResponse response = client.execute(get);
		if (200 != response.getStatusLine().getStatusCode()) {
			get.releaseConnection();
			throw new IOException("Server returned " + response.getStatusLine() + " for URL " + url.toExternalForm());
		}
		FeatureCollection<?, SimpleFeature> collection = gml.decodeFeatureCollection(response.getEntity().getContent());
		List<Feature> dtoFeatures = new ArrayList<Feature>();
		if (null == collection) {
			return dtoFeatures; // empty list
		}
		FeatureConverter converter = new FeatureConverter();
		FeatureIterator<SimpleFeature> it = collection.features();
		if (it.hasNext()) {
			SimpleFeature feature = it.next();
			try {
				dtoFeatures.add(converter.toDto(feature, maxCoordsPerFeature));
			} catch (Exception e) {
				log.error("Error parsing Feature information: " + e.getMessage());
			}
		}
		while (it.hasNext()) {
			SimpleFeature feature = it.next();
			try {
				dtoFeatures.add(converter.toDto(feature, maxCoordsPerFeature));
			} catch (Exception e) {
				// Do nothing...
			}
		}
		return dtoFeatures;
	}

	private GetFeatureInfoFormat getFormatFromUrl(String url) {
		try {
			int index = url.toLowerCase().indexOf(PARAM_FORMAT) + PARAM_FORMAT.length() + 1;
			String format = url.substring(index);
			index = format.indexOf('&');
			if (index > 0) {
				format = format.substring(0, index);
			}
			for (GetFeatureInfoFormat enumValue : GetFeatureInfoFormat.values()) {
				if (enumValue.toString().equalsIgnoreCase(format)) {
					return enumValue;
				}
			}
		} catch (Exception e) {
			log.error("WMS GetFeatureInfo - Cannot understand which format to request... "
					+ "We'll take HTML format as a fallback." + e.getMessage());
		}
		return GetFeatureInfoFormat.HTML;
	}

	private String readUrl(URL url) throws Exception {
		HttpClient client = httpClientFactory.createClientForUrl(url);
		HttpGet get = new HttpGet(url.toExternalForm());
		HttpResponse response = client.execute(get);
		if (200 != response.getStatusLine().getStatusCode()) {
			get.releaseConnection();
			throw new IOException("Server returned " + response.getStatusLine() + " for URL " + url.toExternalForm());
		}
		return EntityUtils.toString(response.getEntity());
	}
}
