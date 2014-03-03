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
package org.geomajas.plugin.graphicsediting.gwt2.client.controller;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.graphics.client.controller.VisibleOnActiveGraphicsController;
import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent;
import org.geomajas.graphics.client.object.Draggable;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.Resizable;
import org.geomajas.graphics.client.service.AbstractGraphicsController;
import org.geomajas.graphics.client.service.GraphicsObjectContainer.Space;
import org.geomajas.graphics.client.service.GraphicsService;
import org.geomajas.graphics.client.shape.AnchoredImage;
import org.geomajas.graphics.client.util.BboxPosition;
import org.geomajas.graphics.client.util.GraphicsUtil;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.plugin.editing.client.event.GeometryEditChangeStateEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditChangeStateHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.graphicsediting.gwt2.client.GraphicsEditing;
import org.geomajas.plugin.graphicsediting.gwt2.client.object.GeometryEditable;
import org.geomajas.plugin.graphicsediting.gwt2.client.operation.GeometryEditOperation;
import org.vaadin.gwtgraphics.client.VectorObjectContainer;

/**
 * Controller for {@link GeometryEditable} objects. The controller will show a pencil icon in the upper left
 * corner when the object is selected.
 * 
 * @author Jan De Moerloose
 * @since 2.0.0
 * 
 */
@Api(allMethods = true)
public class GeometryEditController extends AbstractGraphicsController implements GeometryEditChangeStateHandler,
		GeometryEditStopHandler, GraphicsObjectContainerEvent.Handler, VisibleOnActiveGraphicsController {

	/**
	 * Default value of indentation of pencil button from the object bound.
	 */
	public static final int IMG_DIST = 16;

	/**
	 * Is controller active (listening to mouse events) ?
	 */
	private boolean active;

	private final GeometryEditable object;

	private GeometryEditService service;

	private final MapPresenter mapPresenter;

	/**
	 * Our own container.
	 */
	private VectorObjectContainer container;

	private EditHandler handler;

	// values that determines the offset of the pencil image from the left hand side of the resizable it is linked to.
	private double offsetX;

	private double offsetY;

	/**
	 * Default constructor of {@link GeometryEditController}.
	 * @param object
	 * @param graphicsService
	 * @param mapPresenter
	 * @param offsetX the x value of the offset of the pencil image
	 * @param offsetY the y value of the offset of the pencil image
	 */
	public GeometryEditController(GraphicsObject object, GraphicsService graphicsService,
								  MapPresenter mapPresenter, double offsetX, double offsetY) {
		super(graphicsService, object);
		this.mapPresenter = mapPresenter;
		this.object = object.getRole(GeometryEditable.TYPE);
		container = createContainer();
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		getObjectContainer().addGraphicsObjectContainerHandler(this);
	}

	@Override
	public void setActive(boolean active) {
		if (active != this.active) {
			this.active = active;
			if (active) {
				if (handler == null) {
					// create and (implicitly) activate the handler group
					init();
				} else {
					// the group may be detached, update and reattach !
					handler.update();
					handler.add(container);
				}
			} else {
				// just remove the handler
				if (handler != null) {
					handler.remove(container);
				}
			}
		}
	}

	private void init() {
		// create the handler and attach it
		handler = new EditHandler();
		handler.update();
		// add the handler
		handler.add(container);
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void onAction(GraphicsObjectContainerEvent event) {
		if (handler != null) {
			handler.update();
		}
	}

	private void startEditing() {
		if (service == null) {
			service = GraphicsEditing.getInstance().createClickToStopEditService(mapPresenter);
			service.addGeometryEditChangeStateHandler(this);
			service.addGeometryEditStopHandler(this);
		}
		getService().getMetaController().setActive(false);
		service.start(object.getGeometry());
	}

	@Override
	public void onGeometryEditStop(GeometryEditStopEvent event) {
		Geometry start = object.getGeometry();
		getService().execute(new GeometryEditOperation(getObject(), start, event.getGeometry()));
	}

	@Override
	public void onChangeEditingState(GeometryEditChangeStateEvent event) {
		// do nothing
	}

	@Override
	public void destroy() {
	}

	@Override
	public void setControllerElementsVisible(boolean visible) {
		// toggle visibility of the pencil image
		handler.setVisible(visible);
	}

	/**
	 *  Handler for the pencil icon that is shown on selection of the object.
	 */
	private class EditHandler implements MouseUpHandler {

		private AnchoredImage propertyImage;

		public EditHandler() {
			propertyImage = new AnchoredImage(0, 0, 16, 16, GWT.getModuleBaseURL() + "image/pencil16.png",
					offsetX, offsetY);
			propertyImage.setFixedSize(true);
			propertyImage.addMouseUpHandler(this);
		}

		public void update() {
			if (getObject().hasRole(Resizable.TYPE)) {
				BboxPosition bboxPos = transform(BboxPosition.CORNER_UL, Space.SCREEN, Space.USER);
				Coordinate pos = transform(new Coordinate(IMG_DIST, IMG_DIST), Space.SCREEN, Space.USER);
				if (getObject().hasRole(Resizable.TYPE)) {
					pos = GraphicsUtil.getPosition(getObject().getRole(Resizable.TYPE).getUserBounds(), bboxPos);
				} else if (getObject().hasRole(Draggable.TYPE)) {
					pos = GraphicsUtil.getPosition(getObject().getRole(Draggable.TYPE).getUserBounds(), bboxPos);
				}
				propertyImage.setUserX(pos.getX());
				propertyImage.setUserY(pos.getY());
			} else if (getObject().hasRole(Draggable.TYPE)) {
				Coordinate position = getObject().getRole(Draggable.TYPE).getPosition();
				propertyImage.setUserX(position.getX());
				propertyImage.setUserY(position.getY());
			}
		}

		public void remove(VectorObjectContainer container) {
			container.remove(propertyImage);
		}

		public void add(VectorObjectContainer container) {
			container.add(propertyImage);
		}

		@Override
		public void onMouseUp(MouseUpEvent event) {
			if (isActive()) {
				startEditing();
			}
			event.stopPropagation();
		}

		public void setVisible(boolean visible) {
			propertyImage.setVisible(visible);
		}

	}

}
