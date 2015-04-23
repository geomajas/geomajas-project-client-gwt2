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

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import org.geomajas.gwt2.client.service.AbstractXmlNodeWrapper;
import org.geomajas.gwt2.plugin.tms.client.configuration.ListTileMapInfo;
import org.geomajas.gwt2.plugin.tms.client.configuration.TileMapServiceInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link org.geomajas.gwt2.plugin.tms.client.configuration.TileFormatInfo}
 * interface for TMS version 1.0.0.
 *
 * @author Pieter De Graef
 */
public class TileMapServiceInfo100 extends AbstractXmlNodeWrapper implements TileMapServiceInfo {

	private static final long serialVersionUID = 210L;

	private String title;

	private String abstractt;

	private String version;

	private final List<ListTileMapInfo> tileMaps = new ArrayList<ListTileMapInfo>();

	public TileMapServiceInfo100(Node node) {
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
	public String getAbstract() {
		if (!isParsed()) {
			parse(getNode());
		}
		return abstractt;
	}

	@Override
	public String getVersion() {
		if (!isParsed()) {
			parse(getNode());
		}
		return version;
	}

	@Override
	public List<ListTileMapInfo> getTileMaps() {
		if (!isParsed()) {
			parse(getNode());
		}
		return tileMaps;
	}

	// ------------------------------------------------------------------------
	// AbstractXmlNodeWrapper implementation:
	// ------------------------------------------------------------------------

	@Override
	protected void parse(Node node) {
		NamedNodeMap attributes = node.getAttributes();
		version = getValueRecursive(attributes.getNamedItem("version"));

		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			String nodeName = child.getNodeName();
			if ("Title".equalsIgnoreCase(nodeName)) {
				title = getValueRecursive(child);
			} else if ("Abstract".equalsIgnoreCase(nodeName)) {
				abstractt = getValueRecursive(child);
			} else if ("TileMaps".equalsIgnoreCase(nodeName)) {
				addTileMaps(child);
			}
		}
		if (title == null) {
			throw new IllegalArgumentException("Cannot parse XML into a TileMapServiceInfo object.");
		}

		setParsed(true);
	}

	private void addTileMaps(Node tileMapsNode) {
		Element tileMapsEl = (Element) tileMapsNode;
		NodeList tileSetList = tileMapsEl.getElementsByTagName("TileMap");

		tileMaps.clear();
		for (int i = 0; i < tileSetList.getLength(); i++) {
			Node tileSetNode = tileSetList.item(i);
			tileMaps.add(new ListTileMapInfo100(tileSetNode));
		}
	}
}
