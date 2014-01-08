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

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.plugin.editing.client.merge.GeometryMergeService;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.split.GeometrySplitService;

/**
 * Starting point for the editing plugin. From here on you can create {@link GeometryEditor}s. A GeometryEditor controls
 * the editing process for a single geometry.
 * 
 * TODO Should the GeometryOperationService be a part of this plugin? Shouldn't it be part of the
 * GeomajasServerExtension?
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public final class Editing {

	private static Editing instance;

	private GeometryMergeService mergeService;

	// private GeometryOperationService operationService;

	/**
	 * Get a singleton instance.
	 * 
	 * @return The editing starting point.
	 */
	public static Editing getInstance() {
		if (instance == null) {
			instance = new Editing();
		}
		return instance;
	}

	/**
	 * Create a new {@link GeometryEditor} for the given map. Use this {@link GeometryEditor} to start the editing
	 * process on a single geometry.
	 * 
	 * @param mapPresenter
	 *            The map unto which to render the geometry.
	 * @return The {@link GeometryEditor}.
	 */
	public GeometryEditor createGeometryEditor(MapPresenter mapPresenter) {
		return new GeometryEditorImpl(mapPresenter);
	}

	/**
	 * Create a new geometry splitting service.
	 * 
	 * @param editService
	 *            The editing service that will be used to create the splitting line.
	 * @return The new instance.
	 */
	public GeometrySplitService createGeometrySplitService(GeometryEditService editService) {
		return new GeometrySplitService(editService);
	}

	/**
	 * Get a singleton instance of the {@link GeometryMergeService}. This is a stand-alone service used for creating
	 * unions of multiple geometries.
	 * 
	 * @return The {@link GeometryMergeService}.
	 */
	public GeometryMergeService getGeometryMergeService() {
		if (mergeService == null) {
			mergeService = new GeometryMergeService();
		}
		return mergeService;
	}

	// /**
	// * Get a singleton instance of the {@link GeometryOperationService}. This is a stand-alone service for complex
	// * geometric operations.
	// *
	// * @return The {@link GeometryOperationService}.
	// */
	// public GeometryOperationService getGeometryOperationService() {
	// if (operationService == null) {
	// operationService = new GeometryOperationServiceImpl();
	// }
	// return operationService;
	// }
}