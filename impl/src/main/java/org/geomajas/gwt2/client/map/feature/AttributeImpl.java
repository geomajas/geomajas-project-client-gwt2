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
package org.geomajas.gwt2.client.map.feature;

import org.geomajas.gwt2.client.map.attribute.Attribute;

/**
 * Default implementation of a feature attribute.
 * 
 * @author Jan De Moerloose
 * 
 * @param <T> binding class of the attribute value
 */
public class AttributeImpl<T> implements Attribute<T> {

	private final T value;

	protected AttributeImpl(T value) {
		this.value = value;
	}

	@Override
	public T getValue() {
		return value;
	}
}
