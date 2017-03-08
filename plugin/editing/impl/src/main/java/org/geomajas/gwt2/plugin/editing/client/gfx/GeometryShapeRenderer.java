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
