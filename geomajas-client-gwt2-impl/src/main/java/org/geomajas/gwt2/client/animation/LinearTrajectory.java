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

package org.geomajas.gwt2.client.animation;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.View;

/**
 * <p>
 * Trajectory that implements a straight line. Literally. Note that the scale itself is not a linear function (but a
 * squared function), so using this for zooming will not follow the normal zooming course. It will give you a cool
 * gliding effect though.
 * </p>
 * <p>
 * If you want a straight line when navigating, use the {@link StraightLineTrajectory}.
 * </p>
 * 
 * @author Pieter De Graef
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
		double startScale = beginView.getScale();
		double endScale = endView.getScale();
		double scale = startScale + progress * (endScale - startScale);

		return new View(new Coordinate(x, y), scale);
	}

	public View getBeginView() {
		return beginView;
	}

	public View getEndView() {
		return endView;
	}
}