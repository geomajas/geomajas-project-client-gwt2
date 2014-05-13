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
package org.geomajas.gwt2.widget.client.featureselectbox.presenter;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.MapPresenter;


public interface FeatureSelectBoxPresenter {

	void onFeatureSelected(String label);

	void onActivate(MapPresenter mapPresenter);

	void onDeactivate();

	void onClick(Coordinate worldCoordinate);

	void setPixelBuffer(int pixelBuffer);

}
