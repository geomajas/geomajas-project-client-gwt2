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
 * Renderer responsible for rendering the entire {@link org.geomajas.gwt2.client.map.layer.LayersModel}. When a layer is
 * added to the map, the map will automatically add it's renderer to this <code>LayersModelRenderer</code>.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface LayersModelRenderer extends BasicRenderer {

	/**
	 * Register a renderer for a certain layer. This way it is possible to overwrite the renderer for a certain layer.
	 * 
	 * @param layer
	 *            The layer to define a renderer for.
	 * @param layerRenderer
	 *            The renderer for the given layer. The renderer may not be null.
	 */
	void registerLayerRenderer(Layer layer, LayerRenderer layerRenderer);

	/**
	 * Get the renderer for a certain layer.
	 * 
	 * @param layer
	 *            The layer to search a renderer for.
	 * @return The layer renderer, or null if the renderer cannot be found. Note that if the layer has been added to the
	 *         map, it's renderer will have been added automatically.
	 */
	LayerRenderer getLayerRenderer(Layer layer);

	/**
	 * Turn animation for a certain layer on or off.
	 * 
	 * @param layer
	 *            The layer to enable or disable animation for.
	 * @param animated
	 *            Should animation during navigation be enabled or disabled?
	 */
	void setAnimated(Layer layer, boolean animated);

	/**
	 * Is a certain layer animated during map navigation or not?
	 * 
	 * @param layer
	 *            The layer to ask for.
	 * @return True or false.
	 */
	boolean isAnimated(Layer layer);
}