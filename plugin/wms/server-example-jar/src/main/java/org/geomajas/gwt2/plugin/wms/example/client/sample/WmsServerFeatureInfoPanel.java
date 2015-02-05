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

package org.geomajas.gwt2.plugin.wms.example.client.sample;

import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.gfx.VectorContainer;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapConfiguration.CrsType;
import org.geomajas.gwt2.client.map.MapConfigurationImpl;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.client.widget.MapLayoutPanel;
import org.geomajas.gwt2.example.base.client.ExampleBase;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.plugin.wms.client.WmsServerExtension;
import org.geomajas.gwt2.plugin.wms.client.controller.WmsGetFeatureInfoController;
import org.geomajas.gwt2.plugin.wms.client.layer.FeatureInfoSupported;
import org.geomajas.gwt2.plugin.wms.client.layer.FeatureInfoSupportedWmsServerLayer;
import org.geomajas.gwt2.plugin.wms.client.layer.WmsLayerConfiguration;
import org.geomajas.gwt2.plugin.wms.client.service.WmsService.GetFeatureInfoFormat;
import org.geomajas.gwt2.plugin.wms.client.service.WmsService.WmsVersion;
import org.vaadin.gwtgraphics.client.VectorObject;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates rendering abilities in world space with a map that supports resizing.
 *
 * @author Pieter De Graef
 */
public class WmsServerFeatureInfoPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 *
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, WmsServerFeatureInfoPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private static final String WMS_BASE_URL = "http://apps.geomajas.org/geoserver/demo_world/ows";

	private static final String EPSG = "EPSG:4326";

	private MapPresenter mapPresenter;

	private WmsGetFeatureInfoController controller;

	private VectorContainer featureContainer;

	@UiField
	protected ListBox wmsVersionBox;

	@UiField
	protected ListBox formatBox;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	@UiField
	protected SimplePanel featureInfoParent;

	public Widget asWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Create the mapPresenter and add an InitializationHandler:
		MapConfiguration configuration = new MapConfigurationImpl();
		configuration.setCrs(EPSG, CrsType.DEGREES);
		configuration.setMaxBounds(new Bbox(-180, -90, 360, 180));
		configuration.setHintValue(MapConfiguration.INITIAL_BOUNDS, ExampleBase.BBOX_LATLON_USA);
		configuration.setMinimumResolution(2.1457672119140625E-5);
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter(configuration, 480, 480);
		featureContainer = mapPresenter.getContainerManager().addWorldContainer();

		controller = new WmsGetFeatureInfoController();
		controller.setFormat(getRequestFormat().toString());
		controller.setHtmlCallback(new Callback<String, String>() {

			@Override
			public void onSuccess(String url) {
				RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
				try {
					builder.sendRequest(null, new RequestCallback() {

						@Override
						public void onResponseReceived(Request request, Response response) {
							featureContainer.clear();
							HTML html = new HTML(response.getText());
							featureInfoParent.setWidget(html);
						}

						@Override
						public void onError(Request request, Throwable exception) {
							Window.alert("Something went wrong executing the WMS GetFeatureInfo request: "
									+ exception.getMessage());
						}
					});
				} catch (RequestException e) {
					Window.alert("Something went wrong executing the WMS GetFeatureInfo request: " + e.getMessage());
				}
			}

			@Override
			public void onFailure(String reason) {
				Window.alert("Something went wrong executing the WMS GetFeatureInfo request: " + reason);
			}
		});
		controller.setFeatureCallback(new Callback<List<Feature>, String>() {

			@Override
			public void onFailure(String reason) {
				Window.alert("Something went wrong executing the WMS GetFeatureInfo request: " + reason);
			}

			@Override
			public void onSuccess(List<Feature> result) {
				featureContainer.clear();
				for (Feature feature : result) {
					VectorObject shape = GeomajasImpl.getInstance().getGfxUtil().toShape(feature.getGeometry());
					if (shape != null) {
						GeomajasImpl.getInstance().getGfxUtil().applyFill(shape, "#CC0000", 0.7);
						featureContainer.add(shape);
					}
				}
				HTML html = new HTML("The features are drawn onto the map...");
				featureInfoParent.setWidget(html);
			}
		});
		mapPresenter.addMapListener(controller);
		formatBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				controller.setFormat(getRequestFormat().toString());
			}
		});

		// Define the whole layout:
		MapLayoutPanel mapLayoutPanel = new MapLayoutPanel();
		mapLayoutPanel.setPresenter(mapPresenter);
		mapPanel.setWidget(mapLayoutPanel);

		initialize();

		return layout;
	}

	@UiHandler("goBtn")
	protected void onReloadClicked(ClickEvent event) {
		initialize();
	}

	private void initialize() {
		// Cleanup:
		if (mapPresenter.getLayersModel().getLayerCount() > 0) {
			controller.removeLayer((FeatureInfoSupported) mapPresenter.getLayersModel().getLayer(0));
		}
		mapPresenter.getLayersModel().clear();
		featureContainer.clear();
		featureInfoParent.clear();

		// Now create a WMS layer and add it to the map:
		TileConfiguration tileConfig = new TileConfiguration(256, 256, new Coordinate(-180, -90),
				mapPresenter.getViewPort());
		WmsLayerConfiguration layerConfig = new WmsLayerConfiguration();
		layerConfig.setBaseUrl(WMS_BASE_URL);
		layerConfig.setFormat("image/png");
		layerConfig.setVersion(getWmsVersion());
		layerConfig.setLayers("demo_world:simplified_country_borders");
		layerConfig.setMaximumResolution(Double.MAX_VALUE);
		layerConfig.setMinimumResolution(2.1457672119140625E-5);
		FeatureInfoSupportedWmsServerLayer wmsLayer = WmsServerExtension.getInstance().createLayer("Countries",
				mapPresenter.getViewPort().getCrs(), tileConfig, layerConfig, null);
		wmsLayer.setMaxBounds(new Bbox(-180, -90, 360, 360));
		mapPresenter.getLayersModel().addLayer(wmsLayer);
		controller.addLayer(wmsLayer);
	}

	private WmsVersion getWmsVersion() {
		if (wmsVersionBox.getSelectedIndex() == 0) {
			return WmsVersion.V1_1_1;
		} else if (wmsVersionBox.getSelectedIndex() == 1) {
			return WmsVersion.V1_3_0;
		}
		return WmsVersion.V1_3_0;
	}

	private GetFeatureInfoFormat getRequestFormat() {
		if (formatBox.getSelectedIndex() == 0) {
			return GetFeatureInfoFormat.HTML;
		} else if (formatBox.getSelectedIndex() == 1) {
			return GetFeatureInfoFormat.TEXT;
		} else if (formatBox.getSelectedIndex() == 2) {
			return GetFeatureInfoFormat.JSON;
		} else if (formatBox.getSelectedIndex() == 3) {
			return GetFeatureInfoFormat.GML2;
		} else if (formatBox.getSelectedIndex() == 4) {
			return GetFeatureInfoFormat.GML3;
		}
		return GetFeatureInfoFormat.HTML;
	}
}
