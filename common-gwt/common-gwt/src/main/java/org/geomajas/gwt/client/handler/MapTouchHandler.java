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
package org.geomajas.gwt.client.handler;

import com.google.gwt.event.dom.client.TouchEvent;

/**
 * Handler for all touch events (normal and pointer-event based).
 * 
 * @author Jan De Moerloose
 */
public interface MapTouchHandler {

	void onMapTouchStart(TouchEvent<?> event);

	void onMapTouchMove(TouchEvent<?> event);

	void onMapTouchEnd(TouchEvent<?> event);

	void onMapTouchCancel(TouchEvent<?> event);
}
