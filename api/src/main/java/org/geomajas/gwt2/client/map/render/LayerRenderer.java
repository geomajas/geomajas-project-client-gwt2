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

package org.geomajas.gwt2.client.map.render;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.layer.Layer;

/**
 * Definition of a renderer responsible for rendering a single layer.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public interface LayerRenderer extends BasicRenderer {

	/**
	 * Get the layer this renderer is supposed to render.
	 * 
	 * @return The layer.
	 */
	Layer getLayer();

	/**
	 * Set the opacity of this renderer.
	 * 
	 * @param opacity (1 = opaque, 0 = transparent)
	 * @since 2.1.0
	 */
	void setOpacity(double opacity);

	/**
	 * Get the opacity of this renderer.
	 * 
	 * @param opacity the opacity
	 * @since 2.1.0
	 */
	double getOpacity();
}