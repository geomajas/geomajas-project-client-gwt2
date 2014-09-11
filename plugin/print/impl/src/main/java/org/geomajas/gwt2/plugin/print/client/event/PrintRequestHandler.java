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

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import org.geomajas.annotation.Api;

/**
 * Handler for {@link PrintRequestFinishedEvent}.
 *
 * @author Jan Venstermans
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface PrintRequestHandler extends EventHandler {

	/** Event type. */
	GwtEvent.Type<PrintRequestHandler> TYPE = new GwtEvent.Type<PrintRequestHandler>();

	/**
	 * Called just before a print request is send to the server.
	 *
	 * @param event event, contains a {@link PrintRequestInfo}.
	 */
	void onPrintRequestStarted(PrintRequestStartedEvent event);

	/**
	 * Called when a print request has been successfully processed.
	 *
	 * @param event event, contains a {@link PrintFinishedInfo}.
	 */
	void onPrintRequestFinished(PrintRequestFinishedEvent event);
}
