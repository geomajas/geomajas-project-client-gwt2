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

package org.geomajas.plugin.wms.example.client.sample.v1_1_1;

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
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.plugin.wms.client.WmsClient;
import org.geomajas.plugin.wms.client.layer.WmsLayer;
import org.geomajas.plugin.wms.client.layer.config.WmsLayerConfiguration;
import org.geomajas.plugin.wms.client.layer.config.WmsTileConfiguration;
import org.geomajas.plugin.wms.client.service.WmsService.WmsVersion;

/**
 * ContentPanel that demonstrates rendering abilities in world space with a map that supports resizing.
 *
 * @author Pieter De Graef
 */
public class WmsLayerV111Panel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 *
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, WmsLayerV111Panel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private static final String WMS_BASE_URL = "http://apps.geomajas.org/geoserver/demo_world/ows";

	private static final String EPSG = "EPSG:4326";

	@UiField
	protected ResizeLayoutPanel mapPanel;

	public Widget asWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Create the mapPresenter and add an InitializationHandler:
		MapConfiguration configuration = new MapConfigurationImpl();
		configuration.setCrs(EPSG, CrsType.DEGREES);
		configuration.setMaxBounds(new Bbox(-180, -90, 360, 180));
		configuration.setMaximumScale(8192);
		MapPresenter mapPresenter = GeomajasImpl.getInstance().createMapPresenter(configuration, 480, 480);

		// Now create a WMS layer and add it to the map:
		WmsTileConfiguration tileConfig = new WmsTileConfiguration(256, 256, new Coordinate(-180, -90));
		WmsLayerConfiguration layerConfig = new WmsLayerConfiguration();
		layerConfig.setBaseUrl(WMS_BASE_URL);
		layerConfig.setFormat("image/jpeg");
		layerConfig.setVersion(WmsVersion.V1_1_1);
		layerConfig.setLayers("bluemarble");
		layerConfig.setMaximumScale(8192);
		layerConfig.setMinimumScale(0);

		final WmsLayer wmsLayer = WmsClient.getInstance().createLayer("Blue Marble", tileConfig, layerConfig, null);
		mapPresenter.getLayersModel().addLayer(wmsLayer);

		// Define the whole layout:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		return layout;
	}
}