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

import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureTypeListInfo;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsGetCapabilitiesInfo;

/**
 * DTO for WFS GetCapabilities result.
 * 
 * @author Jan De Moerloose
 *
 */
public class WfsGetCapabilitiesDto implements WfsGetCapabilitiesInfo {

	private static final long serialVersionUID = 100L;

	private WfsFeatureTypeListDto featureTypeList;

	@SuppressWarnings("unused")
	private WfsGetCapabilitiesDto() {

	}

	public WfsGetCapabilitiesDto(WfsFeatureTypeListDto featureTypeListDto) {
		this.featureTypeList = featureTypeListDto;
	}

	public WfsFeatureTypeListInfo getFeatureTypeList() {
		return featureTypeList;
	}

	public void setFeatureTypeList(WfsFeatureTypeListDto featureTypeList) {
		this.featureTypeList = featureTypeList;
	}

}
