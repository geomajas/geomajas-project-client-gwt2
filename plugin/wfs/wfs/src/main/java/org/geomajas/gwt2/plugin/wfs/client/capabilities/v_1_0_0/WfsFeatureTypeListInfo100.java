package org.geomajas.gwt2.plugin.wfs.client.capabilities.v_1_0_0;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt2.client.service.AbstractXmlNodeWrapper;
import org.geomajas.gwt2.plugin.wfs.client.capabilities.WfsFeatureTypeInfo;
import org.geomajas.gwt2.plugin.wfs.client.capabilities.WfsFeatureTypeListInfo;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;


public class WfsFeatureTypeListInfo100 extends AbstractXmlNodeWrapper implements WfsFeatureTypeListInfo {
	
	private transient boolean parsed;
	
	List<WfsFeatureTypeInfo> featureTypes = new ArrayList<WfsFeatureTypeInfo>();
	
	public WfsFeatureTypeListInfo100(Node node) {
		super(node);
	}

	@Override
	public List<WfsFeatureTypeInfo> getFeatureTypes() {
		if(!parsed) {
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
