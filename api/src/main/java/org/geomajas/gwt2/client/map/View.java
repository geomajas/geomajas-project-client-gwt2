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
import org.geomajas.geometry.Coordinate;

/**
 * Defines a view on the map. XYZ in the form of position and resolution.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public class View {

	private final Coordinate position;

	private final double resolution;

	/**
	 * Construct a view for the parameters given.
	 * 
	 * @param position
	 *            The position of the view.
	 * @param resolution
	 *            The resolution of the view.
	 */
	public View(Coordinate position, double resolution) {
		this.position = position;
		this.resolution = resolution;
	}

	/**
	 * Get the position for a certain view.
	 * 
	 * @return The position
	 */
	public Coordinate getPosition() {
		return position;
	}

	/**
	 * Get the resolution for a certain view.
	 * 
	 * @return The resolution
	 */
	public double getResolution() {
		return resolution;
	}

	@Override
	public String toString() {
		return "NavigationView: " + position + ", resolution=" + resolution;
	}

	@Override
	public boolean equals(Object object) {
		if (object != null && object instanceof View) {
			View other = (View) object;
			// We don't compare bounds, because a view may come from a map with a different size. It's resolution and
			// position that matter.
			return other.getPosition().equals(position) && Math.abs(other.getResolution() - resolution) < 1e-10;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return position.hashCode() + (int) Math.round(resolution);
	}
}