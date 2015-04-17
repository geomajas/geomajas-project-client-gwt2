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
package org.geomajas.gwt2.plugin.wfs.server.dto;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureTypeInfo;

/**
 * DTO class for a feature type.
 * 
 * @author Jan De Moerloose
 *
 */
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
