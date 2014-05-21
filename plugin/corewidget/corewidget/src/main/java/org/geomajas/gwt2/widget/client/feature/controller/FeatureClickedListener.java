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

package org.geomajas.gwt2.widget.client.feature.controller;

import java.util.logging.Logger;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.controller.AbstractMapController;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.widget.client.feature.featureselectbox.FeatureSelectBox;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;

/**
 * Controller that provides a feature based on a location clicked on the map. When multiple features are found on the
 * location, a drop down box is presented from where a single feature can be selected. The controller fires
 * {@link org.geomajas.gwt2.widget.client.event.event.FeatureClickedEvent}s.
 * 
 * @author Dosi Bingov
 * @author Oliver May
 * @author Jan De Moerloose
 * 
 */
public class FeatureClickedListener extends AbstractMapController {

	private Logger log = Logger.getLogger(FeatureSelectBox.class.getName());

	private FeatureSelectBox featureSelectBox;

	private static final int MIN_PIXEL_DISTANCE = 120;

	protected Coordinate clickedPosition;

	public FeatureClickedListener() {
		this(true);
	}
	
	public FeatureClickedListener(boolean singleFeature) {
		featureSelectBox = new FeatureSelectBox();
		setSingleFeature(singleFeature);
	}

	public void setSingleFeature(boolean singleFeature) {
		featureSelectBox.setSingleFeature(singleFeature);
	}

	@Override
	public void onActivate(MapPresenter mapPresenter) {
		super.onActivate(mapPresenter);
		featureSelectBox.onActivate(mapPresenter);
	}

	@Override
	public void onDeactivate(MapPresenter mapPresenter) {
		super.onDeactivate(mapPresenter);
		featureSelectBox.onDeactivate();
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		if (clickedPosition != null) {
			if (getLocation(event, RenderSpace.SCREEN).distance(clickedPosition) > MIN_PIXEL_DISTANCE) {
				featureSelectBox.onDeactivate();
			}
		}
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		log.info("FeatureSelectedListener => mouse down fired");
		clickedPosition = getLocation(event, RenderSpace.SCREEN);
		featureSelectBox.onClick(event.getClientX(), event.getClientY(), getLocation(event, RenderSpace.WORLD));
	}

}