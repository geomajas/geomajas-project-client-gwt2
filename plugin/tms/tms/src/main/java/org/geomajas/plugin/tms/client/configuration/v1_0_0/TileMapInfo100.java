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

package org.geomajas.plugin.tms.client.configuration.v1_0_0;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.service.AbstractXmlNodeWrapper;
import org.geomajas.plugin.tms.client.configuration.TileFormatInfo;
import org.geomajas.plugin.tms.client.configuration.TileMapInfo;
import org.geomajas.plugin.tms.client.configuration.TileSetInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link org.geomajas.plugin.tms.client.configuration.TileFormatInfo} interface for TMS version
 * 1.0.0.
 *
 * @author Pieter De Graef
 */
public class TileMapInfo100 extends AbstractXmlNodeWrapper implements TileMapInfo {

	private static final long serialVersionUID = 210L;

	private String href;

	private String version;

	private String tilemapservice;

	private String title;

	private String abstractt;

	private String srs;

	private Bbox boundingBox;

	private Coordinate origin;

	private TileFormatInfo tileFormat;

	private String profile;

	private final List<TileSetInfo> tileSets = new ArrayList<TileSetInfo>();

	public TileMapInfo100(Node node) {
		super(node);
	}

	// ------------------------------------------------------------------------
	// TileSetInfo implementation:
	// ------------------------------------------------------------------------

	@Override
	public String getHref() {
		return href;
	}

	@Override
	public String getVersion() {
		if (!isParsed()) {
			parse(getNode());
		}
		return version;
	}

	@Override
	public String getTilemapservice() {
		if (!isParsed()) {
			parse(getNode());
		}
		return tilemapservice;
	}

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
	public String getSrs() {
		if (!isParsed()) {
			parse(getNode());
		}
		return srs;
	}

	@Override
	public Bbox getBoundingBox() {
		if (!isParsed()) {
			parse(getNode());
		}
		return boundingBox;
	}

	@Override
	public Coordinate getOrigin() {
		if (!isParsed()) {
			parse(getNode());
		}
		return origin;
	}

	@Override
	public TileFormatInfo getTileFormat() {
		if (!isParsed()) {
			parse(getNode());
		}
		return tileFormat;
	}

	@Override
	public String getProfile() {
		if (!isParsed()) {
			parse(getNode());
		}
		return profile;
	}

	@Override
	public List<TileSetInfo> getTileSets() {
		if (!isParsed()) {
			parse(getNode());
		}
		return tileSets;
	}

	// ------------------------------------------------------------------------
	// AbstractXmlNodeWrapper implementation:
	// ------------------------------------------------------------------------

	@Override
	protected void parse(Node node) {
		NamedNodeMap attributes = node.getAttributes();
		version = getValueRecursive(attributes.getNamedItem("version"));
		tilemapservice = getValueRecursive(attributes.getNamedItem("tilemapservice"));

		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			String nodeName = child.getNodeName();
			if ("Title".equalsIgnoreCase(nodeName)) {
				title = getValueRecursive(child);
			} else if ("Abstract".equalsIgnoreCase(nodeName)) {
				abstractt = getValueRecursive(child);
			} else if ("SRS".equalsIgnoreCase(nodeName)) {
				srs = getValueRecursive(child);
			} else if ("BoundingBox".equalsIgnoreCase(nodeName)) {
				boundingBox = getBoundingBox(child);
			} else if ("Origin".equalsIgnoreCase(nodeName)) {
				origin = getCoordinate(child);
			} else if ("TileFormat".equalsIgnoreCase(nodeName)) {
				tileFormat = new TileFormatInfo100(child);
			} else if ("TileSets".equalsIgnoreCase(nodeName)) {
				addTileSets(child);
			}
		}
		if (title == null) {
			throw new IllegalArgumentException("Cannot parse XML into a TileMapInfo object.");
		}
		setParsed(true);
	}

	private void addTileSets(Node tileSetsNode) {
		tileSets.clear();
		Element tileSetsEl = (Element) tileSetsNode;
		NodeList tileSetList = tileSetsEl.getElementsByTagName("TileSet");

		for (int i = 0; i < tileSetList.getLength(); i++) {
			Node tileSetNode = tileSetList.item(i);
			tileSets.add(new TileSetInfo100(tileSetNode));
		}
		if (tileSets.size() > 0) {
			TileSetInfo tileSet = tileSets.get(0);
			href = tileSet.getHref().substring(0, tileSet.getHref().lastIndexOf('/'));
		}

		NamedNodeMap attributes = tileSetsNode.getAttributes();
		profile = getValueRecursive(attributes.getNamedItem("profile"));
	}
}
