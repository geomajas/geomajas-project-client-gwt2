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

package org.geomajas.gwt2.client.gfx;

import org.geomajas.annotation.Api;


/**
 * Style configuration object for shapes.
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public class ShapeStyle {

	private static final int PRIME = 31;

	private String fillColor;

	private float fillOpacity = -1;

	private String strokeColor;

	private float strokeOpacity = -1;

	private int strokeWidth = -1;

	private String dashArray;

	/**
	 * No args constructor for configuration and GWT.
	 */
	public ShapeStyle() {
		// NOSONAR nothing to do
	}

	/**
	 * Copy constructor. Creates a deep copy of the specified {@link ShapeStyle} object.
	 *
	 * @param other the feature style to copy
	 */
	public ShapeStyle(ShapeStyle other) {
		setDashArray(other.getDashArray());
		setFillColor(other.getFillColor());
		setFillOpacity(other.getFillOpacity());
		setStrokeColor(other.getStrokeColor());
		setStrokeOpacity(other.getStrokeOpacity());
		setStrokeWidth(other.getStrokeWidth());
	}

	/**
	 * Applies default values to all properties that have not been set.
	 */
	public void applyDefaults() {
		if (fillColor == null) {
			fillColor = "#ffffff"; // white
		}
		if (strokeColor == null) {
			strokeColor = "#000000"; // black
		}
		if (strokeOpacity == -1) {
			strokeOpacity = 1; // fully opaque by default
		}
		if (fillOpacity == -1) {
			fillOpacity = .5f; // 50% transparent by default
		}
		if (strokeWidth == -1) {
			strokeWidth = 1; // white
		}
	}

	/**
	 * Get fill colour.
	 *
	 * @return fill colour (in "#rrggbb" notation)
	 */
	public String getFillColor() {
		return fillColor;
	}

	/**
	 * Set fill colour.
	 *
	 * @param fillColor fill colour (in "#rrggbb" notation)
	 */
	public void setFillColor(String fillColor) {
		this.fillColor = fillColor;
	}

	/**
	 * Get fill opacity. The value should be in the [0,1] range, where 0 is fully transparent, and 1 is opaque.
	 *
	 * @return opacity for background fill
	 */
	public float getFillOpacity() {
		return fillOpacity;
	}

	/**
	 * Set the fill opacity. The value should be in the [0,1] range, where 0 is fully transparent, and 1 is opaque.
	 *
	 * @param fillOpacity opacity for background fill
	 */
	public void setFillOpacity(float fillOpacity) {
		this.fillOpacity = fillOpacity;
	}

	/**
	 * Get stroke colour.
	 *
	 * @return stroke colour (in "#rrggbb" notation)
	 */
	public String getStrokeColor() {
		return strokeColor;
	}

	/**
	 * Set stroke colour.
	 *
	 * @param strokeColor stroke colour (in "#rrggbb" notation)
	 */
	public void setStrokeColor(String strokeColor) {
		this.strokeColor = strokeColor;
	}

	/**
	 * Get stroke opacity. The value should be in the [0,1] range, where 0 is fully transparent, and 1 is opaque.
	 *
	 * @return opacity for stroke colour
	 */
	public float getStrokeOpacity() {
		return strokeOpacity;
	}

	/**
	 * Set the stroke opacity. The value should be in the [0,1] range, where 0 is fully transparent, and 1 is opaque.
	 *
	 * @param strokeOpacity opacity for the stroke
	 */
	public void setStrokeOpacity(float strokeOpacity) {
		this.strokeOpacity = strokeOpacity;
	}

	/**
	 * Get stroke width.
	 *
	 * @return stroke width
	 */
	public int getStrokeWidth() {
		return strokeWidth;
	}

	/**
	 * Set stroke width.
	 *
	 * @param strokeWidth stroke width
	 */
	public void setStrokeWidth(int strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	/**
	 * Get dash array to apply. When null, the line is solid.
	 *
	 * @return dash array, comma separated list of dash and gap lengths
	 */
	public String getDashArray() {
		return dashArray;
	}

	/**
	 * Set the dash array to apply. When null, the line is solid.
	 *
	 * @param dashArray dash array, comma separated list of dash and gap lengths
	 */
	public void setDashArray(String dashArray) {
		this.dashArray = dashArray;
	}

	@Override
	public String toString() {
		return "VectorObjectStyle { fillColor='" + fillColor + '\'' + ", fillOpacity=" + fillOpacity + ", " +
				"strokeColor='" + strokeColor + '\'' + ", strokeOpacity=" + strokeOpacity + ", " +
				"strokeWidth=" + strokeWidth + ", dashArray='" + dashArray + "\' }";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ShapeStyle)) {
			return false;
		}

		ShapeStyle that = (ShapeStyle) o;
		if (Float.compare(that.fillOpacity, fillOpacity) != 0) {
			return false;
		}
		if (Float.compare(that.strokeOpacity, strokeOpacity) != 0) {
			return false;
		}
		if (strokeWidth != that.strokeWidth) {
			return false;
		}
		if (dashArray != null ? !dashArray.equals(that.dashArray) : that.dashArray != null) {
			return false;
		}
		if (fillColor != null ? !fillColor.equals(that.fillColor) : that.fillColor != null) {
			return false;
		}
		if (strokeColor != null ? !strokeColor.equals(that.strokeColor) : that.strokeColor != null) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = PRIME;
		result = PRIME * result + (fillColor != null ? fillColor.hashCode() : 0);
		result = PRIME * result + (fillOpacity != +0.0f ? (int) (fillOpacity * 10000) : 0);
		result = PRIME * result + (strokeColor != null ? strokeColor.hashCode() : 0);
		result = PRIME * result + (strokeOpacity != +0.0f ? (int) (strokeOpacity * 10000) : 0);
		result = PRIME * result + strokeWidth;
		result = PRIME * result + (dashArray != null ? dashArray.hashCode() : 0);
		return result;
	}
}
