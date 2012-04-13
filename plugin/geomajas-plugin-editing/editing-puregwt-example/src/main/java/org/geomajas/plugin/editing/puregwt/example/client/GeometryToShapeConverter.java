/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.editing.puregwt.example.client;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.puregwt.client.gfx.GfxUtil;
import org.geomajas.puregwt.client.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.shape.Circle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

/**
 * Responsible for the conversion of {@link Geometry} to a {@link Shape} on the map.
 * 
 * @author Emiel Ackermann
 *
 */
public class GeometryToShapeConverter {
	
	private static final ShowCaseGinjector INJECTOR = GWT.create(ShowCaseGinjector.class);
	
	private GfxUtil gfxUtil;

	private final VectorContainer shapeContainer;
	
	private final List<Geometry> geometries = new ArrayList<Geometry>();
	
	private final GeometryEditService editService;

	private final MapPresenter mapPresenter;

	public GeometryToShapeConverter(GeometryEditService editService, MapPresenter mapPresenter) {
		this.editService = editService;
		this.mapPresenter = mapPresenter;
		gfxUtil = INJECTOR.getGfxUtil();
		shapeContainer = mapPresenter.addWorldContainer();
	}

	/**
	 * Process the currently edited geometry into a {@link Shape} and place it into a {@link VectorContainer}.
	 */
	public void processCurrentGeometry() {
		Geometry geometry = null;
		if (editService.isStarted()) {
			geometry = editService.stop();
		}
		processGeometry(geometry);
	}
	
	/**
	 * Process the given geometry into a {@link Shape} and place it into a {@link VectorContainer}.
	 * @param geometry
	 */
	public void processGeometry(Geometry geometry) {
		if (null != geometry) {
			getGeometries().add(geometry);
			Shape shape = null;
			if (Geometry.POINT.equals(geometry.getGeometryType())) {
				Coordinate[] coordinates = geometry.getCoordinates();
				shape = new Circle(coordinates[0].getX(), coordinates[0].getY(), 5);
			} else {
				shape = gfxUtil.toPath(geometry);
			}
			if (null != shape) {
				shape.setStrokeWidth(3);
				shape.setFillOpacity(0);
				shape.addClickHandler(new SelectShapeHandler(geometry, shape));
				shape.addMouseOverHandler(new SelectShapeHandler(geometry, shape));
				shape.addMouseOutHandler(new SelectShapeHandler(geometry, shape));
				if (shape instanceof Circle) {
					shape.setFixedSize(true);
				}
				getShapeContainer().add(shape);
			}
		}
	}
	
	public List<Geometry> getGeometries() {
		return geometries;
	}

	public VectorContainer getShapeContainer() {
		return shapeContainer;
	}

	/**
	 * Handler for converting a {@link Shape} back to a editable {@link Geometry}. 
	 * Also provides the {@link MouseOverHandler} and {@link MouseOutHandler} styles. 
	 * 
	 * @author Emiel Ackermann
	 *
	 */
	class SelectShapeHandler implements ClickHandler, MouseOverHandler, MouseOutHandler {

		private final Geometry geometry;
		private final Shape shape;

		public SelectShapeHandler(Geometry geometry, Shape shape) {
			this.geometry = geometry;
			this.shape = shape;
		}
		
		public void onClick(ClickEvent event) {
			if (editService.getEditingState() == GeometryEditState.IDLE) {
				processCurrentGeometry();
				getShapeContainer().remove(shape);
				getGeometries().remove(geometry);
				editService.start(geometry);			
			}
		}

		public void onMouseOver(MouseOverEvent event) {
			if (editService.getEditingState() == GeometryEditState.IDLE) {
				shape.setStrokeWidth(4);
				shape.setStrokeColor("#F00");
				if (Geometry.POLYGON.equals(geometry.getGeometryType()) || 
						Geometry.MULTI_POLYGON.equals(geometry.getGeometryType())) {
					shape.setFillColor("#F00");
					shape.setFillOpacity(0.3);
				}
			}
		}
		
		public void onMouseOut(MouseOutEvent event) {
			if (editService.getEditingState() == GeometryEditState.IDLE) {
				shape.setStrokeWidth(3);
				shape.setStrokeColor("#000");
				shape.setFillOpacity(0);
			}
		}
	}

	public void clear() {
		shapeContainer.clear();
		geometries.clear();
	}

	public GeometryEditService getEditService() {
		return editService;
	}

}