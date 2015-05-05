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

import org.geomajas.gwt2.client.event.NavigationStartEvent;
import org.geomajas.gwt2.client.event.NavigationStopEvent;
import org.geomajas.gwt2.client.event.NavigationUpdateEvent;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.View;
import org.geomajas.gwt2.client.map.ViewPort;

import com.google.gwt.animation.client.Animation;

/**
 * Default implementation that uses the GWT {@link Animation} class as a base. This implementation sets scale and
 * position as it progresses, and fires the correct events.
 * 
 * @author Pieter De Graef
 */
public class NavigationAnimationImpl extends Animation implements NavigationAnimation {

	private final ViewPort viewPort;

	private final MapEventBus eventBus;

	private final Trajectory trajectory;

	private final int millis;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a new instance. Usually this is done through the {@link NavigationAnimationFactory}.
	 * 
	 * @param viewPort
	 *            The map ViewPort.
	 * @param eventBus
	 *            The EventBus for firing events onto.
	 * @param trajectory
	 *            The animation trajectory to run.
	 * @param millis
	 *            The animation length in milliseconds.
	 */
	public NavigationAnimationImpl(ViewPort viewPort, MapEventBus eventBus, Trajectory trajectory, int millis) {
		this.viewPort = viewPort;
		this.eventBus = eventBus;
		this.trajectory = trajectory;
		this.millis = millis;
	}

	// ------------------------------------------------------------------------
	// NavigationAnimation implementation:
	// ------------------------------------------------------------------------

	@Override
	public void run() {
		run(getAnimationMillis());
	}

	@Override
	public View getView(double progress) {
		return trajectory.getView(progress);
	}

	@Override
	public View getBeginView() {
		return trajectory.getView(0.0);
	}

	@Override
	public View getEndView() {
		return trajectory.getView(1.0);
	}

	// ------------------------------------------------------------------------
	// GWT Animation implementation:
	// ------------------------------------------------------------------------

	@Override
	protected void onUpdate(double progress) {
		View view = trajectory.getView(progress);
		viewPort.applyView(view, progress < 1);
		eventBus.fireEvent(new NavigationUpdateEvent(this, view));
	}

	@Override
	protected void onStart() {
		eventBus.fireEvent(new NavigationStartEvent(this));
		super.onStart();
	}

	@Override
	protected void onCancel() {
		// Do not complete the animation, but leave it as is....
	}

	@Override
	protected void onComplete() {
		super.onComplete();
		eventBus.fireEvent(new NavigationStopEvent(getEndView()));
	}

	@Override
	public int getAnimationMillis() {
		return millis;
	}
}