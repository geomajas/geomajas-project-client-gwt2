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
package org.geomajas.gwt2.widget.client.controller.event;

import com.google.web.bindery.event.shared.Event;
import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.feature.Feature;

import java.util.List;

/**
 * Event thrown when features are clicked on the map. Contains the (screen) coordinate where was clicked and the
 * list of features.
 *
 * @author Oliver May
 * @since 2.0.0
 */
@Api(allMethods = true)
public class FeaturesClickedEvent extends Event<FeaturesClickedHandler> {

	private Coordinate coordinate;
	private List<Feature> features;

	public FeaturesClickedEvent(Coordinate coordinate, List<Feature> features) {
		this.coordinate = coordinate;
		this.features = features;
	}

	/**
	 * Get the coordinate where was clicked on the map (in screen space).
	 *
	 * @return the coordinate
	 */
	public Coordinate getCoordinate() {
		return coordinate;
	}

	/**
	 * Get the list of features at this location.
	 *
	 * @return the list of features
	 */
	public List<Feature> getFeatures() {
		return features;
	}


	@Override
	public Type<FeaturesClickedHandler> getAssociatedType() {
		return FeaturesClickedHandler.TYPE;
	}

	@Override
	protected void dispatch(FeaturesClickedHandler handler) {

	}
}
