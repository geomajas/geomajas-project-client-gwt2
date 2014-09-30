package org.geomajas.gwt2.plugin.wms.client.service;

import com.google.gwt.xml.client.Attr;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;




public class MockElement extends MockNode implements Element {

	private org.w3c.dom.Element element;
	
	public MockElement(org.w3c.dom.Element node) {
		super(node);
		this.element = node;
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

	public NodeList getElementsByTagName(String name) {
		return new MockNodeList(element.getElementsByTagName(name));
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
