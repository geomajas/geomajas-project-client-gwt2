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

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Handler for {@link PrintRequestFinishedEvent}.
 *
 * @author Jan Venstermans
 */
public interface PrintRequestHandler extends EventHandler {

	/** Event type. */
	GwtEvent.Type<PrintRequestHandler> TYPE = new GwtEvent.Type<PrintRequestHandler>();

	/**
	 * Called when the location is selected either because the geocoder returned a match or because the user
	 * chose one of the alternatives.
	 *
	 * @param event event, contains the location which is selected
	 */
	void onPrintRequestStarted(PrintRequestStartedEvent event);

	/**
	 * Called when the location is selected either because the geocoder returned a match or because the user
	 * chose one of the alternatives.
	 *
	 * @param event event, contains the location which is selected
	 */
	void onPrintRequestFinished(PrintRequestFinishedEvent event);
}
