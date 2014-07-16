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

package org.geomajas.gwt2.plugin.print.example.client.sample;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.plugin.print.client.Print;
import org.geomajas.gwt2.plugin.print.client.event.PrintRequestFinishedEvent;
import org.geomajas.gwt2.plugin.print.client.event.PrintRequestHandler;
import org.geomajas.gwt2.plugin.print.client.event.PrintRequestStartedEvent;
import org.geomajas.gwt2.plugin.print.client.widget.OptionsPrintPanel;
import org.geomajas.gwt2.plugin.print.client.widget.PrintWidget;
import org.geomajas.gwt2.plugin.print.example.client.i18n.SampleMessages;

/**
 * Extension of {@link PrintExamplePanel} for custom view.
 *
 * @author Jan Venstermans
 */
public class PrintExampleCustomHandlerPanel extends PrintExamplePanel  {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	@Override
	protected void setPrintPanelContent() {
		//add the print widget
		PrintWidget widget = createPrintWidget();
		PrintRequestListener listener = new PrintRequestListener();
		widget.setPrintRequestHandler(listener);

		VerticalPanel totalPanel = new VerticalPanel();
		totalPanel.add(widget.asWidget());
		totalPanel.add(listener.asWidget());
		printPanel.setWidget(totalPanel);
	}

	@Override
	protected PrintWidget createPrintWidget() {
		OptionsPrintPanel optionsPrintPanel = new OptionsPrintPanel();
		// only show those options that will change the output send to listener after print is finished.
		optionsPrintPanel.getOptionsToShowConfiguration().setShowFileNameOption(true);
		optionsPrintPanel.getOptionsToShowConfiguration().setShowPostPrintActionOption(true);
		optionsPrintPanel.createViewBasedOnConfiguration();
		return new PrintWidget(getMapPresenter(), APPLICATION_ID, optionsPrintPanel);
	}

	/**
	 * Class that will act as a listener to print events, implementing {@link PrintRequestHandler}.
	 * In this case, a panel will show some print info.
	 */
	public class PrintRequestListener implements IsWidget, PrintRequestHandler {

		private VerticalPanel handlerPanel;

		private Label statusLabel = new Label();

		private Label typeLabel = new Label();

		private Anchor urlLink = new Anchor(MESSAGES.printCustomHandlerUrlLabel(), false, null, "_blank");

		public PrintRequestListener() {
			handlerPanel = new VerticalPanel();
			handlerPanel.add(statusLabel);
			handlerPanel.add(typeLabel);
			handlerPanel.add(urlLink);
			setElementVisible(false);
			statusLabel.setText(MESSAGES.printCustomHandlerTitleInitial());
		}

		@Override
		public void onPrintRequestStarted(PrintRequestStartedEvent event) {
			statusLabel.setText(MESSAGES.printCustomHandlerTitleRequestStart());
			setElementVisible(false);
		}

		@Override
		public void onPrintRequestFinished(PrintRequestFinishedEvent event) {
			statusLabel.setText(MESSAGES.printCustomHandlerTitleRequestFinish());
			setTypeLabelContent(Print.getInstance().getPrintUtil().
					toString(event.getPrintFinishedInfo().getPostPrintAction()));
			urlLink.setHref(event.getPrintFinishedInfo().getEncodedUrl());
			setElementVisible(true);
		}

		private void setTypeLabelContent(String type) {
			typeLabel.setText(MESSAGES.printCustomHandlerTypeLabel(type));
		}

		private void setElementVisible(boolean visible) {
			typeLabel.setVisible(visible);
			urlLink.setVisible(visible);
		}

		@Override
		public Widget asWidget() {
			return handlerPanel;
		}
	}

}