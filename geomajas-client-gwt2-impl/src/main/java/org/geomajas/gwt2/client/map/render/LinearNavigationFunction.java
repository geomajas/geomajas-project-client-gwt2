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

package org.geomajas.gwt2.client.map.render;

/**
 * Implementation of the navigation function that implements a linear function (a straight line).
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
public class LinearNavigationFunction extends AbstractNavigationFunction {

	@Override
	public double[] getLocation(double progress) {
		double x = beginX + progress * (endX - beginX);
		double y = beginY + progress * (endY - beginY);
		// width and height vary linearly like x and y, but scale is inversely proportional !!!
		double z = 1 / (1 / beginZ + progress * (1 / endZ - 1 / beginZ));
		return new double[] { x, y, z };
	}
}