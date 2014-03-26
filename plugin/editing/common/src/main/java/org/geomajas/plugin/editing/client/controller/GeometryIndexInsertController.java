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

package org.geomajas.plugin.editing.client.controller;

import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.geomajas.plugin.editing.client.service.GeometryIndexService;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.geomajas.plugin.editing.client.snap.SnapService;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;

/**
 * Controller that inserts vertices by clicking/tapping on the map.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
public class GeometryIndexInsertController extends AbstractGeometryIndexController {
	
	private static final Logger LOGGER = Logger.getLogger(GeometryIndexInsertController.class.getName());

	public GeometryIndexInsertController(GeometryEditService service, SnapService snappingService,
			MapEventParser mapEventParser) {
		super(service, snappingService, mapEventParser);
	}

	public void onDown(HumanInputEvent<?> event) {
		if (service.getEditingState() == GeometryEditState.INSERTING && isRightMouseButton(event)) {
			service.setEditingState(GeometryEditState.IDLE);
		}
	}

	public void onUp(HumanInputEvent<?> event) {
		// Only insert when service is in the correct state:
		if (service.getEditingState() == GeometryEditState.INSERTING) {
			try {
				// Insert the location at the given index:
				GeometryIndex insertIndex = service.getInsertIndex();
				Coordinate location = getSnappedLocationWithinMaxBounds(event);
				if (insertIndex == null) {
					// we are starting a new subgeometry but no index passed !!!
					// construct the right index (linestring, point, shell or hole)
					GeometryIndex index = getSubGeometryIndex(location);
					if(index != null) {
						service.addEmptyChildren(index);
						// Make sure we can start adding coordinates into the empty geometry:
						insertIndex = service.getIndexService().addChildren(index, GeometryIndexType.TYPE_VERTEX, 0);
					} else {
						// geometry without subgeometries, just prepare the first vertex
						insertIndex = service.getIndexService().create(GeometryIndexType.TYPE_VERTEX, 0);
					}
					service.setInsertIndex(insertIndex);
				}
				service.insert(Collections.singletonList(insertIndex),
						Collections.singletonList(Collections.singletonList(location)));
				service.setTentativeMoveOrigin(location);

				// Update the insert index (if allowed):
				if (!service.getGeometry().getGeometryType().equals(Geometry.POINT)
						&& !service.getGeometry().getGeometryType().equals(Geometry.MULTI_POINT)) {
					service.setInsertIndex(service.getIndexService().getNextVertex(insertIndex));
				} else {
					// If the case of a point, no more inserting:
					service.setEditingState(GeometryEditState.IDLE);
				}
			} catch (GeometryOperationFailedException e) {
				// ignore, nothing we can do here (validation errors can be shown to the user)
				LOGGER.log(Level.SEVERE, "Can't insert coordinate ", e);
			} catch (GeometryIndexNotFoundException e) {
				// ignore, nothing we can do here (validation errors can be shown to the user)
				LOGGER.log(Level.SEVERE, "Can't insert coordinate ", e);
			}
		}
	}

	public void onMouseMove(MouseMoveEvent event) {
		if (service.getEditingState() == GeometryEditState.INSERTING) {
			Coordinate location = getLocation(event, RenderSpace.WORLD);
			if (snappingEnabled) {
				Coordinate result = snappingService.snap(location);
				if (snappingService.hasSnapped()) {
					service.setTentativeMoveLocation(result);
				} else {
					service.setTentativeMoveLocation(location);
					service.getIndexStateService().snappingEndAll();
				}
			} else {
				service.setTentativeMoveLocation(location);
				service.getIndexStateService().snappingEndAll();
			}
		}
	}

	public void onDoubleClick(DoubleClickEvent event) {
		if (service.getEditingState() == GeometryEditState.INSERTING) {
			service.setEditingState(GeometryEditState.IDLE);
		}
	}

	private GeometryIndex getSubGeometryIndex(Coordinate location) throws GeometryIndexNotFoundException {
		GeometryIndexService indexService = service.getIndexService();
		Geometry geometry = service.getGeometry();
		if (geometry.getGeometryType().equals(Geometry.MULTI_POINT)
				|| geometry.getGeometryType().equals(Geometry.MULTI_LINE_STRING)
				|| geometry.getGeometryType().equals(Geometry.POLYGON)) {
			return indexService.create(GeometryIndexType.TYPE_GEOMETRY, getNextChildindex(geometry));
		} else if (geometry.getGeometryType().equals(Geometry.MULTI_POLYGON)) {
			GeometryIndex index = service.getIndexService().getLinearRingIndex(geometry, location);
			if (index == null) {
				// outside all polygons, return new polygon and shell index
				GeometryIndex geoIndex = indexService.create(GeometryIndexType.TYPE_GEOMETRY,
						getNextChildindex(geometry));
				return indexService.addChildren(geoIndex, GeometryIndexType.TYPE_GEOMETRY, 0);
			} else {
				GeometryIndex polyIndex = indexService.getParent(index);
				// find the polygon
				Geometry polygon = indexService.getGeometry(geometry, polyIndex);
				if (indexService.getValue(index) == 0) {
					// in shell, return hole index
					return indexService.create(GeometryIndexType.TYPE_GEOMETRY, polyIndex.getValue(),
							getNextChildindex(polygon));
				} else {
					// in hole, return new polygon and shell index
					GeometryIndex geoIndex = indexService.create(GeometryIndexType.TYPE_GEOMETRY,
							getNextChildindex(geometry));
					return indexService.addChildren(geoIndex, GeometryIndexType.TYPE_GEOMETRY, 0);
				}
			}
		} else {
			return null;
		}
	}

	private int getNextChildindex(Geometry geom) {
		return geom.getGeometries() == null ? 0 : geom.getGeometries().length;
	}

}