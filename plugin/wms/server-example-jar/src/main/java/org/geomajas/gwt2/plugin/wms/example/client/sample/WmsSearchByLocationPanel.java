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
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.controller.AbstractRectangleController;
import org.geomajas.gwt2.client.gfx.VectorContainer;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapConfiguration.CrsType;
import org.geomajas.gwt2.client.map.MapConfigurationImpl;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureTypeDescriptionInfo;
import org.geomajas.gwt2.plugin.wms.client.WmsServerExtension;
import org.geomajas.gwt2.plugin.wms.client.layer.FeatureSearchSupportedWmsServerLayer;
import org.geomajas.gwt2.plugin.wms.client.layer.WmsLayerConfiguration;
import org.geomajas.gwt2.plugin.wms.client.service.WmsService.WmsVersion;
import org.vaadin.gwtgraphics.client.VectorObject;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates rendering abilities in world space with a map that supports resizing.
 *
 * @author Pieter De Graef
 */
public class WmsSearchByLocationPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 *
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, WmsSearchByLocationPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private static final String WMS_BASE_URL = "http://apps.geomajas.org/geoserver/demo_world/ows";

	private static final String EPSG = "EPSG:4326";

	private MapPresenter mapPresenter;

	private VectorContainer selectionContainer;

	private FeatureSearchSupportedWmsServerLayer wmsLayer;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	@UiField
	protected ListBox wmsVersionBox;

	@UiField
	protected RadioButton navRadio;

	@UiField
	protected RadioButton selectRadio;

	public Widget asWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Create the mapPresenter and add an InitializationHandler:
		MapConfiguration configuration = new MapConfigurationImpl();
		configuration.setCrs(EPSG, CrsType.DEGREES);
		configuration.setMaxBounds(new Bbox(-180, -90, 360, 180));
		configuration.setMinimumResolution(2.1457672119140625E-5);
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter(configuration, 480, 480);
		selectionContainer = mapPresenter.getContainerManager().addWorldContainer();

		// Define the whole layout:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		// Add handlers to the radio buttons to make sure the correct MapController is active on the map:
		//navRadio.setValue(true);
		navRadio.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
				if (booleanValueChangeEvent.getValue()) {
					mapPresenter.setMapController(null); // Use the default (=navigation).
				}
			}
		});
		selectRadio.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
				if (booleanValueChangeEvent.getValue()) {
					mapPresenter.setMapController(new WmsSelectionController());
				}
			}
		});

		initializeLayer();

		return layout;
	}

	@UiHandler("goBtn")
	protected void onGetCapabilitiesClicked(ClickEvent event) {
		initializeLayer();
	}

	private void initializeLayer() {
		// First clear the panel and the map:
		mapPresenter.getLayersModel().clear();
		selectionContainer.clear();

		// Now create a WMS layer and add it to the map:
		final TileConfiguration tileConfig = new TileConfiguration(256, 256, new Coordinate(-180, -90),
				mapPresenter.getViewPort());
		final WmsLayerConfiguration layerConfig = new WmsLayerConfiguration();
		layerConfig.setBaseUrl(WMS_BASE_URL);
		layerConfig.setFormat("image/png");
		layerConfig.setVersion(getWmsVersion());
		layerConfig.setLayers("demo_world:simplified_country_borders");
		layerConfig.setMaximumResolution(Double.MAX_VALUE);
		layerConfig.setMinimumResolution(2.1457672119140625E-5);
		WmsServerExtension.getInstance().supportsFeatures(WMS_BASE_URL, getWmsVersion(), layerConfig.getLayers(),
				new Callback<WfsFeatureTypeDescriptionInfo, String>() {

					@Override
					public void onFailure(String s) {
						// ignore
					}

					@Override
					public void onSuccess(final WfsFeatureTypeDescriptionInfo wfsLayerConfiguration) {
						wmsLayer = WmsServerExtension.getInstance().createLayer("Countries",
								mapPresenter.getViewPort().getCrs(), tileConfig, layerConfig, null,
								wfsLayerConfiguration);
					}
				});

		wmsLayer.setMaxBounds(new Bbox(-180, -90, 360, 360));
		mapPresenter.getLayersModel().addLayer(wmsLayer);
		mapPresenter.getViewPort().applyBounds(mapPresenter.getViewPort().getMaximumBounds());
	}

	private WmsVersion getWmsVersion() {
		if (wmsVersionBox.getSelectedIndex() == 0) {
			return WmsVersion.V1_1_1;
		} else if (wmsVersionBox.getSelectedIndex() == 1) {
			return WmsVersion.V1_3_0;
		}
		return WmsVersion.V1_3_0;
	}

	/**
	 * @author Pieter De Graef
	 */
	private class WmsSelectionController extends AbstractRectangleController {

		@Override
		public void execute(Bbox worldBounds) {
			Geometry polygon = GeometryService.toPolygon(worldBounds);

			wmsLayer.searchFeatures(polygon, new Callback<List<Feature>, String>() {

				@Override
				public void onFailure(String s) {
				}

				@Override
				public void onSuccess(List<Feature> features) {
					// Now draw all features on the map:
					selectionContainer.clear();
					for (Feature feature : features) {
						VectorObject shape = GeomajasImpl.getInstance().getGfxUtil().toShape(feature.getGeometry());
						GeomajasImpl.getInstance().getGfxUtil().applyFill(shape, "#CC0000", 0.7);
						selectionContainer.add(shape);
					}
				}
			});
		}
	}
}
