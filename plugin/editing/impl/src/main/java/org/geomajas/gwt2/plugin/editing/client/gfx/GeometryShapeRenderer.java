package org.geomajas.gwt2.plugin.editing.client.gfx;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.editing.client.gfx.GeometryRenderer;

/**
 * An SVG based renderer.
 *
 * @author Jan De Moerloose
 * @since 2.4.3
 */
@Api(allMethods = true)
public interface GeometryShapeRenderer extends GeometryRenderer {

	/**
	 * Sets the shape factory for all geometry elements.
	 *
	 * @param shapeFactory
	 */
	void setShapeFactory(GeometryIndexShapeFactory shapeFactory);

	/**
	 * Sets the style factory for all geometry elements.
	 *
	 * @param styleFactory
	 */
	void setStyleFactory(GeometryIndexStyleFactory styleFactory);

	/**
	 * Gets the style provider. Was already an experimental API.
	 *
	 * @return
	 */
	StyleProvider getStyleProvider();

}
