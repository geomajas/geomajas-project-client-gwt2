package org.geomajas.gwt2.plugin.wms.client.service;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.w3c.dom.Element;

public class MockNodeList implements NodeList {

	private org.w3c.dom.NodeList nodeList;

	public MockNodeList(org.w3c.dom.NodeList nodeList) {
		this.nodeList = nodeList;
	}

	@Override
	public int getLength() {
		return nodeList.getLength();
	}

	@Override
	public Node item(int index) {
		return toNode(nodeList.item(index));
	}

	private Node toNode(org.w3c.dom.Node node) {
		return MockNode.toNode(node);
	}

}
