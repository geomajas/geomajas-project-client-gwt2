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
package org.geomajas.gwt2.plugin.wfs.server.command.dto;

import org.geomajas.command.CommandRequest;
import org.geomajas.gwt2.plugin.wfs.server.dto.WfsVersionDto;

/**
 * Request for the {@link org.geomajas.gwt2.plugin.wfs.server.command.WfsGetCapabilitiesCommand}. 
 * Should contain all the info needed to create an actual WFS request.
 *
 * @author Jan De Moerloose
 */
public class WfsGetCapabilitiesRequest implements CommandRequest {

	private static final long serialVersionUID = 100L;

	public static final String COMMAND_NAME = "command.WfsGetCapabilities";

	private String baseUrl;

	private WfsVersionDto version = WfsVersionDto.V1_0_0;

	public WfsGetCapabilitiesRequest() {
	}

	public WfsVersionDto getVersion() {
		return version;
	}

	public void setVersion(WfsVersionDto version) {
		this.version = version;
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
