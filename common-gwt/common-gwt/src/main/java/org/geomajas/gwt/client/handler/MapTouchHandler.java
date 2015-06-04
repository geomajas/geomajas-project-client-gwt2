package org.geomajas.gwt.client.handler;

import com.google.gwt.event.dom.client.TouchEvent;

/**
 * Handler for all touch events (normal and pointer-event based).
 * 
 * @author Jan De Moerloose
 */
public interface MapTouchHandler {

	void onMapTouchStart(TouchEvent<?> event);

	void onMapTouchMove(TouchEvent<?> event);

	void onMapTouchEnd(TouchEvent<?> event);

	void onMapTouchCancel(TouchEvent<?> event);
}
