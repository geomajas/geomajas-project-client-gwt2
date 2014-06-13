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
import org.geomajas.plugin.print.client.event.PrintFinishedEvent;
import org.geomajas.plugin.print.client.event.PrintFinishedHandler;
import org.geomajas.plugin.print.client.event.PrintFinishedInfo;
import org.geomajas.plugin.print.client.template.DefaultTemplateBuilder;
import org.geomajas.plugin.print.client.template.PageSize;
import org.geomajas.plugin.print.client.template.PrintableLayerBuilder;
import org.geomajas.plugin.print.client.template.PrintableMapBuilder;
import org.geomajas.plugin.print.client.template.TemplateBuilder;
import org.geomajas.plugin.print.client.template.TemplateBuilderFactory;
import org.geomajas.plugin.print.client.util.PrintLayout;
import org.geomajas.plugin.print.command.dto.PrintTemplateInfo;

/**
 * Initializes the GWT2 print plugin.
 * 
 * @author An Buyle
 * @author Jan Venstermans
 * 
 */
public class PrintWidgetPresenterImpl implements PrintWidgetPresenter, PrintFinishedHandler {

	/* Fields that are set on construction */

	private MapPresenter mapPresenter;

	private String applicationId;

	private PrintWidgetView view;

	/* Fields used for creation of PrintTemplateInfo objects. They are created using default values.
	 * The {@link TemplateBuilderFactory} can be overwritten via the setter. */

	private PrintableMapBuilder mapBuilder = new PrintableMapBuilder();

	private TemplateBuilderFactory templateBuilderFactory = new TemplateBuilderFactory() {
		@Override
		public TemplateBuilder createTemplateBuilder(PrintableMapBuilder mapBuilder) {
			return new DefaultTemplateBuilder(mapBuilder);
		}
	};

	/* events handler */
	private HandlerManager handlerManager;

	public PrintWidgetPresenterImpl(MapPresenter mapPresenter, String applicationId, PrintWidgetView view) {
		this.view = view;
		this.mapPresenter = mapPresenter;
		this.applicationId = applicationId;
		handlerManager = new HandlerManager(this);
		bind();
	}

	private void bind() {
		view.setHandler(this);
		// set default handlers
		setPrintFinishedHandler(this);
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
			Print.getInstance().getPrintService().print(createDefaultTemplateFromViewData(),
					new Callback<PrintFinishedInfo, Void>() {
				@Override
				public void onFailure(Void reason) {
					// do nothing
				}

				@Override
				public void onSuccess(PrintFinishedInfo printFinishedInfo) {
					 handlerManager.fireEvent(new PrintFinishedEvent(printFinishedInfo));
				}
			});
		}
	}

	/**
	 * Set the print finished handler.
	 * <p/>
	 * There can only be one handler, the default always opens the url in a new window.
	 *
	 * @param handler select location handler
	 * @return handler registration.
	 */
	@Override
	public HandlerRegistration setPrintFinishedHandler(PrintFinishedHandler handler) {
		if (handlerManager.getHandlerCount(PrintFinishedHandler.TYPE) > 0) {
			PrintFinishedHandler previous = handlerManager.getHandler(PrintFinishedHandler.TYPE, 0);
			handlerManager.removeHandler(PrintFinishedHandler.TYPE, previous);
		}
		return handlerManager.addHandler(PrintFinishedHandler.TYPE, handler);
	}

	/**
	 * Build a PrintTemplateInfo object using the {@link DefaultTemplateBuilder}.
	 * Some values are retrieved from the view element.
	 *
	 * @return constructed template
	 */
	protected PrintTemplateInfo createDefaultTemplateFromViewData() {
		TemplateBuilder builder = templateBuilderFactory.createTemplateBuilder(mapBuilder);

		// non-view data
		builder.setApplicationId(this.applicationId);
		builder.setMapPresenter(mapPresenter);
		builder.setMarginX((int) PrintLayout.templateMarginX);
		builder.setMarginY((int) PrintLayout.templateMarginY);

		// view data
		PageSize size = view.getPageSize();
		if (view.isLandscape()) {
			builder.setPageHeight(size.getWidth());
			builder.setPageWidth(size.getHeight());
		} else {
			builder.setPageHeight(size.getHeight());
			builder.setPageWidth(size.getWidth());
		}
		builder.setTitleText(view.getTitle());
		builder.setWithArrow(view.isWithArrow());
		builder.setWithScaleBar(view.isWithScaleBar());
		builder.setRasterDpi(view.getRasterDpi());
		return builder.buildTemplate();
	}

	/**
	 * Default {@link PrintFinishedEvent}. Can be overwritten.
	 * @param event event
	 */
	@Override
	public void onPrintFinished(PrintFinishedEvent event) {
		switch (event.getPrintFinishedInfo().getActionType()) {
			case SAVE:
				// TODO Converted to GWT2
				// // create a hidden iframe to avoid popups ???
				// HTMLPanel hiddenFrame = new HTMLPanel("<iframe src='" + encodedUrl
				// + "'+style='position:absolute;width:0;height:0;border:0'>");
				// hiddenFrame.setVisible(false);
				//
				// addChild(hiddenFrame);
				break;
			default:
				com.google.gwt.user.client.Window.open(event.getPrintFinishedInfo().getEncodedUrl(), "_blank", null);
				break;
		}
	}
}
