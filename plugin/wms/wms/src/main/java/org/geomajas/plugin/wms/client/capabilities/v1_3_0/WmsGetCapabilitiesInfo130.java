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

package org.geomajas.plugin.wms.client.capabilities.v1_3_0;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import org.geomajas.gwt2.client.service.AbstractXmlNodeWrapper;
import org.geomajas.plugin.wms.client.capabilities.WmsGetCapabilitiesInfo;
import org.geomajas.plugin.wms.client.capabilities.WmsLayerInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link WmsGetCapabilitiesInfo} for WMS version 1.3.0.
 *
 * @author Pieter De Graef
 */
public class WmsGetCapabilitiesInfo130 extends AbstractXmlNodeWrapper implements WmsGetCapabilitiesInfo {

	private static final long serialVersionUID = 100L;

	private List<WmsLayerInfo> layers;

	public WmsGetCapabilitiesInfo130(Node node) {
		super(node);
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
			NodeList layerNodes = element.getElementsByTagName("Layer");

			layers = new ArrayList<WmsLayerInfo>();
			for (int i = 0; i < layerNodes.getLength(); i++) {
				Node layerNode = layerNodes.item(i);
				if (layerNode.hasAttributes()) {
					WmsLayerInfo layer = new WmsLayerInfo130(layerNode);
					layers.add(layer);
				}
			}
		}
	}
}