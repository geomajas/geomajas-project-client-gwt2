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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.geometry.Geometry;
import org.geomajas.gwt2.client.event.FeatureDeselectedEvent;
import org.geomajas.gwt2.client.event.FeatureSelectedEvent;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.client.map.attribute.GeometryAttributeType;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.feature.query.Criterion;
import org.geomajas.gwt2.client.map.feature.query.GeometryCriterion;
import org.geomajas.gwt2.client.map.feature.query.Query;
import org.geomajas.gwt2.client.map.layer.FeaturesSupported;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.plugin.wfs.client.WfsServerExtension;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureCollectionInfo;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureTypeDescriptionInfo;
import org.geomajas.gwt2.plugin.wfs.client.service.WfsService;
import org.geomajas.gwt2.plugin.wfs.client.service.WfsService.WfsVersion;
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

		WfsService wfsService = WfsServerExtension.getInstance().getWfsService();

		Criterion criterion = wfsService.buildCriterion().attribute(defaultGeometryName)
				.operation(GeometryCriterion.INTERSECTS).value(geometry).build();

		Query query = wfsService.buildQuery().criterion(criterion)
				.attributeDescriptors(wfsConfig.getAttributeDescriptors()).maxCoordinates(maxC).maxFeatures(maxF)
				.crs(getCrs()).build();

		WfsServerExtension
				.getInstance()
				.getWfsService()
				.getFeatures(WfsVersion.V1_0_0, this, wfsConfig.getBaseUrl(), wfsConfig.getTypeName(), query,
						new Callback<WfsFeatureCollectionInfo, String>() {

							@Override
							public void onSuccess(WfsFeatureCollectionInfo result) {
								callback.onSuccess(result.getFeatures());
							}

							@Override
							public void onFailure(String reason) {
								callback.onFailure(reason);
							}
						});

	}

}
