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
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
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
					// we are starting a new linear ring but no index passed !!!
					// construct the right index (shell or hole)
					GeometryIndex index = getShellOrHoleIndex(location);	
					// add the shell or hole
					if(service.getIndexService().getValue(index) > 0) {
						// add hole in polygon
						index = service.addEmptyChild(service.getIndexService().getParent(index));
					} else {
						// add polygon and shell to multipolygon
						index = service.addEmptyChild();
						index = service.addEmptyChild(index);
					}
					// Make sure we can start adding coordinates into the empty LinearRing:
					insertIndex = service.getIndexService().addChildren(index, GeometryIndexType.TYPE_VERTEX, 0);
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

	private GeometryIndex getShellOrHoleIndex(Coordinate location) {
		if (service.getGeometry().getGeometryType().equals(Geometry.POLYGON)) {
			return service.getIndexService().create(GeometryIndexType.TYPE_GEOMETRY,
					service.getGeometry().getGeometries().length);
		} else {
			int[] index = GeometryService.getLinearRingIndex(service.getGeometry(), location);
			Geometry shellGeom = service.getGeometry();
			if (index.length == 2) {
				// find the polygon
				shellGeom = service.getGeometry().getGeometries()[index[0]];
				if (index[1] == 0) {
					// in shell, return hole index
					return service.getIndexService().create(GeometryIndexType.TYPE_GEOMETRY, index[0],
							shellGeom.getGeometries().length);
				} else {
					// in hole, return new polygon and shell index
					GeometryIndex geoIndex = service.getIndexService().create(GeometryIndexType.TYPE_GEOMETRY,
							service.getGeometry().getGeometries().length);
					return service.getIndexService().addChildren(geoIndex, GeometryIndexType.TYPE_GEOMETRY, 0);
				}
			} else {
				// outside all polygons, return new polygon and shell index
				GeometryIndex geoIndex = service.getIndexService().create(GeometryIndexType.TYPE_GEOMETRY,
						service.getGeometry().getGeometries().length);
				return service.getIndexService().addChildren(geoIndex, GeometryIndexType.TYPE_GEOMETRY, 0);
			}

		}
	}

}