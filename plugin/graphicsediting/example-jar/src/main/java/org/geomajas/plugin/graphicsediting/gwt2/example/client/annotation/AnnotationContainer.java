/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.graphicsediting.gwt2.example.client.annotation;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.graphics.client.object.GRectangle;
import org.geomajas.graphics.client.service.AbstractGraphicsObjectContainer;
import org.geomajas.graphics.client.util.BboxPosition;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.event.ViewPortChangedEvent;
import org.geomajas.gwt2.client.event.ViewPortChangedHandler;
import org.geomajas.gwt2.client.map.MapPresenter;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.web.bindery.event.shared.EventBus;

/**
 * AnnotationContainer.
 * 
 * @author Jan De Moerloose
 */
public class AnnotationContainer extends AbstractGraphicsObjectContainer implements ViewPortChangedHandler {

	private MapPresenter mapPresenter;

	private GRectangle mask;

	public AnnotationContainer(MapPresenter mapPresenter, EventBus eventBus) {
		super(eventBus);
		this.mapPresenter = mapPresenter;
		mapPresenter.getEventBus().addViewPortChangedHandler(this);
		setBackGround(mapPresenter.asWidget());
	}

	@Override
	public void onViewPortChanged(ViewPortChangedEvent event) {
		if (mask != null) {
//			does not work anymore:
//			mask.setUserBounds(event.getViewPort().getBounds());
//			try to get alternative TODO check, this is just some dumy code
			Coordinate from =   event.getFrom().getPosition();
			Coordinate to =   event.getFrom().getPosition();
			Bbox box = new Bbox(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()),
					Math.abs(from.getX() - to.getX()), Math.abs(from.getY() - to.getY()));
			mask.setUserBounds(box);
		}
	}

	@Override
	public Coordinate getScreenCoordinate(MouseEvent<?> event) {
		Element screenElement = mapPresenter.asWidget().getElement();
		return new Coordinate(event.getRelativeX(screenElement), event.getRelativeY(screenElement));
	}

	@Override
	public Coordinate transform(Coordinate coordinate, Space from, Space to) {
		return mapPresenter.getViewPort().getTransformationService().transform(coordinate, convert(from), convert(to));
	}

	@Override
	public Bbox transform(Bbox bounds, Space from, Space to) {
		return mapPresenter.getViewPort().getTransformationService().transform(bounds, convert(from), convert(to));
	}

	private RenderSpace convert(Space space) {
		switch (space) {
			case SCREEN:
				return RenderSpace.SCREEN;
			case USER:
			default:
				return RenderSpace.WORLD;
		}
	}

	@Override
	public BboxPosition transform(BboxPosition position, Space from, Space to) {
		return position;
	}
	
	

}
