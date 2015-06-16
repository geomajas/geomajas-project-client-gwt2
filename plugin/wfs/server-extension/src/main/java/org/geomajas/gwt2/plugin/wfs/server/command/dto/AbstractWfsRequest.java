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
 * Common base for all WFS request DTOs.
 * 
 * @author Jan De Moerloose
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractWfsRequest implements CommandRequest {

	private String baseUrl;

	private WfsVersionDto version;

	private String strategy;

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public WfsVersionDto getVersion() {
		return version;
	}

	public void setVersion(WfsVersionDto version) {
		this.version = version;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

}
