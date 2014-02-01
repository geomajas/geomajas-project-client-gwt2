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

package org.geomajas.plugin.wms.server.command.dto;

import org.geomajas.command.CommandResponse;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;

import java.util.List;

/**
 * Response for the {@link org.geomajas.plugin.wms.server.command.WfsDescribeLayerCommand}. It returns the attribute
 * descriptors for the WFS layer.
 *
 * @author Pieter De Graef
 */
public class WfsDescribeLayerResponse extends CommandResponse {

	private static final long serialVersionUID = 1L;

	private List<AttributeDescriptor> attributeDescriptors;

	public WfsDescribeLayerResponse() {
	}

	public List<AttributeDescriptor> getAttributeDescriptors() {
		return this.attributeDescriptors;
	}

	public void setAttributeDescriptors(List<AttributeDescriptor> attributeDescriptors) {
		this.attributeDescriptors = attributeDescriptors;
	}
}
