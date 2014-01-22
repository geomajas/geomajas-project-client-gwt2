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

package org.geomajas.plugin.wms.client.layer;

import com.google.gwt.core.client.Callback;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.event.FeatureDeselectedEvent;
import org.geomajas.gwt2.client.event.FeatureSelectedEvent;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.plugin.wms.client.WmsServerExtension;
import org.geomajas.plugin.wms.client.capabilities.WmsLayerInfo;
import org.geomajas.plugin.wms.client.layer.config.WmsLayerConfiguration;
import org.geomajas.plugin.wms.client.layer.config.WmsTileConfiguration;
import org.geomajas.plugin.wms.client.service.FeatureCollection;
import org.geomajas.plugin.wms.client.service.WmsService.GetFeatureInfoFormat;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the {@link FeaturesSupportedWmsLayer}.
 *
 * @author Pieter De Graef
 * @author An Buyle (getSelectedFeatures())
 */
public class FeaturesSupportedWmsLayerImpl extends WmsLayerImpl implements FeaturesSupportedWmsLayer {

	private final Map<String, Feature> selection = new HashMap<String, Feature>();

	private String filter;

	public FeaturesSupportedWmsLayerImpl(String title, WmsLayerConfiguration wmsLayerConfig,
			WmsTileConfiguration wmsTileConfig, WmsLayerInfo layerInfo) {
		super(title, wmsLayerConfig, wmsTileConfig, layerInfo);
	}

	// ------------------------------------------------------------------------
	// FeaturesSupported implementation:
	// ------------------------------------------------------------------------

	@Override
	public void setFilter(String filter) {
		this.filter = filter;
		refresh();
	}

	@Override
	public String getFilter() {
		return filter;
	}

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
	public void getFeatureInfo(Coordinate location, Callback<FeatureCollection, String> callback) {
		WmsServerExtension.getInstance().getFeatureService().getFeatureInfo(this, location, callback);
	}

	@Override
	public void getFeatureInfo(Coordinate location, GetFeatureInfoFormat format, Callback<Object, String> callback) {
		WmsServerExtension.getInstance().getFeatureService().getFeatureInfo(this, location, format, callback);
	}

	@Override
	public void searchFeatures(Coordinate coordinate, double tolerance,
			final Callback<FeatureCollection, String> callback) {
		WmsServerExtension.getInstance().getFeatureService().getFeatureInfo(this, coordinate,
				new Callback<FeatureCollection, String>() {

					public void onFailure(String reason) {
						callback.onFailure(reason);
					}

					public void onSuccess(FeatureCollection result) {
						callback.onSuccess(result);
					}
				});
	}
}
