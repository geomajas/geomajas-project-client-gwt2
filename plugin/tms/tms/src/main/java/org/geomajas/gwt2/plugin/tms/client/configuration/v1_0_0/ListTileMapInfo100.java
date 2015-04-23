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

package org.geomajas.gwt2.plugin.tms.client.configuration.v1_0_0;

import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import org.geomajas.gwt2.client.service.AbstractXmlNodeWrapper;
import org.geomajas.gwt2.plugin.tms.client.configuration.ListTileMapInfo;

/**
 * Implementation of the {@link org.geomajas.gwt2.plugin.tms.client.configuration.ListTileMapInfo} interface for TMS
 * version 1.0.0.
 *
 * @author Pieter De Graef
 */
public class ListTileMapInfo100 extends AbstractXmlNodeWrapper implements ListTileMapInfo {

	private static final long serialVersionUID = 210L;

	private String title;

	private String srs;

	private String profile;

	private String href;

	public ListTileMapInfo100(Node node) {
		super(node);
	}

	// ------------------------------------------------------------------------
	// TileSetInfo implementation:
	// ------------------------------------------------------------------------

	@Override
	public String getTitle() {
		if (!isParsed()) {
			parse(getNode());
		}
		return title;
	}

	@Override
	public String getSrs() {
		if (!isParsed()) {
			parse(getNode());
		}
		return srs;
	}

	@Override
	public String getProfile() {
		if (!isParsed()) {
			parse(getNode());
		}
		return profile;
	}

	@Override
	public String getHref() {
		if (!isParsed()) {
			parse(getNode());
		}
		return href;
	}

	// ------------------------------------------------------------------------
	// AbstractXmlNodeWrapper implementation:
	// ------------------------------------------------------------------------

	@Override
	protected void parse(Node node) {
		NamedNodeMap attributes = node.getAttributes();
		title = getValueRecursive(attributes.getNamedItem("title"));
		srs = getValueRecursive(attributes.getNamedItem("srs"));
		profile = getValueRecursive(attributes.getNamedItem("profile"));
		href = getValueRecursive(attributes.getNamedItem("href"));
		setParsed(true);
	}
}
