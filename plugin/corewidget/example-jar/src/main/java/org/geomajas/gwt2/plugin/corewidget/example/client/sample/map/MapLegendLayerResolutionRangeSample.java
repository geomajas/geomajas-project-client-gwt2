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

package org.geomajas.gwt2.plugin.corewidget.example.client.sample.map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;

/**
 * ContentPanel that demonstrates changing layer order.
 * This specific version makes sure some layers are visible/invisible at certain resolutions.
 *
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
public class MapLegendLayerResolutionRangeSample implements SamplePanel {

	/**
	 * UI binder for this widget.
	 *
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, MapLegendLayerResolutionRangeSample> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	@UiField
	protected VerticalPanel legendPanel;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	public Widget asWidget() {
		// Define the left layout:
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter();
		mapPresenter.setSize(480, 480);

		// Add a MapLegendDropDown to the panel on the left:
		legendPanel.add(new MapLegendDropDown(mapPresenter));

		// Add a MapLegendDropDown to the MapPresenter:
		MapLegendDropDown mapDropDown = new MapLegendDropDown(mapPresenter);
		mapPresenter.getWidgetPane().add(mapDropDown);

		// Align the MapLegendDropDown on the map to the top-right:
		mapDropDown.getElement().getStyle().setTop(5, Unit.PX);
		mapDropDown.getElement().getStyle().setRight(5, Unit.PX);

		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		// Initialize the map, and return the layout:
		GeomajasServerExtension.getInstance().initializeMap(mapPresenter, "gwt-app", "mapLegendLayerResolutionRange");
		return layout;
	}
}