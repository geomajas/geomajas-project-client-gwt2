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

package org.geomajas.gwt2.example.client.sample.general;

import org.geomajas.geometry.Bbox;
import org.geomajas.command.dto.TransformGeometryRequest;
import org.geomajas.command.dto.TransformGeometryResponse;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.event.MapInitializationEvent;
import org.geomajas.gwt2.client.event.MapInitializationHandler;
import org.geomajas.gwt2.client.gfx.VectorContainer;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.ZoomOption;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.example.base.client.ExampleBase;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

/**
 * ContentPanel that demonstrates some options regarding map navigation.
 * 
 * @author Pieter De Graef
 */
public class NavigationOptionPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, NavigationOptionPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private final int defaultMillis = 400;

	private final int defaultFadeInMillis = 250;

	private MapPresenter mapPresenter;

	@UiField
	protected TextBox millisBox;

	@UiField
	protected TextBox fadeInBox;

	@UiField
	protected CheckBox cancelAnimationSupport;

	@UiField
	protected VerticalPanel layerPanel;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	private VectorContainer container;
	private Bbox rectangleWideBbox;
	private Bbox rectangleHighBbox;

	public Widget asWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Initialize the map, and return the layout:
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter();
		mapPresenter.setSize(480, 480);
		mapPresenter.getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());
		GeomajasServerExtension.getInstance().initializeMap(mapPresenter, "gwt-app", "mapCountries");
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		// Make sure the text box also reacts to the "Enter" key:
		millisBox.addKeyUpHandler(new KeyUpHandler() {

			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					changeAnimationMillis();
				}
			}
		});
		fadeInBox.addKeyUpHandler(new KeyUpHandler() {

			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					changeFadeInMillis();
				}
			}
		});
		cancelAnimationSupport.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			public void onValueChange(ValueChangeEvent<Boolean> event) {
				mapPresenter.getConfiguration().setHintValue(MapConfiguration.ANIMATION_CANCEL_SUPPORT,
						cancelAnimationSupport.getValue());
			}
		});

		return layout;
	}

	@UiHandler("millisBtn")
	protected void onMillisButtonClicked(ClickEvent event) {
		changeAnimationMillis();
	}

	@UiHandler("fadeInBtn")
	protected void onFadeInButtonClicked(ClickEvent event) {
		changeFadeInMillis();
	}

	@UiHandler("currentLocationBtn")
	protected void onCurrentLocationButtonClicked(ClickEvent event) {
		if (Geolocation.isSupported()) {
			Geolocation.getIfSupported().getCurrentPosition(new Callback<Position, PositionError>() {

				@Override
				public void onSuccess(Position result) {
					TransformGeometryRequest request = new TransformGeometryRequest();
					request.setBounds(new Bbox(result.getCoordinates().getLongitude(), result.getCoordinates()
							.getLatitude(), 0, 0));
					request.setSourceCrs("EPSG:4326");
					request.setTargetCrs(mapPresenter.getViewPort().getCrs());
					GwtCommand command = new GwtCommand(TransformGeometryRequest.COMMAND);
					command.setCommandRequest(request);
					GeomajasServerExtension.getInstance().getCommandService()
							.execute(command, new AbstractCommandCallback<TransformGeometryResponse>() {

								@Override
								public void execute(TransformGeometryResponse response) {
									mapPresenter.getViewPort().applyBounds(response.getBounds());
								}
							});
				};

				@Override
				public void onFailure(PositionError reason) {
					// TODO Auto-generated method stub

				}
			});
		}
	}

	@UiHandler("rectangleWideBtn")
	public void onRectangleWideBtnClicked(ClickEvent event) {
		mapPresenter.getViewPort().applyBounds(rectangleWideBbox, ZoomOption.LEVEL_FIT);
	}

	@UiHandler("rectangleHighBtn")
	public void onRectangleHighBtnClicked(ClickEvent event) {
		mapPresenter.getViewPort().applyBounds(rectangleHighBbox, ZoomOption.LEVEL_FIT);
	}

	@UiHandler("africaBoundsBtn")
	public void onInitialBoundsBtnClicked(ClickEvent event) {
		mapPresenter.getViewPort().applyBounds(ExampleBase.BBOX_AFRICA, ZoomOption.LEVEL_FIT);
	}

	@UiHandler("maxBoundsBtn")
	public void onMaxBoundsBtnClicked(ClickEvent event) {
		mapPresenter.getViewPort().applyBounds(mapPresenter.getViewPort().getMaximumBounds(), ZoomOption.LEVEL_FIT);
	}


	private void changeAnimationMillis() {
		String txt = millisBox.getValue();
		int time = defaultMillis;
		try {
			time = Integer.parseInt(txt);
		} catch (Exception e) { // NOSONAR
			Window.alert("Could not parse milliseconds... Default value of " + defaultMillis + " is used");
			mapPresenter.getConfiguration().setHintValue(MapConfiguration.ANIMATION_TIME, defaultMillis);
			millisBox.setValue(defaultMillis + "");
		}
		mapPresenter.getConfiguration().setHintValue(MapConfiguration.ANIMATION_TIME, time);
	}

	private void changeFadeInMillis() {
		String txt = fadeInBox.getValue();
		int time = defaultFadeInMillis;
		try {
			time = Integer.parseInt(txt);
		} catch (Exception e) { // NOSONAR
			Window.alert("Could not parse milliseconds... Default value of " + defaultFadeInMillis + " is used");
			mapPresenter.getConfiguration().setHintValue(MapConfiguration.FADE_IN_TIME, defaultMillis);
			fadeInBox.setValue(defaultFadeInMillis + "");
		}
		mapPresenter.getConfiguration().setHintValue(MapConfiguration.FADE_IN_TIME, time);
	}

	private void fillContainerAndMakeBboxes() {
		//add rectangleWide
		Rectangle rectangleWide = new Rectangle(1000000, 1000000, 2000000, 1000000);
		rectangleWide.setFillColor("#CC9900");
		rectangleWide.setFillOpacity(0.4);
		container.add(rectangleWide);
		rectangleWideBbox = new Bbox(rectangleWide.getUserX(), rectangleWide.getUserY(),
				rectangleWide.getUserWidth(), rectangleWide.getUserHeight());

		Rectangle rectangleHigh = new Rectangle(-2000000, 1000000, 1000000, 2000000);
		rectangleHigh.setFillColor("#66CC66");
		rectangleHigh.setFillOpacity(0.4);
		container.add(rectangleHigh);
		rectangleHighBbox = new Bbox(rectangleHigh.getUserX(), rectangleHigh.getUserY(),
				rectangleHigh.getUserWidth(), rectangleHigh.getUserHeight());
	}

	/**
	 * Map initialization handler that adds checkboxes for every layer to enable/disable animated rendering for those
	 * layers.
	 * 
	 * @author Pieter De Graef
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		public void onMapInitialized(MapInitializationEvent event) {
			// Add layer specific animation CheckBoxes:
			for (int i = 0; i < mapPresenter.getLayersModel().getLayerCount(); i++) {
				final Layer layer = mapPresenter.getLayersModel().getLayer(i);
				CheckBox cb = new CheckBox("Animate: " + layer.getTitle());
				cb.setValue(mapPresenter.getLayersModelRenderer().isAnimated(layer));
				cb.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					public void onValueChange(ValueChangeEvent<Boolean> event) {
						mapPresenter.getLayersModelRenderer().setAnimated(layer, event.getValue());
					}
				});
				layerPanel.add(cb);
			}

			container = mapPresenter.getContainerManager().addWorldContainer();
			fillContainerAndMakeBboxes();

			// Zoom in (scale times 4), to get a better view:
			mapPresenter.getViewPort().applyBounds(ExampleBase.BBOX_AFRICA);
		}
	}
}
