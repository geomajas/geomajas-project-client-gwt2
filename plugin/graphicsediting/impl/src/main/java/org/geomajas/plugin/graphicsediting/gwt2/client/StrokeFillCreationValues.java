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

/**
 * Singleton class that contains the Stroke and Fill values of polygons and lines upon creation.
 * Class contains getters and setters for each of these parameters.
 *
 * @author Jan Venstermans
 * @since 2.0.0
 */
@Api(allMethods = true)
public final class StrokeFillCreationValues {

	private static StrokeFillCreationValues instance;

	private String polygonCreateFillColor = "#CCFF66";
	private double polygonCreateFillOpacity = 0.3;
	private String polygonCreateStrokeColor = "black";
	private double polygonCreateStrokeOpacity = 1;
	private int polygonCreateStrokeWidth = 1;

	private String lineCreateStrokeColor = "black";
	private double lineCreateStrokeOpacity = 1;
	private int lineCreateStrokeWidth = 1;

	private StrokeFillCreationValues() {

	}

	/**
	 * Get a singleton instance.
	 *
	 * @return The {@link StrokeFillCreationValues} singleton.
	 */
	public static StrokeFillCreationValues getInstance() {
		if (instance == null) {
			instance = new StrokeFillCreationValues();
		}
		return instance;
	}

	/**
	 * Get the value of the fill color a polygon will be instantiated with.
	 * @return  polygonCreateFillColor
	 */
	public String getPolygonCreateFillColor() {
		return polygonCreateFillColor;
	}

	/**
	 * Set the value of the fill color a polygon will be instantiated with.
	 * @param polygonCreateFillColor
	 */
	public void setPolygonCreateFillColor(String polygonCreateFillColor) {
		this.polygonCreateFillColor = polygonCreateFillColor;
	}

	/**
	 * Get the value of the fill opacity a polygon will be instantiated with.
	 * @return polygonCreateFillOpacity
	 */
	public double getPolygonCreateFillOpacity() {
		return polygonCreateFillOpacity;
	}

	/**
	 * Set the value of the fill opacity a polygon will be instantiated with.
	 * @param polygonCreateFillOpacity
	 */
	public void setPolygonCreateFillOpacity(double polygonCreateFillOpacity) {
		this.polygonCreateFillOpacity = polygonCreateFillOpacity;
	}

	/**
	 * Get the value of the stroke color a polygon will be instantiated with.
	 * @return polygonCreateStrokeColor
	 */
	public String getPolygonCreateStrokeColor() {
		return polygonCreateStrokeColor;
	}

	/**
	 * Set the value of the stroke color a polygon will be instantiated with.
	 * @param polygonCreateStrokeColor
	 */
	public void setPolygonCreateStrokeColor(String polygonCreateStrokeColor) {
		this.polygonCreateStrokeColor = polygonCreateStrokeColor;
	}

	/**
	 * Get the value of the stroke opacity a polygon will be instantiated with.
	 * @return  polygonCreateStrokeOpacity
	 */
	public double getPolygonCreateStrokeOpacity() {
		return polygonCreateStrokeOpacity;
	}

	/**
	 * Set the value of the stroke opacity a polygon will be instantiated with.
	 * @param polygonCreateStrokeOpacity
	 */
	public void setPolygonCreateStrokeOpacity(double polygonCreateStrokeOpacity) {
		this.polygonCreateStrokeOpacity = polygonCreateStrokeOpacity;
	}

	/**
	 * Get the value of the stroke width a polygon will be instantiated with.
	 * @return  polygonCreateStrokeWidth
	 */
	public int getPolygonCreateStrokeWidth() {
		return polygonCreateStrokeWidth;
	}

	/**
	 * Set the value of the stroke width a polygon will be instantiated with.
	 * @param polygonCreateStrokeWidth
	 */
	public void setPolygonCreateStrokeWidth(int polygonCreateStrokeWidth) {
		this.polygonCreateStrokeWidth = polygonCreateStrokeWidth;
	}

	/**
	 * Set the value of the stroke color a line will be instantiated with.
	 * @return  lineCreateStrokeColor
	 */
	public String getLineCreateStrokeColor() {
		return lineCreateStrokeColor;
	}

	/**
	 * Get the value of the stroke color a line will be instantiated with.
	 * @param  lineCreateStrokeColor
	 */
	public void setLineCreateStrokeColor(String lineCreateStrokeColor) {
		this.lineCreateStrokeColor = lineCreateStrokeColor;
	}

	/**
	 * Get the value of the stroke opacity a line will be instantiated with.
	 * @return  lineCreateStrokeOpacity
	 */
	public double getLineCreateStrokeOpacity() {
		return lineCreateStrokeOpacity;
	}

	/**
	 * Set the value of the stroke opacity a line will be instantiated with.
	 * @return  lineCreateStrokeOpacity
	 */
	public void setLineCreateStrokeOpacity(double lineCreateStrokeOpacity) {
		this.lineCreateStrokeOpacity = lineCreateStrokeOpacity;
	}

	/**
	 * Get the value of the stroke width a line will be instantiated with.
	 * @return  lineCreateStrokeWidth
	 */
	public int getLineCreateStrokeWidth() {
		return lineCreateStrokeWidth;
	}

	/**
	 * Set the value of the stroke width a line will be instantiated with.
	 * @param lineCreateStrokeWidth
	 */
	public void setLineCreateStrokeWidth(int lineCreateStrokeWidth) {
		this.lineCreateStrokeWidth = lineCreateStrokeWidth;
	}
}