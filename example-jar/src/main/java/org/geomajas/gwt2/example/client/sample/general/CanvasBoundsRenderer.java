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
package org.geomajas.gwt2.example.client.sample.general;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.gfx.CanvasContainer;
import org.geomajas.gwt2.client.gfx.CanvasRect;
import org.geomajas.gwt2.client.gfx.CanvasShape;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.render.LayerRenderer;
import org.geomajas.gwt2.client.map.render.RenderingInfo;

/**
 * Renders bounds on HTML5 canvas.
 * 
 * @author Jan De Moerloose
 * 
 */
public class CanvasBoundsRenderer implements LayerRenderer {

	private Layer layer;

	private CanvasContainer canvasContainer;

	private ViewPort viewPort;

	private List<CanvasShape> shapes = new ArrayList<CanvasShape>();

	private List<Double> scales = new ArrayList<Double>();

	private boolean enabled;

	public CanvasBoundsRenderer(Layer layer, ViewPort viewPort, CanvasContainer canvasContainer) {
		this.layer = layer;
		this.viewPort = viewPort;
		this.canvasContainer = canvasContainer;
	}

	@Override
	public void render(RenderingInfo renderingInfo) {
		if (isEnabled()) {
			double scale = renderingInfo.getView().getScale();
			CanvasShape shape = getBounds(renderingInfo);
			canvasContainer.addShape(shape);
			shapes.add(shape);
			scales.add(scale);
			if (shapes.size() > 20) {
				canvasContainer.removeShape(shapes.remove(0));
				scales.remove(0);
			}

		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	private CanvasShape getBounds(RenderingInfo renderingInfo) {
		Coordinate center = renderingInfo.getView().getPosition();
		double scale = renderingInfo.getView().getScale();
		double width = viewPort.getMapWidth() / scale;
		double height = viewPort.getMapHeight() / scale;
		Bbox bounds = new Bbox(center.getX() - 0.5 * width, center.getY() - 0.5 * height, width, height);
		CanvasRect rect = new CanvasRect(bounds);
		rect.setFillStyle("rgba(255,255,255,0)");
		rect.setStrokeStyle("rgba(0,0,0,255)");
		rect.setStrokeWidthPixels(1);
		return rect;
	}

	@Override
	public Layer getLayer() {
		return layer;
	}

}
