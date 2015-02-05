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

package org.geomajas.gwt2.client.widget;

import org.geomajas.annotation.Api;

/**
 * This enumeration lists the default widgets that can be added to the map at startup.
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public enum DefaultMapWidget {

	/**
	 * The default zooming control. It provides a zoom in and a zoom out button.
	 */
	ZOOM_CONTROL,

	/**
	 * An extended zooming control, that displays the available zooming levels as well as a zoom in and zoom out
	 * button.
	 */
	ZOOM_STEP_CONTROL,

	/**
	 * A single button that allows the user to zoom in to a rectangle. After clicking this button, the user must drag a
	 * rectangle on the map.
	 */
	ZOOM_TO_RECTANGLE_CONTROL,

	/**
	 * A control that contains 4 buttons for panning in the 4 directions (north, east, south, west).
	 */
	PAN_CONTROL,

	/**
	 * The default scale bar. Displays the current scale.
	 */
	SCALEBAR
}
