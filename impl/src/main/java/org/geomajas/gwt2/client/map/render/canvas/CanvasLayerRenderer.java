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

import com.google.gwt.canvas.client.Canvas;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.render.LayerRenderer;
import org.geomajas.gwt2.client.map.render.RenderingInfo;

/**
 * LayerRenderer implementation that uses a HTML5 Canvas tag to render a layer upon.
 *
 * @author Pieter De Graef
 */
public class CanvasLayerRenderer implements LayerRenderer {

	private final Layer layer;

	private Canvas canvas;

	public CanvasLayerRenderer(Layer layer) {
		this.layer = layer;
	}

	@Override
	public Layer getLayer() {
		return layer;
	}

	@Override
	public void render(RenderingInfo renderingInfo) {

	}

	/**
	 * Set the canvas onto which to render the layer.
	 *
	 * @param canvas The canvas to render upon.
	 */
	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}
}