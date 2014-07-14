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

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.widget.MapLayoutPanel;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.plugin.print.client.widget.PrintWidget;

/**
 * ContentPanel that demonstrates printing.
 * 
 * @author Jan De Moerloose
 */
public class PrintExamplePanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Jan De Moerloose
	 */
	interface MyUiBinder extends UiBinder<Widget, PrintExamplePanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	@UiField
	protected SimplePanel printPanel;

	public static final String APPLICATION_ID = "gwt-print-app";
	public static final String MAP_ID = "mapPrint";

	public Widget asWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Create the mapPresenter and add an InitializationHandler:
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter();
		mapPresenter.setSize(480, 480);

		// Define the whole layout:
		MapLayoutPanel mapLayout = new MapLayoutPanel();
		mapLayout.setPresenter(mapPresenter);
		mapPanel.add(mapLayout);

		// Initialize the map, and return the layout:
		GeomajasServerExtension.getInstance().initializeMap(mapPresenter, APPLICATION_ID, MAP_ID);

		setPrintPanelContent();

		return layout;
	}

	protected MapPresenter getMapPresenter() {
		return mapPresenter;
	}

	protected PrintWidget createPrintWidget() {
		return new PrintWidget(mapPresenter, APPLICATION_ID);
	}

	protected void setPrintPanelContent() {
		PrintWidget widget = createPrintWidget();
		printPanel.setWidget(widget.asWidget());
	}

}