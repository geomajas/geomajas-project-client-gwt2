package org.geomajas.gwt2.plugin.wfs.server.command.dto;

import org.geomajas.command.CommandResponse;
import org.geomajas.gwt2.plugin.wfs.server.dto.WfsGetCapabilitiesDto;

public class WfsGetCapabilitiesResponse extends CommandResponse {

	private static final long serialVersionUID = 1L;

	private WfsGetCapabilitiesDto getCapabilitiesDto;

	public WfsGetCapabilitiesResponse() {
	}

	public WfsGetCapabilitiesDto getGetCapabilitiesDto() {
		return getCapabilitiesDto;
	}

	public void setGetCapabilitiesDto(WfsGetCapabilitiesDto getCapabilitiesDto) {
		this.getCapabilitiesDto = getCapabilitiesDto;
	}

}
