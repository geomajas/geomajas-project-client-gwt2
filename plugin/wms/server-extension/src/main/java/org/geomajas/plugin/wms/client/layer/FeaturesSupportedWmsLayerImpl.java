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

package org.geomajas.plugin.wms.client.layer;

import com.google.gwt.core.client.Callback;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt2.client.event.FeatureDeselectedEvent;
import org.geomajas.gwt2.client.event.FeatureSelectedEvent;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.plugin.wms.client.WmsServerExtension;
import org.geomajas.plugin.wms.client.capabilities.WmsLayerInfo;
import org.geomajas.plugin.wms.client.service.WmsService.GetFeatureInfoFormat;
import org.geomajas.plugin.wms.server.command.dto.WfsDescribeLayerRequest;
import org.geomajas.plugin.wms.server.command.dto.WfsDescribeLayerResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of the {@link FeaturesSupportedWmsLayer}.
 *
 * @author Pieter De Graef
 * @author An Buyle (getSelectedFeatures())
 */
public class FeaturesSupportedWmsLayerImpl extends WmsLayerImpl implements FeaturesSupportedWmsLayer {

	private final Map<String, Feature> selection = new HashMap<String, Feature>();

	private final List<AttributeDescriptor> descriptors = new ArrayList<AttributeDescriptor>();

	private final Callback<List<AttributeDescriptor>, String> onInitialized;

	public FeaturesSupportedWmsLayerImpl(String title, WmsLayerConfiguration wmsLayerConfig,
			WmsTileConfiguration wmsTileConfig, WmsLayerInfo layerInfo) {
		this(title, wmsLayerConfig, wmsTileConfig, layerInfo, null);
	}

	public FeaturesSupportedWmsLayerImpl(String title, WmsLayerConfiguration wmsLayerConfig,
			WmsTileConfiguration wmsTileConfig, WmsLayerInfo layerInfo, Callback<List<AttributeDescriptor>,
			String> onInitialized) {
		super(title, wmsLayerConfig, wmsTileConfig, layerInfo);
		this.onInitialized = onInitialized;
		wfsDescribeLayer();
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
	public void getFeatureInfo(Coordinate location, Callback<List<Feature>, String> callback) {
		WmsServerExtension.getInstance().getFeatureService().getFeatureInfo(viewPort, this, location, callback);
	}

	@Override
	public void getFeatureInfo(Coordinate location, GetFeatureInfoFormat format, Callback<Object, String> callback) {
		WmsServerExtension.getInstance().getFeatureService().getFeatureInfo(viewPort, this, location, format, callback);
	}

	@Override
	public void searchFeatures(Coordinate coordinate, double tolerance,
			final Callback<List<Feature>, String> callback) {
		WmsServerExtension.getInstance().getFeatureService().getFeatureInfo(viewPort, this, coordinate,
				new Callback<List<Feature>, String>() {

					public void onFailure(String reason) {
						callback.onFailure(reason);
					}

					public void onSuccess(List<Feature> result) {
						callback.onSuccess(result);
					}
				});
	}

	@Override
	public void searchFeatures(Geometry geometry, Callback<List<Feature>, String> callback) {

	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void wfsDescribeLayer() {
		GwtCommand command = new GwtCommand(WfsDescribeLayerRequest.COMMAND_NAME);
		command.setCommandRequest(new WfsDescribeLayerRequest(wmsConfig.getBaseUrl(), id));
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<WfsDescribeLayerResponse>() {

			@Override
			public void execute(WfsDescribeLayerResponse response) {
				descriptors.addAll(response.getAttributeDescriptors());
				onInitialized.onSuccess(getAttributeDescriptors());
			}
		});
	}
}
