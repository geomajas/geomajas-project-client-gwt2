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
 * Event that is thrown when a print request has finished.
 *
 * @author Jan Venstermans
 */
public class PrintRequestFinishedEvent extends GwtEvent<PrintRequestHandler> {

	private PrintFinishedInfo printFinishedInfo;

	public PrintRequestFinishedEvent(PrintFinishedInfo printFinishedInfo) {
		this.printFinishedInfo = printFinishedInfo;
	}

	@Override
	public Type<PrintRequestHandler> getAssociatedType() {
		return PrintRequestHandler.TYPE;
	}

	@Override
	protected void dispatch(PrintRequestHandler handler) {
	   handler.onPrintRequestFinished(this);
	}

	public PrintFinishedInfo getPrintFinishedInfo() {
		return printFinishedInfo;
	}

}