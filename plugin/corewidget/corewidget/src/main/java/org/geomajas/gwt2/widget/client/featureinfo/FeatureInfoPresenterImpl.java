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

package org.geomajas.gwt2.widget.client.featureinfo;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.ZoomOption;
import org.geomajas.gwt2.client.map.feature.Feature;

/**
 * Presenter implementation for the {@link org.geomajas.gwt2.widget.client.featureinfo.FeatureInfoWidget}.
 * The presenter can make the view show features and hide or show options. It can also zoom to a feature
 * on the map.
 *
 * @author Youri Flement
 */
public class FeatureInfoPresenterImpl implements FeatureInfoPresenter {

	private FeatureInfoView view;

	private MapPresenter mapPresenter;

	private Feature feature;

	public FeatureInfoPresenterImpl(FeatureInfoView view) {
		this.view = view;
	}

	/**
	 * Zoom to the feature on the map (and select it).
	 *
	 * @param feature the feature to zoom to.
	 */
	@Override
	public void zoomToObject(Feature feature) {
		// Make sure it's only selected once:
		if (!feature.getLayer().isFeatureSelected(feature.getId())) {
			feature.getLayer().selectFeature(feature);
		}

		Bbox bounds = GeometryService.getBounds(feature.getGeometry());
		mapPresenter.getViewPort().applyBounds(bounds, ZoomOption.LEVEL_FIT);
	}

	@Override
	public void setMapPresenter(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
	}

	@Override
	public void setFeature(Feature feature) {
		this.feature = feature;
		view.setFeature(feature);
	}

	public Feature getFeature() {
		return feature;
	}

}
