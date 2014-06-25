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

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.plugin.wms.client.WmsClient;
import org.geomajas.plugin.wms.client.capabilities.WmsGetCapabilitiesInfo;
import org.geomajas.plugin.wms.client.capabilities.WmsLayerInfo;
import org.geomajas.plugin.wms.client.layer.WmsLayer;
import org.geomajas.plugin.wms.client.layer.WmsLayerConfiguration;
import org.geomajas.plugin.wms.client.service.WmsService.WmsRequest;
import org.geomajas.plugin.wms.client.service.WmsService.WmsUrlTransformer;
import org.geomajas.plugin.wms.client.service.WmsService.WmsVersion;

/**
 * ContentPanel that demonstrates rendering abilities in world space with a map that supports resizing.
 *
 * @author Pieter De Graef
 */
public class CapabilitiesPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 *
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, CapabilitiesPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private static final String WMS_BASE_URL = "http://apps.geomajas.org/geoserver/demo_world/ows";

	private MapPresenter mapPresenter;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	@UiField
	protected VerticalPanel layerList;

	@UiField
	protected ListBox wmsVersionBox;

	public Widget asWidget() {
		WmsClient.getInstance().getWmsService().setWmsUrlTransformer(new WmsUrlTransformer() {

			@Override
			public String transform(WmsRequest request, String url) {
				switch (request) {
					case GETCAPABILITIES:
					case GETFEATUREINFO:
						return "d/proxy?url=" + url;
					default:
						return url;
				}
			}
		});
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Create the mapPresenter and add an InitializationHandler:
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter();
		mapPresenter.setSize(480, 480);

		// Define the whole layout:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		// Initialize the map, and return the layout:
		GeomajasServerExtension.getInstance().initializeMap(mapPresenter, "gwt-app", "mapEmpty");

		return layout;
	}

	@UiHandler("goBtn")
	protected void onGetCapabilitiesClicked(ClickEvent event) {
		getCapabilities();
	}

	private void getCapabilities() {
		// First clear the panel and the map:
		mapPresenter.getLayersModel().clear();
		layerList.clear();

		WmsClient.getInstance().getWmsService()
				.getCapabilities(WMS_BASE_URL, getWmsVersion(), new Callback<WmsGetCapabilitiesInfo, String>() {

					@Override
					public void onSuccess(WmsGetCapabilitiesInfo result) {
						if (result.getLayers() != null) {
							for (WmsLayerInfo layerInfo : result.getLayers()) {
								TileConfiguration tileConfig = WmsClient.getInstance().createTileConfig(layerInfo,
										mapPresenter.getViewPort(), 256, 256);
								WmsLayerConfiguration layerConfig = WmsClient.getInstance().createLayerConfig(
										layerInfo, WMS_BASE_URL, getWmsVersion());
								final WmsLayer layer = WmsClient.getInstance().createLayer(layerInfo.getTitle(),
										mapPresenter.getConfiguration(), tileConfig, layerConfig, layerInfo);

								CheckBox layerBox = new CheckBox(layer.getTitle());
								layerBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

									@Override
									public void onValueChange(ValueChangeEvent<Boolean> event) {
										if (event.getValue()) {
											mapPresenter.getLayersModel().addLayer(layer);
											mapPresenter.getLayersModelRenderer().setAnimated(layer, true);
										} else {
											mapPresenter.getLayersModel().removeLayer(layer.getId());
										}
									}
								});

								layerList.add(layerBox);
							}
						}
					}

					@Override
					public void onFailure(String reason) {
						Window.alert("We're very sorry, but something went wrong: " + reason);
					}
				});
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
