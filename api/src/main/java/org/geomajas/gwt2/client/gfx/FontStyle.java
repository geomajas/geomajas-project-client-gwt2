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
 * Information about how to access and how to render the label attribute.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public class FontStyle {

	/** Default font family used in legend text rendering. */
	public static final String DEFAULT_FONT_FAMILY = "arial";

	/** Default font size used for legend style labels. */
	public static final int DEFAULT_FONT_SIZE = 13;

	/** Default font color used in legend text rendering. */
	public static final String DEFAULT_FONT_COLOR = "0x000000";

	private int size = DEFAULT_FONT_SIZE;

	private String family = DEFAULT_FONT_FAMILY;

	private String weight = "normal";

	private String style = "normal";

	private String color = DEFAULT_FONT_COLOR;

	private float opacity = 1.0f;

	/**
	 * Get the font size.
	 * 
	 * @return font size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Set the font size.
	 * 
	 * @param size
	 *            The new font size.
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * Get the font family.
	 * 
	 * @return font family
	 */
	public String getFamily() {
		return family;
	}

	/**
	 * Set the font family ("Verdana", "Arial", ...).
	 * 
	 * @param family
	 *            The new font family.
	 */
	public void setFamily(String family) {
		this.family = family;
	}

	/**
	 * Get the font weight.
	 * 
	 * @return font weight ("normal", "bold")
	 */
	public String getWeight() {
		return weight;
	}

	/**
	 * Set the font weight ("normal", "bold").
	 * 
	 * @param weight
	 *            The new font weight.
	 */
	public void setWeight(String weight) {
		this.weight = weight;
	}

	/**
	 * Get the font style.
	 * 
	 * @return font style ("normal", "italic", ...)
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * Set the font style ("normal", "italic", ...).
	 * 
	 * @param style
	 *            The new font style.
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * Get the font color.
	 * 
	 * @return font color (as HTML color)
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Set the font color (as HTML color).
	 * 
	 * @param color
	 *            The new color.
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * Get the font opacity.
	 * 
	 * @return font opacity (between 0 and 1)
	 */
	public float getOpacity() {
		return opacity;
	}

	/**
	 * Set the font opacity (between 0 and 1).
	 * 
	 * @param opacity
	 *            The new opacity.
	 */
	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}
}