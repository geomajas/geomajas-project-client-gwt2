package org.geomajas.gwt2.plugin.wms.client.service;

import com.google.gwt.xml.client.Attr;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;


public class MockDocumentElement implements Element {

	private org.w3c.dom.Element documentElement;

	public MockDocumentElement(org.w3c.dom.Element documentElement) {
		this.documentElement = documentElement;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeList getChildNodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getFirstChild() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getLastChild() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short getNodeType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getNodeValue() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasChildNodes() {
		// TODO Auto-generated method stub
		return false;
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

	@Override
	public String getAttribute(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attr getAttributeNode(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeList getElementsByTagName(String name) {
		return new MockNodeList(documentElement.getElementsByTagName(name));
	}

	@Override
	public String getTagName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasAttribute(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeAttribute(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAttribute(String name, String value) {
		// TODO Auto-generated method stub

	}

}
