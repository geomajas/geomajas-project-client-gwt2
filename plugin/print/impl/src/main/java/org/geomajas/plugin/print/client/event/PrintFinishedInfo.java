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
package org.geomajas.plugin.print.client.event;


import org.geomajas.plugin.print.client.PrintSettings;

/**
 * Info object, containing info necessary for throwing event.
 * 
 * @author Jan Venstermans
 * 
 */
public class PrintFinishedInfo {
	private String encodedUrl;
	private PrintSettings.ActionType actionType;

	public String getEncodedUrl() {
		return encodedUrl;
	}

	public void setEncodedUrl(String encodedUrl) {
		this.encodedUrl = encodedUrl;
	}

	public PrintSettings.ActionType getActionType() {
		return actionType;
	}

	public void setActionType(PrintSettings.ActionType actionType) {
		this.actionType = actionType;
	}
}
