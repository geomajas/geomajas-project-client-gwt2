/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.gwt2.tools.example.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.tools.client.tool.zoomin.ZoomInToolWidget;

/**
 * @author Oliver May
 */
public class ToolPanel implements SamplePanel {

	private MapPresenter mapPresenter;

	@UiField
	protected SimplePanel toolsPanel;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	/**
	 * UI binder for this widget.
	 *
	 * @author Oliver May
	 */
	interface MyUiBinder extends UiBinder<Widget, ToolPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	@Override
	public Widget asWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Initialize the map, and return the layout:
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter();
		mapPresenter.setSize(480, 480);
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		GeomajasServerExtension.initializeMap(mapPresenter, "gwt-app", "mapCountries");

		ZoomInToolWidget zoomInWidget = new ZoomInToolWidget(mapPresenter);
		toolsPanel.add(zoomInWidget);

		return layout;
	}
}
