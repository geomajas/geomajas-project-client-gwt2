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
package org.geomajas.gwt2.widget.client.feature.featureselectbox;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.MapPresenter;

/**
 * MVP presenter for {@link org.geomajas.gwt2.widget.client.FeatureSelectBox}.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface FeatureSelectBoxPresenter {

	/**
	 * Called when user selects a feature from the list.
	 * 
	 * @param label
	 */
	void onFeatureSelected(String label);

	/**
	 * Activate the presenter.
	 * 
	 * @param mapPresenter
	 */
	void onActivate(MapPresenter mapPresenter);

	/**
	 * Deactivate the presenter.
	 */
	void onDeactivate();

	/**
	 * Called when users clicks map in new position.
	 * 
	 * @param y
	 * @param x
	 * @param worldCoordinate
	 */
	void onClick(int x, int y, Coordinate worldCoordinate);

	/**
	 * Set the pixel buffer to search for features.
	 * 
	 * @param pixelBuffer
	 */
	void setPixelBuffer(int pixelBuffer);

	void setSingleFeature(boolean singleFeature);

}
