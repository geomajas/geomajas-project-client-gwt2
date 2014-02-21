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

package org.geomajas.plugin.tms.example.client.sample;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapConfiguration.CrsType;
import org.geomajas.gwt2.client.map.MapConfigurationImpl;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.plugin.tms.client.TmsClient;
import org.geomajas.plugin.tms.client.configuration.TileMapInfo;
import org.geomajas.plugin.tms.client.layer.TmsLayer;

import java.util.ArrayList;
import java.util.List;

/**
 * ContentPanel that demonstrates rendering abilities in world space with a map that supports resizing.
 *
 * @author Pieter De Graef
 */
public class TmsLayerPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 *
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, TmsLayerPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private static final String TMS_BASE_URL = "http://apps.geomajas.org/geoserver/gwc/service/tms/1.0.0/" +
			"demo_world%3Asimplified_country_borders@EPSG%3A4326@png";

	private static final String EPSG = "EPSG:4326";

	private MapPresenter mapPresenter;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	public Widget asWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Create the mapPresenter and add an InitializationHandler:
		MapConfiguration configuration = new MapConfigurationImpl();
		configuration.setCrs(EPSG, CrsType.DEGREES);
		configuration.setMaxBounds(new Bbox(-180, -90, 360, 180));
		configuration.setMaximumScale(8192);
		List<Double> resolutions = new ArrayList<Double>();
//		resolutions.add(0.703125);
//		resolutions.add(0.3515625);
//		resolutions.add(0.17578125);
//		resolutions.add(0.087890625);
//		resolutions.add(0.0439453125);
		resolutions.add(0.703125 * 0.75);
		resolutions.add(0.3515625 * 0.75);
		resolutions.add(0.17578125 * 0.75);
		resolutions.add(0.087890625 * 0.75);
		resolutions.add(0.0439453125 * 0.75);
		configuration.setResolutions(resolutions);
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter(configuration, 480, 480);

		mapPresenter.getConfiguration().setHintValue(MapConfiguration.ANIMATION_TIME, 4000);

		// Define the whole layout:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		initialize();

		return layout;
	}

	private void initialize() {
		TmsClient.getInstance().getTileMap("d/proxy?url=http://apps.geomajas.org/geoserver/gwc/service/tms/1.0.0" +
				"/demo_world%3Asimplified_country_borders@EPSG%3A4326@png", new Callback<TileMapInfo, String>() {

			@Override
			public void onSuccess(TileMapInfo result) {
				TmsLayer layer = TmsClient.getInstance().createLayer(result);
				mapPresenter.getLayersModel().addLayer(layer);
				mapPresenter.getLayersModelRenderer().setAnimated(layer, true);
			}

			@Override
			public void onFailure(String reason) {
				Window.alert("We're very sorry, but something went wrong: " + reason);
			}
		});

//		// First clear the panel and the map:
//		mapPresenter.getLayersModel().clear();
//
//		// Create the configuration objects:
//		TileConfiguration tileConfig = new TileConfiguration(256, 256, new Coordinate(-180, -90));
//		TmsLayerConfiguration layerConfig = new TmsLayerConfiguration();
//		layerConfig.setBaseUrl(TMS_BASE_URL);
//		layerConfig.setFileExtension(".png");
//
//		// Now create the layer and add it to the map:
//		final TmsLayer tmsLayer = TmsClient.getInstance().createLayer("Countries", tileConfig, layerConfig);
//		mapPresenter.getLayersModel().addLayer(tmsLayer);
	}
}