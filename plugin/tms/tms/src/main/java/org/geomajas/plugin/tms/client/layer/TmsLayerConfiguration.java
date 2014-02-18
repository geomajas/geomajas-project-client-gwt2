/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.tms.client.layer;

import org.geomajas.annotation.Api;

import java.io.Serializable;

/**
 * Configuration file for {@link org.geomajas.plugin.tms.client.layer.TmsLayer}s. The TMS layer will use these values to
 * fetch the correct tiles.
 *
 * @author Pieter De Graef
 * @since 2.1.0
 */
@Api(allMethods = true)
public class TmsLayerConfiguration implements Serializable {

	private static final long serialVersionUID = 210L;

	private String baseUrl;

	private String fileExtension;

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	/**
	 * Get the base URL to the TMS layer service.
	 *
	 * @return The base URL to the TMS layer service.
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Set the base URL to the TMS layer service. If the URL does not end with a '/' this method will add it.
	 *
	 * @param baseUrl The base URL to the TMS layer service.
	 */
	public void setBaseUrl(String baseUrl) {
		if (baseUrl.endsWith("/")) {
			this.baseUrl = baseUrl;
		} else {
			this.baseUrl = baseUrl + "/";
		}
	}

	/**
	 * Get the file extension used when fetching tiles.
	 *
	 * @return The file extension.
	 */
	public String getFileExtension() {
		return fileExtension;
	}

	/**
	 * Set the file extension used when fetching tiles. If the file extension does not start with a '.', this method
	 * will add it.
	 *
	 * @param fileExtension The file extension.
	 */
	public void setFileExtension(String fileExtension) {
		if (fileExtension.startsWith(".")) {
			this.fileExtension = fileExtension;
		} else {
			this.fileExtension = "." + fileExtension;
		}
	}
}