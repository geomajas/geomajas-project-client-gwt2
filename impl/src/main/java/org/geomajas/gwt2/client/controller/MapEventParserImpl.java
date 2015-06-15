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
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.map.MapPresenter;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEvent;

/**
 * Utility methods for acquiring information out of events that come from the map.
 * 
 * @author Pieter De Graef
 */
public class MapEventParserImpl implements MapEventParser {

	private final MapPresenter mapPresenter;

	/**
	 * This object must be initialized with the map it's supposed to interpret the events from.
	 * 
	 * @param mapPresenter The map that is the origin of the events.
	 */
	public MapEventParserImpl(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
	}

	@Override
	public Coordinate getLocation(HumanInputEvent<?> event, RenderSpace renderSpace) {
		switch (renderSpace) {
			case WORLD:
				Coordinate screen = getLocation(event, RenderSpace.SCREEN);
				return mapPresenter.getViewPort().getTransformationService()
						.transform(screen, RenderSpace.SCREEN, RenderSpace.WORLD);
			case SCREEN:
			default:
				Element element = mapPresenter.asWidget().getElement();
				if (event instanceof MouseEvent<?>) {
					double offsetX = ((MouseEvent<?>) event).getRelativeX(element);
					double offsetY = ((MouseEvent<?>) event).getRelativeY(element);
					return new Coordinate(offsetX, offsetY);
				} else if (event instanceof TouchEvent<?>) {
					Touch touch = null;
					if (event instanceof TouchEndEvent) {
						touch = ((TouchEvent<?>) event).getChangedTouches().get(0);
					} else {
						touch = ((TouchEvent<?>) event).getTouches().get(0);
					}
					double offsetX = touch.getRelativeX(element);
					double offsetY = touch.getRelativeY(element);
					return new Coordinate(offsetX, offsetY);
				}
				return new Coordinate(event.getNativeEvent().getClientX(), event.getNativeEvent().getClientY());
		}
	}

	@Override
	public Element getTarget(HumanInputEvent<?> event) {
		EventTarget target = event.getNativeEvent().getEventTarget();
		if (Element.is(target)) {
			return Element.as(target);
		}
		return null;
	}

}