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

package org.geomajas.gwt2.client.controller;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.MathService;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.event.ViewPortChangedEvent;
import org.geomajas.gwt2.client.event.ViewPortChangedHandler;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.View;
import org.geomajas.gwt2.client.map.ViewPort;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.GestureChangeEvent;
import com.google.gwt.event.dom.client.GestureEndEvent;
import com.google.gwt.event.dom.client.GestureStartEvent;
import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchStartEvent;

/**
 * Generic controller that is used on touch devices. Note that gestures and multi touch are not supported by some mobile
 * browsers.
 *
 * @author Jan De Moerloose
 * @since 2.0.0
 */
public class TouchNavigationController extends NavigationController {

	protected boolean zooming;

	protected Coordinate startCenter;

	protected double startDist;

	// ------------------------------------------------------------------------
	// Touch events:
	// ------------------------------------------------------------------------

	@Override
	public void onTouchStart(TouchStartEvent event) {
		if (event.getTouches().length() == 2) {
			Coordinate p1 = getWorldLocation(event.getTouches().get(0), RenderSpace.WORLD);
			Coordinate p2 = getWorldLocation(event.getTouches().get(1), RenderSpace.WORLD);
			startCenter = getCenter(p1, p2);
			startDist = MathService.distance(p1, p2);
			stopPanning(null);
			this.zooming = true;
		} else {
			onDown(event);
		}
		event.preventDefault();
		event.stopPropagation();
	}

	@Override
	public void onTouchMove(TouchMoveEvent event) {
		if (event.getTouches().length() == 2 && zooming) {
			Coordinate p1 = getWorldLocation(event.getTouches().get(0), RenderSpace.WORLD);
			Coordinate p2 = getWorldLocation(event.getTouches().get(1), RenderSpace.WORLD);
			double scale = MathService.distance(p1, p2) / startDist;
			Coordinate newCenter = getCenter(p1, p2);
			double dx = startCenter.getX() - newCenter.getX();
			double dy = startCenter.getY() - newCenter.getY();
			double resolution = mapPresenter.getViewPort().getResolution() / scale;
			Coordinate position = calculatePosition(scale, startCenter);
			mapPresenter.getViewPort().applyView(
					new View(new Coordinate(position.getX() + dx, position.getY() + dy), resolution), true);
		} else {
			onDrag(event);
		}
		event.preventDefault();
		event.stopPropagation();
	}

	@Override
	public void onTouchEnd(TouchEndEvent event) {
		if (zooming) {
			if (event.getTouches().length() == 0) {
				zooming = false;
			}
			mapPresenter.getViewPort().finishIntermediate();
		} else {
//			onUp(event);
		}
		event.preventDefault();
		event.stopPropagation();
	}

	private Coordinate getWorldLocation(Touch touch, RenderSpace world) {
		Element element = mapPresenter.asWidget().getElement();
		Coordinate c = new Coordinate(touch.getRelativeX(element), touch.getRelativeY(element));
		return mapPresenter.getViewPort().getTransformationService()
				.transform(c, RenderSpace.SCREEN, RenderSpace.WORLD);
	}

	private Coordinate getScreenLocation(Coordinate c) {
		return mapPresenter.getViewPort().getTransformationService()
				.transform(c, RenderSpace.WORLD, RenderSpace.SCREEN);
	}

	private Coordinate getCenter(Coordinate p1, Coordinate p2) {
		return new Coordinate(0.5 * (p1.getX() + p2.getX()), 0.5 * (p1.getY() + p2.getY()));
	}

	/**
	 * Calculate the target position should there be a rescale point. The idea is that after zooming in or out, the
	 * mouse cursor would still lie at the same position in world space.
	 */
	protected Coordinate calculatePosition(double scale, Coordinate rescalePoint) {
		ViewPort viewPort = mapPresenter.getViewPort();
		Coordinate position = viewPort.getPosition();
		double dX = (rescalePoint.getX() - position.getX()) * (1 - 1 / scale);
		double dY = (rescalePoint.getY() - position.getY()) * (1 - 1 / scale);
		return new Coordinate(position.getX() + dX, position.getY() + dY);
	}

	@Override
	public void onTouchCancel(TouchCancelEvent event) {
		event.preventDefault();
		event.stopPropagation();
	}

	// ------------------------------------------------------------------------
	// Gesture events:
	// ------------------------------------------------------------------------

	@Override
	public void onGestureStart(GestureStartEvent event) {
	}

	@Override
	public void onGestureEnd(GestureEndEvent event) {
	}

	@Override
	public void onGestureChange(GestureChangeEvent event) {
	}

	// ------------------------------------------------------------------------
	// Methods:
	// ------------------------------------------------------------------------

	@Override
	public void onActivate(final MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
		this.eventParser = mapPresenter.getMapEventParser();
	}

	@Override
	public void onDeactivate(MapPresenter mapPresenter) {
	}

}