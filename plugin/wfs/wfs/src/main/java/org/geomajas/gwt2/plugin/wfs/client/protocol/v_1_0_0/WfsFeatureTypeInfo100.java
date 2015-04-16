package org.geomajas.gwt2.plugin.wfs.client.protocol.v_1_0_0;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.service.AbstractXmlNodeWrapper;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureTypeInfo;

import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class WfsFeatureTypeInfo100 extends AbstractXmlNodeWrapper implements WfsFeatureTypeInfo {

	private static final long serialVersionUID = 100L;

	private String name;

	private String title;

	private String abstractt;

	private List<String> keywords = new ArrayList<String>();

	private String defaultCrs;

	private Bbox wGS84BoundingBox;

	private transient boolean parsed;

	public WfsFeatureTypeInfo100(Node node) {
		super(node);
	}

	public String getName() {
		if (!parsed) {
			parse(getNode());
		}
		return name;
	}

	public String getTitle() {
		if (!parsed) {
			parse(getNode());
		}
		return title;
	}

	public String getAbstract() {
		if (!parsed) {
			parse(getNode());
		}
		return abstractt;
	}

	public List<String> getKeywords() {
		if (!parsed) {
			parse(getNode());
		}
		return keywords;
	}

	public String getDefaultCrs() {
		if (!parsed) {
			parse(getNode());
		}
		return defaultCrs;
	}

	public Bbox getWGS84BoundingBox() {
		if (!parsed) {
			parse(getNode());
		}
		return wGS84BoundingBox;
	}

	// ------------------------------------------------------------------------
	// AbstractNodeInfo implementation:
	// ------------------------------------------------------------------------

	protected void parse(Node node) {
		parsed = true;
		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			String nodeName = child.getNodeName();
			if ("Name".equalsIgnoreCase(nodeName)) {
				name = getValueRecursive(child);
			} else if ("Title".equalsIgnoreCase(nodeName)) {
				title = getValueRecursive(child);
			} else if ("Abstract".equalsIgnoreCase(nodeName)) {
				abstractt = getValueRecursive(child);
			} else if ("Keywords".equalsIgnoreCase(nodeName)) {
				addKeyWords(child);
			} else if ("SRS".equalsIgnoreCase(nodeName)) {
				defaultCrs = getValueRecursive(child);
			} else if ("LatLongBoundingBox".equalsIgnoreCase(nodeName)) {
				addLatLonBoundingBox(child);
			}
		}
	}

	private void addKeyWords(Node keywordListNode) {
		String keywordList = getValueRecursive(keywordListNode);
		if (keywordList != null) {
			if (keywordList.contains(",")) {
				String[] parts = keywordList.split(",");
				for (int i = 0; i < parts.length; i++) {
					keywords.add(parts[i].trim());
				}
			} else {
				keywords.add(keywordList.trim());
			}
		}
	}

	private void addLatLonBoundingBox(Node bboxNode) {
		NamedNodeMap attributes = bboxNode.getAttributes();
		double x = 0, y = 0, maxx = 0, maxy = 0;
		Node n = attributes.getNamedItem("minx");
		if (n != null) {
			x = getValueRecursiveAsDouble(n);
		}
		n = attributes.getNamedItem("miny");
		if (n != null) {
			y = getValueRecursiveAsDouble(n);
		}
		n = attributes.getNamedItem("maxx");
		if (n != null) {
			maxx = getValueRecursiveAsDouble(n);
		}
		n = attributes.getNamedItem("maxy");
		if (n != null) {
			maxy = getValueRecursiveAsDouble(n);
		}
		wGS84BoundingBox = new Bbox(x, y, maxx - x, maxy - y);
	}

}
