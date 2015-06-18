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
package org.geomajas.gwt2.plugin.graphicsediting.example.client.annotation;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.web.bindery.event.shared.EventBus;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.graphics.client.render.BaseRectangle;
import org.geomajas.graphics.client.service.objectcontainer.GraphicsObjectContainerImpl;
import org.geomajas.graphics.client.util.BboxPosition;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.event.ViewPortChangedEvent;
import org.geomajas.gwt2.client.event.ViewPortChangedHandler;
import org.geomajas.gwt2.client.map.MapPresenter;

/**
 * AnnotationContainer.
 * 
 * @author Jan De Moerloose
 */
public class AnnotationContainer extends GraphicsObjectContainerImpl implements ViewPortChangedHandler {

	private MapPresenter mapPresenter;

	private BaseRectangle mask;

	public AnnotationContainer(MapPresenter mapPresenter, EventBus eventBus) {
		super(eventBus);
		this.mapPresenter = mapPresenter;
		mapPresenter.getEventBus().addViewPortChangedHandler(this);
		setBackGround(mapPresenter.asWidget());
	}

	@Override
	public void onViewPortChanged(ViewPortChangedEvent event) {
		if (mask != null) {
			mask.setUserBounds(mapPresenter.getViewPort().getBounds());
		}
	}

	@Override
	public Coordinate getScreenCoordinate(HumanInputEvent<?> event) {
		Element screenElement = mapPresenter.asWidget().getElement();
		//TODO: test
		return new Coordinate(event.getNativeEvent().getScreenX() - screenElement.getAbsoluteLeft(),
				event.getNativeEvent().getScreenY() - screenElement.getAbsoluteTop());
	}

	@Override
	public Coordinate transform(Coordinate coordinate, org.geomajas.graphics.client.render.RenderSpace from,
								org.geomajas.graphics.client.render.RenderSpace to) {
		return mapPresenter.getViewPort().getTransformationService().transform(coordinate, convert(from), convert(to));
	}

	@Override
	public Bbox transform(Bbox bounds, org.geomajas.graphics.client.render.RenderSpace from,
						  org.geomajas.graphics.client.render.RenderSpace to) {
		return mapPresenter.getViewPort().getTransformationService().transform(bounds, convert(from), convert(to));
	}

	private RenderSpace convert(org.geomajas.graphics.client.render.RenderSpace space) {
		switch (space) {
			case SCREEN:
				return RenderSpace.SCREEN;
			case USER:
			default:
				return RenderSpace.WORLD;
		}
	}

	@Override
	public BboxPosition transform(BboxPosition position, org.geomajas.graphics.client.render.RenderSpace from,
								  org.geomajas.graphics.client.render.RenderSpace to) {
		return position;
	}
	
	

}
