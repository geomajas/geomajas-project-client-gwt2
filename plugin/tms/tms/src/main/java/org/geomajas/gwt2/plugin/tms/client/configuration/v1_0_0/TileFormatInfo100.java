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
import org.geomajas.gwt2.plugin.tms.client.configuration.TileFormatInfo;

/**
 * Implementation of the {@link org.geomajas.gwt2.plugin.tms.client.configuration.TileFormatInfo}
 * interface for TMS version 1.0.0.
 *
 * @author Pieter De Graef
 */
public class TileFormatInfo100 extends AbstractXmlNodeWrapper implements TileFormatInfo {

	private static final long serialVersionUID = 210L;

	private int width = -1;

	private int height = -1;

	private String mimeType;

	private String extension;

	public TileFormatInfo100(Node node) {
		super(node);
	}

	// ------------------------------------------------------------------------
	// TileSetInfo implementation:
	// ------------------------------------------------------------------------

	@Override
	public int getWidth() {
		if (!isParsed()) {
			parse(getNode());
		}
		return width;
	}

	@Override
	public int getHeight() {
		if (!isParsed()) {
			parse(getNode());
		}
		return height;
	}

	@Override
	public String getMimeType() {
		if (!isParsed()) {
			parse(getNode());
		}
		return mimeType;
	}

	@Override
	public String getExtension() {
		if (!isParsed()) {
			parse(getNode());
		}
		return extension;
	}

	// ------------------------------------------------------------------------
	// AbstractXmlNodeWrapper implementation:
	// ------------------------------------------------------------------------

	@Override
	protected void parse(Node node) {
		NamedNodeMap attributes = node.getAttributes();
		width = getValueRecursiveAsInteger(attributes.getNamedItem("width"));
		height = getValueRecursiveAsInteger(attributes.getNamedItem("height"));
		mimeType = getValueRecursive(attributes.getNamedItem("mime-type"));
		extension = getValueRecursive(attributes.getNamedItem("extension"));
		setParsed(true);
	}
}
