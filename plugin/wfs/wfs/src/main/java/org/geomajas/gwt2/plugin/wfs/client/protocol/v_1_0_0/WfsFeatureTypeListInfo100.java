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
package org.geomajas.gwt2.plugin.wfs.client.protocol.v_1_0_0;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt2.client.service.AbstractXmlNodeWrapper;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureTypeInfo;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureTypeListInfo;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

/**
 * {@link WfsFeatureTypeListInfo} for version 1.0.0.
 * 
 * @author Jan De Moerloose
 *
 */
public class WfsFeatureTypeListInfo100 extends AbstractXmlNodeWrapper implements WfsFeatureTypeListInfo {

	private transient boolean parsed;

	private List<WfsFeatureTypeInfo> featureTypes = new ArrayList<WfsFeatureTypeInfo>();

	public WfsFeatureTypeListInfo100(Node node) {
		super(node);
	}

	@Override
	public List<WfsFeatureTypeInfo> getFeatureTypes() {
		if (!parsed) {
			parse(getNode());
		}
		return featureTypes;
	}

	protected void parse(Node node) {
		parsed = true;
		if (node instanceof Element) {
			Element element = (Element) node;
			NodeList ftList = element.getElementsByTagName("FeatureType");
			for (int i = 0; i < ftList.getLength(); i++) {
				Node ftNode = ftList.item(i);
				WfsFeatureTypeInfo100 requestInfo = new WfsFeatureTypeInfo100(ftNode);
				featureTypes.add(requestInfo);
			}
		}
	}

}
