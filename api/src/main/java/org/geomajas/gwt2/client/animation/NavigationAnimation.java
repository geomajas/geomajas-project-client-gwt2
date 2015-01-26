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
import org.geomajas.gwt2.client.map.View;

/**
 * Definition of an animation that makes the map navigate from the begin view to the end view over a given trajectory.
 * Animations should be registered in the {@link org.geomajas.gwt2.client.map.ViewPort} if you want to run them. Note
 * that the {@link org.geomajas.gwt2.client.map.ViewPort} does not officially support animations for Internet Explorer
 * 8.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public interface NavigationAnimation extends Trajectory {

	/** Run this animation. It will navigate the map from "beginView" to "endView". */
	void run();

	/**
	 * Get the length of the animation in milliseconds.
	 * 
	 * @return The animation time.
	 */
	int getAnimationMillis();

	/** Cancel the navigation animation. Leave everything as is. */
	void cancel();

	/**
	 * Get the starting point for this animation. In most situations this will equal the current position on the map.
	 * 
	 * @return The begin view.
	 */
	View getBeginView();

	/**
	 * Get the end point for this animation. This is where it all ends....
	 * 
	 * @return The end view.
	 */
	View getEndView();
}