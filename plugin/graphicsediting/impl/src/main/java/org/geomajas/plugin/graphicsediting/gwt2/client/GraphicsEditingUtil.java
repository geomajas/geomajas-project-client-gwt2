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

package org.geomajas.plugin.graphicsediting.gwt2.client;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.gwt.client.Editing;
import org.geomajas.plugin.editing.gwt.client.GeometryEditor;

/**
 * Starting point for the Graphics Editing plugin. From here on you can create
 * {@link org.geomajas.plugin.editing.gwt.client.GeometryEditor}s. A GeometryEditor controls
 * the editing process for a single geometry.
 *
 * @author Jan Venstermans
 * @since 2.0.0
 */
@Api(allMethods = true)
public final class GraphicsEditingUtil {

	private static String polygonCreateFillColor = "#CCFF66";
	private static double polygonCreateFillOpacity = 0.3;
	private static String polygonCreateStrokeColor = "black";
	private static double polygonCreateStrokeOpacity = 1;
	private static int polygonCreateStrokeWidth = 1;

	private static String lineCreateStrokeColor = "black";
	private static double lineCreateStrokeOpacity = 1;
	private static int lineCreateStrokeWidth = 1;

	private GraphicsEditingUtil() {

	}

	/**
	 * Create a new {@link org.geomajas.plugin.editing.client.service.GeometryEditService} for the given map,
	 * with the specification of clickToStop being true.
	 * TODO could this be part of the Editing plugin? It is used a lot in Graphics Editing classes.
	 *
	 * @param mapPresenter
	 *            The map unto which to render the geometry.
	 * @return The {@link org.geomajas.plugin.editing.client.service.GeometryEditService}.
	 */
	public static GeometryEditService createClickToStopEditService(MapPresenter mapPresenter) {
		GeometryEditor editor = Editing.getInstance().createGeometryEditor(mapPresenter);
		editor.getBaseController().setClickToStop(true);
		return editor.getEditService();
	}

	/**
	 * Get the value of the fill color a polygon will be instantiated with.
	 * @return  polygonCreateFillColor
	 */
	public static String getPolygonCreateFillColor() {
		return polygonCreateFillColor;
	}

	/**
	 * Set the value of the fill color a polygon will be instantiated with.
	 * @param polygonCreateFillColor
	 */
	public static void setPolygonCreateFillColor(String polygonCreateFillColor) {
		GraphicsEditingUtil.polygonCreateFillColor = polygonCreateFillColor;
	}

	/**
	 * Get the value of the fill opacity a polygon will be instantiated with.
	 * @return polygonCreateFillOpacity
	 */
	public static double getPolygonCreateFillOpacity() {
		return polygonCreateFillOpacity;
	}

	/**
	 * Set the value of the fill opacity a polygon will be instantiated with.
	 * @param polygonCreateFillOpacity
	 */
	public static void setPolygonCreateFillOpacity(double polygonCreateFillOpacity) {
		GraphicsEditingUtil.polygonCreateFillOpacity = polygonCreateFillOpacity;
	}

	/**
	 * Get the value of the stroke color a polygon will be instantiated with.
	 * @return polygonCreateStrokeColor
	 */
	public static String getPolygonCreateStrokeColor() {
		return polygonCreateStrokeColor;
	}

	/**
	 * Set the value of the stroke color a polygon will be instantiated with.
	 * @param polygonCreateStrokeColor
	 */
	public static void setPolygonCreateStrokeColor(String polygonCreateStrokeColor) {
		GraphicsEditingUtil.polygonCreateStrokeColor = polygonCreateStrokeColor;
	}

	/**
	 * Get the value of the stroke opacity a polygon will be instantiated with.
	 * @return  polygonCreateStrokeOpacity
	 */
	public static double getPolygonCreateStrokeOpacity() {
		return polygonCreateStrokeOpacity;
	}

	/**
	 * Set the value of the stroke opacity a polygon will be instantiated with.
	 * @param polygonCreateStrokeOpacity
	 */
	public static void setPolygonCreateStrokeOpacity(double polygonCreateStrokeOpacity) {
		GraphicsEditingUtil.polygonCreateStrokeOpacity = polygonCreateStrokeOpacity;
	}

	/**
	 * Get the value of the stroke width a polygon will be instantiated with.
	 * @return  polygonCreateStrokeWidth
	 */
	public static int getPolygonCreateStrokeWidth() {
		return polygonCreateStrokeWidth;
	}

	/**
	 * Set the value of the stroke width a polygon will be instantiated with.
	 * @param polygonCreateStrokeWidth
	 */
	public static void setPolygonCreateStrokeWidth(int polygonCreateStrokeWidth) {
		GraphicsEditingUtil.polygonCreateStrokeWidth = polygonCreateStrokeWidth;
	}

	/**
	 * Set the value of the stroke color a line will be instantiated with.
	 * @return  lineCreateStrokeColor
	 */
	public static String getLineCreateStrokeColor() {
		return lineCreateStrokeColor;
	}

	/**
	 * Get the value of the stroke color a line will be instantiated with.
	 * @param  lineCreateStrokeColor
	 */
	public static void setLineCreateStrokeColor(String lineCreateStrokeColor) {
		GraphicsEditingUtil.lineCreateStrokeColor = lineCreateStrokeColor;
	}

	/**
	 * Get the value of the stroke opacity a line will be instantiated with.
	 * @return  lineCreateStrokeOpacity
	 */
	public static double getLineCreateStrokeOpacity() {
		return lineCreateStrokeOpacity;
	}

	/**
	 * Set the value of the stroke opacity a line will be instantiated with.
	 * @return  lineCreateStrokeOpacity
	 */
	public static void setLineCreateStrokeOpacity(double lineCreateStrokeOpacity) {
		GraphicsEditingUtil.lineCreateStrokeOpacity = lineCreateStrokeOpacity;
	}

	/**
	 * Get the value of the stroke width a line will be instantiated with.
	 * @return  lineCreateStrokeWidth
	 */
	public static int getLineCreateStrokeWidth() {
		return lineCreateStrokeWidth;
	}

	/**
	 * Set the value of the stroke width a line will be instantiated with.
	 * @param lineCreateStrokeWidth
	 */
	public static void setLineCreateStrokeWidth(int lineCreateStrokeWidth) {
		GraphicsEditingUtil.lineCreateStrokeWidth = lineCreateStrokeWidth;
	}
}