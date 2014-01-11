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

package org.geomajas.gwt2.example.client.sample.general;

import java.util.Iterator;

import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.event.MapInitializationEvent;
import org.geomajas.gwt2.client.event.MapInitializationHandler;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.widget.MapLayoutPanel;
import org.geomajas.gwt2.client.widget.control.pan.PanControl;
import org.geomajas.gwt2.client.widget.control.zoom.ZoomControl;
import org.geomajas.gwt2.client.widget.control.zoom.ZoomStepControl;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;

import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates alternative controls on the map.
 * 
 * @author Pieter De Graef
 */
public class AlternativeControlsPanel implements SamplePanel {

	private MapPresenter mapPresenter;

	public Widget asWidget() {
		// Define the layout:
		ResizeLayoutPanel resizeLayoutPanel = new ResizeLayoutPanel();
		final MapLayoutPanel layout = new MapLayoutPanel();
		resizeLayoutPanel.setWidget(layout);
		resizeLayoutPanel.setSize("100%", "100%");

		// Create the MapPresenter and add to the layout:
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter();
		layout.setPresenter(mapPresenter);

		// Initialize the map, and return the layout:
		GeomajasServerExtension.getInstance().initializeMap(mapPresenter, "gwt-app", "mapOsm");

		// Install alternative controls on the map - when the map has been initialized:
		mapPresenter.getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());

		return resizeLayoutPanel;
	}

	/**
	 * Initialization handler that deletes the default zoom control and adds 2 new controls.
	 * 
	 * @author Pieter De Graef
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		@Override
		public void onMapInitialized(MapInitializationEvent event) {
			// Search for the ZoomControl widget and remove it:
			Iterator<Widget> iterator = mapPresenter.getWidgetPane().iterator();
			while (iterator.hasNext()) {
				Widget widget = iterator.next();
				if (widget instanceof ZoomControl) {
					mapPresenter.getWidgetPane().remove(widget);
				}
			}

			// Now add the alternative controls:
			mapPresenter.getWidgetPane().add(new PanControl(mapPresenter));
			mapPresenter.getWidgetPane().add(new ZoomStepControl(mapPresenter));
		}
	}
}