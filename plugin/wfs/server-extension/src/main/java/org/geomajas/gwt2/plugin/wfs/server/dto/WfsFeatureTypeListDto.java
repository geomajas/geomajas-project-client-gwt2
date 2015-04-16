package org.geomajas.gwt2.plugin.wfs.server.dto;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureTypeInfo;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureTypeListInfo;

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
