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
package org.geomajas.plugin.print.client.widget;


import com.google.gwt.core.client.Callback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.RootPanel;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.plugin.print.client.Print;
import org.geomajas.plugin.print.client.event.PrintFinishedInfo;
import org.geomajas.plugin.print.client.event.PrintRequestInfo;
import org.geomajas.plugin.print.client.event.PrintRequestFinishedEvent;
import org.geomajas.plugin.print.client.event.PrintRequestHandler;
import org.geomajas.plugin.print.client.event.PrintRequestStartedEvent;
import org.geomajas.plugin.print.client.template.DefaultTemplateBuilder;
import org.geomajas.plugin.print.client.template.PrintableLayerBuilder;
import org.geomajas.plugin.print.client.template.PrintableMapBuilder;
import org.geomajas.plugin.print.client.template.TemplateBuilder;
import org.geomajas.plugin.print.client.template.TemplateBuilderFactory;
import org.geomajas.plugin.print.client.util.PrintLayout;
import org.geomajas.plugin.print.command.dto.PrintTemplateInfo;

/**
 * Default implementation of {@link PrintWidgetPresenter}.
 * 
 * @author An Buyle
 * @author Jan Venstermans
 * 
 */
public class PrintWidgetPresenterImpl implements PrintWidgetPresenter {

	/* Fields that are set on construction */

	private MapPresenter mapPresenter;

	private String applicationId;

	private PrintWidgetView view;

	/* Fields used for creation of PrintTemplateInfo objects. They are created using default values.
	 * These defaults can be overwritten via their setter. */

	/**
	 *  Field used for creation of {@link PrintTemplateInfo} objects. It is created with a default value.
	 * This defaults can be overwritten via the setter
	 */
	private PrintableMapBuilder mapBuilder = new PrintableMapBuilder();

	/**
	 *  Field used for creation of {@link PrintTemplateInfo} objects. It is created with a default value.
	 * This defaults can be overwritten via the setter
	 */
	private TemplateBuilderFactory templateBuilderFactory = new TemplateBuilderFactory() {
		@Override
		public TemplateBuilder createTemplateBuilder(PrintableMapBuilder mapBuilder) {
			return new DefaultTemplateBuilder(mapBuilder);
		}
	};

	/* events handler */
	private HandlerManager handlerManager;

	/**
	 * Default constructor.
	 *
	 * @param mapPresenter mapPresenter of the map to be printed.
	 * @param applicationId Id of the application whose elements need to be printed.
	 * @param view view
	 */
	public PrintWidgetPresenterImpl(MapPresenter mapPresenter, String applicationId, PrintWidgetView view) {
		this.view = view;
		this.mapPresenter = mapPresenter;
		this.applicationId = applicationId;
		handlerManager = new HandlerManager(this);
		bind();
	}

	private void bind() {
		view.setHandler(this);
		// set default handler
		setPrintRequestHandler(new DefaultPrintRequestHandler());
	}

	@Override
	public void registerLayerBuilder(PrintableLayerBuilder layerBuilder) {
		mapBuilder.registerLayerBuilder(layerBuilder);
	}

	@Override
	public PrintWidgetView getView() {
		return view;
	}

	@Override
	public void setTemplateBuilderFactory(TemplateBuilderFactory templateBuilderFactory) {
		this.templateBuilderFactory = templateBuilderFactory;
	}

	@Override
	public void setMapBuilder(PrintableMapBuilder mapBuilder) {
		this.mapBuilder = mapBuilder;
	}

	@Override
	public void print() {
		if (mapPresenter != null) {
			PrintRequestInfo printRequestInfo = new PrintRequestInfo();
			printRequestInfo.setPostPrintAction(view.getActionType());
			printRequestInfo.setFileName(view.getFileName());
			printRequestInfo.setPrintTemplateInfo(createDefaultTemplateFromViewData());
			PrintRequestStartedEvent startedEvent = new PrintRequestStartedEvent();
			startedEvent.setPrintRequestInfo(printRequestInfo);
			handlerManager.fireEvent(startedEvent);
			Print.getInstance().getPrintService().print(printRequestInfo,
					new Callback<PrintFinishedInfo, Void>() {
				@Override
				public void onFailure(Void reason) {
					// do nothing
				}

				@Override
				public void onSuccess(PrintFinishedInfo printFinishedInfo) {
					 handlerManager.fireEvent(new PrintRequestFinishedEvent(printFinishedInfo));
				}
			});
		}
	}

	/**
	 * Set the {@link PrintRequestHandler}; there can only be one handler.
	 * <p/>
	 * The default handler is {@link DefaultPrintRequestHandler}.
	 *
	 * @param handler select location handler
	 * @return handler registration.
	 */
	@Override
	public HandlerRegistration setPrintRequestHandler(PrintRequestHandler handler) {
		if (handlerManager.getHandlerCount(PrintRequestHandler.TYPE) > 0) {
			PrintRequestHandler previous = handlerManager.getHandler(PrintRequestHandler.TYPE, 0);
			handlerManager.removeHandler(PrintRequestHandler.TYPE, previous);
		}
		return handlerManager.addHandler(PrintRequestHandler.TYPE, handler);
	}

	/**
	 * Build a PrintTemplateInfo object using the {@link DefaultTemplateBuilder}.
	 * Some values are retrieved from the view element.
	 *
	 * @return constructed template
	 */
	protected PrintTemplateInfo createDefaultTemplateFromViewData() {
		TemplateBuilder builder = templateBuilderFactory.createTemplateBuilder(mapBuilder);
		builder.setApplicationId(this.applicationId);
		builder.setMapPresenter(mapPresenter);
		builder.setMarginX((int) PrintLayout.templateMarginX);
		builder.setMarginY((int) PrintLayout.templateMarginY);

		Print.getInstance().getPrintUtil().copyProviderDataToBuilder(builder, view.getTemplateBuilderDataProvider());

		return builder.buildTemplate();
	}

	/**
	 * Default implementation of {@link PrintRequestHandler}. This handler will be registered by default
	 * when the {@link org.geomajas.plugin.print.client.widget.PrintWidgetPresenterImpl} is created.
	 */
	public class DefaultPrintRequestHandler implements PrintRequestHandler {

		@Override
		public void onPrintRequestStarted(PrintRequestStartedEvent event) {
			// do nothing
		}

		/**
		 * Default {@link org.geomajas.plugin.print.client.event.PrintRequestFinishedEvent}. Can be overwritten.
		 * @param event event
		 */
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
}
