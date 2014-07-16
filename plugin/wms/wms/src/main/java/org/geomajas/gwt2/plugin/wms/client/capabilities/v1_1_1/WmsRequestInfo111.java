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

package org.geomajas.gwt2.plugin.wms.client.capabilities.v1_1_1;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import org.geomajas.gwt2.client.service.AbstractXmlNodeWrapper;
import org.geomajas.gwt2.plugin.wms.client.service.WmsService;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsRequestInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link org.geomajas.gwt2.plugin.wms.client.capabilities.WmsRequestInfo} for WMS version 1.1.1.
 *
 * @author Pieter De Graef
 */
public class WmsRequestInfo111 extends AbstractXmlNodeWrapper implements WmsRequestInfo {

	private static final long serialVersionUID = 100L;

	private List<String> formats = new ArrayList<String>();

	private WmsService.WmsRequest request;

	public WmsRequestInfo111(Node node) {
		super(node);
	}

	@Override
	public WmsService.WmsRequest getRequestType() {
		if (request == null) {
			parse(getNode());
		}
		return request;
	}

	@Override
	public List<String> getFormats() {
		if (request == null) {
			parse(getNode());
		}
		return formats;
	}

	protected void parse(Node node) {
		if (node instanceof Element) {
			formats = new ArrayList<String>();
			Element formatListEl = (Element) node;
			NodeList formatList = formatListEl.getElementsByTagName("Format");
			for (int i = 0; i < formatList.getLength(); i++) {
				Node formatNode = formatList.item(i);
				formats.add(getValueRecursive(formatNode));
			}
		}
		request = WmsService.WmsRequest.fromString(node.getNodeName());
	}
}
