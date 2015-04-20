/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.plugin.wms.client.capabilities.v1_1_1;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt2.client.service.AbstractXmlNodeWrapper;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsGetCapabilitiesInfo;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsLayerInfo;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsRequestInfo;
import org.geomajas.gwt2.plugin.wms.client.capabilities.v1_3_0.WmsRequestInfo130;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

/**
 * Implementation of the {@link WmsGetCapabilitiesInfo} for WMS version 1.1.1.
 *
 * @author Pieter De Graef
 */
public class WmsGetCapabilitiesInfo111 extends AbstractXmlNodeWrapper implements WmsGetCapabilitiesInfo {

	private static final long serialVersionUID = 100L;

	private List<WmsRequestInfo> requests;

	private List<WmsLayerInfo> layers;

	private WmsLayerInfo111 rootLayer;

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
	
	@Override
	public WmsLayerInfo getRootLayer() {
		if (rootLayer == null) {
			parse(getNode());
		}
		return rootLayer;
	}

	protected void parse(Node node) {
		if (node instanceof Element) {
			Element element = (Element) node;

			requests = new ArrayList<WmsRequestInfo>(5);
			NodeList requestParentNode = element.getElementsByTagName("Request");
			NodeList requestNodes = requestParentNode.item(0).getChildNodes();
			for (int i = 0; i < requestNodes.getLength(); i++) {
				Node requestNode = requestNodes.item(i);
				WmsRequestInfo111 requestInfo = new WmsRequestInfo130(requestNode);
				if (requestInfo.getRequestType() != null) {
					requests.add(requestInfo);
				}
			}

			NodeList layerNodes = element.getElementsByTagName("Layer");
			layers = new ArrayList<WmsLayerInfo>();
			if (layerNodes.getLength() > 0) {
				Node rootNode = layerNodes.item(0);
				rootLayer = new WmsLayerInfo111(rootNode);
				if (rootLayer.getName() != null) {
					layers.add(rootLayer);
				}
				addChildrenRecursive(rootLayer, layers);
			} else {
				throw new IllegalArgumentException("Capabilities has no root layer !");
			}
		}
	}

	private void addChildrenRecursive(WmsLayerInfo111 parentLayer, List<WmsLayerInfo> namedLayers) {
		Node parentNode = parentLayer.getNode();
		if (parentNode.getChildNodes() != null) {
			for (int i = 0; i < parentNode.getChildNodes().getLength(); i++) {
				Node child = parentNode.getChildNodes().item(i);
				String nodeName = child.getNodeName();
				if ("Layer".equals(nodeName)) {
					WmsLayerInfo111 childLayer = new WmsLayerInfo111(child);
					addChildrenRecursive(childLayer, namedLayers);
					if (childLayer.getName() != null) {
						namedLayers.add(childLayer);
					}
					parentLayer.getLayers().add(childLayer);
				}
			}
		}
	}

}
