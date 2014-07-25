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

package org.geomajas.gwt2.plugin.tms.client.configuration.v1_0_0;

import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import org.geomajas.gwt2.client.service.AbstractXmlNodeWrapper;
import org.geomajas.gwt2.plugin.tms.client.configuration.TileSetInfo;

/**
 * Implementation of the {@link TileSetInfo} interface for TMS version 1.0.0.
 *
 * @author Pieter De Graef
 */
public class TileSetInfo100 extends AbstractXmlNodeWrapper implements TileSetInfo {

	private static final long serialVersionUID = 210L;

	private String href;

	private double unitsPerPixel = -1;

	private int order = -1;

	public TileSetInfo100(Node node) {
		super(node);
	}

	// ------------------------------------------------------------------------
	// TileSetInfo implementation:
	// ------------------------------------------------------------------------

	@Override
	public String getHref() {
		if (!isParsed()) {
			parse(getNode());
		}
		return href;
	}

	@Override
	public double getUnitsPerPixel() {
		if (!isParsed()) {
			parse(getNode());
		}
		return unitsPerPixel;
	}

	@Override
	public int getOrder() {
		if (!isParsed()) {
			parse(getNode());
		}
		return order;
	}

	// ------------------------------------------------------------------------
	// AbstractXmlNodeWrapper implementation:
	// ------------------------------------------------------------------------

	@Override
	protected void parse(Node node) {
		NamedNodeMap attributes = node.getAttributes();
		order = getValueRecursiveAsInteger(attributes.getNamedItem("order"));
		unitsPerPixel = getValueRecursiveAsDouble(attributes.getNamedItem("units-per-pixel"));
		href = getValueRecursive(attributes.getNamedItem("href"));
		setParsed(true);
	}
}
