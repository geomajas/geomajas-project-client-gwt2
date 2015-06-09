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

package org.geomajas.gwt.client.controller;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.GestureChangeEvent;
import com.google.gwt.event.dom.client.GestureEndEvent;
import com.google.gwt.event.dom.client.GestureStartEvent;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEvent;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchStartEvent;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.event.PointerTouchCancelEvent;
import org.geomajas.gwt.client.event.PointerTouchEndEvent;
import org.geomajas.gwt.client.event.PointerTouchMoveEvent;
import org.geomajas.gwt.client.event.PointerTouchStartEvent;
import org.geomajas.gwt.client.handler.MapDownHandler;
import org.geomajas.gwt.client.handler.MapDragHandler;
import org.geomajas.gwt.client.handler.MapTouchHandler;
import org.geomajas.gwt.client.handler.MapUpHandler;
import org.geomajas.gwt.client.map.RenderSpace;

/**
 * <p> Base implementation of the {@link Controller} interface that tries to align mouse and touch behavior. It does
 * this by providing extra methods through the {@link MapDownHandler}, {@link MapUpHandler} and {@link MapDragHandler}
 * interfaces. When using this class a base (which is recommended), you can chose whether to support mouse events only,
 * touch events only or both simultaneously. </p> <p> In short, here are your three options: <ul> <li>Supporting mouse
 * events only: Override the mouse handler methods (onMouseDown, onMouseUp, ....)</li> <li>Supporting touch events only:
 * Override the touch handler methods (onTouchStart, onTouchMove, ...)</li> <li>Supporting both (recommended): Override
 * the onDown, onUp and onDrag methods. By default both the onMouseDown and the onTouchStart will invoke the onDown
 * method. The same goes for the onUp and onDrag. So by implementing those methods you will have both mobile and desktop
 * support.</li> </ul> </p> <p> One extra note to point out is that by default the touch event methods will stop any
 * further propagation and prevent the default behavior of the events. This is done because, by default, browsers on
 * mobile devices tend to scroll all over the place - creating unwanted behavior. </p>
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public abstract class AbstractController implements Controller, MapDownHandler, MapUpHandler, MapDragHandler,
		MapTouchHandler {

	protected boolean dragging;

	protected MapEventParser eventParser;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Construct controller.
	 *
	 * @param dragging are we dragging?
	 */
	public AbstractController(boolean dragging) {
		this.dragging = dragging;
	}

	/**
	 * Construact controller.
	 *
	 * @param eventParser event parser
	 * @param dragging    are we dragging
	 */
	public AbstractController(MapEventParser eventParser, boolean dragging) {
		this.dragging = dragging;
		this.eventParser = eventParser;
	}

	// ------------------------------------------------------------------------
	// MapEventParser implementation:
	// ------------------------------------------------------------------------

	@Override
	public Coordinate getLocation(HumanInputEvent<?> event, RenderSpace renderSpace) {
		return eventParser.getLocation(event, renderSpace);
	}

	@Override
	public Element getTarget(HumanInputEvent<?> event) {
		return eventParser.getTarget(event);
	}

	protected void setMapEventParser(MapEventParser eventParser) {
		this.eventParser = eventParser;
	}

	@Override
	public boolean isRightMouseButton(HumanInputEvent<?> event) {
		if (event instanceof MouseEvent<?>) {
			return ((MouseEvent<?>) event).getNativeButton() == NativeEvent.BUTTON_RIGHT;
		}
		return false;
	}

	// ------------------------------------------------------------------------
	// Methods for aligning mouse and touch events for dragging :
	// ------------------------------------------------------------------------

	@Override
	public void onDown(HumanInputEvent<?> event) {
	}

	@Override
	public void onUp(HumanInputEvent<?> event) {
	}

	@Override
	public void onDrag(HumanInputEvent<?> event) {
	}

	/**
	 * @todo javadoc unknown.
	 */
	public boolean isDragging() {
		return dragging;
	}

	// ------------------------------------------------------------------------
	// Methods for aligning mouse and touch events (general case) :
	// ------------------------------------------------------------------------

	/**
	 * Forward as mouse down and stop the event.
	 * @since 2.4.0
	 */
	@Override
	public void onMapTouchStart(TouchEvent<?> event) {
		onDown(event);
		event.stopPropagation();
		event.preventDefault();
	}

	/**
	 * Forward as mouse move and stop the event.
	 * @since 2.4.0
	 */
	@Override
	public void onMapTouchMove(TouchEvent<?> event) {
		onDrag(event);
		event.stopPropagation();
		event.preventDefault();
	}

	/**
	 * Forward as mouse up and stop the event.
	 * @since 2.4.0
	 */
	@Override
	public void onMapTouchEnd(TouchEvent<?> event) {
		onUp(event);
		event.stopPropagation();
		event.preventDefault();
	}

	/**
	 * Forward as mouse up and stop the event.
	 * @since 2.4.0
	 */
	@Override
	public void onMapTouchCancel(TouchEvent<?> event) {
		onUp(event);
		event.stopPropagation();
		event.preventDefault();
	}

	// ------------------------------------------------------------------------
	// Mouse Handler implementations:
	// ------------------------------------------------------------------------

	@Override
	public void onMouseDown(MouseDownEvent event) {
		dragging = true;
		onDown(event);
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		dragging = false;
		onUp(event);
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		if (dragging) {
			onDrag(event);
		}
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
	}

	@Override
	public void onMouseWheel(MouseWheelEvent event) {
	}

	@Override
	public void onDoubleClick(DoubleClickEvent event) {
	}

	// ------------------------------------------------------------------------
	// Touch Handler implementations:
	// ------------------------------------------------------------------------

	/**
	 * Don't override this method, override {@link #onMapTouchStart(TouchEvent)} instead.
	 */
	@Override
	public void onTouchStart(TouchStartEvent event) {
		onMapTouchStart(event);
	}

	/**
	 * Don't override this method, override {@link #onMapTouchMove(TouchEvent)} instead.
	 */
	@Override
	public void onTouchMove(TouchMoveEvent event) {
		onMapTouchMove(event);
	}

	/**
	 * Don't override this method, override {@link #onMapTouchEnd(TouchEvent)} instead.
	 */
	@Override
	public void onTouchEnd(TouchEndEvent event) {
		onMapTouchEnd(event);
	}

	/**
	 * Don't override this method, override {@link #onMapTouchCancel(TouchEvent)} instead.
	 */
	@Override
	public void onTouchCancel(TouchCancelEvent event) {
		onMapTouchCancel(event);
	}

	// ------------------------------------------------------------------------
	// Pointer Touch Handler implementations:
	// ------------------------------------------------------------------------

	/**
	 * Don't override this method, override {@link #onMapTouchStart(TouchEvent)} instead.
	 * @since 2.4.0
	 */
	@Override
	public void onPointerTouchStart(PointerTouchStartEvent event) {
		onMapTouchStart(event);
	}

	/**
	 * Don't override this method, override {@link #onMapTouchMove(TouchEvent)} instead.
	 * @since 2.4.0
	 */
	@Override
	public void onPointerTouchMove(PointerTouchMoveEvent event) {
		onMapTouchMove(event);
	}

	/**
	 * Don't override this method, override {@link #onMapTouchEnd(TouchEvent)} instead.
	 * @since 2.4.0
	 */
	@Override
	public void onPointerTouchEnd(PointerTouchEndEvent event) {
		onMapTouchEnd(event);
	}

	/**
	 * Don't override this method, override {@link #onMapTouchCancel(TouchEvent)} instead.
	 * @since 2.4.0
	 */
	@Override
	public void onPointerTouchCancel(PointerTouchCancelEvent event) {
		onMapTouchCancel(event);
	}	

	// ------------------------------------------------------------------------
	// Gesture Handler implementations:
	// ------------------------------------------------------------------------

	@Override
	public void onGestureStart(GestureStartEvent event) {
	}

	@Override
	public void onGestureChange(GestureChangeEvent event) {
	}

	@Override
	public void onGestureEnd(GestureEndEvent event) {
	}
}