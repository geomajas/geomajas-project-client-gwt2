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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.MathService;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.animation.KineticTrajectory;
import org.geomajas.gwt2.client.animation.NavigationAnimationFactory;
import org.geomajas.gwt2.client.animation.NavigationAnimationImpl;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.View;
import org.geomajas.gwt2.client.map.ViewPort;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;

/**
 * Generic navigation map controller. This controller allows for panning and zooming on the map in many different ways.
 * Options are the following:
 * <ul>
 * <li><b>Panning</b>: Drag the map to pan.</li>
 * <li><b>Double click</b>: Double clicking on some location will see the map zoom in to that location.</li>
 * <li><b>Zoom to rectangle</b>: By holding shift or ctrl and dragging at the same time, a rectangle will appear on the
 * map. On mouse up, the map will than zoom to that rectangle.</li>
 * <li></li>
 * </ul>
 * For zooming using the mouse wheel there are 2 options, defined by the {@link ScrollZoomType} enum. These options are
 * the following:
 * <ul>
 * <li><b>ScrollZoomType.ZOOM_CENTER</b>: Zoom in/out so that the current center of the map remains.</li>
 * <li><b>ScrollZoomType.ZOOM_POSITION</b>: Zoom in/out so that the mouse world position remains the same. Can be great
 * for many subsequent double clicks, to make sure you keep zooming to the same location (wherever the mouse points to).
 * </li>
 * </ul>
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public class NavigationController extends AbstractMapController {

	private static Logger logger = Logger.getLogger("MapPresenterImpl");

	/** Zooming types on mouse wheel scroll. */
	public static enum ScrollZoomType {
		/** When scroll zooming, retain the center of the map position. */
		ZOOM_CENTER,

		/** When scroll zooming, retain the mouse position. */
		ZOOM_POSITION
	}

	private final ZoomToRectangleController zoomToRectangleController;

	protected Coordinate dragOrigin;

	protected List<Coordinate> dragPositions = new ArrayList<Coordinate>();

	protected List<Date> dragTimes = new ArrayList<Date>();

	protected Coordinate lastClickPosition;

	protected boolean zooming;

	protected boolean dragging;

	private ScrollZoomType scrollZoomType = ScrollZoomType.ZOOM_POSITION;

	private long lastMillis;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/** Create a NavigationController instance. */
	public NavigationController() {
		super(false);
		zoomToRectangleController = new ZoomToRectangleController();
	}

	// ------------------------------------------------------------------------
	// MapController implementation:
	// ------------------------------------------------------------------------

	@Override
	public void onActivate(MapPresenter mapPresenter) {
		super.onActivate(mapPresenter);
		zoomToRectangleController.onActivate(mapPresenter);
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		super.onMouseDown(event);
		if (event.isControlKeyDown() || event.isShiftKeyDown()) {
			// Trigger the dragging on the zoomToRectangleController:
			zoomToRectangleController.onMouseDown(event);
		}
	}

	@Override
	public void onDown(HumanInputEvent<?> event) {
		if (event.isControlKeyDown() || event.isShiftKeyDown()) {
			zooming = true;
		} else if (!isRightMouseButton(event)) {
			dragging = true;
			dragOrigin = getLocation(event, RenderSpace.SCREEN);
			dragPositions.clear();
			dragTimes.clear();
			mapPresenter.setCursor("move");
		}
		lastClickPosition = getLocation(event, RenderSpace.WORLD);
	}

	@Override
	public void onUp(HumanInputEvent<?> event) {
		if (zooming) {
			logger.info("zooming");
			Coordinate coordinate = getLocation(event, RenderSpace.WORLD);
			if (!coordinate.equals(lastClickPosition)) {
				zoomToRectangleController.onUp(event);
			}
			zooming = false;
		} else if (dragging) {
			stopPanning(event);
		}
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		if (zooming) {
			zoomToRectangleController.onMouseMove(event);
		} else if (dragging) {
			super.onMouseMove(event);
		}
	}

	@Override
	public void onDrag(HumanInputEvent<?> event) {
		dragPositions.add(getLocation(event, RenderSpace.SCREEN));
		Date time = new Date();
		dragTimes.add(time);
		if (time.getTime() - this.dragTimes.get(0).getTime() > 200) {
			this.dragTimes.remove(0);
			this.dragPositions.remove(0);
		}
		updateView(event);
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		if (zooming) {
			zoomToRectangleController.onMouseOut(event);
		} else {
			stopPanning(null);
		}
	}

	@Override
	public void onDoubleClick(DoubleClickEvent event) {
		mapPresenter.getViewPort().registerAnimation(
				NavigationAnimationFactory.createZoomIn(mapPresenter, calculatePosition(true, lastClickPosition)));
	}

	@Override
	public void onMouseWheel(MouseWheelEvent event) {
		final boolean isNorth;
		if (event.getDeltaY() == 0) {
			isNorth = (getWheelDelta(event.getNativeEvent()) < 0);
		} else {
			isNorth = event.isNorth();
		}
		Coordinate location = getLocation(event, RenderSpace.WORLD);
		scrollZoomTo(isNorth, location);
	}

	// Getters and setters:
	// ------------------------------------------------------------------------

	/**
	 * Get the scroll zoom type of this controller.
	 * 
	 * @return the scroll zoom type.
	 */
	public ScrollZoomType getScrollZoomType() {
		return scrollZoomType;
	}

	/**
	 * Set the scroll zoom type of this controller.
	 * 
	 * @param scrollZoomType the scroll zoom type.
	 */
	public void setScrollZoomType(ScrollZoomType scrollZoomType) {
		this.scrollZoomType = scrollZoomType;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	protected void stopPanning(HumanInputEvent<?> event) {
		dragging = false;
		mapPresenter.setCursor("default");
		if (null != event && dragPositions.size() >= 2) {
			logger.info("kinetics started");
			// start kinetic animation
			Coordinate dragStart = toWorld(dragPositions.get(0));
			Coordinate dragStop = toWorld(dragPositions.get(dragPositions.size() - 1));
			long timeStart = dragTimes.get(0).getTime();
			long timeStop = dragTimes.get(dragTimes.size() - 1).getTime();
			int delta = (int) (timeStop - timeStart);
			if (delta > 0) {
				// map moves in the inverse direction !
				Coordinate direction = subtract(dragStart, dragStop);
				double distance = abs(direction);
				Coordinate current = mapPresenter.getViewPort().getPosition();
				double resolution = mapPresenter.getViewPort().getResolution();
				KineticTrajectory trajectory = new KineticTrajectory(new View(current, resolution), direction, distance
						/ delta);
				mapPresenter.getViewPort().registerAnimation(
						new NavigationAnimationImpl(mapPresenter.getViewPort(), mapPresenter.getEventBus(), trajectory,
								(int) trajectory.getDuration()));
			}
		}
	}

	private double abs(Coordinate c) {
		return MathService.distance(c, new Coordinate());
	}

	private Coordinate toWorld(Coordinate coordinate) {
		return mapPresenter.getViewPort().getTransformationService()
				.transform(coordinate, RenderSpace.SCREEN, RenderSpace.WORLD);
	}

	private Coordinate subtract(Coordinate c1, Coordinate c2) {
		return new Coordinate(c1.getX() - c2.getX(), c1.getY() - c2.getY());
	}

	private Coordinate add(Coordinate c1, Coordinate c2) {
		return new Coordinate(c2.getX() + c1.getX(), c2.getY() + c1.getY());
	}

	protected void updateView(HumanInputEvent<?> event) {
		if (dragging) {
			Coordinate end = getLocation(event, RenderSpace.SCREEN);
			Coordinate beginWorld = mapPresenter.getViewPort().getTransformationService()
					.transform(dragOrigin, RenderSpace.SCREEN, RenderSpace.WORLD);
			Coordinate endWorld = mapPresenter.getViewPort().getTransformationService()
					.transform(end, RenderSpace.SCREEN, RenderSpace.WORLD);
			double x = mapPresenter.getViewPort().getPosition().getX() + beginWorld.getX() - endWorld.getX();
			double y = mapPresenter.getViewPort().getPosition().getY() + beginWorld.getY() - endWorld.getY();
			View view = new View(new Coordinate(x, y), mapPresenter.getViewPort().getResolution());
			view.setDragging(true);
			mapPresenter.getViewPort().applyView(view);
			dragOrigin = end;
		}
	}

	protected native int getWheelDelta(NativeEvent evt) /*-{
		return Math.round(-evt.wheelDelta) || 0;
	}-*/;

	protected void scrollZoomTo(boolean isNorth, Coordinate location) {
		ViewPort viewPort = mapPresenter.getViewPort();
		int index = viewPort.getResolutionIndex(viewPort.getResolution());
		if (isNorth) {
			if (index < viewPort.getResolutionCount() - 1) {
				if (scrollZoomType == ScrollZoomType.ZOOM_POSITION) {
					Coordinate position = calculatePosition(true, location);
					viewPort.registerAnimation(NavigationAnimationFactory.createZoomIn(mapPresenter, position));
				} else {
					viewPort.registerAnimation(NavigationAnimationFactory.createZoomIn(mapPresenter));
				}
			}
		} else {
			if (index > 0) {
				if (scrollZoomType == ScrollZoomType.ZOOM_POSITION) {
					Coordinate position = calculatePosition(false, location);
					viewPort.registerAnimation(NavigationAnimationFactory.createZoomOut(mapPresenter, position));
				} else {
					viewPort.registerAnimation(NavigationAnimationFactory.createZoomOut(mapPresenter));
				}
			}
		}
	}

	/**
	 * Calculate the target position should there be a rescale point. The idea is that after zooming in or out, the
	 * mouse cursor would still lie at the same position in world space.
	 */
	protected Coordinate calculatePosition(boolean zoomIn, Coordinate rescalePoint) {
		ViewPort viewPort = mapPresenter.getViewPort();
		Coordinate position = viewPort.getPosition();
		int index = viewPort.getResolutionIndex(viewPort.getResolution());
		double resolution = viewPort.getResolution();
		if (zoomIn && index < viewPort.getResolutionCount() - 1) {
			resolution = viewPort.getResolution(index + 1);

		} else if (!zoomIn && index > 0) {
			resolution = viewPort.getResolution(index - 1);
		}
		double factor = viewPort.getResolution() / resolution;
		double dX = (rescalePoint.getX() - position.getX()) * (1 - 1 / factor);
		double dY = (rescalePoint.getY() - position.getY()) * (1 - 1 / factor);

		return new Coordinate(position.getX() + dX, position.getY() + dY);
	}

}
