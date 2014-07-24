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

package org.geomajas.plugin.print.example.client.sample;

import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.widget.MapLayoutPanel;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.plugin.print.client.Print;
import org.geomajas.plugin.print.client.event.DefaultPrintRequestHandler;
import org.geomajas.plugin.print.client.event.PrintRequestFinishedEvent;
import org.geomajas.plugin.print.client.event.PrintRequestInfo;
import org.geomajas.plugin.print.client.event.PrintRequestStartedEvent;
import org.geomajas.plugin.print.client.template.PageSize;
import org.geomajas.plugin.print.client.template.TemplateBuilderDataProvider;
import org.geomajas.plugin.print.client.template.impl.DefaultTemplateBuilder;
import org.geomajas.plugin.print.client.util.PrintConfiguration;
import org.geomajas.plugin.print.example.client.i18n.SampleMessages;
import org.geomajas.plugin.print.example.client.resources.PrintPluginExampleResource;
import org.geomajas.plugin.printing.command.dto.PrintTemplateInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Panel for example: calling {@link org.geomajas.plugin.print.client.PrintService}.
 * 
 * @author Jan Venstermans
 */
public class PrintExamplePrintServicePanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Jan De Moerloose
	 */
	interface MyUiBinder extends UiBinder<Widget, PrintExamplePrintServicePanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private static final PrintPluginExampleResource RESOURCE = PrintPluginExampleResource.INSTANCE;

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	private static final String WAIT_SUFFIX = "wait";

	private MapPresenter mapPresenter;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	private FlowPanel panel;

	public static final String APPLICATION_ID = "gwt-print-app";
	public static final String MAP_ID = "mapPrint";

	public Widget asWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);
		RESOURCE.css().ensureInjected();

		// Create the mapPresenter and add an InitializationHandler:
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter();
		mapPresenter.setSize(480, 480);

		// Define the whole layout:
		MapLayoutPanel mapLayout = new MapLayoutPanel();
		mapLayout.setPresenter(mapPresenter);
		mapPanel.add(mapLayout);

		// Initialize the map, and return the layout:
		GeomajasServerExtension.getInstance().initializeMap(mapPresenter, APPLICATION_ID, MAP_ID);

		panel = new FlowPanel();
		Button button = new Button();
		button.addStyleName(RESOURCE.css().printPanelButton());
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				PrintRequestInfo printRequestInfo = new PrintRequestInfo();
				printRequestInfo.setFileName(MESSAGES.printServiceExampleFixedFileName());
				printRequestInfo.setPostPrintAction(PrintConfiguration.PostPrintAction.SAVE);
				PrintTemplateInfo printTemplateInfo = Print.getInstance().getPrintUtil().
						createPrintTemplateInfo(mapPresenter, APPLICATION_ID,
								new DefaultTemplateBuilder(),
								new TemplateBuilderDataProviderImpl());
				printRequestInfo.setPrintTemplateInfo(printTemplateInfo);
				Print.getInstance().getPrintService().print(printRequestInfo, new CustomPrintRequestHandler());
			}

		});
		panel.add(button);
		panel.setStyleName(RESOURCE.css().printPanel());
		mapPresenter.getWidgetPane().add(panel.asWidget());

		return layout;
	}

	public void setWaitCursor(boolean isWait) {
		if (isWait) {
			panel.addStyleDependentName(WAIT_SUFFIX);
		} else {
			panel.removeStyleDependentName(WAIT_SUFFIX);
		}
	}

	/**
	 * Custom {@link DefaultPrintRequestHandler} that will change cursor of the print button.
	 */
	private class CustomPrintRequestHandler extends DefaultPrintRequestHandler {

		@Override
		public void onPrintRequestStarted(PrintRequestStartedEvent event) {
			setWaitCursor(true);
		}

		/**
		 * Default {@link org.geomajas.plugin.print.client.event.PrintRequestFinishedEvent}. Can be overwritten.
		 * @param event event
		 */
		@Override
		public void onPrintRequestFinished(PrintRequestFinishedEvent event) {
			setWaitCursor(false);
			super.onPrintRequestFinished(event);
		}

	}

	/**
	 * Custom {@link TemplateBuilderDataProvider} that provides default data (unchangeable for user).
	 */
	private class TemplateBuilderDataProviderImpl implements TemplateBuilderDataProvider {

		@Override
		public String getTitle() {
			return MESSAGES.printServiceExampleFixedPrintTitle();
		}

		@Override
		public boolean isLandscape() {
			return true;
		}

		@Override
		public PageSize getPageSize() {
			return PageSize.A4;
		}

		@Override
		public boolean isWithArrow() {
			return false;
		}

		@Override
		public boolean isWithScaleBar() {
			return true;
		}

		@Override
		public Integer getRasterDpi() {
			return 200;
		}
	}

}