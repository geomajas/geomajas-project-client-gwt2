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
package org.geomajas.plugin.print.client.util;


/**
 * Configuration options for download.
 * 
 * @author An Buyle
 * @author Jan Venstermans
 * 
 */
public interface PrintConfiguration {

	/**
	 * Enum choice of what action should be performed when the print object has been created.
	 * @author Jan Venstermans
	 */
	public enum PostPrintAction {
		SAVE, // save as a file
		OPEN; // open in a new window
	}

	String URL_DOWNLOAD_YES = "1";

	String URL_DOWNLOAD_NO = "0";

	String DEFAULT_DOWNLOAD_EXTENSION = ".pdf";

}
