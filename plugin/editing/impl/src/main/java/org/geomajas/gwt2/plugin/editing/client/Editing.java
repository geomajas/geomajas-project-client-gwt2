/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.plugin.editing.client;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.MapPresenter;

/**
 * Starting point for the editing plugin. From here on you can create {@link GeometryEditor}s. A GeometryEditor controls
 * the editing process for a single geometry.
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public final class Editing {

	private static Editing instance;

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
	 * @param mapPresenter The map unto which to render the geometry.
	 * @return The {@link GeometryEditor}.
	 */
	public GeometryEditor createGeometryEditor(MapPresenter mapPresenter) {
		return new GeometryEditorImpl(mapPresenter);
	}
}