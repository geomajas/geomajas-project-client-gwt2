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
package org.geomajas.gwt.client.event.impl;

import org.geomajas.gwt.client.event.PointerEventType;

/**
 * IE10 implementation of {@link org.geomajas.gwt.client.event.PointerEvents}.
 * 
 * @author Jan De Moerloose
 *
 */
public class PointerEventsImplIE10 extends PointerEventsImpl {

	public String getNativeTypeName(PointerEventType type) {
		switch (type) {
			case POINTER_CANCEL:
				return "MSPointerCancel";
			case POINTER_DOWN:
				return "MSPointerDown";
			case POINTER_MOVE:
				return "MSPointerMove";
			case POINTER_UP:
				return "MSPointerUp";
		}
		throw new IllegalArgumentException("Could not determine name for " + type);
	}

}
