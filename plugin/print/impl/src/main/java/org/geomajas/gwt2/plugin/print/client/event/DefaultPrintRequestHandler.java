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

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Default implementation of {@link PrintRequestHandler}.
 * It will do nothing on a {@link PrintRequestStartedEvent}.
 * On a {@link PrintRequestFinishedEvent}, it open or download the printed document,
 * depending on the event information.
 *
 * @author Jan Venstermans
 */
public class DefaultPrintRequestHandler implements PrintRequestHandler {

	@Override
	public void onPrintRequestStarted(PrintRequestStartedEvent event) {
		// do nothing
	}

	@Override
	public void onPrintRequestFinished(PrintRequestFinishedEvent event) {
		switch (event.getPrintFinishedInfo().getPostPrintAction()) {
			case SAVE:
				Frame frame = new Frame();
				frame.setVisible(false);
				frame.setUrl(event.getPrintFinishedInfo().getEncodedUrl());
				frame.setPixelSize(0, 0);
				frame.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
				frame.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
				RootPanel.get().add(frame);
				break;
			default:
				Window.open(event.getPrintFinishedInfo().getEncodedUrl(), "_blank", null);
				break;
		}
	}
}
