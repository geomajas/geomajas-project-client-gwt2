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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Abstract implementation of the HasResolutions interface.
 *
 * @author Pieter De Graef
 */
public class HasResolutionsImpl implements HasResolutions {

	protected final List<Double> resolutions = new ArrayList<Double>();

	@Override
	public int getResolutionCount() {
		return resolutions.size();
	}

	@Override
	public double getResolution(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("Resolution cannot be found.");
		}
		if (index >= resolutions.size()) {
			throw new IllegalArgumentException("Resolution cannot be found.");
		}
		return resolutions.get(index);
	}

	@Override
	public int getResolutionIndex(double resolution) {
		double maximumResolution = getMaximumResolution();
		if (resolution >= maximumResolution) {
			return 0;
		}
		double minimumResolution = getMinimumResolution();
		if (resolution <= minimumResolution) {
			return resolutions.size() - 1;
		}

		for (int i = 0; i < resolutions.size(); i++) {
			double upper = resolutions.get(i);
			double lower = resolutions.get(i + 1);
			if (resolution < upper && resolution >= lower) {
				if (Math.abs(upper - resolution) >= Math.abs(lower - resolution)) {
					return i + 1;
				} else {
					return i;
				}
			}
		}
		return 0;
	}

	@Override
	public double getMaximumResolution() {
		if (resolutions.size() == 0) {
			return Double.MAX_VALUE;
		}
		return resolutions.get(0);
	}

	@Override
	public double getMinimumResolution() {
		if (resolutions.size() == 0) {
			return 0;
		}
		return resolutions.get(resolutions.size() - 1);
	}

	/**
	 * Set the list of resolutions. This method will automatically sort the list from large to small (zoom out to
	 * zoom in).
	 * @param resolutions The unsorted list of resolutions.
	 */
	public void setResolutions(List<Double> resolutions) {
		this.resolutions.clear();
		this.resolutions.addAll(resolutions);
		Collections.sort(this.resolutions, new Comparator<Double>() {

			@Override
			public int compare(Double o1, Double o2) {
				return o2.compareTo(o1);
			}
		});
	}
}
