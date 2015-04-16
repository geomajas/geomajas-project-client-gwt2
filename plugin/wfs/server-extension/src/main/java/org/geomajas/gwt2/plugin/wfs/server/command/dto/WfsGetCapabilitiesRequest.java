package org.geomajas.gwt2.plugin.wfs.server.command.dto;

import org.geomajas.command.CommandRequest;


public class WfsGetCapabilitiesRequest implements CommandRequest {

	private static final long serialVersionUID = 100L;

	public static final String COMMAND_NAME = "command.WfsGetCapabilities";

	private String baseUrl;

	private WfsGetCapabilitiesRequest() {
	}

	public WfsGetCapabilitiesRequest(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
}
