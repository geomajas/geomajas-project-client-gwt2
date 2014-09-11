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

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.snap.SnapService;

import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller that inserts vertices by clicking/tapping on the map.
 * 
 * @author Pieter De Graef
 */
public class GeometryIndexInsertController extends AbstractGeometryIndexController {
	
	private static final int MIN_DOUBLECLICK_DISTANCE = 5;

	private static Logger logger = Logger.getLogger(GeometryIndexInsertController.class.getName());
	
	private Coordinate lastClickedPosition;

	public GeometryIndexInsertController(GeometryEditService service, SnapService snappingService,
			MapEventParser mapEventParser) {
		super(service, snappingService, mapEventParser);
	}

	public void onDown(HumanInputEvent<?> event) {
		if (service.getEditingState() == GeometryEditState.INSERTING && isRightMouseButton(event)) {
			stopInserting();
		}
	}

	public void onUp(HumanInputEvent<?> event) {
		// Only insert when service is in the correct state:
		if (service.getEditingState() == GeometryEditState.INSERTING) {
			try {
				if (isDoubleClick(event)) {
					stopInserting();					
				} else {
					// Insert the location at the given index:
					GeometryIndex insertIndex = service.getInsertIndex();
					Coordinate location = getSnappedLocationWithinMaxBounds(event);
					service.insert(Collections.singletonList(insertIndex),
							Collections.singletonList(Collections.singletonList(location)));

					String geometryType = service.getGeometry().getGeometryType();
					if (geometryType.equals(Geometry.POINT) || geometryType.equals(Geometry.MULTI_POINT)) {
						stopInserting();
					}
				}
			} catch (GeometryOperationFailedException e) {
				logger.log(Level.WARNING, "Operation failed", e);
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
			stopInserting();
		}
	}

	private void stopInserting() {
		try {
			service.finish(service.getInsertIndex());
		} catch (GeometryOperationFailedException e) {
			logger.log(Level.WARNING, "Stop inserting failed", e);
		}					
		lastClickedPosition = null;
	}

	private boolean isDoubleClick(HumanInputEvent<?> event) {
		Coordinate clickPosition = getLocation(event, RenderSpace.SCREEN);
		boolean doubleClicked = false;
		if (lastClickedPosition != null) {
			int distance = (int) Math.hypot(clickPosition.getX() - lastClickedPosition.getX(), clickPosition.getY()
					- lastClickedPosition.getY());
			if (distance < MIN_DOUBLECLICK_DISTANCE) {
				doubleClicked = true;
			} 
		} 
		if (doubleClicked) {
			lastClickedPosition = null;
			return true;
		} else {
			lastClickedPosition = clickPosition;
			return false;
		}
	}
}