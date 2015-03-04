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

package org.geomajas.gwt2.plugin.tms.example.client.sample;

import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.animation.LinearTrajectory;
import org.geomajas.gwt2.client.animation.NavigationAnimationFactory;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.View;
import org.geomajas.gwt2.client.map.ZoomOption;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.plugin.tms.client.TmsClient;
import org.geomajas.gwt2.plugin.tms.client.TmsClient.Profile;
import org.geomajas.gwt2.plugin.tms.client.configuration.ListTileMapInfo;
import org.geomajas.gwt2.plugin.tms.client.configuration.TileMapInfo;
import org.geomajas.gwt2.plugin.tms.client.configuration.TileMapServiceInfo;
import org.geomajas.gwt2.plugin.tms.client.layer.TmsLayer;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.UrlBuilder;
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

/**
 * ContentPanel that demonstrates the 3 TMS profiles.
 *
 * @author Jan De Moerloose
 */
public class ProfilesPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 *
	 * @author Jan De Moerloose
	 */
	interface MyUiBinder extends UiBinder<Widget, ProfilesPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	@UiField
	protected ListBox profileBox;

	@UiField
	protected VerticalPanel layerList;

	@UiField
	protected ParagraphElement capaUrl;

	public Widget asWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);
		profileBox.addItem("global-geodetic", Profile.GLOBAL_GEODETIC.name());
		profileBox.addItem("global-mercator", Profile.GLOBAL_MERCATOR.name());
		profileBox.setSelectedIndex(0);
		onProfile(null);
		capaUrl.setInnerHTML(getCapaUrl());
		return layout;
	}

	@UiHandler("profileBox")
	protected void onProfile(ChangeEvent event) {
		String name = profileBox.getValue(profileBox.getSelectedIndex());
		if (name != null) {
			Profile profile = Profile.valueOf(name);			
			MapConfiguration configuration = null;
			if (Profile.GLOBAL_GEODETIC.equals(profile)) {
				configuration = TmsClient.getInstance().createGeodeticMap();
			} else {
				configuration = TmsClient.getInstance().createMercatorMap();
			}
			mapPanel.clear();
			mapPresenter = GeomajasImpl.getInstance().createMapPresenter(configuration, 480, 480);
			DecoratorPanel mapDecorator = new DecoratorPanel();
			mapDecorator.add(mapPresenter.asWidget());
			mapPanel.add(mapDecorator);
			getCapabilities();
		}
	}

	private void getCapabilities() {
		// First clear the panel and the map:
		mapPresenter.getLayersModel().clear();
		layerList.clear();

		TmsClient.getInstance().getTileMapService(/* USE_PROXY + */getCapaUrl(),
				new Callback<TileMapServiceInfo, String>() {

					@Override
					public void onSuccess(TileMapServiceInfo result) {
						boolean first = true;
						for (final ListTileMapInfo tileMap : result.getTileMaps()) {
							// Only add layers with correct profile:
							if (mapPresenter.getConfiguration().getHintValue(TmsClient.PROFILE).getProfile()
									.equalsIgnoreCase(tileMap.getProfile())) {
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
								if (first) {
									addLayer(tileMap);
									layerBox.setValue(true);
									first = false;
								}
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
		TmsClient.getInstance().getTileMap(tileMap.getHref(), new Callback<TileMapInfo, String>() {

			@Override
			public void onSuccess(TileMapInfo result) {
				TmsLayer layer = TmsClient.getInstance().createLayer(result);
				mapPresenter.getLayersModel().addLayer(layer);

				// Now animate-zoom to the layer bounding box:
				View beginView = mapPresenter.getViewPort().getView();
				View endView = mapPresenter.getViewPort().asView(result.getBoundingBox(), ZoomOption.FREE);
				mapPresenter.getViewPort().registerAnimation(
						NavigationAnimationFactory.create(mapPresenter, new LinearTrajectory(beginView, endView), 500));
			}

			@Override
			public void onFailure(String reason) {
				Window.alert("We're very sorry, but something went wrong: " + reason);
			}
		});
	}

	private String getCapaUrl() {
		UrlBuilder builder = Window.Location.createUrlBuilder();
		String url = builder.buildString();

		int pos = url.indexOf("index.html");
		url = url.substring(0, pos) + "d/tms/1.0.0";
		return url;
	}
}
