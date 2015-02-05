/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.plugin.print.client.event;

import org.geomajas.gwt2.plugin.print.client.event.PrintFinishedInfo.HttpMethod;
import org.geomajas.gwt2.plugin.print.client.util.UrlBuilder;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Default implementation of {@link PrintRequestHandler}. It will do nothing on a {@link PrintRequestStartedEvent}. On a
 * {@link PrintRequestFinishedEvent}, it open or download the printed document, depending on the event information.
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
		PrintFinishedInfo info = event.getPrintFinishedInfo();
		if (info.getMethod() == HttpMethod.GET) {
			switch (info.getPostPrintAction()) {
				case SAVE:
					createHiddenFrame(info);
					break;
				case OPEN:
				default:
					UrlBuilder builder = new UrlBuilder(info.getUrl());
					for (String name : info.getParams().keySet()) {
						builder.addParameter(name, info.getParams().get(name));
					}
					Window.open(builder.toString(), "_blank", null);
					break;
			}
		} else {
			createHiddenForm(info);
		}
	}

	private void createHiddenForm(PrintFinishedInfo info) {
		final FormPanel panel;
		switch (info.getPostPrintAction()) {
			case SAVE:
				panel = new FormPanel();
				break;
			case OPEN:
			default:
				panel = new FormPanel("_blank");
				break;
		}
		panel.setVisible(false);
		panel.setPixelSize(0, 0);
		panel.setAction(info.getUrl());
		panel.setMethod(FormPanel.METHOD_POST);
		FlowPanel fieldsPanel = new FlowPanel();
		panel.add(fieldsPanel);
		for (String name : info.getParams().keySet()) {
			fieldsPanel.add(new Hidden(name, info.getParams().get(name)));
		}
		panel.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
		panel.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
		RootPanel.get().add(panel);
		panel.submit();
	}

	private void createHiddenFrame(PrintFinishedInfo info) {
		final Frame frame = new Frame();
		frame.setVisible(false);
		UrlBuilder builder = new UrlBuilder(info.getUrl());
		for (String name : info.getParams().keySet()) {
			builder.addParameter(name, info.getParams().get(name));
		}
		frame.setUrl(builder.toString());
		frame.setPixelSize(0, 0);
		frame.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
		frame.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
		frame.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(LoadEvent event) {
				frame.removeFromParent();
			}
		});
		RootPanel.get().add(frame);
	}
}
