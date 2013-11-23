/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
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
 * Defines a view on the map. XYZ in the form of position and scale.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class View {

	private final Coordinate position;

	private final double scale;

	/**
	 * Construct a view for the parameters given.
	 * 
	 * @param position
	 *            The position of the view.
	 * @param scale
	 *            The scale of the view.
	 */
	public View(Coordinate position, double scale) {
		this.position = position;
		this.scale = scale;
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
	 * Get the scale for a certain view.
	 * 
	 * @return The scale
	 */
	public double getScale() {
		return scale;
	}

	@Override
	public String toString() {
		return "NavigationView: " + position + ", scale=" + scale;
	}

	@Override
	public boolean equals(Object object) {
		if (object != null && object instanceof View) {
			View other = (View) object;
			// We don't compare bounds, because a view may come from a map with a different size. It's scale and
			// position that matter.
			return other.getPosition().equals(position) && Math.abs(other.getScale() - scale) < 1e-10;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return position.hashCode() + (int) Math.round(scale);
	}
}