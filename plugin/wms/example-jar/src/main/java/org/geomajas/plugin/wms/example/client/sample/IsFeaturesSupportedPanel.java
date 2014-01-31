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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.client.widget.MapLayoutPanel;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.plugin.wms.client.WmsClient;
import org.geomajas.plugin.wms.client.WmsServerExtension;
import org.geomajas.plugin.wms.client.capabilities.WmsGetCapabilitiesInfo;
import org.geomajas.plugin.wms.client.capabilities.WmsLayerInfo;
import org.geomajas.plugin.wms.client.layer.FeaturesSupportedWmsLayer;
import org.geomajas.plugin.wms.client.layer.WmsLayerConfiguration;
import org.geomajas.plugin.wms.client.layer.WmsTileConfiguration;
import org.geomajas.plugin.wms.client.service.WmsService.WmsRequest;
import org.geomajas.plugin.wms.client.service.WmsService.WmsUrlTransformer;
import org.geomajas.plugin.wms.client.service.WmsService.WmsVersion;

import java.util.List;

/**
 * ContentPanel that demonstrates rendering abilities in world space with a map that supports resizing.
 *
 * @author Pieter De Graef
 */
public class IsFeaturesSupportedPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 *
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, IsFeaturesSupportedPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private static final String WMS_BASE_URL = "http://apps.geomajas.org/geoserver/demo_world/ows";

	private MapPresenter mapPresenter;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	@UiField
	protected VerticalPanel layerList;

	@UiField
	protected VerticalPanel attributePanel;

	@UiField
	protected ListBox wmsVersionBox;

	public Widget asWidget() {
		// We let the GetCapabilities and GetFeatureInfo run through a Proxy Servlet to avoid cross domain security:
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

		// Build the GUI for this panel:
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Create the mapPresenter and add an InitializationHandler:
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter();
		MapLayoutPanel mapLayoutPanel = new MapLayoutPanel();
		mapLayoutPanel.setPresenter(mapPresenter);
		mapPanel.setWidget(mapLayoutPanel);

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
		attributePanel.clear();

		WmsClient.getInstance().getWmsService()
				.getCapabilities(WMS_BASE_URL, getWmsVersion(), new Callback<WmsGetCapabilitiesInfo, String>() {

					@Override
					public void onSuccess(WmsGetCapabilitiesInfo result) {
						if (result.getLayers() != null) {
							for (WmsLayerInfo layerInfo : result.getLayers()) {
								buildLayerPanel(layerInfo);
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

	private void buildLayerPanel(final WmsLayerInfo layerInfo) {
		final RadioButton radioButton = new RadioButton("layer", layerInfo.getTitle());
		layerList.add(radioButton);

		// For every layer we check if it supports features or not:
		WmsServerExtension.getInstance().supportsFeatures(WMS_BASE_URL, layerInfo.getName(),
				new Callback<Boolean, String>() {

					@Override
					public void onFailure(String s) {
						// Failure? We do not accept failure!
					}

					@Override
					public void onSuccess(final Boolean thisLayerSupportsFeatures) {
						radioButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
							@Override
							public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
								installLayer(layerInfo, thisLayerSupportsFeatures);
							}
						});
					}
				});
	}

	private void installLayer(WmsLayerInfo layerInfo, boolean featuresSupported) {
		// First remove all layers from the map:
		mapPresenter.getLayersModel().clear();

		// Also clear the possible list of shown attribute descriptors:
		attributePanel.clear();

		// Prepare the layer configuration:
		final WmsTileConfiguration tileConfig = WmsClient.getInstance().createTileConfig(layerInfo,
				mapPresenter.getViewPort().getCrs(), 256, 256);
		final WmsLayerConfiguration layerConfig = WmsClient.getInstance().createLayerConfig(
				mapPresenter.getViewPort(), layerInfo, WMS_BASE_URL, getWmsVersion());

		// Then add the new WMS layer to the map:
		if (featuresSupported) {
			final FeaturesSupportedWmsLayer layer = WmsServerExtension.getInstance().createLayer(layerInfo.getTitle(),
					tileConfig, layerConfig, layerInfo, new Callback<List<AttributeDescriptor>, String>() {

				@Override
				public void onFailure(String s) {
				}

				@Override
				public void onSuccess(List<AttributeDescriptor> descriptors) {
					// When this layer is initialized, we can write out it's attribute descriptors:
					for (AttributeDescriptor descriptor : descriptors) {
						attributePanel.add(new HTML("Attribute: <b>" + descriptor.getName() + "</b> (" + descriptor
								.getType().getName() + ")"));
					}
				}
			});
			mapPresenter.getLayersModel().addLayer(layer);
		} else {
			mapPresenter.getLayersModel().addLayer(WmsClient.getInstance().createLayer(layerInfo.getTitle(),
					tileConfig, layerConfig, layerInfo));
			attributePanel.add(new HTML("This layer does not support features..."));
		}
	}
}