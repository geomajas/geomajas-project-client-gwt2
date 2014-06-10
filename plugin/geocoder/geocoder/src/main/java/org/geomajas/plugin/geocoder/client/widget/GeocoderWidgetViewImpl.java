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

package org.geomajas.plugin.geocoder.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.geometry.Bbox;
import org.geomajas.plugin.geocoder.client.Geocoder;
import org.geomajas.plugin.geocoder.client.i18n.GeocoderMessages;

/**
 * Default implementation of {@link GeocoderWidgetView}.
 * The widget create contains a {@link GeocoderTextBox} and a clickable magnifying glass.
 * <br/>The position of the widget on the GWT2 map can can be configured via
 * constructor {@link #GeocoderWidgetViewImpl(int, int)}.
 *
 * @author Emiel Ackermann
 * @author Jan Venstermans
 */
public class GeocoderWidgetViewImpl implements GeocoderWidgetView {

	private GeocoderWidgetResource resource;

	@UiField
	protected FlowPanel flowPanel;

	@UiField
	protected GeocoderTextBox textBox;

	@UiField
	protected Button glass;

	private GeocoderMessages messages = GWT.create(GeocoderMessages.class);

	private GeocoderWidgetPresenter geocoderPresenter;

	private static final GeocoderWidgetViewImplBinder UIBINDER = GWT.create(GeocoderWidgetViewImplBinder.class);

	/**
	 * UI binder interface.
	 *
	 * @author Jan De Moerloose
	 *
	 */
	interface GeocoderWidgetViewImplBinder extends UiBinder<Widget, GeocoderWidgetViewImpl> {
	}

	/* constructors */

	/**
	 * Default constructor.
	 */
	public GeocoderWidgetViewImpl() {
		this(Geocoder.getInstance().getBundleFactory().createGeocoderWidgetResource());
	}

	/**
	 * Resource constructor.
	 * @param resource
	 */
	public GeocoderWidgetViewImpl(GeocoderWidgetResource resource) {
		this.resource = resource;
		resource.css().ensureInjected();
		UIBINDER.createAndBindUi(this);
		textBox.setGeocoderPresenter(geocoderPresenter);
		bind();
	}

	/**
	 * The int attributes are used to position the widget within the screen boundaries of the map.
	 *
	 * @param top amount of pixels between the position of the widget and the top part of the map's screen.
	 *            If a negative value is provided, the absolute value will be the amount of pixels
	 *            between the bottom part of the map's screen and the widget.
	 * @param left amount of pixels between the position of the widget and the left part of the map's screen.
	 *            If a negative value is provided, the absolute value will be the amount of pixels
	 *            between the right hand side of the map's screen and the widget.
	 */
	public GeocoderWidgetViewImpl(int top, int left) {
		this();
		if (top > 0) {
			asWidget().getElement().getStyle().setTop(top, Unit.PX);
		} else if (top < 0) {
			asWidget().getElement().getStyle().setBottom(top, Unit.PX);
		}
		if (left > 0) {
			asWidget().getElement().getStyle().setLeft(left, Unit.PX);
		} else if (left < 0) {
			asWidget().getElement().getStyle().setRight(left, Unit.PX);
		}
	}

	/**
	 * Add handlers
	 */
	private void bind() {
		StopPropagationHandler stopHandler = new StopPropagationHandler();
		flowPanel.addDomHandler(stopHandler, MouseDownEvent.getType());
		flowPanel.addDomHandler(stopHandler, MouseUpEvent.getType());
		flowPanel.addDomHandler(stopHandler, ClickEvent.getType());
		flowPanel.addDomHandler(stopHandler, DoubleClickEvent.getType());

		// add textBox handlers
		textBox.addKeyPressHandler(new KeyPressHandler() {

			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					executeFind();
				}
			}
		});
		textBox.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (messages.findPlaceOnMap().equals(textBox.getValue())) {
					setValue(null);
					flowPanel.removeStyleName(resource.css().geocoderGadgetTip());
				}
			}
		});
		textBox.addBlurHandler(new BlurHandler() {

			public void onBlur(BlurEvent event) {
				if (textBox.getValue() == null || "".equals(textBox.getValue())) {
					setValue(messages.findPlaceOnMap());
					flowPanel.addStyleName(resource.css().geocoderGadgetTip());
				}
			}
		});

		// add magnifying glass handler
		glass.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				executeFind();
				event.stopPropagation();
			}
		});
	}

	/* View interface methods*/

	@Override
	public Widget asWidget() {
		return flowPanel;
	}

	@Override
	public void setPresenter(GeocoderWidgetPresenter presenter) {
		this.geocoderPresenter = presenter;
	}

	@Override
	public void alternativesViewIsShown(boolean shown) {
		if (shown) {
			flowPanel.addStyleName(resource.css().geocoderGadgetTextBoxWithAlts());
		} else {
			flowPanel.removeStyleName(resource.css().geocoderGadgetTextBoxWithAlts());
		}
	}

	@Override
	public void setValue(String value) {
		textBox.setValue(value);
	}

	/* extra methods for some non-standard functions */

	/**
	 *  Return the bbox of the textbox element. These bounds will be used to position the
	 *  alternative locations view.
	 *
	 * @return bbox of the textbox
	 */
	public Bbox getWidgetViewBbox() {
		return new Bbox(textBox.getAbsoluteLeft(), textBox.getAbsoluteTop(),
				textBox.getOffsetWidth(), textBox.getOffsetHeight());
	}

	/**
	 * Shows an alert message. Called when location can not be found.
	 *
	 * @param location the location (as string) that could be found
	 */
	public void showLocationNotFoundMessage(String location) {
		Window.alert(messages.locationNotFound(location));
	}

	/* private methods */

	private void executeFind() {
		geocoderPresenter.findLocation(textBox.getValue());
	}

	/**
	 * Combination of different handlers with a single goal: stop all the events from propagating to the map. This is
	 * meant to be used for clickable widgets.
	 * 
	 * @author Pieter De Graef
	 */
	public class StopPropagationHandler implements MouseDownHandler, MouseUpHandler, ClickHandler, 
			DoubleClickHandler {

		public void onDoubleClick(DoubleClickEvent event) {
			event.stopPropagation();
		}

		public void onClick(ClickEvent event) {
			event.stopPropagation();
		}

		public void onMouseDown(MouseDownEvent event) {
			event.stopPropagation();
		}

		public void onMouseUp(MouseUpEvent event) {
			event.stopPropagation();
		}
	}
}