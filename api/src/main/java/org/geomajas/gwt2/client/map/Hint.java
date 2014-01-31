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
 * Enum-like class that uniquely represents a typed map hint. Define a static instance of this class for every map hint.
 * 
 * @author Jan De Moerloose
 * 
 * @param <T>
 *            The hint type class
 * @since 1.0.0
 */
@Api(allMethods = true)
public class Hint<T> {

	private final String name;

	/**
	 * Construct a map hint with the following name.
	 * 
	 * @param name
	 *            The name of the hint.
	 */
	public Hint(String name) {
		this.name = name;
	}

	/**
	 * Get the name of this map hint. Uniqueness is not necessary as this is guaranteed by the class.
	 * 
	 * @return name Get the name of this hint.
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
