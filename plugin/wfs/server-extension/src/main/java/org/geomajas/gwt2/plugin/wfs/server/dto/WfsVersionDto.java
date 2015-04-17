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

import java.io.Serializable;

/**
 * DTO for WFS version.
 * 
 * @author Jan De Moerloose
 *
 */
public enum WfsVersionDto implements Serializable {
	/**
	 * Version 1.0.0.
	 */
	V1_0_0("1.0.0"),
	/**
	 * Version 1.1.0.
	 */
	V1_1_0("1.1.0"),
	/**
	 * Version 2.0.0.
	 */
	V2_0_0("2.0.0");

	private String version;

	private WfsVersionDto(String version) {
		this.version = version;
	}

	public static WfsVersionDto fromString(String text) {
		if (text != null) {
			for (WfsVersionDto version : WfsVersionDto.values()) {
				if (text.equalsIgnoreCase(version.toString())) {
					return version;
				}
			}
		}
		return null;
	}

	public String toString() {
		return version;
	}
}
