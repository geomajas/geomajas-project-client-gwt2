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

package org.geomajas.gwt2.example.client.sample.rendering;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.controller.AbstractRectangleController;
import org.geomajas.gwt2.client.event.MapInitializationEvent;
import org.geomajas.gwt2.client.event.MapInitializationHandler;
import org.geomajas.gwt2.client.gfx.CanvasContainer;
import org.geomajas.gwt2.client.gfx.CanvasRect;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.example.client.ExampleClientBundle;
import org.geomajas.gwt2.example.client.ExampleJar;

/**
 * ContentPanel that demonstrates canvas rendering abilities.
 * 
 * @author Jan Venstermans
 */
public class CanvasImageRenderingPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Jan De Moerloose
	 */
	interface MyUiBinder extends UiBinder<Widget, CanvasImageRenderingPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	private CanvasContainer container;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	@UiField
	protected Label label;

	@UiField
	protected RadioButton internalImageRadioButton;

	@UiField
	protected RadioButton remoteImageRadioButton;

	@UiField
	protected RadioButton locationCenterButton;

	@UiField
	protected RadioButton locationCustomButton;

	@UiField
	protected TextBox imageUrlTextBox;

	public Widget asWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);

		internalImageRadioButton.setValue(true);
		locationCenterButton.setValue(true);
		remoteImageRadioButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
				imageUrlTextBox.setEnabled(remoteImageRadioButton.getValue());
			}
		});

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter();
		mapPresenter.setSize(480, 480);
		mapPresenter.getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());

		// Define the whole layout:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		// Initialize the map, and return the layout:
		GeomajasServerExtension.getInstance().initializeMap(mapPresenter, "gwt-app", "mapOsm");
		label.setVisible(false);
		return layout;
	}

	@UiHandler("clearCanvas")
	public void onClearCanvasBtnClicked(ClickEvent event) {
		container.clear();
	}

	@UiHandler("drawButton")
	public void onDrawBtnClicked(ClickEvent event) {
		if (container != null) {
			String url = null;
			if (internalImageRadioButton.getValue()) {
				ExampleClientBundle exampleClientBundle = GWT.create(ExampleClientBundle.class);
				url = exampleClientBundle.imageTuxTest().getSafeUri().asString();
			} else if (remoteImageRadioButton.getValue()) {
				url = imageUrlTextBox.getText();
				// fallback value
				if (url == null || url.isEmpty()) {
					url = "http://tuxpaint.org/stamps/stamps/animals/birds/cartoon/tux.png";
				}
			}

			Bbox boundsCenter = null;
			if (locationCenterButton.getValue()) {
				boundsCenter = getCurrentQuarterCenterBounds();
			} else if (locationCustomButton.getValue()) {
				final String urlSelection = url;
				mapPresenter.setMapController(new AbstractRectangleController() {
					@Override
					public void execute(Bbox worldBounds) {
						mapPresenter.setMapController(null);
						drawElements(worldBounds, urlSelection);
					}
				});
				return;
			}

			drawElements(boundsCenter, url);
		}
	}

	/* private methods */

	private void drawElements(Bbox bounds, String url) {
		if (bounds != null && url != null) {
			drawRedRectangle(bounds, container);
			drawImageFromUrl(url, bounds, container);
		}
	}

	private Bbox getCurrentQuarterCenterBounds() {
		Bbox bbox = mapPresenter.getViewPort().getBounds();
		double centerX = ( bbox.getX() + bbox.getMaxX()) / 2;
		double centerY = ( bbox.getY() + bbox.getMaxY()) / 2;
		double width = bbox.getWidth() / 4;
		double height = bbox.getHeight() / 4;
		return new Bbox(centerX - width / 2, centerY - height / 2, width, height);
	}

	private void drawRedRectangle(Bbox bounds, CanvasContainer canvasContainer) {
		CanvasRect canvasRect = new CanvasRect(bounds);
		canvasRect.setFillStyle("rgba(20,20,150,50)");
		canvasRect.setStrokeStyle("rgba(0,0,0,255)");
		canvasRect.setStrokeWidthPixels(1);
		canvasContainer.addShape(canvasRect);
	}

	private void drawImageFromUrl(String url, final Bbox bounds, final CanvasContainer canvasContainer) {
		new ImageLoader(url,
				new Callback<ImageElement, String>() {
					@Override
					public void onFailure(String s) {

					}

					@Override
					public void onSuccess(ImageElement imageElement) {
						drawImageFromImageElement(imageElement, bounds, canvasContainer);
					}
				});
		container.repaint();
	}

	private void drawImageFromImageElement(ImageElement imageElement,
										   final Bbox bounds, final CanvasContainer canvasContainer) {
		CanvasImageElement canvasImageElement =
				new CanvasImageElement(imageElement, bounds);
		canvasContainer.addShape(canvasImageElement);
	}

	/**
	 * Map initialization handler that adds a CheckBox to the layout for every layer. With these CheckBoxes, the user
	 * can toggle the layer's visibility.
	 * 
	 * @author Pieter De Graef
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		public void onMapInitialized(MapInitializationEvent event) {
			if (Canvas.isSupported()) {
				container = mapPresenter.getContainerManager().addWorldCanvasContainer();
			} else {
				label.setText(ExampleJar.getMessages().renderingMissingCanvas());
				label.setVisible(true);
			}
		}
	}
}