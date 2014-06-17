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
package org.geomajas.plugin.print.client;


import com.google.gwt.core.client.Callback;
import org.geomajas.plugin.print.client.event.PrintFinishedInfo;
import org.geomajas.plugin.print.client.event.PrintRequestHandler;
import org.geomajas.plugin.print.client.event.PrintRequestInfo;

/**
 * Service for print-related server calls.
 * 
 * @author Jan Venstermans
 * 
 */
public interface PrintService {

	/**
	 * Generic print method based on a {@link org.geomajas.plugin.print.command.dto.PrintTemplateInfo}.
	 * A {@link org.geomajas.plugin.print.command.dto.PrintGetTemplateRequest} is send to server.
	 * Based on response, an encode url is created.
	 * This url returned in a callback.
	 *
	 * @param printRequestInfo info necessary to create the request (and later process it), containing the template
	 * @param callback callback with the encodeUrl
	 */
	void print(PrintRequestInfo printRequestInfo, Callback<PrintFinishedInfo, Void> callback);

	/**
	 * Generic print method based on a {@link org.geomajas.plugin.print.command.dto.PrintTemplateInfo}.
	 * A {@link org.geomajas.plugin.print.command.dto.PrintGetTemplateRequest} is send to server.
	 * Based on response, an encode url is created.
	 * This url returned in a callback.
	 *
	 * @param printRequestInfo info necessary to create the request (and later process it), containing the template
	 * @param printRequestHandler handler that will listen to request events
	 */
	void print(PrintRequestInfo printRequestInfo, PrintRequestHandler printRequestHandler);

}
