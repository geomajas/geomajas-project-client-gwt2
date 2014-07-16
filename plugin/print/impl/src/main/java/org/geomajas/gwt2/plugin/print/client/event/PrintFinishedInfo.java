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
package org.geomajas.gwt2.plugin.print.client.event;


import org.geomajas.gwt2.plugin.print.client.util.PrintConfiguration;

/**
 * Info object, containing info of the successful end of a print request.
 * 
 * @author Jan Venstermans
 * 
 */
public class PrintFinishedInfo {
	private String encodedUrl;
	private PrintConfiguration.PostPrintAction postPrintAction;

	public String getEncodedUrl() {
		return encodedUrl;
	}

	public void setEncodedUrl(String encodedUrl) {
		this.encodedUrl = encodedUrl;
	}

	public PrintConfiguration.PostPrintAction getPostPrintAction() {
		return postPrintAction;
	}

	public void setPostPrintAction(PrintConfiguration.PostPrintAction postPrintAction) {
		this.postPrintAction = postPrintAction;
	}
}
