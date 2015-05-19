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


/**
 * Request for the {@link org.geomajas.gwt2.plugin.wfs.server.command.WfsGetCapabilitiesCommand}. 
 * Should contain all the info needed to create an actual WFS request.
 *
 * @author Jan De Moerloose
 */
public class WfsGetCapabilitiesRequest extends AbstractWfsRequest {

	private static final long serialVersionUID = 100L;

	public static final String COMMAND_NAME = "command.WfsGetCapabilities";

	public WfsGetCapabilitiesRequest() {
	}

	public WfsGetCapabilitiesRequest(String baseUrl) {
		setBaseUrl(baseUrl);
	}
}
