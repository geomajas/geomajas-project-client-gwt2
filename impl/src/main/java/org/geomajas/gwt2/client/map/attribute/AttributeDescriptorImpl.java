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

package org.geomajas.gwt2.client.map.attribute;

/**
 * Default implementation of an {@link AttributeDescriptor}.
 *
 * @author Pieter De Graef
 */
public class AttributeDescriptorImpl implements AttributeDescriptor {

	private AttributeType type;

	private String name;

	public AttributeDescriptorImpl() {
	}

	public AttributeDescriptorImpl(AttributeType type, String name) {
		this.type = type;
		this.name = name;
	}

	@Override
	public AttributeType getType() {
		return type;
	}

	public void setType(AttributeType type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override

	public String getName() {
		return name;
	}
}