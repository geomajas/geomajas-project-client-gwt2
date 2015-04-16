package org.geomajas.gwt2.plugin.wfs.server.command.dto;

import org.geomajas.command.CommandResponse;
import org.geomajas.gwt2.plugin.wfs.server.dto.WfsFeatureTypeDescriptionDto;

public class WfsDescribeFeatureTypeResponse extends CommandResponse {

	private static final long serialVersionUID = 100L;

	private WfsFeatureTypeDescriptionDto featureTypeDescription;

	public WfsDescribeFeatureTypeResponse() {
	}

	public WfsFeatureTypeDescriptionDto getFeatureTypeDescription() {
		return featureTypeDescription;
	}

	public void setFeatureTypeDescription(WfsFeatureTypeDescriptionDto featureTypeDescription) {
		this.featureTypeDescription = featureTypeDescription;
	}

}
