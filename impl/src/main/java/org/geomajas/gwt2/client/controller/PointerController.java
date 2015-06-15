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

import org.geomajas.gwt.client.event.PointerTouchCancelHandler;
import org.geomajas.gwt.client.event.PointerTouchEndHandler;
import org.geomajas.gwt.client.event.PointerTouchMoveHandler;
import org.geomajas.gwt.client.event.PointerTouchStartHandler;

/**
 * Generic controller that is used on pointer devices.
 *
 * @author Jan De Moerloose
 */
public interface PointerController extends PointerTouchStartHandler, PointerTouchEndHandler, PointerTouchMoveHandler,
		PointerTouchCancelHandler {

}
