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

package org.geomajas.plugin.wms.server.command;

import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.command.Command;
import org.geomajas.geometry.conversion.jts.GeometryConverterService;
import org.geomajas.layer.feature.Feature;
import org.geomajas.plugin.wms.server.command.dto.WfsGetFeaturesRequest;
import org.geomajas.plugin.wms.server.command.dto.WfsGetFeaturesResponse;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Command that executes a WFS GetFeatures request.<p/> This command is not part of the API and shouldn't be used
 * directly.
 *
 * @author Pieter De Graef
 * @author An Buyle
 */
@Component
public class WfsGetFeaturesCommand implements Command<WfsGetFeaturesRequest, WfsGetFeaturesResponse> {

	public void execute(WfsGetFeaturesRequest request, WfsGetFeaturesResponse response) throws Exception {
		String capa = request.getBaseUrl() + "?service=wfs&version=1.0.0&request=GetCapabilities";

		Map<String, String> connectionParameters = new HashMap<String, String>();
		connectionParameters.put("WFSDataStoreFactory:GET_CAPABILITIES_URL", capa);
		connectionParameters.put("WFSDataStoreFactory:TIMEOUT", "10000");

		// Get the WFS feature source:
		DataStore data = DataStoreFinder.getDataStore(connectionParameters);
		FeatureSource<SimpleFeatureType, SimpleFeature> source = data.getFeatureSource(request.getLayer());
		SimpleFeatureType schema;

		schema = data.getSchema(request.getLayer()); // forward all exceptions

		// Filter:
		String geomName = schema.getGeometryDescriptor().getLocalName();
		Geometry location = GeometryConverterService.toJts(request.getLocation());
		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
		Filter filter = ff.intersects(ff.property(geomName), ff.literal(location));

		int maxFeatures = request.getMaxNumOfFeatures();
		if (maxFeatures < 0) {
			maxFeatures = Integer.MAX_VALUE;
		}
		Query query = new Query(request.getLayer(), filter, maxFeatures, Query.ALL_NAMES, null);
		FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(query);
		FeatureIterator<SimpleFeature> it = collection.features();

		// Now fetch the features:
		List<Feature> dtoFeatures = new ArrayList<Feature>();
		FeatureConverter converter = new FeatureConverter();
		while (it.hasNext()) {
			try {
				SimpleFeature feature = it.next();
				dtoFeatures.add(converter.toDto(feature, request.getMaxCoordsPerFeature()));
			} catch (Exception e) {
				continue;
			}
		}

		response.setFeatures(dtoFeatures);
	}

	public WfsGetFeaturesResponse getEmptyCommandResponse() {
		return new WfsGetFeaturesResponse();
	}
}
