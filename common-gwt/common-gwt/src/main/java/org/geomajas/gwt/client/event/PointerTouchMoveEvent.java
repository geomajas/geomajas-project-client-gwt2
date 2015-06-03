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
package org.geomajas.gwt.client.event;

import com.google.gwt.event.dom.client.TouchEvent;

/**
 * Represents a "touchified" pointer move event.
 * 
 * @author Jan De Moerloose
 */
public class PointerTouchMoveEvent extends TouchEvent<PointerTouchMoveHandler> {

	/**
	 * Event type for touch Move events. Represents the meta-data associated with this event.
	 */
	private static final Type<PointerTouchMoveHandler> TYPE = new Type<PointerTouchMoveHandler>(
			PointerEventType.POINTER_MOVE.getType(), new PointerTouchMoveEvent());

	/**
	 * Gets the event type associated with touch Move events.
	 *
	 * @return the handler type
	 */
	public static Type<PointerTouchMoveHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<PointerTouchMoveHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PointerTouchMoveHandler handler) {
		handler.onTouchMove(this);
	}

}
