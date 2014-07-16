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
package org.geomajas.gwt2.plugin.print.client.widget;


import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.plugin.print.client.Print;
import org.geomajas.gwt2.plugin.print.client.layerbuilder.PrintableLayerBuilder;
import org.geomajas.gwt2.plugin.print.client.event.PrintRequestHandler;
import org.geomajas.gwt2.plugin.print.client.template.TemplateBuilderFactory;

/**
 * Widget for print function.
 * 
 * @author Jan Venstermans
 * @since 2.1.0
 * 
 */
@Api(allMethods = true)
public class PrintWidget implements IsWidget {

	private PrintWidgetPresenter presenter;

	/**
	 * Default constructor. Print widget needs the {@link org.geomajas.gwt2.client.map.MapPresenter} and the
	 * applicationId for the map that needs to be printed.
	 *
	 * @param mapPresenter
	 * @param applicationId
	 */
	public PrintWidget(MapPresenter mapPresenter, String applicationId) {
		this(mapPresenter, applicationId, Print.getInstance().getViewFactory().createPrintWidgetView());
	}

	/**
	 * View constructor. The view can be customized.
	 *
	 * @param mapPresenter
	 * @param applicationId
	 * @param view
	 */
	public PrintWidget(MapPresenter mapPresenter, String applicationId, PrintWidgetView view) {
		presenter = new PrintWidgetPresenterImpl(mapPresenter, applicationId, view);
	}

	@Override
	public Widget asWidget() {
		return presenter.getView().asWidget();
	}

	/**
	 * Method for registering a {@link org.geomajas.gwt2.plugin.print.client.layerbuilder.PrintableLayerBuilder}.
	 * This will allow for specific layers to be rendered in the printed map.
	 * Multiple layerBuilders can be registered at the same time.
	 *
	 * @param layerBuilder
	 */
	public void registerLayerBuilder(PrintableLayerBuilder layerBuilder) {
		presenter.registerLayerBuilder(layerBuilder);
	}

	/**
	 * Setter for the single {@link TemplateBuilderFactory}.
	 * @param templateBuilderFactory
	 */
	public void setTemplateBuilderFactory(TemplateBuilderFactory templateBuilderFactory) {
		presenter.setTemplateBuilderFactory(templateBuilderFactory);
	}

	/**
	 * Setter for the {@link PrintRequestHandler}. There can only be one handler registered at the time.
	 * Registering a handler will overwrite the previously registered one.
	 *
	 * @param handler
	 * @return registration object of the handler
	 */
	public HandlerRegistration setPrintRequestHandler(PrintRequestHandler handler) {
		return presenter.setPrintRequestHandler(handler);
	}
}
