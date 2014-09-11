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
package org.geomajas.gwt2.plugin.print.client;


import com.google.gwt.core.client.Callback;
import org.geomajas.annotation.Api;
import org.geomajas.gwt2.plugin.print.client.event.PrintRequestInfo;
import org.geomajas.gwt2.plugin.print.client.event.PrintFinishedInfo;
import org.geomajas.gwt2.plugin.print.client.event.PrintRequestHandler;

/**
 * Service for print-related server calls.
 * 
 * @author Jan Venstermans
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface PrintService {

	/**
	 * Generic print method. Input is a {@link org.geomajas.gwt2.plugin.print.client.event.PrintRequestInfo} object.<br>
	 * After a server processing has finished, a {@link PrintFinishedInfo} is returned in a callback.
	 * This respons object contains an encoded url for the printed object.
	 *
	 * @param printRequestInfo info necessary to create the request (and later process it), containing the template
	 * @param callback callback with the encodeUrl
	 */
	void print(PrintRequestInfo printRequestInfo, Callback<PrintFinishedInfo, Void> callback);

	/**
	 * Print method for a {@link PrintRequestHandler}.
	 * The handler will receive event calls when processing the {@link PrintRequestInfo}.
	 *
	 * @param printRequestInfo info necessary to create the request (and later process it), containing the template
	 * @param printRequestHandler handler that will listen to request events
	 */
	void print(PrintRequestInfo printRequestInfo, PrintRequestHandler printRequestHandler);

}
