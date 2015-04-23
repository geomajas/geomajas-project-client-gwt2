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

package org.geomajas.gwt2.plugin.tilebasedlayer.example.client.sample;

import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.plugin.tilebasedlayer.client.TileBasedLayerClient;
import org.geomajas.gwt2.plugin.tilebasedlayer.client.layer.OsmLayer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

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

	@UiField
	protected ResizeLayoutPanel mapPanel;

	public Widget asWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Create the mapPresenter
		MapConfiguration configuration = TileBasedLayerClient.getInstance().createOsmMap(19);

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
		String[] urls = new String[] { 
				"http://a.tile.openstreetmap.org/{z}/{x}/{y}.png",
				"http://b.tile.openstreetmap.org/{z}/{x}/{y}.png",
				"http://c.tile.openstreetmap.org/{z}/{x}/{y}.png" };
		// Create a new layer with the configurations and add it to the maps:
		OsmLayer osmLayer = TileBasedLayerClient.getInstance().createOsmLayer("osmCountries", 19, urls);
		mapPresenter.getLayersModel().addLayer(osmLayer);
	}

}
