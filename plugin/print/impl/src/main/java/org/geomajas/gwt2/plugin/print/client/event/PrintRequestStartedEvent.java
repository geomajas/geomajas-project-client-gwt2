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

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that is thrown when a print request has started.
 *
 * @author Jan Venstermans
 */
public class PrintRequestStartedEvent extends GwtEvent<PrintRequestHandler> {

	private PrintRequestInfo printRequestInfo;

	public PrintRequestStartedEvent(PrintRequestInfo printRequestInfo) {
		this.printRequestInfo = printRequestInfo;
	}

	@Override
	public Type<PrintRequestHandler> getAssociatedType() {
		return PrintRequestHandler.TYPE;
	}

	@Override
	protected void dispatch(PrintRequestHandler handler) {
	   handler.onPrintRequestStarted(this);
	}

	public PrintRequestInfo getPrintRequestInfo() {
		return printRequestInfo;
	}
}