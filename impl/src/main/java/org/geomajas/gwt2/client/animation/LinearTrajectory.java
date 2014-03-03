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

package org.geomajas.gwt2.client.animation;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.View;

/**
 * <p>
 * Trajectory that determines its views by using linear interpolation between the initial and final bounds of the
 * trajectory. Notice that this amounts to linear interpolation of the inverted scale as the width/height of the map
 * bounds are inversely proportional to the scale (for constant pixel size of the map).
 * </p>
 *
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
public class LinearTrajectory implements Trajectory {

	protected final View beginView;

	protected final View endView;

	public LinearTrajectory(View beginView, View endView) {
		this.beginView = beginView;
		this.endView = endView;
	}

	@Override
	public View getView(double progress) {
		if (beginView == null || endView == null) {
			throw new IllegalStateException("beginView or endView cannot be null.");
		}

		// Given the last passed view and the progress to the next, calculate the linear values:
		Coordinate startPos = beginView.getPosition();
		Coordinate endPos = endView.getPosition();
		double x = startPos.getX() + progress * (endPos.getX() - startPos.getX());
		double y = startPos.getY() + progress * (endPos.getY() - startPos.getY());

		// Now calculate the NavigationView values to return:
		double startResolution = beginView.getResolution();
		double endResolution = endView.getResolution();

		// width/height vary linearly like x and y, but resolution is inversely proportional !!!
		double resolution = startResolution + progress * (endResolution - startResolution);

		return new View(new Coordinate(x, y), resolution);
	}

	public View getBeginView() {
		return beginView;
	}

	public View getEndView() {
		return endView;
	}
}