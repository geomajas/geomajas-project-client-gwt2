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
import org.geomajas.annotation.Api;
import org.geomajas.plugin.print.client.event.PrintRequestHandler;
import org.geomajas.plugin.print.client.template.TemplateBuilderFactory;
import org.geomajas.plugin.print.client.layerbuilder.PrintableLayerBuilder;

/**
 * Presenter for the {@link org.geomajas.plugin.print.client.widget.PrintWidget}.
 * 
 * @author Jan Venstermans
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface PrintWidgetPresenter {

	/**
	 * Setter for the single {@link TemplateBuilderFactory}.
	 * @param templateBuilderFactory
	 */
	void setTemplateBuilderFactory(TemplateBuilderFactory templateBuilderFactory);

	/**
	 * Basic print method.
	 */
	void print();

	/**
	 * Method for registering a {@link PrintableLayerBuilder}.
	 * This will allow for specific layers to be rendered in the printed map.
	 * Multiple layerBuilders can be registered at the same time.
	 *
	 * @param layerBuilder
	 */
	void registerLayerBuilder(PrintableLayerBuilder layerBuilder);

	/**
	 * Returns the view of the presenter.
	 * @return view
	 */
	PrintWidgetView getView();

	/**
	 * Setter for the {@link PrintRequestHandler}. There can only be one handler registered at the time.
	 * Registering a handler will overwrite the previously registered one.
	 *
	 * @param handler
	 * @return registration object of the handler
	 */
	HandlerRegistration setPrintRequestHandler(PrintRequestHandler handler);
}
