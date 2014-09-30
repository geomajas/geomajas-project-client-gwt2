package org.geomajas.gwt2.plugin.wms.client.service;

import com.google.gwt.xml.client.CDATASection;
import com.google.gwt.xml.client.Comment;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.DocumentFragment;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.ProcessingInstruction;
import com.google.gwt.xml.client.Text;

import org.apache.xerces.parsers.DOMParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;


public class MockDocument implements Document {
	
	private org.w3c.dom.Document document;
	
	public MockDocument(String xml) throws SAXException, IOException {
		DOMParser parser = new DOMParser();
		parser.parse(new InputSource(new StringReader(xml)));
		document = parser.getDocument();
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
		return new MockNodeList(document.getChildNodes());
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
	public CDATASection createCDATASection(String data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comment createComment(String data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DocumentFragment createDocumentFragment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element createElement(String tagName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessingInstruction createProcessingInstruction(String target, String data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Text createTextNode(String data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element getDocumentElement() {
		return new MockDocumentElement(document.getDocumentElement());
	}

	@Override
	public Element getElementById(String elementId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeList getElementsByTagName(String tagname) {
		return null;
	}

	@Override
	public Node importNode(Node importedNode, boolean deep) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
