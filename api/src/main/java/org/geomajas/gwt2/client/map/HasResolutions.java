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
 * Interface for resolutions based entities, such as certain layers, or the ViewPort.
 *
 * @author Pieter De Graef
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface HasResolutions {

	/**
	 * Get the total number of preferred fixed resolutions. These resolutions are used among others by the zooming
	 * controls on the map.
	 *
	 * @return The total number of fixed zoom resolutions, or -1 if no fixed list of scales is known.
	 */
	int getResolutionCount();

	/**
	 * Get a preferred fixed resolution at a certain index.
	 *
	 * @param index The index to get a scale for. Index 0 means the maximum resolution (=zoomed out).
	 * @return Returns the preferred resolution.
	 */
	double getResolution(int index);

	/**
	 * Get the index for the fixed resolution that is closest to the provided resolution.
	 *
	 * @param resolution The resolution to request the closest fixed resolution level for.
	 * @return Returns the fixed resolution level index.
	 */
	int getResolutionIndex(double resolution);

	/**
	 * Return the minimum allowed resolution. This means the maximum zoom out.
	 *
	 * @return The minimum allowed resolution.
	 */
	double getMaximumResolution();

	/**
	 * Return the maximum allowed resolution. This means the maximum zoom in.
	 *
	 * @return The maximum allowed resolution.
	 */
	double getMinimumResolution();
}
