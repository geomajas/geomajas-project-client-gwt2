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

package org.geomajas.gwt2.plugin.tilebasedlayer.example.client.sample;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapConfiguration.CrsType;
import org.geomajas.gwt2.client.map.MapConfigurationImpl;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.plugin.tilebasedlayer.client.TileBasedLayerClient;
import org.geomajas.gwt2.plugin.tilebasedlayer.client.layer.OsmLayer;

import java.util.ArrayList;
import java.util.List;

/**
 * ContentPanel that demonstrates rendering abilities in world space with a map with an OSM layer.
 *
 * @author Youri Flement
 */
public class ClientSideOsmLayerPanel implements SamplePanel {

	/**
	 * UiBinder for this class.
	 *
	 * @author Youri Flement
	 */
	interface MyUiBinder extends UiBinder<Widget, ClientSideOsmLayerPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	private List<Double> resolutions;

	private static final int TILE_DIMENSION = 256;

	private static final int MAX_ZOOM_LEVELS = 19;

	private static final String EPSG = "EPSG:900913";

	private static final double EQUATOR_IN_METERS = 40075016.686;

	private static final double HALF_EQUATOR_IN_METERS = 40075016.686 / 2;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	public Widget asWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);
		initializeResolutions();

		// Create the mapPresenter
		MapConfiguration configuration = new MapConfigurationImpl();
		Bbox bounds = new Bbox(-HALF_EQUATOR_IN_METERS, -HALF_EQUATOR_IN_METERS, EQUATOR_IN_METERS, EQUATOR_IN_METERS);
		configuration.setCrs(EPSG, CrsType.METRIC);
		configuration.setHintValue(MapConfiguration.INITIAL_BOUNDS, bounds);
		configuration.setMaxBounds(bounds);
		configuration.setResolutions(resolutions);

		mapPresenter = GeomajasImpl.getInstance().createMapPresenter(configuration, 480, 480);

		// Define the whole layout:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		// Initialize the OSM layer:
		initializeLayer();

		return layout;
	}

	/**
	 * Initialize the OSM layer by creating a new {@link org.geomajas.gwt2.client.map.layer.tile.TileBasedLayer}
	 * and a {@link org.geomajas.gwt2.client.map.layer.tile.TileConfiguration}.
	 * <p/>
	 * Values such as {@code {z}, {x}, {y}} are optional and will be used to substitute the tile level, X-ordinate and
	 * Y-ordinate.
	 * <p/>
	 * The tile based layer service can be given different URLs in which case Round-robin will be performed to
	 * determine the next URL to load tiles from.
	 */
	private void initializeLayer() {
		// Set the URL to the service and the file extension:
		String[] domains = new String[] { "a", "b", "c" };
		List<String> urls = new ArrayList<String>();
		for (String domain : domains) {
			urls.add("http://" + domain + ".tile.openstreetmap.org/{z}/{x}/{y}.png");
		}

		// Create the configuration for the tiles:
		Coordinate tileOrigin = new Coordinate(-HALF_EQUATOR_IN_METERS, -HALF_EQUATOR_IN_METERS);
		TileConfiguration tileConfig = new TileConfiguration(TILE_DIMENSION, TILE_DIMENSION, tileOrigin, resolutions);

		// Create a new layer with the configurations and add it to the maps:
		OsmLayer osmLayer = TileBasedLayerClient.getInstance().createOsmLayer("osmCountries", tileConfig, urls);
		mapPresenter.getLayersModel().addLayer(osmLayer);
	}

	private void initializeResolutions() {
		resolutions = new ArrayList<Double>();
		for (int i = 0; i < MAX_ZOOM_LEVELS; i++) {
			resolutions.add(EQUATOR_IN_METERS / (TILE_DIMENSION * Math.pow(2, i)));
		}
	}

}
