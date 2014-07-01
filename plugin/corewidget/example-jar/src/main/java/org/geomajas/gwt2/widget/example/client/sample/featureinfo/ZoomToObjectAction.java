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

package org.geomajas.gwt2.widget.example.client.sample.featureinfo;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.ZoomOption;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.widget.client.featureinfo.FeatureAction;

/**
 * Implementation of a {@link FeatureAction} to zoom to a feature on the map.
 *
 * @author Youri Flement
 */
public class ZoomToObjectAction extends Button implements FeatureAction, ClickHandler {

	protected Feature feature;

	private MapPresenter mapPresenter;

	public ZoomToObjectAction(MapPresenter mapPresenter) {
		setText("Zoom to object");
		addClickHandler(this);
		this.mapPresenter = mapPresenter;
	}

	@Override
	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	@Override
	public void onClick(ClickEvent event) {
		if (feature != null) {
			// Make sure it's only selected once:
			if (!feature.getLayer().isFeatureSelected(feature.getId())) {
				feature.getLayer().selectFeature(feature);
			}

			// Get bounds and zoom:
			Bbox bounds = GeometryService.getBounds(feature.getGeometry());
			mapPresenter.getViewPort().applyBounds(bounds, ZoomOption.LEVEL_FIT);
		}
	}
}
