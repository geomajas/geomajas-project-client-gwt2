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

package org.geomajas.gwt2.plugin.wms.client.describelayer.v1_1_1;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt2.client.service.AbstractXmlNodeWrapper;
import org.geomajas.gwt2.plugin.wms.client.describelayer.WmsDescribeLayerInfo;
import org.geomajas.gwt2.plugin.wms.client.describelayer.WmsLayerDescriptionInfo;

/**
 * Implementation of the {@link WmsDescribeLayerInfo} for WMS version 1.1.1.
 * 
 * @author Jan De Moerloose
 */
public class WmsDescribeLayerInfo111 extends AbstractXmlNodeWrapper implements WmsDescribeLayerInfo {

	private static final long serialVersionUID = 100L;

	private List<WmsLayerDescriptionInfo> layerDescriptions;

	public WmsDescribeLayerInfo111(Node node) {
		super(node);
	}

	@Override
	public List<WmsLayerDescriptionInfo> getLayerDescriptions() {
		if (layerDescriptions == null) {
			parse(getNode());
		}
		return layerDescriptions;
	}

	protected void parse(Node node) {
		if (node instanceof Element) {
			Element element = (Element) node;
			NodeList layerNodes = element.getElementsByTagName("LayerDescription");
			layerDescriptions = new ArrayList<WmsLayerDescriptionInfo>();
			for (int i = 0; i < layerNodes.getLength(); i++) {
				layerDescriptions.add(new WmsLayerDescriptionInfo111(layerNodes.item(i)));
			}
		}
	}
}
