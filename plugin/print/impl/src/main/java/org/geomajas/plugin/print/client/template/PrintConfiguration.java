package org.geomajas.plugin.print.client.template;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.gwt2.client.map.Hint;

public class PrintConfiguration {

	private Map<Hint<?>, Object> hintValues = new HashMap<Hint<?>, Object>();

	/** Page size for the default template. */
	public static final Hint<PageSize> TEMPLATE_PAGE_SIZE = new Hint<PageSize>("templatePageSize", PageSize.A4);

	/** X margin for the default template. */
	public static final Hint<Double> TEMPLATE_MARGIN_X = new Hint<Double>("templateMarginX", 20D);

	/** X margin for the default template. */
	public static final Hint<Double> TEMPLATE_MARGIN_Y = new Hint<Double>("templateMarginY", 20D);

	/** X margin for the default template. */
	public static final Hint<Double> TEMPLATE_NORTH_ARROW_WIDTH = new Hint<Double>("templateNorthArrowWidth", 10D);

	/** Font family for the legend in the template. */
	public static final Hint<String> TEMPLATE_DEFAULT_FONT_FAMILY = new Hint<String>("templateDefaultFontFamily",
			"Arial");

	/** Font style for the legend in the template. */
	public static final Hint<String> TEMPLATE_DEFAULT_FONT_STYLE = new Hint<String>("templateDefaultFontStyle",
			"Italic");

	/** Font size for the legend in the template. */
	public static final Hint<Double> TEMPLATE_DEFAULT_FONT_SIZE = new Hint<Double>("templateDefaultFontSize", 14D);

	/** Background colour for the legend in the template. */
	public static final Hint<String> TEMPLATE_DEFAULT_BACKGROUND_COLOR = new Hint<String>(
			"templateDefaultBackgroundColor", "0xFFFFFF");

	/** Border colour for the legend in the template. */
	public static final Hint<String> TEMPLATE_DEFAULT_BORDER_COLOR = new Hint<String>("templateDefaultBorderColor",
			"0x000000");

	/** Font colour for the legend in the template. */
	public static final Hint<String> TEMPLATE_DEFAULT_COLOR = new Hint<String>("templateDefaultColor", "0x000000");

	/** Should the default template include a scale bar? */
	public static final Hint<Boolean> TEMPLATE_INCLUDE_SCALEBAR = new Hint<Boolean>("templateIncludeScaleBar", true);

	/** Should the default template include a legend? */
	public static final Hint<Boolean> TEMPLATE_INCLUDE_LEGEND = new Hint<Boolean>("templateIncludeLegend", true);

	/** Should the default template include a north arrow? */
	public static final Hint<Boolean> TEMPLATE_INCLUDE_NORTH_ARROW = new Hint<Boolean>("templateIncludeNorthArrow",
			true);

	/**
	 * Apply a new value for a specific print configuration hint.
	 * 
	 * @param hint The hint to change the value for.
	 * @param value The new actual value. If the value is null, an IllegalArgumentException is thrown.
	 */
	public <T> void setHintValue(Hint<T> hint, T value) {
		if (value == null) {
			throw new IllegalArgumentException("Null value passed.");
		}
		hintValues.put(hint, value);
	}

	/**
	 * Get the value for a specific print configuration. All hints have a default value, so this method will never
	 * return <code>null</code>.
	 * 
	 * @param hint The hint to retrieve the current value for.
	 * @return The map hint value.
	 */
	public <T> T getHintValue(Hint<T> hint) {
		if (hintValues.containsKey(hint.getName())) {
			return (T) hintValues.get(hint);
		} else {
			return hint.getDefaultValue();
		}
	}

}
