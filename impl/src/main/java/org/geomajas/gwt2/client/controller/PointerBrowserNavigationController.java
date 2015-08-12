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

package org.geomajas.gwt2.client.controller;

import com.google.gwt.event.dom.client.TouchEvent;
import org.geomajas.annotation.Api;

import java.util.logging.Logger;

/**
 * Extension of {@link NavigationController} for brosers using Pointer Events.
 * The methods called by pointer events need to be redirected to general methods,
 * much like onMouseMove, onMouseUp, onMouseDown.
 * 
 * @author Jan Venstermans
 * @since 2.4.2
 */
@Api(allMethods = true)
public class PointerBrowserNavigationController extends NavigationController {

	private static Logger logger = Logger.getLogger("PointerBrowserNavigationController");

	@Override
	public void onMapTouchMove(TouchEvent<?> event) {
		if (dragging) {
			super.onMapTouchMove(event);
		}
	}

	@Override
	public void onMapTouchStart(TouchEvent<?> event) {
		dragging = true;
		onDown(event);
	}

	@Override
	public void onMapTouchEnd(TouchEvent<?> event) {
		dragging = false;
		onUp(event);
	}
}
