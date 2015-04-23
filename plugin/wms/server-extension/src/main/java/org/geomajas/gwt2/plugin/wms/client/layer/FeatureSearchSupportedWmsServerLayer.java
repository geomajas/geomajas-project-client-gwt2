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

package org.geomajas.gwt2.plugin.wms.client.layer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.event.FeatureDeselectedEvent;
import org.geomajas.gwt2.client.event.FeatureSelectedEvent;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.client.map.attribute.GeometryAttributeType;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.feature.query.GeometryCriterionDto;
import org.geomajas.gwt2.client.map.layer.FeaturesSupported;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureTypeDescriptionInfo;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsGetFeatureRequest;
import org.geomajas.gwt2.plugin.wfs.server.command.dto.WfsGetFeatureResponse;
import org.geomajas.gwt2.plugin.wfs.server.dto.WfsVersionDto;
import org.geomajas.gwt2.plugin.wms.client.WmsServerExtension;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsLayerInfo;

import com.google.gwt.core.client.Callback;

/**
 * Server-based implementation of {@link FeatureSearchSupported} WMS layer.
 *
 * @author Jan De Moerloose
 */
public class FeatureSearchSupportedWmsServerLayer extends FeatureInfoSupportedWmsServerLayer implements
		FeaturesSupported, FeatureSearchSupported {

	private final Map<String, Feature> selection = new HashMap<String, Feature>();

	private final List<AttributeDescriptor> descriptors;

	private final WfsFeatureTypeDescriptionInfo wfsConfig;

	private String defaultGeometryName;

	/**
	 * Create a WMS layer with feature info support and a schema for the features.
	 * 
	 * @param title
	 * @param crs
	 * @param wmsLayerConfig
	 * @param wmsTileConfig
	 * @param layerInfo
	 * @param descriptors
	 */
	public FeatureSearchSupportedWmsServerLayer(String title, String crs, WmsLayerConfiguration wmsLayerConfig,
			TileConfiguration wmsTileConfig, WmsLayerInfo layerInfo, WfsFeatureTypeDescriptionInfo wfsConfig) {
		super(title, crs, wmsLayerConfig, wmsTileConfig, layerInfo);
		this.descriptors = wfsConfig.getAttributeDescriptors();
		for (AttributeDescriptor attributeDescriptor : descriptors) {
			if (attributeDescriptor.getType() instanceof GeometryAttributeType) {
				defaultGeometryName = attributeDescriptor.getName();
			}
		}
		this.wfsConfig = wfsConfig;
	}

	// ------------------------------------------------------------------------
	// FeaturesSupported implementation:
	// ------------------------------------------------------------------------

	@Override
	public boolean isFeatureSelected(String featureId) {
		return selection.containsKey(featureId);
	}

	@Override
	public boolean selectFeature(Feature feature) {
		if (!selection.containsValue(feature) && feature.getLayer() == this) {
			selection.put(feature.getId(), feature);
			eventBus.fireEvent(new FeatureSelectedEvent(this, feature));
		}
		return false;
	}

	@Override
	public boolean deselectFeature(Feature feature) {
		if (selection.containsKey(feature.getId())) {
			selection.remove(feature.getId());
			eventBus.fireEvent(new FeatureDeselectedEvent(this, feature));
			return true;
		}
		return false;
	}

	@Override
	public void clearSelectedFeatures() {
		for (Feature feature : selection.values()) {
			eventBus.fireEvent(new FeatureDeselectedEvent(this, feature));
		}
		selection.clear();
	}

	@Override
	public Collection<Feature> getSelectedFeatures() {
		return selection.values();
	}

	@Override
	public List<AttributeDescriptor> getAttributeDescriptors() {
		return descriptors;
	}

	@Override
	public void searchFeatures(Geometry geometry, final Callback<List<Feature>, String> callback) {
		Integer maxC = WmsServerExtension.getInstance().getHintValue(WmsServerExtension.GET_FEATUREINFO_MAX_COORDS);
		Integer maxF = WmsServerExtension.getInstance().getHintValue(WmsServerExtension.GET_FEATUREINFO_MAX_FEATURES);

		GeometryCriterionDto criterionDto = new GeometryCriterionDto(defaultGeometryName,
				GeometryCriterionDto.INTERSECTS, geometry);
		WfsGetFeatureRequest request = new WfsGetFeatureRequest();
		request.setBaseUrl(wfsConfig.getBaseUrl());
		request.setCriterion(criterionDto);
		request.setCrs(getCrs());
		request.setMaxCoordsPerFeature(maxC);
		request.setMaxFeatures(maxF);
		request.setSchema(wfsConfig.getAttributeDescriptors());
		request.setTypeName(wfsConfig.getTypeName());
		request.setVersion(WfsVersionDto.V1_0_0);

		GwtCommand command = new GwtCommand(WfsGetFeatureRequest.COMMAND_NAME);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<WfsGetFeatureResponse>() {

			@Override
			public void execute(WfsGetFeatureResponse response) {
				List<Feature> features = new ArrayList<Feature>();
				for (org.geomajas.layer.feature.Feature feature : response.getFeatureCollection().getFeatures()) {
					Feature newFeature = GeomajasServerExtension.getInstance().getServerFeatureService()
							.create(feature, FeatureSearchSupportedWmsServerLayer.this);
					features.add(newFeature);
				}
				callback.onSuccess(features);
			}
		});
	}

}
