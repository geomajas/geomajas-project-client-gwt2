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

import com.google.gwt.core.shared.GWT;

/**
 * A trajectory that follow a straight line when zooming in. This trajectory is used by default when zooming in or out.
 * Note that the scale itself is not a linear function (but a squared function), so this trajectory is too.
 * 
 * @author Pieter De Graef
 */
public class StraightLineTrajectory implements Trajectory {

	protected final View beginView;

	protected final View endView;

	public StraightLineTrajectory(View beginView, View endView) {
		this.beginView = beginView;
		this.endView = endView;
		GWT.log("Trajectory: From = " + beginView.getPosition() + ", TO = " + endView.getPosition());
	}

	// ------------------------------------------------------------------------
	// Trajectory implementation:
	// ------------------------------------------------------------------------

	@Override
	public View getView(double progress) {
		// Given the last passed view and the progress to the next, calculate the linear values:
		Coordinate startPos = beginView.getPosition();
		Coordinate endPos = endView.getPosition();

		double deltaX = getDelta(startPos.getX(), endPos.getX());
		double deltaY = getDelta(startPos.getY(), endPos.getY());

		double startLog2X = log2(startPos.getX() + deltaX);
		double endLog2X = log2(endPos.getX() + deltaX);
		double x = Math.pow(2, startLog2X + progress * (endLog2X - startLog2X)) - deltaX;

		double startLog2Y = log2(startPos.getY() + deltaY);
		double endLog2Y = log2(endPos.getY() + deltaY);
		double y = Math.pow(2, startLog2Y + progress * (endLog2Y - startLog2Y)) - deltaY;

		// Now calculate the NavigationView values to return:
		double startLog2Scale = log2(beginView.getScale());
		double endLog2Scale = log2(endView.getScale());
		double scale = Math.pow(2, startLog2Scale + progress * (endLog2Scale - startLog2Scale));

		// GWT.log("Trajectory update: " + x + ", " + y);
		return new View(new Coordinate(x, y), scale);
	}

	private double log2(double value) {
		if (value == 0.0) {
			value = Double.MIN_VALUE;
		}
		double logValue = Math.log(value) / Math.log(2);
		if (Double.isNaN(logValue)) {
			return -Double.NEGATIVE_INFINITY;
		}
		return logValue;
	}

	// Return a delta value that would make both v1 and v2 equal than or bigger then 1.
	private double getDelta(double v1, double v2) {
		double delta = 0.0;
		if (v1 < v2) {
			if (v1 < 1) {
				return 1 - v1;
			}
		} else {
			if (v2 < 1) {
				return 1 - v2;
			}
		}
		return delta;
	}
	//
	// public static void main(String[] args) {
	// View v1 = new ViewImpl(new Coordinate(-1, -1), 1);
	// View v2 = new ViewImpl(new Coordinate(7, 7), 4);
	// StraightLineTrajectory t = new StraightLineTrajectory(v1, v2);
	// System.out.println(t.getView(0.0));
	// System.out.println(t.getView(0.5));
	// System.out.println(t.getView(1.0));
	// }
}