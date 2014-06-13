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


import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.plugin.print.client.Print;
import org.geomajas.plugin.print.client.event.PrintRequestHandler;
import org.geomajas.plugin.print.client.template.PrintableLayerBuilder;
import org.geomajas.plugin.print.client.template.PrintableMapBuilder;
import org.geomajas.plugin.print.client.template.TemplateBuilderFactory;

/**
 *
 * 
 * @author Jan Venstermans
 * 
 */
public class PrintWidget implements IsWidget {

	private PrintWidgetPresenter presenter;

	public PrintWidget(MapPresenter mapPresenter, String applicationId) {
		this(mapPresenter, applicationId, Print.getInstance().getViewFactory().createPrintWidgetView());
	}

	public PrintWidget(MapPresenter mapPresenter, String applicationId, PrintWidgetView view) {
		presenter = new PrintWidgetPresenterImpl(mapPresenter, applicationId, view);
	}

	@Override
	public Widget asWidget() {
		return presenter.getView().asWidget();
	}

	public void registerLayerBuilder(PrintableLayerBuilder layerBuilder) {
		presenter.registerLayerBuilder(layerBuilder);
	}

	public void setTemplateBuilderFactory(TemplateBuilderFactory templateBuilderFactory) {
		presenter.setTemplateBuilderFactory(templateBuilderFactory);
	}

	public void setMapBuilder(PrintableMapBuilder mapBuilder) {
		presenter.setMapBuilder(mapBuilder);
	}

	public HandlerRegistration setPrintRequestHandler(PrintRequestHandler handler) {
		return presenter.setPrintRequestHandler(handler);
	}
}
