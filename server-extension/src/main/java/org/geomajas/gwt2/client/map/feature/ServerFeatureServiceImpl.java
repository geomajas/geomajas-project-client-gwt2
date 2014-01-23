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

package org.geomajas.gwt2.client.map.feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchByLocationResponse;
import org.geomajas.command.dto.SearchFeatureRequest;
import org.geomajas.command.dto.SearchFeatureResponse;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.attribute.Attribute;
import org.geomajas.gwt2.client.map.layer.FeaturesSupported;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.layer.ServerLayer;
import org.geomajas.layer.feature.SearchCriterion;

/**
 * <p>
 * Service for feature retrieval and manipulation. This service is map specific, and so the methods may assume the
 * features come from layers within the map's {@link org.geomajas.gwt.client.map.layer.LayersModel}.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class ServerFeatureServiceImpl implements ServerFeatureService {

	// ------------------------------------------------------------------------
	// Searching features by attributes:
	// ------------------------------------------------------------------------

	@Override
	public void search(String crs, final FeaturesSupported layer, SearchCriterion[] criteria, 
			LogicalOperator operator, int maxResultSize, final FeatureMapFunction callback) {
		SearchFeatureRequest request = new SearchFeatureRequest();
		request.setBooleanOperator(operator.getValue());
		request.setCriteria(criteria);
		request.setMax(maxResultSize);
		if (layer instanceof ServerLayer) {
			ServerLayer<?> serverLayer = (ServerLayer<?>) layer;
			request.setLayerId(serverLayer.getServerLayerId());
		}
		request.setCrs(crs);
		request.setFilter(layer.getFilter());
		request.setFeatureIncludes(11);

		GwtCommand command = new GwtCommand(SearchFeatureRequest.COMMAND);
		command.setCommandRequest(request);
		GeomajasServerExtension.getInstance().getCommandService().execute(command,
				new AbstractCommandCallback<SearchFeatureResponse>() {

					public void execute(SearchFeatureResponse response) {
						List<Feature> features = new ArrayList<Feature>();
						for (org.geomajas.layer.feature.Feature feature : response.getFeatures()) {
							features.add(create(feature, layer));
						}
						Map<FeaturesSupported, List<Feature>> mapping = new HashMap<FeaturesSupported, List<Feature>>();
						mapping.put(layer, features);
						callback.execute(mapping);
					}
				});
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	@Override
	public Feature create(org.geomajas.layer.feature.Feature feature, FeaturesSupported layer) {
		Map<String, Attribute<?>> attributes = new HashMap<String, Attribute<?>>();
		for (String key : feature.getAttributes().keySet()) {
			attributes.put(key, new AttributeImpl(feature.getAttributes().get(key)));
		}
		return new FeatureImpl(layer, feature.getId(), attributes, feature.getGeometry(), feature.getLabel());
	}

	// ------------------------------------------------------------------------
	// Searching features by location:
	// ------------------------------------------------------------------------

	@Override
	public void search(String crs, final FeaturesSupported layer, Geometry location, double buffer,
			final FeatureMapFunction callback) {
		SearchByLocationRequest request = new SearchByLocationRequest();
		request.setBuffer(buffer);
		if (layer instanceof ServerLayer) {
			ServerLayer<?> serverLayer = (ServerLayer<?>) layer;
			request.addLayerWithFilter(serverLayer.getServerLayerId(), serverLayer.getServerLayerId(),
					layer.getFilter());
		}
		request.setLocation(location);
		request.setSearchType(SearchLayerType.SEARCH_ALL_LAYERS.getValue());
		request.setCrs(crs);
		request.setFeatureIncludes(11);

		GwtCommand command = new GwtCommand(SearchByLocationRequest.COMMAND);
		command.setCommandRequest(request);
		GeomajasServerExtension.getInstance().getCommandService().execute(command,
				new AbstractCommandCallback<SearchByLocationResponse>() {

					public void execute(SearchByLocationResponse response) {
						for (List<org.geomajas.layer.feature.Feature> dtos : response.getFeatureMap().values()) {
							List<Feature> features = new ArrayList<Feature>(dtos.size());
							for (org.geomajas.layer.feature.Feature feature : dtos) {
								features.add(create(feature, layer));
							}
							Map<FeaturesSupported, List<Feature>> map = new HashMap<FeaturesSupported, List<Feature>>();
							map.put(layer, features);
							callback.execute(map);
						}
					}
				});
	}

	@Override
	public void search(final MapPresenter mapPresenter, Geometry location, double buffer, QueryType queryType,
			SearchLayerType searchType, float ratio, final FeatureMapFunction callback) {
		SearchByLocationRequest request = new SearchByLocationRequest();

		// Add all FeaturesSupported layers, together with their filters:
		switch (searchType) {
			case SEARCH_SELECTED_LAYER:
				Layer layer = mapPresenter.getLayersModel().getSelectedLayer();
				if (layer != null && layer instanceof FeaturesSupported && layer instanceof ServerLayer) {
					ServerLayer<?> serverLayer = (ServerLayer<?>) layer;
					request.addLayerWithFilter(serverLayer.getServerLayerId(), serverLayer.getServerLayerId(),
							((FeaturesSupported) layer).getFilter());
				} else {
					throw new IllegalStateException(
							"No selected layer, or selected layer is not of the type FeaturesSupported.");
				}
				break;
			default:
				for (int i = 0; i < mapPresenter.getLayersModel().getLayerCount(); i++) {
					Layer layer2 = mapPresenter.getLayersModel().getLayer(i);
					if (layer2 instanceof FeaturesSupported && layer2 instanceof ServerLayer) {
						ServerLayer<?> serverLayer = (ServerLayer<?>) layer2;
						request.addLayerWithFilter(serverLayer.getServerLayerId(), serverLayer.getServerLayerId(),
								((FeaturesSupported) layer2).getFilter());
					}
				}
		}

		request.setBuffer(buffer);
		request.setLocation(location);
		request.setQueryType(queryType.getValue());
		request.setSearchType(searchType.getValue());
		request.setRatio(ratio);
		request.setCrs(mapPresenter.getViewPort().getCrs());
		request.setFeatureIncludes(11);

		GwtCommand command = new GwtCommand(SearchByLocationRequest.COMMAND);
		command.setCommandRequest(request);
		GeomajasServerExtension.getInstance().getCommandService().execute(command,
				new AbstractCommandCallback<SearchByLocationResponse>() {

					public void execute(SearchByLocationResponse response) {
						Map<FeaturesSupported, List<Feature>> mapping = new HashMap<FeaturesSupported, List<Feature>>();
						for (Entry<String, List<org.geomajas.layer.feature.Feature>> entry : response.getFeatureMap()
								.entrySet()) {
							FeaturesSupported layer = searchLayer(mapPresenter, entry.getKey());
							List<Feature> features = new ArrayList<Feature>(entry.getValue().size());
							for (org.geomajas.layer.feature.Feature feature : entry.getValue()) {
								features.add(create(feature, layer));
							}
							mapping.put(layer, features);
						}
						callback.execute(mapping);
					}
				});
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private FeaturesSupported searchLayer(MapPresenter mapPresenter, String layerId) {
		if (layerId != null) {
			for (int i = 0; i < mapPresenter.getLayersModel().getLayerCount(); i++) {
				Layer layer = mapPresenter.getLayersModel().getLayer(i);
				if (layer instanceof ServerLayer && layer instanceof FeaturesSupported) {
					ServerLayer<?> serverLayer = (ServerLayer<?>) layer;
					if (layerId.equals(serverLayer.getServerLayerId())) {
						return (FeaturesSupported) layer;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Default implementation of a feature attribute.
	 * 
	 * @author Pieter De Graef
	 */
	@SuppressWarnings({ "serial", "rawtypes" })
	private final class AttributeImpl implements Attribute {

		private final org.geomajas.layer.feature.Attribute<?> delegate;

		private AttributeImpl(org.geomajas.layer.feature.Attribute<?> attribute) {
			this.delegate = attribute;
		}

		@Override
		public Object getValue() {
			return delegate.getValue();
		}
	}
}