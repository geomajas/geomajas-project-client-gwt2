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

package org.geomajas.gwt2.client.widget.control.zoom;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.gwt.client.util.Dom;
import org.geomajas.gwt2.client.animation.NavigationAnimationFactory;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.widget.AbstractMapWidget;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Map widget that displays zoom in, zoom out and zoom to maximum extent buttons. This widget is meant to be added to
 * the map's widget pane (see {@link MapPresenter#getWidgetPane()}).
 *
 * @author Pieter De Graef
 * @author Dosi Bingov
 */
public class ZoomControl extends AbstractMapWidget {

	private static Logger logger = Logger.getLogger("");

	/**
	 * UI binder definition for the {@link ZoomControl} widget.
	 *
	 * @author Pieter De Graef
	 */
	interface ZoomControlUiBinder extends UiBinder<Widget, ZoomControl> {

	}

	private static final ZoomControlUiBinder UI_BINDER = GWT.create(ZoomControlUiBinder.class);

	private final ZoomControlResource resource;

	@UiField
	protected SimplePanel zoomInElement;

	@UiField
	protected SimplePanel zoomOutElement;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a widget with zoom in, zoom out and zoom to maximum extent button.
	 *
	 * @param mapPresenter The map to show this widget on.
	 */
	public ZoomControl(MapPresenter mapPresenter) {
		this(mapPresenter, (ZoomControlResource) GWT.create(ZoomControlResource.class));
	}

	/**
	 * Create a widget with zoom in, zoom out and zoom to maximum extent button.
	 *
	 * @param mapPresenter The map to show this widget on.
	 * @param resource Custom resource bundle in case you want to provide your own style for this widget.
	 */
	public ZoomControl(MapPresenter mapPresenter, ZoomControlResource resource) {
		super(mapPresenter);
		this.resource = resource;
		this.resource.css().ensureInjected();
		buildGui();
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void buildGui() {
		initWidget(UI_BINDER.createAndBindUi(this));
		zoomInElement.getElement().setInnerText("+");
		zoomOutElement.getElement().setInnerText("-");
		getElement().getStyle().setPosition(Position.ABSOLUTE);
		getElement().getStyle().setTop(5, Unit.PX);
		getElement().getStyle().setLeft(5, Unit.PX);

		if (Dom.isTouchSupported()) {
			initializeForTouchDevice();
		} else {
			initializeForDesktop();
		}
	}

	/**
	 * Initialize handlers for desktop browser.
	 */
	private void initializeForDesktop() {
		logger.log(Level.INFO, "ZoomControl ->initializeForDesktop()");
		StopPropagationHandler preventWeirdBehaviourHandler = new StopPropagationHandler();
		addDomHandler(preventWeirdBehaviourHandler, MouseDownEvent.getType());
		addDomHandler(preventWeirdBehaviourHandler, MouseUpEvent.getType());
		addDomHandler(preventWeirdBehaviourHandler, ClickEvent.getType());
		addDomHandler(preventWeirdBehaviourHandler, DoubleClickEvent.getType());

		final ViewPort viewPort = mapPresenter.getViewPort();

		// Zoom in button:
		zoomInElement.addDomHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				logger.log(Level.INFO, "ZoomControl -> zoomInElement onClick()");
				int index = viewPort.getFixedScaleIndex(viewPort.getScale());
				if (index < viewPort.getFixedScaleCount() - 1) {
					viewPort.registerAnimation(NavigationAnimationFactory.createZoomIn(mapPresenter));
				}
				event.stopPropagation();
			}
		}, ClickEvent.getType());

		// Zoom out button:
		zoomOutElement.addDomHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				logger.log(Level.INFO, "ZoomControl -> zoomOutElement onClick()");
				int index = viewPort.getFixedScaleIndex(viewPort.getScale());
				if (index > 0) {
					viewPort.registerAnimation(NavigationAnimationFactory.createZoomOut(mapPresenter));
				}
				event.stopPropagation();
			}
		}, ClickEvent.getType());
	}

	/**
	 * Initialize handlers for mobile devices.
	 */
	private void initializeForTouchDevice() {
		logger.log(Level.INFO, "ZoomControl -> initializeForTouchDevice()");
		// Add touch handlers to the zoom in button:
		zoomInElement.addDomHandler(new TouchStartHandler() {

			@Override
			public void onTouchStart(TouchStartEvent event) {
				event.stopPropagation();
				event.preventDefault();

				logger.log(Level.INFO, "ZoomControl -> zoomInElement onTouchStart()");
				ViewPort viewPort = mapPresenter.getViewPort();
				int index = viewPort.getFixedScaleIndex(viewPort.getScale());

				if (index < viewPort.getFixedScaleCount() - 1) {
					viewPort.applyScale(viewPort.getFixedScale(index + 1));
					viewPort.getPosition();
				}
			}
		}, TouchStartEvent.getType());

		// Add touch handlers to the zoom out button:
		zoomOutElement.addDomHandler(new TouchStartHandler() {

			@Override
			public void onTouchStart(TouchStartEvent event) {
				logger.log(Level.INFO, "zoomOutElement -> zoomInElement onTouchStart()");
				event.stopPropagation();
				event.preventDefault();

				ViewPort viewPort = mapPresenter.getViewPort();
				int index = viewPort.getFixedScaleIndex(viewPort.getScale());

				if (index > 0) {
					viewPort.applyScale(viewPort.getFixedScale(index - 1));
				}
			}
		}, TouchStartEvent.getType());
	}

}