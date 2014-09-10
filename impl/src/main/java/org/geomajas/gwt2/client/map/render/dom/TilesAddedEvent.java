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

package org.geomajas.gwt2.client.map.render.dom;

import com.google.web.bindery.event.shared.Event;
import org.geomajas.gwt2.client.animation.Trajectory;
import org.geomajas.gwt2.client.map.View;

/**
 * Event which is passed when (typically unloaded) tiles are added to the map.
 *
 * @author Youri Flement
 */
public class TilesAddedEvent extends Event<TilesAddedHandler> {

	/**
	 * The view on the map.
	 */
	private View view;

	/**
	 * The current trajectory.
	 */
	private Trajectory trajectory;

	/**
	 * Create an event that indicates tiles were added to the map.
	 *
	 * @param view       The view on the map.
	 * @param trajectory The current trajectory.
	 */
	public TilesAddedEvent(View view, Trajectory trajectory) {
		this.view = view;
		this.trajectory = trajectory;
	}

	/**
	 * Get the view on the map where the tiles were added to.
	 *
	 * @return The view.
	 */
	public View getView() {
		return view;
	}

	/**
	 * Get the current trajectory.
	 *
	 * @return The trajectory.
	 */
	public Trajectory getTrajectory() {
		return trajectory;
	}

	@Override
	public Type<TilesAddedHandler> getAssociatedType() {
		return TilesAddedHandler.TYPE;
	}

	@Override
	protected void dispatch(TilesAddedHandler handler) {
		handler.onTilesAdded(this);
	}
}
