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

package org.geomajas.gwt2.plugin.wfs.server.command;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.geomajas.command.Command;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.global.GeomajasException;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.client.map.feature.query.CriterionDto;
import org.geomajas.gwt2.plugin.wfs.server.command.converter.FeatureConverter;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsGetFeatureRequest;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsGetFeatureResponse;
import org.geomajas.gwt2.plugin.wfs.server.command.factory.CriterionToFilterConverter;
import org.geomajas.gwt2.plugin.wfs.server.command.factory.CriterionToFilterConverterFactory;
import org.geomajas.gwt2.plugin.wfs.server.command.factory.URLBuilder;
import org.geomajas.gwt2.plugin.wfs.server.command.factory.WfsDataStoreFactory;
import org.geomajas.gwt2.plugin.wfs.server.command.factory.WfsHttpClientFactory;
import org.geomajas.gwt2.plugin.wfs.server.command.factory.impl.DefaultCriterionFilterConverter;
import org.geomajas.gwt2.plugin.wfs.server.command.factory.impl.DefaultWfsDataStoreFactory;
import org.geomajas.gwt2.plugin.wfs.server.command.factory.impl.DefaultWfsHttpClientFactory;
import org.geomajas.gwt2.plugin.wfs.server.dto.WfsFeatureCollectionDto;
import org.geomajas.gwt2.plugin.wfs.server.dto.WfsVersionDto;
import org.geomajas.layer.feature.Feature;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.geotools.data.DataStore;
import org.geotools.data.Query;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.GeographicCRS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

/**
 * Command that executes a WFS GetFeatures request.
 * <p/>
 * This command is not part of the API and shouldn't be used directly.
 *
 * @author Jan De Moerloose
 * 
 */
@Component(WfsGetFeatureRequest.COMMAND_NAME)
public class WfsGetFeatureCommand implements Command<WfsGetFeatureRequest, WfsGetFeatureResponse> {

	private final Logger log = LoggerFactory.getLogger(WfsGetFeatureCommand.class);

	@Autowired
	private FilterService filterService;

	@Autowired
	private DtoConverterService converterService;

	private WfsDataStoreFactory dataStoreFactory;

	private CriterionToFilterConverterFactory criterionToFilterFactory;

	private WfsHttpClientFactory httpClientFactory;

	public WfsGetFeatureCommand() {
		dataStoreFactory = new DefaultWfsDataStoreFactory();
		criterionToFilterFactory = new CriterionToFilterConverterFactory() {

			@Override
			public CriterionToFilterConverter createConverter() {
				return new DefaultCriterionFilterConverter(filterService, converterService);
			}
		};
		httpClientFactory = new DefaultWfsHttpClientFactory();
	}

	public void setDataStoreFactory(WfsDataStoreFactory dataStoreFactory) {
		this.dataStoreFactory = dataStoreFactory;
	}

	public void setCriterionToFilterFactory(CriterionToFilterConverterFactory criterionToFilterFactory) {
		this.criterionToFilterFactory = criterionToFilterFactory;
	}

	public void setHttpClientFactory(WfsHttpClientFactory httpClientFactory) {
		this.httpClientFactory = httpClientFactory;
	}

	public void execute(WfsGetFeatureRequest request, WfsGetFeatureResponse response) throws Exception {
		FeatureCollection<SimpleFeatureType, SimpleFeature> features = performWfsQuery(request);
		int maxCoordinates = request.getMaxCoordsPerFeature();
		double distance = 10;
		// Note: features.getSchema() can be null when no features have been found
		if (null != features.getSchema()
				&& features.getSchema().getCoordinateReferenceSystem() instanceof GeographicCRS) {
			distance = 0.0001;
		}
		WfsFeatureCollectionDto featureCollectionDto = new WfsFeatureCollectionDto();
		featureCollectionDto.setBaseUrl(request.getBaseUrl());
		featureCollectionDto.setTypeName(request.getTypeName());
		List<Feature> dtoFeatures = convertFeatures(features, maxCoordinates, distance);
		featureCollectionDto.setFeatures(dtoFeatures);
		featureCollectionDto.setBoundingBox(getTotalBounds(dtoFeatures));
		response.setFeatureCollection(featureCollectionDto);
		log.info("Found " + response.getFeatureCollection().size() + " features for layer " + request.getTypeName());
	}

