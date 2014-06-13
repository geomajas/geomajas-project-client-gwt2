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
import org.geomajas.plugin.print.client.event.PrintFinishedHandler;
import org.geomajas.plugin.print.client.template.PrintableLayerBuilder;
import org.geomajas.plugin.print.client.template.PrintableMapBuilder;
import org.geomajas.plugin.print.client.template.TemplateBuilderFactory;

/**
 * Presenter for the print widgets.
 * 
 * @author Jan Venstermans
 * 
 */
public interface PrintWidgetPresenter {

	void setTemplateBuilderFactory(TemplateBuilderFactory templateBuilderFactory);

	void setMapBuilder(PrintableMapBuilder mapBuilder);

	void print();

	void registerLayerBuilder(PrintableLayerBuilder layerBuilder);

	PrintWidgetView getView();

	HandlerRegistration setPrintFinishedHandler(PrintFinishedHandler handler);
}
