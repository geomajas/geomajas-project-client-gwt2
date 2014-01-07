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

package org.geomajas.plugin.editing.gwt.client;

import org.geomajas.gwt2.client.controller.MapController;
import org.geomajas.gwt2.client.event.ViewPortChangedEvent;
import org.geomajas.gwt2.client.event.ViewPortChangedHandler;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.plugin.editing.client.event.GeometryEditStartEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStartHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditServiceImpl;
import org.geomajas.plugin.editing.client.snap.SnapService;
import org.geomajas.plugin.editing.gwt.client.controller.EditGeometryBaseController;
import org.geomajas.plugin.editing.gwt.client.gfx.GeometryRendererImpl;

/**
 * Central editor for geometries on the map.
 * 
 * @author Pieter De Graef
 */
public class GeometryEditorImpl implements GeometryEditor, GeometryEditStartHandler, GeometryEditStopHandler {

	private final MapPresenter mapPresenter;

	private final GeometryRendererImpl renderer;

	private final GeometryEditService editService;

	private final SnapService snappingService;

	private EditGeometryBaseController baseController;

	private MapController previousController;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	protected GeometryEditorImpl(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;

		// Initialize the editing service:
		editService = new GeometryEditServiceImpl();
		editService.addGeometryEditStartHandler(this);
		editService.addGeometryEditStopHandler(this);

		// Initialize the rest:
		snappingService = new SnapService();
		baseController = new EditGeometryBaseController(editService, snappingService);
		renderer = new GeometryRendererImpl(mapPresenter, editService);

		mapPresenter.getEventBus().addViewPortChangedHandler(new ViewPortChangedHandler() {

			public void onViewPortChanged(ViewPortChangedEvent event) {
				editService.getIndexStateService().highlightEndAll();
				snappingService.update(GeometryEditorImpl.this.mapPresenter.getViewPort().getBounds());
			}
		});
	}

	// ------------------------------------------------------------------------
	// GeometryEditStartHandler implementation:
	// ------------------------------------------------------------------------

	public void onGeometryEditStart(GeometryEditStartEvent event) {
		// Store the current controller:
		previousController = mapPresenter.getMapController();
		mapPresenter.setMapController(baseController);
	}

	// ------------------------------------------------------------------------
	// GeometryEditStopHandler implementation:
	// ------------------------------------------------------------------------

	public void onGeometryEditStop(GeometryEditStopEvent event) {
		// Restore the original map controller:
		mapPresenter.setMapController(previousController);
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	public MapPresenter getMapPresenter() {
		return mapPresenter;
	}

	public GeometryRendererImpl getRenderer() {
		return renderer;
	}

	public GeometryEditService getEditService() {
		return editService;
	}

	public SnapService getSnappingService() {
		return snappingService;
	}

	public boolean isBusyEditing() {
		return false;
	}

	public boolean isSnapOnDrag() {
		return baseController.getDragController().isSnappingEnabled();
	}

	public void setSnapOnDrag(boolean b) {
		baseController.getDragController().setSnappingEnabled(b);
	}

	public void setSnapOnInsert(boolean b) {
		baseController.getInsertController().setSnappingEnabled(b);
	}

	public EditGeometryBaseController getBaseController() {
		return baseController;
	}

	public void setBaseController(EditGeometryBaseController baseController) {
		this.baseController = baseController;
	}
	//
	// @Override
	// public void addVertexHandlerFactory(VertexMapHandlerFactory factory) {
	// renderer.addVertexHandlerFactory(factory);
	// }
	//
	// @Override
	// public void addEdgeHandlerFactory(EdgeMapHandlerFactory factory) {
	// renderer.addEdgeHandlerFactory(factory);
	// }
}