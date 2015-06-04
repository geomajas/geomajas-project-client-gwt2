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
 * Represents a "touchified" pointer up event.
 * 
 * @author Jan De Moerloose
 */
public class PointerTouchEndEvent extends TouchEvent<PointerTouchEndHandler> {

	/**
	 * Event type for touch End events. Represents the meta-data associated with this event.
	 */
	private static final Type<PointerTouchEndHandler> TYPE = new Type<PointerTouchEndHandler>(
			PointerEventType.POINTER_UP.getType(), new PointerTouchEndEvent());

	/**
	 * Gets the event type associated with touch End events.
	 *
	 * @return the handler type
	 */
	public static Type<PointerTouchEndHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<PointerTouchEndHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PointerTouchEndHandler handler) {
		handler.onPointerTouchEnd(this);
	}

}
