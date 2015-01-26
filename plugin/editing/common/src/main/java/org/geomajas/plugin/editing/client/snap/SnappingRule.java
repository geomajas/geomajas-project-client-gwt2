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

package org.geomajas.plugin.editing.client.snap;

import org.geomajas.annotation.Api;

/**
 * The class represents a single snapping rule that can be added to the {@link SnapService}.
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public class SnappingRule {

	private final SnapAlgorithm algorithm;

	private final SnapSourceProvider sourceProvider;

	private final double distance;

	/**
	 * Constructor that immediately requires all fields.
	 *
	 * @param algorithm      The snapping algorithm to use.
	 * @param sourceProvider Provides the geometries to snap to.
	 * @param distance       The maximum distance for snaapping.
	 */
	public SnappingRule(SnapAlgorithm algorithm, SnapSourceProvider sourceProvider, double distance) {
		this.algorithm = algorithm;
		this.sourceProvider = sourceProvider;
		this.distance = distance;
	}

	/**
	 * Get the snapping algorithm to use.
	 *
	 * @return The snapping algorithm.
	 */
	public SnapAlgorithm getAlgorithm() {
		return algorithm;
	}

	/**
	 * Get the source for the geometries to snap to.
	 *
	 * @return Provides the geometries.
	 */
	public SnapSourceProvider getSourceProvider() {
		return sourceProvider;
	}

	/**
	 * Get the maximum distance for snapping. This value is expressed in units of the map CRS.
	 *
	 * @return The maximum snapping distance.
	 */
	public double getDistance() {
		return distance;
	}
}