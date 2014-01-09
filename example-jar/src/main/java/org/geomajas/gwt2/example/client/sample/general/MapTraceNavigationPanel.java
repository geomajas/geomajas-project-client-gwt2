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

import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.animation.NavigationAnimationFactory;
import org.geomajas.gwt2.client.event.MapInitializationEvent;
import org.geomajas.gwt2.client.event.MapInitializationHandler;
import org.geomajas.gwt2.client.gfx.CanvasContainer;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.View;
import org.geomajas.gwt2.client.map.ZoomOption;
import org.geomajas.gwt2.client.widget.MapLayoutPanel;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;

import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates the way the map navigates by leaving bounds traces on a canvas.
 * 
 * @author Jan De Moerloose
 */
public class MapTraceNavigationPanel implements SamplePanel {

	private MapPresenter mapPresenter;

	public Widget asWidget() {
		// Define the layout:
		ResizeLayoutPanel resizeLayoutPanel = new ResizeLayoutPanel();
		final MapLayoutPanel layout = new MapLayoutPanel();
		resizeLayoutPanel.setWidget(layout);
		resizeLayoutPanel.setSize("100%", "100%");

		// Create the MapPresenter and add to the layout:
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter();
		mapPresenter.getConfiguration().setMapHintValue(MapConfiguration.ANIMATION_TIME, 1000);
		mapPresenter.getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());
		layout.setPresenter(mapPresenter);

		// Initialize the map, and return the layout:
		GeomajasServerExtension.initializeMap(mapPresenter, "gwt-app", "mapOsm");
		return resizeLayoutPanel;
	}

	/**
	 * Map initialization handler that adds the tracing layer.
	 * 
	 * @author Jan De Moerloose
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		public void onMapInitialized(MapInitializationEvent event) {
			CanvasContainer container = mapPresenter.getContainerManager().addWorldCanvasContainer();
			final TracingLayer layer = new TracingLayer(mapPresenter.getViewPort(), container);
			mapPresenter.getLayersModel().addLayer(layer);
			mapPresenter.getConfiguration().setAnimated(layer, true);
			Timer timer = new Timer() {

				private boolean zoomOut;

				@Override
				public void run() {
					View endView = mapPresenter.getViewPort().asView(getNextBounds(), ZoomOption.LEVEL_CLOSEST);
					mapPresenter.getViewPort().registerAnimation(
							NavigationAnimationFactory.createZoomIn(mapPresenter, endView));
				}

				private Bbox getNextBounds() {
					if (zoomOut) {
						layer.setEnabled(false);
						zoomOut = false;
						return new Bbox(-5000000, -5000000, 10000000, 10000000);
					} else {
						layer.setEnabled(true);
						double x = (Random.nextDouble() - 0.5) * 5000000;
						double y = (Random.nextDouble() - 0.5) * 5000000;
						double width = 5000;
						double height = 5000;
						zoomOut = true;
						return new Bbox(x, y, width, height);
					}
				}

			};
			timer.scheduleRepeating(2000);
		}
	}
}