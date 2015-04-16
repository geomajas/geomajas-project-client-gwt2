package org.geomajas.gwt2.plugin.wfs.server.dto;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureTypeInfo;

public class WfsFeatureTypeDto implements WfsFeatureTypeInfo {

	private static final long serialVersionUID = 100L;

	private String name;

	private String title;

	private String abstractt;

	private List<String> keywords = new ArrayList<String>();

	private String defaultCrs;

	private Bbox wGS84BoundingBox;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAbstract() {
		return abstractt;
	}

	public void setAbstract(String abstractt) {
		this.abstractt = abstractt;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public String getDefaultCrs() {
		return defaultCrs;
	}

	public void setDefaultCrs(String defaultCrs) {
		this.defaultCrs = defaultCrs;
	}

	public Bbox getWGS84BoundingBox() {
		return wGS84BoundingBox;
	}

	public void setWGS84BoundingBox(Bbox wGS84BoundingBox) {
		this.wGS84BoundingBox = wGS84BoundingBox;
	}

}
