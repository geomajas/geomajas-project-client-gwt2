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

package org.geomajas.gwt2.client.map.render.canvas;

import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.render.LayerRenderer;
import org.geomajas.gwt2.client.map.render.LayersModelRenderer;
import org.geomajas.gwt2.client.map.render.RenderingInfo;

/**
 * LayersModelRenderer implementation that uses a HTML5 Canvas tag to render upon.
 *
 * @author Pieter De Graef
 */
public class CanvasLayersModelRenderer implements LayersModelRenderer {

	@Override
	public void registerLayerRenderer(Layer layer, LayerRenderer layerRenderer) {

	}

	@Override
	public LayerRenderer getLayerRenderer(Layer layer) {
		return null;
	}

	@Override
	public void setAnimated(Layer layer, boolean animated) {

	}

	@Override
	public boolean isAnimated(Layer layer) {
		return false;
	}

	@Override
	public void render(RenderingInfo renderingInfo) {

	}
}
