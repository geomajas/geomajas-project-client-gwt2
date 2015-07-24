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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.command.dto.TransformGeometryRequest;
import org.geomajas.command.dto.TransformGeometryResponse;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.event.MapInitializationEvent;
import org.geomajas.gwt2.client.event.MapInitializationHandler;
import org.geomajas.gwt2.client.gfx.VectorContainer;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.example.base.client.ExampleBase;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import java.util.logging.Logger;

/**
 * ContentPanel that demonstrates some options regarding getting and setting the map's bounding box.
 * 
 * @author Jan Venstermans
 */
public class MapBoundingBoxPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, MapBoundingBoxPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private static Logger logger = Logger.getLogger(MapBoundingBoxPanel.class.getName());

	private MapPresenter mapPresenter;

	@UiField
	protected TextBox crs;

	@UiField
	protected TextBox bboxX;

	@UiField
	protected TextBox bboxY;

	@UiField
	protected TextBox bboxWidth;

	@UiField
	protected TextBox bboxHeight;

	@UiField
	protected TextBox bboxMaxX;

	@UiField
	protected TextBox bboxMaxY;

	@UiField
	protected HTMLPanel widthHeightPanel;

	@UiField
	protected HTMLPanel maxXMaxYPanel;

	@UiField
	protected RadioButton widthHeightRadioBtn;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	private Rectangle applyRectangle;

	private VectorContainer container;

	public Widget asWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);
		updateWidthHeightPanelAndMaxXMaxYPanelVisibility();

		// Initialize the map, and return the layout:
		mapPresenter = GeomajasImpl.getInstance().createMapPresenter();
		mapPresenter.setSize(480, 480);
		mapPresenter.getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());
		GeomajasServerExtension.getInstance().initializeMap(mapPresenter, "gwt-app", "mapCountries");
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		return layout;
	}

	@UiHandler("widthHeightRadioBtn")
	protected void onWidthHeightRadioBtnClicked(ClickEvent event) {
		updateWidthHeightPanelAndMaxXMaxYPanelVisibility();
	}

	@UiHandler("maxXMaxYRadioBtn")
	protected void onMaxXMaxYRadioBtnClicked(ClickEvent event) {
		updateWidthHeightPanelAndMaxXMaxYPanelVisibility();
	}

	@UiHandler("bboxApplyBtn")
	protected void onBboxAppkyBtnClicked(ClickEvent event) {
		applyBboxToMap();
	}

	@UiHandler("bboxGetBtn")
	protected void onBboxGetBtnClicked(ClickEvent event) {
		getBboxFromMap();
	}

	@UiHandler("drawBboxBtn")
	protected void ondrawBboxBtnClicked(ClickEvent event) {
		clearRectangleContainter();
		Bbox bbox = getBboxFromGui();
		if (bbox != null) {
			String crsMap = mapPresenter.getViewPort().getCrs();
			if (crsMap.equals(crs.getValue())) {
				drawRectangle(toRectangle(bbox));
			} else {
				transformBbox(bbox, crs.getValue(), crsMap, new BboxTransformationCallback() {
					@Override
					public void onBboxTransformed(Bbox bbox) {
						drawRectangle(toRectangle(bbox));
					}
				});
			}
		} else {
//			alert("Not enough info for drawing a rectangle.");
		}
	}

	@UiHandler("clearBboxBtn")
	protected void onClearBboxBtnClicked(ClickEvent event) {
		clearRectangleContainter();
	}

	private void clearRectangleContainter() {
		if (applyRectangle != null) {
			container.remove(applyRectangle);
			applyRectangle = null;
		}
	}

	private void updateWidthHeightPanelAndMaxXMaxYPanelVisibility() {
		if (widthHeightRadioBtn.getValue()) {
			maxXMaxYPanel.setVisible(false);
			widthHeightPanel.setVisible(true);
		} else {
			widthHeightPanel.setVisible(false);
			maxXMaxYPanel.setVisible(true);
		}
	}

	/**
	 * Retuns null if data insufficient
	 * @return
	 */
	private Bbox getBboxFromGui() {
		System.out.println("started getBboxFromGui() method");
		logger.info("started getBboxFromGui() method");
		try {
			double bboxXValue = textValueToDouble(bboxX);
			double bboxYValue = textValueToDouble(bboxY);
			if (widthHeightRadioBtn.getValue()) {
				double bboxWidthValue = textValueToDouble(bboxWidth);
				double bboxHeightValue = textValueToDouble(bboxHeight);
				logger.info("will return widthHeightRadioBtn created bbox, with values: " +
						bboxXValue + " " + bboxYValue + " " + bboxWidthValue + " " + bboxHeightValue);
				System.out.println("will return widthHeightRadioBtn created bbox, with values: " +
						bboxXValue + " " + bboxYValue + " " + bboxWidthValue + " " + bboxHeightValue);
				return new Bbox(bboxXValue, bboxYValue, bboxWidthValue, bboxHeightValue);
			} else {
				double bboxMaxXValue = textValueToDouble(bboxMaxX);
				double bboxMaxYValue = textValueToDouble(bboxMaxY);
				Bbox bbox = new Bbox();
				bbox.setX(bboxXValue);
				bbox.setY(bboxYValue);
				bbox.setMaxX(bboxMaxXValue);
				bbox.setMaxY(bboxMaxYValue);
				logger.info("will return maxXMaxYRadioBtn created bbox, with values: " +
						bboxXValue + " " + bboxYValue + " " + bboxMaxXValue + " " + bboxMaxYValue);
				System.out.println("will return maxXMaxYRadioBtn created bbox, with values: " +
						bboxXValue + " " + bboxYValue + " " + bboxMaxXValue + " " + bboxMaxYValue);
				return bbox;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("an exception occured, returning null");
			System.out.println("an exception occured, returning null");
			// do nothing
		}
		return null;
	}

	private double textValueToDouble(TextBox textBox) throws Exception {
		String textValue = textBox.getValue();
		if (textValue != null && !textValue.isEmpty()) {
			Double value = Double.parseDouble(textBox.getValue());
			if (value != null) {
				return value;
			}
		}
		throw new Exception();
	}

	private void getBboxFromMap() {
		clearAllBboxValues();
		Bbox bbox = mapPresenter.getViewPort().getBounds();
		String crsMap = mapPresenter.getViewPort().getCrs();
		if (crsMap.equals(crs.getValue())) {
			showBbox(bbox);
		} else {
			transformBbox(bbox, crsMap, crs.getValue(), new BboxTransformationCallback() {
				@Override
				public void onBboxTransformed(Bbox bbox) {
					showBbox(bbox);
				}
			});
		}
	}

	private void applyBboxToMap() {
		Bbox bbox = getBboxFromGui();
		if (bbox != null) {
			String crsMap = mapPresenter.getViewPort().getCrs();
			if (crsMap.equals(crs.getValue())) {
				showBbox(bbox);
			} else {
				transformBbox(bbox, crs.getValue(), crsMap, new BboxTransformationCallback() {
					@Override
					public void onBboxTransformed(Bbox bbox) {
						mapPresenter.getViewPort().applyBounds(bbox);
					}
				});
			}
		} else {
//			alert("Not enough info for drawing a rectangle.");
		}
	}

	private void showBbox(Bbox bbox) {
		if (bbox != null) {
			bboxX.setValue(bbox.getX() + "");
			bboxY.setValue(bbox.getY() + "");
			bboxMaxX.setValue(bbox.getMaxX() + "");
			bboxMaxY.setValue(bbox.getMaxY() + "");
			bboxWidth.setValue(bbox.getWidth() + "");
			bboxHeight.setValue(bbox.getHeight() + "");
		} else {
			clearAllBboxValues();
		}
	}

	private void clearAllBboxValues() {
		bboxX.setValue(null);
		bboxY.setValue(null);
		bboxMaxX.setValue(null);
		bboxMaxY.setValue(null);
		bboxWidth.setValue(null);
		bboxHeight.setValue(null);
	}

	private void drawRectangle(Rectangle rectangle) {
		if (rectangle != null) {
			rectangle.setFillColor("#CC9900");
			rectangle.setFillOpacity(0.4);
			if (applyRectangle != null) {
				container.remove(applyRectangle);
			}
			applyRectangle = rectangle;
			container.add(applyRectangle);
		}
	}

	private Rectangle toRectangle(Bbox bbox) {
		return new Rectangle(bbox.getX(), bbox.getY(), bbox.getWidth(), bbox.getHeight());
	}

	private void transformBbox(Bbox bbox, String crsFrom, String crsTo, final BboxTransformationCallback callback) {
		final TransformGeometryRequest request = new TransformGeometryRequest();
		request.setBounds(bbox);
		request.setSourceCrs(crsFrom);
		request.setTargetCrs(crsTo);
		GwtCommand command = new GwtCommand(TransformGeometryRequest.COMMAND);
		command.setCommandRequest(request);
		GeomajasServerExtension.getInstance().getCommandService()
				.execute(command, new AbstractCommandCallback<TransformGeometryResponse>() {

					@Override
					public void execute(TransformGeometryResponse response) {
						if (callback != null) {
							callback.onBboxTransformed(response.getBounds());
						}
					}
				});
	}

	/**
	 * Map initialization handler that adds checkboxes for every layer to enable/disable animated rendering for those
	 * layers.
	 * 
	 * @author Pieter De Graef
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		public void onMapInitialized(MapInitializationEvent event) {
			crs.setText(mapPresenter.getViewPort().getCrs());

			container = mapPresenter.getContainerManager().addWorldContainer();

			// Zoom in (scale times 4), to get a better view:
			mapPresenter.getViewPort().applyBounds(ExampleBase.BBOX_AFRICA);
		}
	}

	/**
	 * Interface for callback functions when bbox is transformed on server side.
	 * @author Jan Venstermans
	 */
	private interface BboxTransformationCallback {
		void onBboxTransformed(Bbox bbox);
	}
}
