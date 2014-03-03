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

package org.geomajas.plugin.graphicsediting.gwt2.client;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.gwt.client.Editing;
import org.geomajas.plugin.editing.gwt.client.GeometryEditor;

/**
 * Starting point for the graphics editing plugin.
 * From here on you can get {@link StrokeFillCreationValues} singleton class and
 * create a {@link GeometryEditService} that is click-to-stop.
 *
 * @author Jan Venstermans
 * @since 2.0.0
 */
@Api(allMethods = true)
public final class GraphicsEditing {

	private static GraphicsEditing instance;

	private StrokeFillCreationValues strokeFillCreationValues = StrokeFillCreationValues.getInstance();

	/**
	 * Get a singleton instance.
	 *
	 * @return The editing starting point.
	 */
	public static GraphicsEditing getInstance() {
		if (instance == null) {
			instance = new GraphicsEditing();
		}
		return instance;
	}

	/**
	 * Returns the {@link StrokeFillCreationValues} singleton.
	 *
	 * @return The {@link StrokeFillCreationValues} singleton.
	 */
	public StrokeFillCreationValues getStrokeFillCreationValues() {
		return strokeFillCreationValues;
	}

	/**
	 * Create a new {@link org.geomajas.plugin.editing.client.service.GeometryEditService} for the given map,
	 * with the specification of clickToStop being true.
	 *
	 * @param mapPresenter
	 *            The map unto which to render the geometry.
	 * @return The {@link org.geomajas.plugin.editing.client.service.GeometryEditService}.
	 */
	public GeometryEditService createClickToStopEditService(MapPresenter mapPresenter) {
		GeometryEditor editor = Editing.getInstance().createGeometryEditor(mapPresenter);
		editor.getBaseController().setClickToStop(true);
		return editor.getEditService();
	}
}