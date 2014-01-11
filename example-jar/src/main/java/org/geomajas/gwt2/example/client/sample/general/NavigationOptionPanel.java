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

package org.geomajas.gwt2.example.client.sample.general;

import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.event.MapInitializationEvent;
import org.geomajas.gwt2.client.event.MapInitializationHandler;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.example.base.client.ExampleBase;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
				mapPresenter.getConfiguration().setMapHintValue(MapConfiguration.ANIMATION_CANCEL_SUPPORT,
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

	private void changeAnimationMillis() {
		String txt = millisBox.getValue();
		int time = defaultMillis;
		try {
			time = Integer.parseInt(txt);
		} catch (Exception e) { // NOSONAR
			Window.alert("Could not parse milliseconds... Default value of " + defaultMillis + " is used");
			mapPresenter.getConfiguration().setMapHintValue(MapConfiguration.ANIMATION_TIME, defaultMillis);
			millisBox.setValue(defaultMillis + "");
		}
		mapPresenter.getConfiguration().setMapHintValue(MapConfiguration.ANIMATION_TIME, time);
	}

	private void changeFadeInMillis() {
		String txt = fadeInBox.getValue();
		int time = defaultFadeInMillis;
		try {
			time = Integer.parseInt(txt);
		} catch (Exception e) { // NOSONAR
			Window.alert("Could not parse milliseconds... Default value of " + defaultFadeInMillis + " is used");
			mapPresenter.getConfiguration().setMapHintValue(MapConfiguration.FADE_IN_TIME, defaultMillis);
			fadeInBox.setValue(defaultFadeInMillis + "");
		}
		mapPresenter.getConfiguration().setMapHintValue(MapConfiguration.FADE_IN_TIME, time);
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

			// Zoom in (scale times 4), to get a better view:
			mapPresenter.getViewPort().applyBounds(ExampleBase.BBOX_AFRICA);
		}
	}
}