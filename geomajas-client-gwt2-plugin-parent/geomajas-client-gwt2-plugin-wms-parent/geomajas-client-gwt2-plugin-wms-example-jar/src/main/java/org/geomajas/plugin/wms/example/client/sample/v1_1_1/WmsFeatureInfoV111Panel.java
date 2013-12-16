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

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.map.MapOptions;
import org.geomajas.gwt2.client.map.MapOptions.CrsType;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.widget.MapLayoutPanel;
import org.geomajas.gwt2.example.base.client.ExampleBase;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.plugin.wms.client.controller.WmsGetFeatureInfoController;
import org.geomajas.plugin.wms.client.layer.FeaturesSupportedWmsLayer;
import org.geomajas.plugin.wms.client.layer.FeaturesSupportedWmsLayerImpl;
import org.geomajas.plugin.wms.client.layer.config.WmsLayerConfiguration;
import org.geomajas.plugin.wms.client.layer.config.WmsTileConfiguration;
import org.geomajas.plugin.wms.client.service.WmsService.GetFeatureInfoFormat;
import org.geomajas.plugin.wms.client.service.WmsService.WmsVersion;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates rendering abilities in world space with a map that supports resizing.
 * 
 * @author Pieter De Graef
 */
public class WmsFeatureInfoV111Panel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, WmsFeatureInfoV111Panel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private static final String WMS_BASE_URL = "http://apps.geomajas.org/geoserver/demo_world/ows";

	private static final String EPSG = "EPSG:4326";

	@UiField
	protected ResizeLayoutPanel mapPanel;

	@UiField
	protected SimplePanel featureInfoParent;

	public Widget asWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Create the mapPresenter and add an InitializationHandler:
		MapOptions mapOptions = new MapOptions();
		mapOptions.setCrs(EPSG, CrsType.DEGREES);
		mapOptions.setMaxBounds(new Bbox(-180, -90, 360, 180));
		mapOptions.setInitialBounds(ExampleBase.BBOX_LATLON_USA);
		mapOptions.setMaximumScale(8192);
		//mapOptions.setUnitLength(111319.4907932264);
		MapPresenter mapPresenter = GeomajasImpl.getInstance().createMapPresenter(mapOptions, 480, 480);

		// Now create a WMS layer and add it to the map:
		WmsTileConfiguration tileConfig = new WmsTileConfiguration(256, 256, new Coordinate(-180, -90));
		WmsLayerConfiguration layerConfig = new WmsLayerConfiguration();
		layerConfig.setBaseUrl(WMS_BASE_URL);
		layerConfig.setFormat("image/jpeg");
		layerConfig.setVersion(WmsVersion.V1_1_1);
		layerConfig.setLayers("simplified_country_borders");
		layerConfig.setMaximumScale(8192);
		layerConfig.setMinimumScale(0);
		FeaturesSupportedWmsLayer wmsLayer = new FeaturesSupportedWmsLayerImpl("Countries", layerConfig, tileConfig);
		mapPresenter.getLayersModel().addLayer(wmsLayer);

		// Define the whole layout:
		MapLayoutPanel mapLayoutPanel = new MapLayoutPanel();
		mapLayoutPanel.setPresenter(mapPresenter);
		mapPanel.setWidget(mapLayoutPanel);

		WmsGetFeatureInfoController controller = new WmsGetFeatureInfoController();
		controller.addLayer(wmsLayer);
		controller.setFormat(GetFeatureInfoFormat.HTML);
		controller.setHtmlCallback(new Callback<Object, String>() {

			@Override
			public void onSuccess(Object result) {
				HTML html = new HTML((String) result);
				featureInfoParent.setWidget(html);
			}

			@Override
			public void onFailure(String reason) {
				Window.alert("Something went wrong executing the WMS GetFeatureInfo request: " + reason);
			}
		});
		mapPresenter.addMapListener(controller);

		return layout;
	}
}