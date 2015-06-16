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

package org.geomajas.gwt2.plugin.wfs.example.client.sample;

import org.geomajas.command.dto.TransformGeometryRequest;
import org.geomajas.command.dto.TransformGeometryResponse;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.gfx.VectorContainer;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.attribute.AttributeDescriptor;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.feature.query.Criterion;
import org.geomajas.gwt2.client.map.feature.query.CriterionBuilder;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.example.base.client.widget.ShowcaseDialogBox;
import org.geomajas.gwt2.plugin.corewidget.client.feature.featureinfo.FeatureInfoWidget;
import org.geomajas.gwt2.plugin.tilebasedlayer.client.TileBasedLayerClient;
import org.geomajas.gwt2.plugin.tilebasedlayer.client.layer.OsmLayer;
import org.geomajas.gwt2.plugin.wfs.client.WfsServerExtension;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureTypeDescriptionInfo;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsFeatureTypeInfo;
import org.geomajas.gwt2.plugin.wfs.client.protocol.WfsGetCapabilitiesInfo;
import org.geomajas.gwt2.plugin.wfs.client.service.WfsService.WfsVersion;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates WFS GetCapabilities, DescribeFeatureType and GetFeature functionality.
 *
 * @author Jan De Moerloose
 */
public class WfsCapabilitiesPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 *
	 * @author Jan De Moerloose
	 */
	interface MyUiBinder extends UiBinder<Widget, WfsCapabilitiesPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	@UiField
	protected VerticalPanel layerList;

	@UiField
	protected TextBox capsText;

	@UiField
	protected IntegerBox nrOfFeaturesText;

	@UiField
	protected IntegerBox nrOfCoordinatesText;

	@UiField
	protected ListBox wfsVersionBox;

	@UiField
	protected ListBox strategyBox;

	@UiField
	protected ListBox attributeBox;

	@UiField
	protected TextBox filterText;

	@UiField
	protected Label loading;

	private SimpleWfsLayer layer;

	private static final String OSM_EPSG = "EPSG:3857";

	private OsmLayer osmLayer;

	public Widget asWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Create the mapPresenter and add an InitializationHandler:
		MapConfiguration mapConfiguration = TileBasedLayerClient.getInstance().createOsmMap(25);
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter(mapConfiguration, 480, 480);
		mapPresenter.getEventBus().addHandler(ShowFeatureHandler.TYPE, new ShowFeatureHandler() {

			@Override
			public void onShowFeature(ShowFeatureEvent event) {
				showFeature(event.getFeature());
			}
		});
		osmLayer = TileBasedLayerClient.getInstance().createOsmLayer("osm", 25,
				"http://otile1.mqcdn.com/tiles/1.0.0/osm/{z}/{x}/{y}.png");
		mapPresenter.getLayersModel().addLayer(osmLayer);
		// Define the whole layout:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		return layout;
	}

	public void showFeature(Feature feature) {
		FeatureInfoWidget featureInfo = new FeatureInfoWidget();
		featureInfo.asWidget().getElement().getStyle().setWidth(250, Unit.PX);
		featureInfo.setFeature(feature);

		// Put it all in a dialog box:
		ShowcaseDialogBox dialogBox = new ShowcaseDialogBox();
		String title = "Feature detail: " + feature.getLabel();
		dialogBox.setWidget(featureInfo);
		dialogBox.setText(title);
		dialogBox.setTitle(title);
		dialogBox.setModal(false);
		dialogBox.show();

	}

	private void transformBoundsAndNavigate(Bbox wgs84BoundingBox) {
		TransformGeometryRequest transformGeometryRequest = new TransformGeometryRequest();
		transformGeometryRequest.setBounds(wgs84BoundingBox);
		transformGeometryRequest.setSourceCrs("EPSG:4326");
		transformGeometryRequest.setTargetCrs(OSM_EPSG);
		GwtCommand command = new GwtCommand(TransformGeometryRequest.COMMAND);
		command.setCommandRequest(transformGeometryRequest);
		GeomajasServerExtension.getInstance().getCommandService()
				.execute(command, new AbstractCommandCallback<TransformGeometryResponse>() {

					@Override
					public void execute(TransformGeometryResponse response) {
						mapPresenter.getViewPort().applyBounds(response.getBounds());

					}
				});

	}

	@UiHandler("goBtn")
	protected void onGetCapabilitiesClicked(ClickEvent event) {
		getCapabilities();
	}

	@UiHandler("filterBtn")
	protected void onFilter(ClickEvent event) {
		if (filterText.getValue() != null && !filterText.getValue().isEmpty()) {
			if (attributeBox.getSelectedValue() != null) {
				CriterionBuilder b = WfsServerExtension.getInstance().getWfsService().buildCriterion();
				Criterion c = b.attribute(attributeBox.getSelectedValue()).operation("=").value(filterText.getValue())
						.build();
				if (layer != null) {
					layer.getRenderer().setFilter(c);
				}
			}
		} else {
			if (layer != null) {
				layer.getRenderer().setFilter(null);
			}
		}
	}

	private void startLoading() {
		loading.setVisible(true);
		loading.setText("Loading...");
	}

	private void stopLoading() {
		loading.setVisible(true);
		loading.setText("Done");
	}

	private void getCapabilities() {
		// First clear the panel and the map:
		mapPresenter.getLayersModel().clear();
		mapPresenter.getLayersModel().addLayer(osmLayer);
		layerList.clear();
		startLoading();
		WfsServerExtension.getInstance().getWfsService()
				.getCapabilities(getWfsVersion(), getCapabilitiesurl(), new Callback<WfsGetCapabilitiesInfo, String>() {

					@Override
					public void onSuccess(WfsGetCapabilitiesInfo result) {
						stopLoading();
						if (result.getFeatureTypeList().getFeatureTypes() != null) {
							for (final WfsFeatureTypeInfo layerInfo : result.getFeatureTypeList().getFeatureTypes()) {
								CheckBox layerBox = new CheckBox(layerInfo.getTitle());
								layerBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

									@Override
									public void onValueChange(ValueChangeEvent<Boolean> event) {
										if (event.getValue()) {
											loadLayer(layerInfo);
										} else {
											mapPresenter.getLayersModel().removeLayer(layerInfo.getName());
										}
									}

								});

								layerList.add(layerBox);
							}
						}
					}

					@Override
					public void onFailure(String reason) {
						stopLoading();
						Window.alert("We're very sorry, but something went wrong: " + reason);
					}
				});
	}

	private void loadLayer(final WfsFeatureTypeInfo layerInfo) {
		startLoading();
		WfsServerExtension
				.getInstance()
				.getWfsService()
				.describeFeatureType(getWfsVersion(), getCapabilitiesurl(), layerInfo.getName(),
						new Callback<WfsFeatureTypeDescriptionInfo, String>() {

							@Override
							public void onSuccess(WfsFeatureTypeDescriptionInfo result) {
								attributeBox.clear();
								for (AttributeDescriptor desc : result.getAttributeDescriptors()) {
									if (desc.getType().getBinding().equals(String.class)) {
										attributeBox.addItem(desc.getName());
									}
								}
								VectorContainer container = mapPresenter.getContainerManager().addWorldContainer();
								layer = new SimpleWfsLayer(getWfsVersion(), getCapabilitiesurl(), layerInfo, result,
										layerInfo.getName(), mapPresenter.getViewPort(), container, mapPresenter
												.getEventBus());
								layer.getRenderer().setMaxCoordinates(nrOfFeaturesText.getValue());
								layer.getRenderer().setMaxFeatures(nrOfFeaturesText.getValue());
								mapPresenter.getLayersModel().addLayer(layer);
								transformBoundsAndNavigate(layerInfo.getWGS84BoundingBox());
								stopLoading();
							}

							@Override
							public void onFailure(String reason) {
								stopLoading();
								Window.alert("We're very sorry, but something went wrong: " + reason);
							}
						});
	}

	private String getCapabilitiesurl() {
		boolean strategySelected = strategyBox.getSelectedIndex() >= 1;
		boolean containsQuery = capsText.getText().contains("?");
		if (strategySelected) {
			if (containsQuery) {
				return capsText.getText() + "&strategy=" + strategyBox.getSelectedValue();
			} else {
				return capsText.getText() + "?strategy=" + strategyBox.getSelectedValue();
			}
		} else {
			return capsText.getText();
		}
	}

	private WfsVersion getWfsVersion() {
		if (wfsVersionBox.getSelectedIndex() == 0) {
			return WfsVersion.V1_0_0;
		} else if (wfsVersionBox.getSelectedIndex() == 1) {
			return WfsVersion.V1_1_0;
		} else if (wfsVersionBox.getSelectedIndex() == 2) {
			return WfsVersion.V2_0_0;
		}
		return null;
	}

}
