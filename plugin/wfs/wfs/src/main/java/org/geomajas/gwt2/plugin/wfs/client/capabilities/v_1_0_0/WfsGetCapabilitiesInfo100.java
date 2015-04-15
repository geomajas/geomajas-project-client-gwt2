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

package org.geomajas.gwt2.plugin.wfs.client.capabilities.v_1_0_0;

import org.geomajas.gwt2.client.service.AbstractXmlNodeWrapper;
import org.geomajas.gwt2.plugin.wfs.client.capabilities.WfsGetCapabilitiesInfo;
import org.geomajas.gwt2.plugin.wfs.client.capabilities.WfsFeatureTypeListInfo;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

/**
 * Generic WFS GetCapabilities definition.
 *
 * @author Jan De Moerloose
 */
public class WfsGetCapabilitiesInfo100 extends AbstractXmlNodeWrapper implements WfsGetCapabilitiesInfo {

	private static final long serialVersionUID = 100L;

	WfsFeatureTypeListInfo featureTypeList;
	
	private transient boolean parsed;

	public WfsGetCapabilitiesInfo100(Node node) {
		super(node);
	}

	@Override
	public WfsFeatureTypeListInfo getFeatureTypeList() {
		if(!parsed) {
			parse(getNode());
		}
		return featureTypeList;
	}

	protected void parse(Node node) {
		parsed = true;
		if (node instanceof Element) {
			Element element = (Element) node;
			NodeList ftList = element.getElementsByTagName("FeatureTypeList");
			if(ftList.getLength() == 1) {
				featureTypeList = new WfsFeatureTypeListInfo100(ftList.item(0));
			} else {
				throw new IllegalArgumentException("Capabilities has no FeatureTypeList !");
			}
		}
	}

}