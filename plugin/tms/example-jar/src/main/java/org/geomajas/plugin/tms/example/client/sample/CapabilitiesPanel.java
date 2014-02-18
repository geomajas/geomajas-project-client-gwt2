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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.plugin.tms.client.TmsClient;
import org.geomajas.plugin.tms.client.configuration.ListTileMapInfo;
import org.geomajas.plugin.tms.client.configuration.TileMapInfo;
import org.geomajas.plugin.tms.client.configuration.TileMapServiceInfo;
import org.geomajas.plugin.tms.client.layer.TmsLayer;

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

	private static final String USE_PROXY = "d/proxy?url=";

	private static final String TMS_BASE_URL = "http://apps.geomajas.org/geoserver/gwc/service/tms/1.0.0";

	private MapPresenter mapPresenter;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	@UiField
	protected VerticalPanel layerList;

	public Widget asWidget() {
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

		TmsClient.getInstance().getTileMapService(USE_PROXY + TMS_BASE_URL, new Callback<TileMapServiceInfo,
				String>() {

			@Override
			public void onSuccess(TileMapServiceInfo result) {
				for (final ListTileMapInfo tileMap : result.getTileMaps()) {

					// Only add layers using LatLon:
					if ("EPSG:4326".equalsIgnoreCase(tileMap.getSrs())) {
						CheckBox layerBox = new CheckBox(tileMap.getTitle());
						layerBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

							@Override
							public void onValueChange(ValueChangeEvent<Boolean> event) {
								if (event.getValue()) {
									addLayer(tileMap);
								} else {
									mapPresenter.getLayersModel().removeLayer(tileMap.getTitle());
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

	private void addLayer(ListTileMapInfo tileMap) {
		TmsClient.getInstance().getTileMap(USE_PROXY + tileMap.getHref(), new Callback<TileMapInfo, String>() {

			@Override
			public void onSuccess(TileMapInfo result) {
				TmsLayer layer = TmsClient.getInstance().createLayer(result);
				mapPresenter.getLayersModel().addLayer(layer);
				//mapPresenter.getLayersModelRenderer().setAnimated(layer, true);
			}

			@Override
			public void onFailure(String reason) {
				Window.alert("We're very sorry, but something went wrong: " + reason);
			}
		});
	}
}
