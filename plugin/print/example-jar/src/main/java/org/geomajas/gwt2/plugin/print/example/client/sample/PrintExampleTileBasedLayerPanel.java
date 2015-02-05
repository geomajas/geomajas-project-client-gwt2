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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.event.MapInitializationEvent;
import org.geomajas.gwt2.client.event.MapInitializationHandler;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.plugin.tilebasedlayer.client.TileBasedLayerClient;
import org.geomajas.gwt2.plugin.tilebasedlayer.client.layer.OsmLayer;

import com.google.gwt.user.client.ui.Widget;

/**
 * Extension of {@link PrintExamplePanel} for tile based layer.
 * @author Jan De Moerloose
 */
public class PrintExampleTileBasedLayerPanel extends PrintExamplePanel {

	private static final int TILE_DIMENSION = 256;

	private static final int MAX_ZOOM_LEVELS = 19;

	private static final double EQUATOR_IN_METERS = 40075016.686;

	private static final double HALF_EQUATOR_IN_METERS = 40075016.686 / 2;

	@Override
	public Widget asWidget() {
		Widget widget = super.asWidget();
		getMapPresenter().getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());
		return widget;
	}
	
	/**
	 * Map initialization handler that adds osm layer
	 * 
	 * @author Jan De Moerloose
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		public void onMapInitialized(MapInitializationEvent event) {
			// Set the URL to the service and the file extension:
			String[] domains = new String[] { "a", "b", "c" };
			List<String> urls = new ArrayList<String>();
			for (String domain : domains) {
				urls.add("http://" + domain + ".tile.opencyclemap.org/cycle/{z}/{x}/{y}.png");
			}

			// Create the configuration for the tiles:
			Coordinate tileOrigin = new Coordinate(-HALF_EQUATOR_IN_METERS, -HALF_EQUATOR_IN_METERS);
			List<Double> resolutions = new ArrayList<Double>();
			for (int i = 0; i < MAX_ZOOM_LEVELS; i++) {
				resolutions.add(EQUATOR_IN_METERS / (TILE_DIMENSION * Math.pow(2, i)));
			}
			TileConfiguration tileConfig = new TileConfiguration(TILE_DIMENSION, TILE_DIMENSION, tileOrigin,
					resolutions);
			OsmLayer osmLayer = TileBasedLayerClient.getInstance().createOsmLayer("osmLayer", tileConfig, urls);
			getMapPresenter().getLayersModel().clear();
			getMapPresenter().getLayersModel().addLayer(osmLayer);
		}
	}

}