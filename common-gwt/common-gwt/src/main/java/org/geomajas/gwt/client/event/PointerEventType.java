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

/**
 * Type enumeration of pointer events.
 * 
 * @author Jan De Moerloose
 * @since 2.4.0
 *
 */
@Api(allMethods = true)
public enum PointerEventType {

	/**
	 * pointer down.
	 */
	POINTER_DOWN, // down
	/**
	 * pointer move.
	 */
	POINTER_MOVE, // move
	/**
	 * pointer up.
	 */
	POINTER_UP, // up
	/**
	 * pointer cancel.
	 */
	POINTER_CANCEL; // cancel

	/**
	 * Get the type of the event.
	 * 
	 * @return
	 */
	public String getType() {
		return PointerEvents.IMPL.getNativeTypeName(this);
	}

	/**
	 * Get the pointer event from its type.
	 * 
	 * @param type
	 * @return the event
	 */
	public static PointerEventType fromType(String type) {
		for (PointerEventType eventType : PointerEventType.values()) {
			if (type.equals(eventType.getType())) {
				return eventType;
			}
		}
		throw new IllegalArgumentException("could not determine event type for " + type);
	}

}
