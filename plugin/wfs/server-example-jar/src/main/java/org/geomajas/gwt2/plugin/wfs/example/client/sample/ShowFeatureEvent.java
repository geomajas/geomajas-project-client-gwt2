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
package org.geomajas.gwt2.plugin.wfs.example.client.sample;

import org.geomajas.gwt2.client.map.feature.Feature;

import com.google.web.bindery.event.shared.Event;

/**
 * Event that indicates a feature should be shown.
 * 
 * @author Jan De Moerloose
 *
 */
public class ShowFeatureEvent extends Event<ShowFeatureHandler> {

	private Feature feature;

	/**
	 * Main constructor.
	 *
	 * @param feature
	 */
	public ShowFeatureEvent(Feature feature) {
		this.feature = feature;
	}

	/**
	 * Get the list of features at this location.
	 *
	 * @return the list of features
	 */
	public Feature getFeature() {
		return feature;
	}

	@Override
	public Type<ShowFeatureHandler> getAssociatedType() {
		return ShowFeatureHandler.TYPE;
	}

	@Override
	protected void dispatch(ShowFeatureHandler handler) {
		handler.onShowFeature(this);
	}
}
