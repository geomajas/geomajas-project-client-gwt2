package org.geomajas.gwt2.plugin.wfs.server.command.dto;

import org.geomajas.command.CommandRequest;

public class WfsDescribeFeatureTypeRequest implements CommandRequest {

	private static final long serialVersionUID = 200L;

	public static final String COMMAND_NAME = "command.WfsDescribeFeatureType";

	private String baseUrl;

	private String typeName;

	@SuppressWarnings("unused")
	private WfsDescribeFeatureTypeRequest() {
	}

	public WfsDescribeFeatureTypeRequest(String baseUrl, String typeName) {
		this.baseUrl = baseUrl;
		this.typeName = typeName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
}
