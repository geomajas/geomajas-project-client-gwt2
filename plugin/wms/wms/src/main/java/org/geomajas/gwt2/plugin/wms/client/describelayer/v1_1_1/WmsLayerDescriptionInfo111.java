// Copyright (C) 2010-2011 DOV, http://dov.vlaanderen.be/
// All rights reserved

package org.geomajas.gwt2.plugin.wms.client.describelayer.v1_1_1;

import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;

import org.geomajas.gwt2.client.service.AbstractXmlNodeWrapper;
import org.geomajas.gwt2.plugin.wms.client.describelayer.WmsLayerDescriptionInfo;

/**
 * Implementation of the {@link WmsLayerDescriptionInfo} for WMS version 1.1.1.
 * 
 * @author Jan De Moerloose
 */
public class WmsLayerDescriptionInfo111 extends AbstractXmlNodeWrapper implements WmsLayerDescriptionInfo {

	private static final long serialVersionUID = 100L;

	private String name;

	private String wfs;

	private String owsType;

	private String owsUrl;

	public WmsLayerDescriptionInfo111(Node node) {
		super(node);
	}

	@Override
	public String getName() {
		if (name == null) {
			parse(getNode());
		}
		return name;
	}

	@Override
	public String getWfs() {
		if (name == null) {
			parse(getNode());
		}
		return wfs;
	}

	@Override
	public String getOwsType() {
		if (name == null) {
			parse(getNode());
		}
		return owsType;
	}

	@Override
	public String getOwsUrl() {
		if (name == null) {
			parse(getNode());
		}
		return owsUrl;
	}

	@Override
	protected void parse(Node node) {
		NamedNodeMap attributes = node.getAttributes();
		name = getValueRecursive(attributes.getNamedItem("name"));
		wfs = getValueRecursive(attributes.getNamedItem("wfs"));
		owsType = getValueRecursive(attributes.getNamedItem("owsType"));
		owsUrl = getValueRecursive(attributes.getNamedItem("owsUrl"));
	}
}
