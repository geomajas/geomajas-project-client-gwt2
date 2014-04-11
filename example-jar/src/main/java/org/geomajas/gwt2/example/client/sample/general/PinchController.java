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
package org.geomajas.gwt2.example.client.sample.general;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.controller.AbstractMapController;
import org.geomajas.gwt2.client.gfx.VectorContainer;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.View;
import org.geomajas.gwt2.client.map.ViewPort;
import org.vaadin.gwtgraphics.client.shape.Circle;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;

/**
 * Emulates pinching by drawing 2 circles for the touches. After that, dragging = pinching.
 * 
 * @author Jan De Moerloose
 * 
 */
public class PinchController extends AbstractMapController {

	private boolean pinching;

	private MapPresenter mapPresenter;

	private Circle touch1;

	private Circle touch2;

	private VectorContainer touchContainer;

	private double lastPinchDistance;

	public PinchController(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
		touchContainer = mapPresenter.getContainerManager().addWorldContainer();
		touch1 = new Circle(0, 0, 5);
		touch1.setFixedSize(true);
		touch2 = new Circle(0, 0, 5);
		touch2.setFixedSize(true);
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		if (!pinching) {
			Coordinate world = getLocation(event, RenderSpace.WORLD);
			touch1.setUserX(world.getX());
			touch1.setUserY(world.getY());
			touch2.setUserX(world.getX());
			touch2.setUserY(world.getY());
			touchContainer.add(touch1);
			touchContainer.add(touch2);
			pinching = true;
		}
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		if (pinching) {
			pinching = false;
			lastPinchDistance = 0;
			touchContainer.remove(touch1);
			touchContainer.remove(touch2);
		}

	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		if (pinching) {
			Coordinate world = getLocation(event, RenderSpace.WORLD);
			touch2.setUserX(world.getX());
			touch2.setUserY(world.getY());
			if (lastPinchDistance != 0) {
				ViewPort viewPort = mapPresenter.getViewPort();
				double pinchDistance = getPinchDistance(RenderSpace.WORLD);
				// calculate the new view
				double newResolution = viewPort.getResolution() * lastPinchDistance / pinchDistance;
				Coordinate pinchMiddle = new Coordinate(0.5 * (touch1.getUserX() + touch2.getUserX()),
						0.5 * (touch1.getUserY() + touch2.getUserY()));
				View view = new View(calculatePosition(newResolution, pinchMiddle), newResolution);
				// and apply
				mapPresenter.getViewPort().applyView(view);
				// pick a minimum distance of 10 pixels to start pinching !!!
			} else if (getPinchDistance(RenderSpace.SCREEN) > 10) {
				lastPinchDistance = Math.hypot(touch2.getUserX() - touch1.getUserX(),
						touch2.getUserY() - touch1.getUserY());
			}
		}
	}

	private double getPinchDistance(RenderSpace space) {
		switch (space) {
			case SCREEN:
				return Math.hypot(touch2.getX() - touch1.getX(), touch2.getY() - touch1.getY());
			case WORLD:
			default:
				return Math.hypot(touch2.getUserX() - touch1.getUserX(), touch2.getUserY() - touch1.getUserY());

		}
	}

	/**
	 * Calculate the target position should there be a rescale point. The idea is that after zooming in or out, the
	 * mouse cursor would still lie at the same position in world space.
	 */
	protected Coordinate calculatePosition(double resolution, Coordinate rescalePoint) {
		ViewPort viewPort = mapPresenter.getViewPort();
		Coordinate position = viewPort.getPosition();
		double factor = viewPort.getResolution() / resolution;
		double dX = (rescalePoint.getX() - position.getX()) * (1 - 1 / factor);
		double dY = (rescalePoint.getY() - position.getY()) * (1 - 1 / factor);
		return new Coordinate(position.getX() + dX, position.getY() + dY);
	}

}
