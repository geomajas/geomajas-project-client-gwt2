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
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.plugin.print.client.Print;
import org.geomajas.plugin.print.client.event.DefaultPrintRequestHandler;
import org.geomajas.plugin.print.client.event.PrintFinishedInfo;
import org.geomajas.plugin.print.client.event.PrintRequestFinishedEvent;
import org.geomajas.plugin.print.client.event.PrintRequestHandler;
import org.geomajas.plugin.print.client.event.PrintRequestInfo;
import org.geomajas.plugin.print.client.event.PrintRequestStartedEvent;
import org.geomajas.plugin.print.client.layerbuilder.PrintableLayerBuilder;
import org.geomajas.plugin.print.client.template.impl.DefaultPrintableMapBuilder;
import org.geomajas.plugin.print.client.template.impl.DefaultTemplateBuilder;
import org.geomajas.plugin.print.client.template.PrintableMapBuilder;
import org.geomajas.plugin.print.client.template.TemplateBuilder;
import org.geomajas.plugin.print.client.template.TemplateBuilderFactory;

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

	/**
	 *  Field used for creation of {@link org.geomajas.plugin.print.command.dto.PrintTemplateInfo} objects.
	 *  It is created with a default value.
	 */
	private PrintableMapBuilder mapBuilder = new DefaultPrintableMapBuilder();

	/**
	 *  Field used for creation of {@link org.geomajas.plugin.print.command.dto.PrintTemplateInfo} objects.
	 *  It is created with a default value. This defaults can be overwritten via the setter
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
	public void print() {
		if (mapPresenter != null) {
			PrintRequestInfo printRequestInfo = new PrintRequestInfo();
			printRequestInfo.setPostPrintAction(view.getPostPrintAction());
			printRequestInfo.setFileName(view.getFileName());
			printRequestInfo.setPrintTemplateInfo(Print.getInstance().getPrintUtil().
					createPrintTemplateInfo(mapPresenter, applicationId,
							templateBuilderFactory.createTemplateBuilder(mapBuilder),
							view.getTemplateBuilderDataProvider()));
			handlerManager.fireEvent(new PrintRequestStartedEvent(printRequestInfo));
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

	@Override
	public HandlerRegistration setPrintRequestHandler(PrintRequestHandler handler) {
		if (handlerManager.getHandlerCount(PrintRequestHandler.TYPE) > 0) {
			PrintRequestHandler previous = handlerManager.getHandler(PrintRequestHandler.TYPE, 0);
			handlerManager.removeHandler(PrintRequestHandler.TYPE, previous);
		}
		return handlerManager.addHandler(PrintRequestHandler.TYPE, handler);
	}
}
