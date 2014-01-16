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

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.View;

/**
 * Factory for creating the most basic navigation animations, such as zooming animations etc.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public final class NavigationAnimationFactory {

	private NavigationAnimationFactory() {
	}

	/**
	 * Create a new {@link NavigationAnimation} with a provided trajectory.
	 * 
	 * @param mapPresenter
	 *            The map onto which you want the animation to run.
	 * @param eventBus
	 *            The EventBus that will fire navigation events. Must come from the same map as the ViewPort.
	 * @param trajectory
	 *            The trajectory to follow while navigating.
	 * @return The animation. Just call "run" to have it animate the map.
	 */
	public static NavigationAnimation create(MapPresenter mapPresenter, Trajectory trajectory, int millis) {
		return new NavigationAnimationImpl(mapPresenter.getViewPort(), mapPresenter.getEventBus(), trajectory, millis);
	}

	/**
	 * Create a new zoom in {@link NavigationAnimation}. It will have the map zoom in to the next fixed scale.
	 * 
	 * @param mapPresenter
	 *            The map onto which we need a zoom in animation.
	 * @return The animation. Just call "run" to have it animate the map.
	 */
	public static NavigationAnimation createZoomIn(MapPresenter mapPresenter) {
		View beginView = mapPresenter.getViewPort().getView();
		View endView = mapPresenter.getViewPort().getView();
		int index = mapPresenter.getViewPort().getFixedScaleIndex(mapPresenter.getViewPort().getScale());
		if (index < mapPresenter.getViewPort().getFixedScaleCount() - 1) {
			endView = new View(mapPresenter.getViewPort().getPosition(), mapPresenter.getViewPort().getFixedScale(
					index + 1));
		}
		return new NavigationAnimationImpl(mapPresenter.getViewPort(), mapPresenter.getEventBus(),
				new LinearTrajectory(beginView, endView), getMillis(mapPresenter));
	}

	/**
	 * Create a new zoom in {@link NavigationAnimation} that zooms in to the provided end view.
	 * 
	 * @param mapPresenter
	 *            The map onto which you want the zoom in animation to run.
	 * @param endView
	 *            The final view to arrive at when the animation finishes.
	 * @return The animation. Just call "run" to have it animate the map.
	 */
	public static NavigationAnimation createZoomIn(MapPresenter mapPresenter, View endView) {
		return new NavigationAnimationImpl(mapPresenter.getViewPort(), mapPresenter.getEventBus(),
				new LinearTrajectory(mapPresenter.getViewPort().getView(), endView), getMillis(mapPresenter));
	}

	/**
	 * Create a new zoom in {@link NavigationAnimation} that zooms in one fixed scale level, to the requested position.
	 * 
	 * @param mapPresenter
	 *            The map onto which you want the zoom in animation to run.
	 * @param position
	 *            The position to arrive at.
	 * @return The animation. Just call "run" to have it animate the map.
	 */
	public static NavigationAnimation createZoomIn(MapPresenter mapPresenter, Coordinate position) {
		View endView = null;
		int index = mapPresenter.getViewPort().getFixedScaleIndex(mapPresenter.getViewPort().getScale());
		if (index < mapPresenter.getViewPort().getFixedScaleCount() - 1) {
			endView = new View(position, mapPresenter.getViewPort().getFixedScale(index + 1));
		} else {
			endView = new View(position, mapPresenter.getViewPort().getScale());
		}
		return new NavigationAnimationImpl(mapPresenter.getViewPort(), mapPresenter.getEventBus(),
				new LinearTrajectory(mapPresenter.getViewPort().getView(), endView), getMillis(mapPresenter));
	}

	/**
	 * Create a new zoom out {@link NavigationAnimation}. It will have the map zoom out to the next fixed scale.
	 * 
	 * @param mapPresenter
	 *            The map onto which you want the zoom out animation to run.
	 * @return The animation. Just call "run" to have it animate the map.
	 */
	public static NavigationAnimation createZoomOut(MapPresenter mapPresenter) {
		View beginView = mapPresenter.getViewPort().getView();
		View endView = mapPresenter.getViewPort().getView();
		int index = mapPresenter.getViewPort().getFixedScaleIndex(mapPresenter.getViewPort().getScale());
		if (index > 0) {
			endView = new View(mapPresenter.getViewPort().getPosition(), mapPresenter.getViewPort().getFixedScale(
					index - 1));
		}
		return new NavigationAnimationImpl(mapPresenter.getViewPort(), mapPresenter.getEventBus(),
				new LinearTrajectory(beginView, endView), getMillis(mapPresenter));
	}

	/**
	 * Create a new zoom out {@link NavigationAnimation}. It will have the map zoom out to the next fixed scale.
	 * 
	 * @param mapPresenter
	 *            The map onto which you want the zoom out animation to run.
	 * @param position
	 *            The target position to zoom out to.
	 * @return The animation. Just call "run" to have it animate the map.
	 */
	public static NavigationAnimation createZoomOut(MapPresenter mapPresenter, Coordinate position) {
		View beginView = mapPresenter.getViewPort().getView();
		View endView = null;
		int index = mapPresenter.getViewPort().getFixedScaleIndex(mapPresenter.getViewPort().getScale());
		if (index > 0) {
			endView = new View(position, mapPresenter.getViewPort().getFixedScale(index - 1));
		} else {
			endView = new View(position, mapPresenter.getViewPort().getScale());
		}
		return new NavigationAnimationImpl(mapPresenter.getViewPort(), mapPresenter.getEventBus(),
				new LinearTrajectory(beginView, endView), getMillis(mapPresenter));
	}

	/**
	 * Create a new panning {@link NavigationAnimation}. It will have the map translate to the requested coordinate,
	 * keeping the same scale level.
	 * 
	 * @param mapPresenter
	 *            The map onto which you want the panning animation to run.
	 * @param target
	 *            The target coordinate to arrive at when the animation finishes.
	 * @return The animation. Just call "run" to have it animate the map.
	 */
	public static NavigationAnimation createPanning(MapPresenter mapPresenter, Coordinate target) {
		View beginView = mapPresenter.getViewPort().getView();
		View endView = new View(target, beginView.getScale());
		return new NavigationAnimationImpl(mapPresenter.getViewPort(), mapPresenter.getEventBus(),
				new LinearTrajectory(beginView, endView), getMillis(mapPresenter));
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private static int getMillis(MapPresenter mapPresenter) {
		return mapPresenter.getConfiguration().getHintValue(MapConfiguration.ANIMATION_TIME);
	}
}
