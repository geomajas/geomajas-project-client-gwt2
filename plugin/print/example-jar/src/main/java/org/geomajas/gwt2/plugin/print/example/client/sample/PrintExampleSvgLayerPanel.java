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

package org.geomajas.gwt2.plugin.print.example.client.sample;

import org.geomajas.gwt2.client.event.MapInitializationEvent;
import org.geomajas.gwt2.client.event.MapInitializationHandler;
import org.geomajas.gwt2.client.gfx.VectorContainer;
import org.geomajas.gwt2.plugin.print.client.layerbuilder.SvgClientLayerBuilder;
import org.geomajas.gwt2.plugin.print.client.widget.PrintWidget;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import com.google.gwt.user.client.ui.Widget;

/**
 * Extension of {@link PrintExamplePanel} for custom view.
 *
 * @author Jan Venstermans
 */
public class PrintExampleSvgLayerPanel extends PrintExamplePanel  {

	private VectorContainer container;

	@Override
	public Widget asWidget() {
		Widget widget = super.asWidget();
		getMapPresenter().getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());
		return widget;
	}

	@Override
	protected PrintWidget createPrintWidget() {
		PrintWidget printWidget = super.createPrintWidget();
		printWidget.registerLayerBuilder(new SvgClientLayerBuilder());
		return printWidget;
	}

	/**
	 * Map initialization handler that adds a CheckBox to the layout for every layer. With these CheckBoxes, the user
	 * can toggle the layer's visibility.
	 *
	 * @author Pieter De Graef
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		public void onMapInitialized(MapInitializationEvent event) {
			container = getMapPresenter().getContainerManager().addWorldContainer();

			Circle circle = new Circle(0, 0, 3000000);
			circle.setFillColor("#66CC66");
			circle.setFillOpacity(0.4);
			container.add(circle);

			Rectangle rectangle = new Rectangle(1000000, 1000000, 2000000, 1000000);
			rectangle.setFillColor("#CC9900");
			rectangle.setFillOpacity(0.4);
			container.add(rectangle);
		}
	}

}