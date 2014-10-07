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

package org.geomajas.gwt2.client.service;

import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;

/**
 * Abstract definition for configuration objects that are based around an XML node.
 *
 * @author Pieter De Graef
 */
public abstract class AbstractXmlNodeWrapper {

	private final Node node;

	private boolean parsed;

	public AbstractXmlNodeWrapper(Node node) {
		this.node = node;
	}

	protected abstract void parse(Node node);

	/**
	 * Get the XML node.
	 *
	 * @return The XML node.
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * Has this node been parsed or not? Implementation should set and request this value.
	 *
	 * @return Has this node been parsed or not? Implementation should set and request this value.
	 */
	protected boolean isParsed() {
		return parsed;
	}

	/**
	 * Indicate this node has been parsed.
	 *
	 * @param parsed The new value.
	 */
	protected void setParsed(boolean parsed) {
		this.parsed = parsed;
	}

	protected String getValueRecursive(Node node) {
		if (node != null) {
			if (node.getNodeValue() != null) {
				return node.getNodeValue().trim();
			}
			if (node.getFirstChild() != null) {
				return getValueRecursive(node.getFirstChild());
			}
		}
		return null;
	}

	protected double getValueRecursiveAsDouble(Node node) {
		String value = getValueRecursive(node);
		if (value != null) {
			try {
				value = value.replace(",", ".");
				return Double.parseDouble(value);
			} catch (Exception e) {
			}
		}
		return 0;
	}

	protected int getValueRecursiveAsInteger(Node node) {
		String value = getValueRecursive(node);
		if (value != null) {
			try {
				value = value.replace(",", ".");
				int pos = value.indexOf(".");
				if (pos > 0) {
					value = value.substring(0, pos);
				}
				return Integer.parseInt(value);
			} catch (Exception e) {
			}
		}
		return 0;
	}

	protected boolean hasAttribute(Node node, String name) {
		return node.hasAttributes() && node.getAttributes().getNamedItem(name) != null;
	}
	
	protected double getAttributeAsDouble(Node node, String name) {
		Node attr = node.getAttributes().getNamedItem(name);
		return getValueRecursiveAsDouble(attr);
	}

	protected Bbox getBoundingBox(Node node) {
		NamedNodeMap attributes = node.getAttributes();
		Node minx = attributes.getNamedItem("minx");
		Node miny = attributes.getNamedItem("miny");
		Node maxx = attributes.getNamedItem("maxx");
		Node maxy = attributes.getNamedItem("maxy");

		double x = getValueRecursiveAsDouble(minx);
		double y = getValueRecursiveAsDouble(miny);
		double width = getValueRecursiveAsDouble(maxx) - x;
		double height = getValueRecursiveAsDouble(maxy) - y;
		return new Bbox(x, y, width, height);
	}

	protected Coordinate getCoordinate(Node node) {
		NamedNodeMap attributes = node.getAttributes();
		double x = getValueRecursiveAsDouble(attributes.getNamedItem("x"));
		double y = getValueRecursiveAsDouble(attributes.getNamedItem("y"));
		return new Coordinate(x, y);
	}
}
