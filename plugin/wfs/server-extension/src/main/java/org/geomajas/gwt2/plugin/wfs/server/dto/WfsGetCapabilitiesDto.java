package org.geomajas.gwt2.plugin.wfs.server.dto;

import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureTypeListInfo;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsGetCapabilitiesInfo;

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
