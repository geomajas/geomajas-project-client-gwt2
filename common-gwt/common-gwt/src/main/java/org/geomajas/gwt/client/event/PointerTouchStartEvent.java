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

import org.geomajas.annotation.Api;

import com.google.gwt.event.dom.client.TouchEvent;

/**
 * Represents a "touchified" pointer start event.
 * 
 * @author Jan De Moerloose
 * @since 2.4.0
 */
@Api(allMethods = true)
public class PointerTouchStartEvent extends TouchEvent<PointerTouchStartHandler> {

	/**
	 * Event type for touch start events. Represents the meta-data associated with this event.
	 */
	private static final Type<PointerTouchStartHandler> TYPE = new Type<PointerTouchStartHandler>(
			PointerEventType.POINTER_DOWN.getType(), new PointerTouchStartEvent());

	/**
	 * Gets the event type associated with touch start events.
	 *
	 * @return the handler type
	 */
	public static Type<PointerTouchStartHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<PointerTouchStartHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PointerTouchStartHandler handler) {
		handler.onPointerTouchStart(this);
	}

}
