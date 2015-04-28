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

import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureTypeInfo;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureTypeListInfo;

/**
 * DTO class for a feature type list.
 * 
 * @author Jan De Moerloose
 *
 */
public class WfsFeatureTypeListDto implements WfsFeatureTypeListInfo {

	private static final long serialVersionUID = 100L;

	private List<WfsFeatureTypeInfo> featureTypes = new ArrayList<WfsFeatureTypeInfo>();

	public List<WfsFeatureTypeInfo> getFeatureTypes() {
		return featureTypes;
	}

	public void setFeatureTypes(List<WfsFeatureTypeDto> featureTypes) {
		this.featureTypes = new ArrayList<WfsFeatureTypeInfo>(featureTypes);
	}

	public void add(WfsFeatureTypeDto wfsFeatureTypeDto) {
		featureTypes.add(wfsFeatureTypeDto);
	}

}
