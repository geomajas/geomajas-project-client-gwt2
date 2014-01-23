/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.client.map;

import org.geomajas.annotation.Api;

/**
 * Zoom options. These express the different ways to zoom in and out on a map.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public enum ZoomOption {

	/** Zoom to a scale level that is as close as possible to the requested scale. */
	LEVEL_CLOSEST,

	/** Zoom to a scale level that makes the requested scale fit inside our view. */
	LEVEL_FIT,

	/**
	 * Completely free zooming (while keeping within the minimum and maximum scale). It will not snap to a fixed
	 * resolution.
	 */
	FREE
}