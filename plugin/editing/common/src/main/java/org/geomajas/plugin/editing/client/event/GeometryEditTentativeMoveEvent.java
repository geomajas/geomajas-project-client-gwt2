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
package org.geomajas.plugin.editing.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that reports mouse move events that don't necessarily have to commit to anything.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public class GeometryEditTentativeMoveEvent extends GwtEvent<GeometryEditTentativeMoveHandler> {

	private final Coordinate origin;

	private final Coordinate currentPosition;

	/**
	 * Create a new event.
	 * 
	 * @param origin
	 *            The original position.
	 * @param currentPosition
	 *            The current tentative position.
	 */
	public GeometryEditTentativeMoveEvent(Coordinate origin, Coordinate currentPosition) {
		this.origin = origin;
		this.currentPosition = currentPosition;
	}

	@Override
	public Type<GeometryEditTentativeMoveHandler> getAssociatedType() {
		return GeometryEditTentativeMoveHandler.TYPE;
	}

	@Override
	protected void dispatch(GeometryEditTentativeMoveHandler geometryEditInsertMoveHandler) {
		geometryEditInsertMoveHandler.onTentativeMove(this);
	}

	/**
	 * Get the original position.
	 * 
	 * @return The position.
	 */
	public Coordinate getOrigin() {
		return origin;
	}

	/**
	 * Get the tentative move position (where the mouse currently is).
	 * 
	 * @return The current tentative move position.
	 */
	public Coordinate getCurrentPosition() {
		return currentPosition;
	}
}