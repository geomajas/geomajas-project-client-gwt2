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

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Implemented by widgets that handle all "touchified" pointer events.
 * 
 * @author Jan De Moerloose
 *
 */
public interface HasAllPointerTouchHandlers {

	/**
	 * Adds a {@link PointerTouchStartEvent} handler.
	 *
	 * @param handler the touch start handler
	 * @return {@link HandlerRegistration} used to remove this handler
	 */
	HandlerRegistration addPointerTouchStartHandler(PointerTouchStartHandler handler);

	/**
	 * Adds a {@link PointerTouchEndEvent} handler.
	 *
	 * @param handler the touch start handler
	 * @return {@link HandlerRegistration} used to remove this handler
	 */
	HandlerRegistration addPointerTouchEndHandler(PointerTouchEndHandler handler);

	/**
	 * Adds a {@link PointerTouchMoveEvent} handler.
	 *
	 * @param handler the touch start handler
	 * @return {@link HandlerRegistration} used to remove this handler
	 */
	HandlerRegistration addPointerTouchMoveHandler(PointerTouchMoveHandler handler);

	/**
	 * Adds a {@link PointerTouchCancelEvent} handler.
	 *
	 * @param handler the touch start handler
	 * @return {@link HandlerRegistration} used to remove this handler
	 */
	HandlerRegistration addPointerTouchCancelHandler(PointerTouchCancelHandler handler);

}
