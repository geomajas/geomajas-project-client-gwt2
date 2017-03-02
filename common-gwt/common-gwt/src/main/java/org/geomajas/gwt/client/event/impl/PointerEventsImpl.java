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
package org.geomajas.gwt.client.event.impl;

import java.util.logging.Logger;

import org.geomajas.gwt.client.event.PointerEventType;
import org.geomajas.gwt.client.event.PointerEvents;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.impl.DOMImplStandard;

/**
 * Standard implementation of {@link PointerEvents}.
 *
 * @author Jan De Moerloose
 *
 */
public class PointerEventsImpl extends PointerEvents {

	private static Logger logger = Logger.getLogger("PointerEvents");

	private static JavaScriptObject bitlessEventDispatchers;

	private static JavaScriptObject touches;

	//$wnd.navigator.pointerEnabled,  $wnd.navigator.maxTouchPoints >= IE11
	//$wnd.navigator.msPointerEnabled, $wnd.navigator.msMaxTouchPoints	< IE11
	// To test for touch capable hardware and pointer event support
	protected native boolean supports() /*-{
		if ($wnd.navigator.pointerEnabled && ($wnd.navigator.maxTouchPoints !== 0)) {
			return true;
		}
		return false;
	}-*/;

	public String getNativeTypeName(PointerEventType type) {
		switch (type) {
			case POINTER_CANCEL:
				return "pointercancel";
			case POINTER_DOWN:
				return "pointerdown";
			case POINTER_MOVE:
				return "pointermove";
			case POINTER_UP:
				return "pointerup";
		}
		throw new IllegalArgumentException("Could not determine name for " + type);
	}

	public void doInit() {
		if (supports()) {
			logger.info("PointerEventsImpl.doInit");
			if (bitlessEventDispatchers == null) {
				bitlessEventDispatchers = JavaScriptObject.createObject();
				touches = JavaScriptObject.createArray();
				for (PointerEventType e : PointerEventType.values()) {
					addBitlessDispatcher(e.getType(), bitlessEventDispatchers);
				}
				DOMImplStandard.addBitlessEventDispatchers(bitlessEventDispatchers);
				catchAll(PointerEventType.POINTER_CANCEL.getType(), Document.get().getDocumentElement());
				catchAll(PointerEventType.POINTER_UP.getType(), Document.get().getDocumentElement());
			}
		}
	}

	private static void dispatchEvent(Event event) {
		PointerEventType type = PointerEventType.fromType(event.getType());
		switch (type) {
			case POINTER_CANCEL:
				subtractTouch(event, touches);
				break;
			case POINTER_DOWN:
				addTouch(event, touches);
				break;
			case POINTER_MOVE:
				updateTouch(event, touches);
				break;
			case POINTER_UP:
				subtractTouch(event, touches);
				break;
			default:
				break;
		}
		dispatchTouchEvent(event);
	}

	private static void subtractTouchAll(Event event) {
		subtractTouch(event, touches);
	}

	private static native void dispatchTouchEvent(Event e)
	/*-{
		@com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent(Lcom/google/gwt/user/client/Event;)(e);
	}-*/;

	private static native void addTouch(Event e, JavaScriptObject touches)
	/*-{
		var isNew = true;
		for (var i = 0; i < touches.length; i++) {
			if (touches[i].pointerId === e.pointerId) {
				isNew = false;
				break;
			}
		}
		if (isNew) {
			touches.push(e);
		}

		e.touches = touches.slice();
		e.changedTouches = [ e ];
	}-*/;

	private static native void updateTouch(Event e, JavaScriptObject touches)
	/*-{
		for (var i = 0; i < touches.length; i++) {
			if (touches[i].pointerId === e.pointerId) {
				touches[i] = e;
				break;
			}
		}

		e.touches = touches.slice();
		e.changedTouches = [ e ];
	}-*/;

	private static native void subtractTouch(Event e, JavaScriptObject touches)
	/*-{
		for (var i = 0; i < touches.length; i++) {
			if (touches[i].pointerId === e.pointerId) {
				touches.splice(i, 1);
				break;
			}
		}

		e.touches = touches.slice();
		e.changedTouches = [ e ];
	}-*/;

	private static native void addBitlessDispatcher(String type, JavaScriptObject map)
	/*-{
		map[type] = @org.geomajas.gwt.client.event.impl.PointerEventsImpl::dispatchEvent(*);
	}-*/;

//CHECKSTYLE: OFF
	private static native void catchAll(String type, Element element)
	/*-{
		element.addEventListener(type, @org.geomajas.gwt.client.event.impl.PointerEventsImpl::subtractTouchAll(*), false);
	}-*/;
//CHECKSTYLE: ON
}
