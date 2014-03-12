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

package org.geomajas.plugin.wms.example.client.sample;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ListBox;
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
import org.geomajas.plugin.wms.client.WmsClient;
import org.geomajas.plugin.wms.client.layer.WmsLayer;
import org.geomajas.plugin.wms.client.layer.WmsLayerConfiguration;
import org.geomajas.plugin.wms.client.service.WmsService.WmsVersion;

/**
 * ContentPanel that demonstrates rendering abilities in world space with a map that supports resizing.
 *
 * @author Pieter De Graef
 */
public class WmsLayerPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 *
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, WmsLayerPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private static final String WMS_BASE_URL = "http://apps.geomajas.org/geoserver/demo_world/ows";

	private static final String EPSG = "EPSG:4326";

	private MapPresenter mapPresenter;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	@UiField
	protected ListBox wmsVersionBox;

	public Widget asWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Create the mapPresenter and add an InitializationHandler:
		MapConfiguration configuration = new MapConfigurationImpl();
		configuration.setCrs(EPSG, CrsType.DEGREES);
		configuration.setMaxBounds(new Bbox(-180, -90, 360, 180));
		configuration.setMinimumResolution(2.1457672119140625E-5);
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter(configuration, 480, 480);

		// Define the whole layout:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		initialize();

		return layout;
	}

	@UiHandler("goBtn")
	protected void onGetCapabilitiesClicked(ClickEvent event) {
		initialize();
	}

	private void initialize() {
		// First clear the panel and the map:
		mapPresenter.getLayersModel().clear();

		// Now create a WMS layer and add it to the map:
		TileConfiguration tileConfig = new TileConfiguration(256, 256, new Coordinate(-180, -90));
		WmsLayerConfiguration layerConfig = new WmsLayerConfiguration();
		layerConfig.setBaseUrl(WMS_BASE_URL);
		layerConfig.setFormat("image/png");
		layerConfig.setVersion(getWmsVersion());
		layerConfig.setLayers("demo_world:simplified_country_borders");
		layerConfig.setMaximumResolution(Double.MAX_VALUE);
		layerConfig.setMinimumResolution(2.1457672119140625E-5);

		final WmsLayer wmsLayer = WmsClient.getInstance().createLayer("Blue Marble", tileConfig, layerConfig, null);
		mapPresenter.getLayersModel().addLayer(wmsLayer);

	}

	private WmsVersion getWmsVersion() {
		if (wmsVersionBox.getSelectedIndex() == 0) {
			return WmsVersion.V1_1_1;
		} else if (wmsVersionBox.getSelectedIndex() == 1) {
			return WmsVersion.V1_3_0;
		}
		return WmsVersion.V1_3_0;
	}
}