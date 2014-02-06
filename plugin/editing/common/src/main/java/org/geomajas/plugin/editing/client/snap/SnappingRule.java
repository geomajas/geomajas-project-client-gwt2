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

package org.geomajas.plugin.editing.client.snap;

import org.geomajas.annotation.Api;

/**
 * Snapping rule.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public class SnappingRule {

	private final SnapAlgorithm algorithm;

	private final SnapSourceProvider sourceProvider;

	private final double distance;

	public SnappingRule(SnapAlgorithm algorithm, SnapSourceProvider sourceProvider, double distance) {
		this.algorithm = algorithm;
		this.sourceProvider = sourceProvider;
		this.distance = distance;
	}

	public SnapAlgorithm getAlgorithm() {
		return algorithm;
	}

	public SnapSourceProvider getSourceProvider() {
		return sourceProvider;
	}

	public double getDistance() {
		return distance;
	}
}