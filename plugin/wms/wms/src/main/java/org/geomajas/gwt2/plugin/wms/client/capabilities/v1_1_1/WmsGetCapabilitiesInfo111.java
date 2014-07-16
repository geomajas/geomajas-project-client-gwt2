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
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsLayerInfo;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsGetCapabilitiesInfo;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsRequestInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link WmsGetCapabilitiesInfo} for WMS version 1.1.1.
 *
 * @author Pieter De Graef
 */
public class WmsGetCapabilitiesInfo111 extends AbstractXmlNodeWrapper implements WmsGetCapabilitiesInfo {

	private static final long serialVersionUID = 100L;

	private List<WmsRequestInfo> requests;

	private List<WmsLayerInfo> layers;

	public WmsGetCapabilitiesInfo111(Node node) {
		super(node);
	}

	public List<WmsRequestInfo> getRequests() {
		if (requests == null) {
			parse(getNode());
		}
		return requests;
	}

	public List<WmsLayerInfo> getLayers() {
		if (layers == null) {
			parse(getNode());
		}
		return layers;
	}

	protected void parse(Node node) {
		if (node instanceof Element) {
			Element element = (Element) node;

			requests = new ArrayList<WmsRequestInfo>(5);
			NodeList requestParentNode = element.getElementsByTagName("Request");
			NodeList requestNodes = requestParentNode.item(0).getChildNodes();
			for (int i = 0; i < requestNodes.getLength(); i++) {
				Node requestNode = requestNodes.item(i);
				WmsRequestInfo111 requestInfo = new WmsRequestInfo111(requestNode);
				if (requestInfo.getRequestType() != null) {
					requests.add(requestInfo);
				}
			}

			NodeList layerNodes = element.getElementsByTagName("Layer");
			layers = new ArrayList<WmsLayerInfo>();
			for (int i = 0; i < layerNodes.getLength(); i++) {
				Node layerNode = layerNodes.item(i);
				if (layerNode.hasAttributes()) {
					WmsLayerInfo layer = new WmsLayerInfo111(layerNode);
					layers.add(layer);
				}
			}
		}
	}
}
