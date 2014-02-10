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

import com.google.web.bindery.event.shared.HandlerRegistration;
import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.role.Strokable;
import org.geomajas.graphics.client.operation.AddOperation;
import org.geomajas.graphics.client.service.AbstractGraphicsController;
import org.geomajas.graphics.client.service.GraphicsService;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.geomajas.plugin.graphicsediting.gwt2.client.GraphicsEditing;
import org.geomajas.plugin.graphicsediting.gwt2.client.StrokeFillCreationValues;
import org.geomajas.plugin.graphicsediting.gwt2.client.object.GGeometryPath;
import org.vaadin.gwtgraphics.client.VectorObjectContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for Polygon objects.
 * 
 * @author Jan De Moerloose
 * @since 2.0.0
 * 
 */
@Api(allMethods = true)
public class CreateLineController extends AbstractGraphicsController implements GeometryEditStopHandler {

	private boolean active;

	private List<HandlerRegistration> registrations = new ArrayList<HandlerRegistration>();

	private GraphicsObject path;

	private VectorObjectContainer container;

	private final MapPresenter mapPresenter;

	private GeometryEditService editService;

	/**
	 * Default constructor for{@link CreateLineController} .
	 * @param graphicsService
	 * @param mapPresenter
	 */
	public CreateLineController(GraphicsService graphicsService, MapPresenter mapPresenter) {
		super(graphicsService);
		this.mapPresenter = mapPresenter;
		container = createContainer();
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
		if (active) {
			container = createContainer();
			startEditing();
		} else {
			for (HandlerRegistration r : registrations) {
				r.removeHandler();
			}
			registrations.clear();
			path = null;
			if (container != null) {
				removeContainer(container);
			}
			if (editService != null) {
				editService.stop();
			}
			container = null;
		}
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void destroy() {
	}

	private void startEditing() {
		if (path == null) {
			
			Geometry line = new Geometry(Geometry.LINE_STRING, 0, -1);
			if (editService == null) {
				editService = GraphicsEditing.getInstance().createClickToStopEditService(mapPresenter);
				editService.addGeometryEditStopHandler(this);
			}
			editService.start(line);
			
			GeometryIndex index = editService.getIndexService().create(GeometryIndexType.TYPE_VERTEX, 0);
			editService.setEditingState(GeometryEditState.INSERTING);
			editService.setInsertIndex(index);
		}
	}

	@Override
	public void onGeometryEditStop(GeometryEditStopEvent event) {
		if (isActive()) {
			try {
				path = createObject(event.getGeometry());
				execute(new AddOperation(path));
			} catch (Exception e) {
				// do nothing
			}
			path = null;
		}
	}

	protected GraphicsObject createObject(Geometry geometry) {
		GGeometryPath path = new GGeometryPath(geometry, null);
		StrokeFillCreationValues creationValues = GraphicsEditing.getInstance().getStrokeFillCreationValues();
		path.getRole(Strokable.TYPE).setStrokeColor(creationValues.getLineCreateStrokeColor());
		path.getRole(Strokable.TYPE).setStrokeOpacity(creationValues.getLineCreateStrokeOpacity());
		path.getRole(Strokable.TYPE).setStrokeWidth(creationValues.getLineCreateStrokeWidth());
		return path;
	}
}
