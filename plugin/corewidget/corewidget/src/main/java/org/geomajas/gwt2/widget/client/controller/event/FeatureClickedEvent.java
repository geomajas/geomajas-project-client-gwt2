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

package org.geomajas.gwt2.widget.client.controller.event;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.feature.Feature;
import com.google.web.bindery.event.shared.Event;

/**
 * Event which is passed when a feature is clicked.
 *
 * @author Dosi Bingov
 * @since 2.0.0
 */
@Api(allMethods = true)
public class FeatureClickedEvent extends Event<FeatureClickedHandler> {

	private Coordinate coordinate;
	private final Feature feature;

	/**
	 * Create an event for the specified feature.
	 *
	 * @param coordinate the coordinate where this feature is selected
	 * @param feature the selected feature
	 */
	public FeatureClickedEvent(Coordinate coordinate, Feature feature) {
		this.feature = feature;
		this.coordinate = coordinate;
	}

	/**
	 * Get the selected feature.
	 *
	 * @return The feature.
	 */
	public Feature getFeature() {
		return feature;
	}

	@Override
	public Type<FeatureClickedHandler> getAssociatedType() {
		return FeatureClickedHandler.TYPE;
	}

	@Override
	protected void dispatch(FeatureClickedHandler featureClickHandler) {
		featureClickHandler.onFeatureClicked(this);
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}
}