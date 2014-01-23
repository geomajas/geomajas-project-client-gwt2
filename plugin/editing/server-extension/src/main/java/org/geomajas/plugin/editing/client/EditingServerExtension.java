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

package org.geomajas.plugin.editing.client;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.editing.client.merge.GeometryMergeService;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.split.GeometrySplitService;

/**
 * Starting point for the server extension of the editing plugin.
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public final class EditingServerExtension {

	private static EditingServerExtension instance;

	private GeometryMergeService mergeService;

	/**
	 * Get a singleton instance.
	 *
	 * @return The editing starting point.
	 */
	public static EditingServerExtension getInstance() {
		if (instance == null) {
			instance = new EditingServerExtension();
		}
		return instance;
	}

	/**
	 * Create a new geometry splitting service.
	 *
	 * @param editService The editing service that will be used to create the splitting line.
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
}