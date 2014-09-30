package org.geomajas.gwt2.plugin.wms.client.service;

import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;

import org.w3c.dom.Element;

public class MockNamedNodeMap implements NamedNodeMap {

	private org.w3c.dom.NamedNodeMap attributes;

	public MockNamedNodeMap(org.w3c.dom.NamedNodeMap attributes) {
		this.attributes = attributes;
	}

	@Override
	public int getLength() {
		return attributes.getLength();
	}

	@Override
	public Node getNamedItem(String name) {
		return MockNode.toNode(attributes.getNamedItem(name));
	}

	@Override
	public Node item(int index) {
		return toNode(attributes.item(index));
	}
	private Node toNode(org.w3c.dom.Node node) {
		return MockNode.toNode(node);
	}


}
