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
package org.geomajas.gwt2.client.map.render;

/**
 * Priority class for use in {@link TilePriorityFunction}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class TilePriority implements Comparable<TilePriority> {

	/**
	 * Special priority instance for discarding tiles.
	 */
	public static final TilePriority DISCARD = new TilePriority(Double.MAX_VALUE);

	private Double value;

	/**
	 * Create a priority with the specified value.
	 * 
	 * @param value
	 */
	public TilePriority(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	@Override
	public int compareTo(TilePriority o) {
		return value.compareTo(o.value);
	}

}