	protected FeatureCollection<SimpleFeatureType, SimpleFeature> performWfsQuery(WfsGetFeatureRequest request)
			throws IOException {
		try {
			// create Geotools query
			Query query = createQuery(request.getTypeName(), request.getSchema(), request.getCriterion(),
					request.getMaxFeatures(), request.getStartIndex(), request.getRequestedAttributeNames(),
					request.getCrs());

			String sourceUrl = request.getBaseUrl();
			URL targetUrl = httpClientFactory.getTargetUrl(sourceUrl);
			HTTPClient client = httpClientFactory.create(sourceUrl);

			// run it
			return getFeatures(targetUrl, client, request.getTypeName(), query, request.getVersion());
		} catch (SAXException e) {
			throw new IOException(e);
		} catch (ParserConfigurationException e) {
			throw new IOException(e);
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}

	}

	/**
	 * Query a geotools source for features using the criterion as a filter.
	 *
	 * @param source
	 * @param criterion
	 * @param attributeNames
	 * @param crs
	 * @return
	 * @throws IOException
	 * @throws GeomajasException
	 */
	protected Query createQuery(String typeName, List<AttributeDescriptor> schema, CriterionDto criterion,
			int maxFeatures, int startIndex, List<String> attributeNames, String crs) throws IOException {
		CriterionToFilterConverter converter = criterionToFilterFactory.createConverter();
		Filter filter = converter.convert(criterion, schema);
		Query query = null;
		if (attributeNames == null) {
			query = new Query(typeName, filter, maxFeatures, Query.ALL_NAMES, null);
		} else {
			query = new Query(typeName, filter, maxFeatures, attributeNames.toArray(new String[attributeNames.size()]),
					null);
		}
		query.setStartIndex(startIndex);
		if (null != crs) {
			try {
				query.setCoordinateSystem(CRS.decode(crs));
			} catch (NoSuchAuthorityCodeException e) { // assume non-fatal
				log.warn("Problem getting CRS for id " + crs + ": " + e.getMessage());
			} catch (FactoryException e) {
				// assume non-fatal
				log.warn("Problem getting CRS for id " + crs + ": " + e.getMessage());
			}
		}
		return query;
	}

	protected FeatureCollection<SimpleFeatureType, SimpleFeature> getFeatures(URL baseUrl, HTTPClient client,
			String typeName, Query query, WfsVersionDto version) throws IOException, SAXException,
			ParserConfigurationException, URISyntaxException {

		URL url = URLBuilder.createWfsURL(baseUrl, version, "GetCapabilities");
		String capa = url.toExternalForm();
		Map<String, Serializable> connectionParameters = new HashMap<String, Serializable>();
		connectionParameters.put(WFSDataStoreFactory.MAXFEATURES.key, query.getMaxFeatures());
		connectionParameters.put(WFSDataStoreFactory.URL.key, capa);
		connectionParameters.put(WFSDataStoreFactory.PROTOCOL.key, Boolean.TRUE);
		DataStore data = dataStoreFactory.createDataStore(connectionParameters, client);
		SimpleFeatureSource features = data.getFeatureSource(typeName.replace(":", "_"));
		return features.getFeatures(query);
	}

	/**
	 * Convert a feature collection to its dto equivalent.
	 *
	 * @param features
	 * @return
	 * @throws IOException
	 * @throws GeomajasException
	 */
	protected List<Feature> convertFeatures(FeatureCollection<SimpleFeatureType, SimpleFeature> features,
			int maxCoordinates, double startDistance) throws IOException, GeomajasException {
		FeatureIterator<SimpleFeature> iterator = features.features();
		List<Feature> dtoFeatures = new ArrayList<Feature>();
		FeatureConverter converter = new FeatureConverter();
		while (iterator.hasNext()) {
			try {
				SimpleFeature feature = iterator.next();
				dtoFeatures.add(converter.toDto(feature, maxCoordinates));
			} catch (Exception e) {
				continue;
			}
		}
		iterator.close();
		return dtoFeatures;
	}

	/**
	 * Get the total bounds of all features in the collection.
	 *
	 * @param features
	 * @return bounds or null if no features
	 */
	protected Bbox getTotalBounds(List<Feature> features) {
		Bbox total = null;
		for (Feature featureDto : features) {
			org.geomajas.geometry.Geometry geom = featureDto.getGeometry();
			if (geom != null) {
				Bbox b = GeometryService.getBounds(geom);
				if (total == null) {
					total = b;
				} else {
					total = BboxService.union(total, b);
				}
			}
		}
		return total;
	}

	public WfsGetFeatureResponse getEmptyCommandResponse() {
		return new WfsGetFeatureResponse();
	}

}