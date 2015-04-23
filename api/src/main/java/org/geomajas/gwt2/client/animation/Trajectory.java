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

package org.geomajas.gwt2.client.animation;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.gwt2.client.map.View;

/**
 * Mathematical trajectory for a map navigation animation. It defines the course to go from A to B. Example
 * implementations may implement a straight line, or a bezier curve, or...
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@UserImplemented
@Api(allMethods = true)
public interface Trajectory {

	/**
	 * Get the view at a certain point on the trajectory.
	 * 
	 * @param progress
	 *            A number ranging from 0 to 1, where 0 represents the starting point and 1 the end point.
	 * @return The view associated with the progress along the trajectory.
	 */
	View getView(double progress);
}