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

package org.geomajas.gwt2.client.controller;

import com.google.gwt.event.dom.client.GestureChangeEvent;
import com.google.gwt.event.dom.client.GestureEndEvent;
import com.google.gwt.event.dom.client.GestureStartEvent;
import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEvent;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchStartEvent;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.map.MapPresenter;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generic controller that is used on touch devices. Note that gestures and multi touch are not supported by some mobile
 * browsers.
 *
 * @author Dosi Bingov
 * @since 2.0.0
 */
public class TouchNavigationController extends AbstractMapController {

	private static Logger logger = Logger.getLogger("");

	protected Coordinate touchedOrigin;

	protected Coordinate lastTouchedPosition;

	protected boolean zooming;

	protected Coordinate centerPoint;

	private double beginResolution;

	public TouchNavigationController() {
		super(false);
	}

	// ------------------------------------------------------------------------
	// Touch events:
	// ------------------------------------------------------------------------

	@Override
	public void onTouchStart(TouchStartEvent event) {
		logger.log(Level.INFO, "TouchNavigationController -> onTouchStart()");
		event.preventDefault();
		lastTouchedPosition = getLocation(event, RenderSpace.WORLD);
		touchedOrigin = getLocation(event, RenderSpace.SCREEN);
		centerPoint = getMidPoint(event);
	}

	@Override
	public void onTouchEnd(TouchEndEvent event) {
		logger.log(Level.INFO, "TouchNavigationController -> onTouchEnd()");
		//TODO: it is never fired = find out what is wrong with that
	}

	@Override
	public void onTouchMove(TouchMoveEvent event) {
		logger.log(Level.INFO, "TouchNavigationController -> onTouchMove()");
		event.preventDefault();
		event.stopPropagation();
		panView(event);
	}

	@Override
	public void onTouchCancel(TouchCancelEvent event) {
		event.preventDefault();
		event.stopPropagation();
		logger.log(Level.INFO, "TouchNavigationController -> onTouchCancel()");
	}

	// ------------------------------------------------------------------------
	// Gesture events:
	// ------------------------------------------------------------------------

	@Override
	public void onGestureStart(GestureStartEvent event) {
		event.preventDefault();
		event.stopPropagation();
		beginResolution = mapPresenter.getViewPort().getResolution();
		zooming = true;
		logger.log(Level.INFO, "TouchNavigationController -> onGestureStart() viewport resolution=" + beginResolution);
		//zoomTo(event.getResolution(), false);
	}

	@Override
	public void onGestureEnd(GestureEndEvent event) {
		logger.log(Level.INFO, "TouchNavigationController -> onGestureEnd()");
		event.preventDefault();
		event.stopPropagation();
		zooming = false;
	}

	@Override
	public void onGestureChange(GestureChangeEvent event) {
		logger.log(Level.INFO, "TouchNavigationController -> onGestureChange()");
		event.preventDefault();
		event.stopPropagation();
		zoomTo(event.getScale());
	}

	// ------------------------------------------------------------------------
	// Methods:
	// ------------------------------------------------------------------------

	@Override
	public void onActivate(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
		this.eventParser = mapPresenter.getMapEventParser();
	}

	@Override
	public void onDeactivate(MapPresenter mapPresenter) {
	}

	protected void zoomTo(double scale) {
		logger.log(Level.INFO, "TouchNavigationController -> zoomTo(resolution=" + scale + " )");
		logger.log(Level.INFO, "begin viewport resolution =" + beginResolution);
		double zoomResolution = beginResolution / scale;
		logger.log(Level.INFO, "zoomResolution=" + zoomResolution);
		mapPresenter.getViewPort().registerAnimation(null); // without animation for touch devices
		mapPresenter.getViewPort().applyResolution(zoomResolution);
		zooming = true;
	}

	/**
	 * Method used to calculate exact middle point between multiple touches of a touch events.
	 *
	 * @param event a touch event
	 * @return middle point
	 */
	protected Coordinate getMidPoint(TouchEvent<?> event) {
		Coordinate[] coords = new Coordinate[event.getTouches().length()];
		for (int i = 0; i < event.getTargetTouches().length(); i++) {
			coords[i] = new Coordinate(event.getTouches().get(i).getClientX(), event.getTouches().get(i).getClientY());
		}

		double x = 0;
		double y = 0;

		for (Coordinate coord : coords) {
			x += coord.getX();
			y += coord.getY();
		}

		x /= coords.length;
		y /= coords.length;

		return new Coordinate(x, y);
	}

	/**
	 * Update the view of the map when touching and dragging.
	 *
	 * @param event
	 */
	protected void panView(TouchEvent<?> event) {
		logger.log(Level.INFO, "TouchNavigationController -> panView(TouchEvent<?> event, boolean isTouchEnded)");
		logger.log(Level.INFO, "zooming=" + zooming);
		if (zooming) {
			return;
		}

		Coordinate end = getMidPoint(event);
		mapPresenter.getViewPort().applyPosition(transformToMapCoords(end));
		touchedOrigin = end;
	}

	/**
	 * Transforms pixel coordinates to map coordinates.
	 *
	 * @param pixelCoords
	 * @return Coordinate object on the map
	 */
	protected Coordinate transformToMapCoords(Coordinate pixelCoords) {
		Coordinate beginWorld = mapPresenter.getViewPort().getTransformationService()
				.transform(touchedOrigin, RenderSpace.SCREEN, RenderSpace.WORLD);
		Coordinate endWorld = mapPresenter.getViewPort().getTransformationService()
				.transform(pixelCoords, RenderSpace.SCREEN, RenderSpace.WORLD);
		double x = mapPresenter.getViewPort().getPosition().getX() + beginWorld.getX() - endWorld.getX();
		double y = mapPresenter.getViewPort().getPosition().getY() + beginWorld.getY() - endWorld.getY();

		return new Coordinate(x, y);
	}
}