package org.geomajas.gwt2.plugin.wms.client.service;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.w3c.dom.Element;

public class MockNode implements Node {

	protected org.w3c.dom.Node node;

	public MockNode(org.w3c.dom.Node node) {
		this.node = node;
	}

	@Override
	public Node appendChild(Node newChild) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node cloneNode(boolean deep) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NamedNodeMap getAttributes() {
		return new MockNamedNodeMap(node.getAttributes());
	}

	@Override
	public NodeList getChildNodes() {
		return new MockNodeList(node.getChildNodes());
	}

	@Override
	public Node getFirstChild() {
		return MockNode.toNode(node.getFirstChild());
	}

	@Override
	public Node getLastChild() {
		return MockNode.toNode(node.getLastChild());
	}

	@Override
	public String getNamespaceURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getNextSibling() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNodeName() {
		return node.getNodeName();
	}

	@Override
	public short getNodeType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getNodeValue() {
		return node.getNodeValue();
	}

	@Override
	public Document getOwnerDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getParentNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrefix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getPreviousSibling() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasAttributes() {
		return node.hasAttributes();
	}

	@Override
	public boolean hasChildNodes() {
		return node.hasChildNodes();
	}

	@Override
	public Node insertBefore(Node newChild, Node refChild) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void normalize() {
		// TODO Auto-generated method stub

	}

	@Override
	public Node removeChild(Node oldChild) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node replaceChild(Node newChild, Node oldChild) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNodeValue(String nodeValue) {
		// TODO Auto-generated method stub

	}

	static Node toNode(org.w3c.dom.Node node) {
		if (node == null) {
			return null;
		} else if (node instanceof Element) {
			return new MockElement((Element) node);
		} else {
			return new MockNode(node);
		}
	}

}
