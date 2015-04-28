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
package org.geomajas.gwt2.example.client.sample.general;

import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.gfx.CanvasContainer;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.render.LayerRenderer;

/**
 * {@link Layer} that demonstrates the way the map navigates by leaving bounds traces on a canvas.
 * 
 * @author Jan De Moerloose
 */
public class TracingLayer implements Layer {

	private CanvasBoundsRenderer renderer;

	public TracingLayer(ViewPort viewPort, CanvasContainer container) {
		renderer = new CanvasBoundsRenderer(this, viewPort, container);
	}

	@Override
	public String getId() {
		return "TracingLayer";
	}

	@Override
	public String getTitle() {
		return "TracingLayer";
	}

	@Override
	public void setSelected(boolean selected) {
	}

	@Override
	public boolean isSelected() {
		return false;
	}

	@Override
	public void setMarkedAsVisible(boolean markedAsVisible) {
	}

	@Override
	public boolean isMarkedAsVisible() {
		return true;
	}

	@Override
	public boolean isShowing() {
		return true;
	}

	@Override
	public void refresh() {
	}

	@Override
	public LayerRenderer getRenderer() {
		return renderer;
	}

	public void setEnabled(boolean enabled) {
		renderer.setEnabled(enabled);
	}

	@Override
	public void setOpacity(double opacity) {
	}

	@Override
	public double getOpacity() {
		return 0;
	}

	@Override
	public Bbox getMaxBounds() {
		return Bbox.ALL;
	}

	@Override
	public void setMaxBounds(Bbox maxBounds) {
	}

	@Override
	public double getMaxResolution() {
		return Double.MAX_VALUE;
	}

	@Override
	public double getMinResolution() {
		return Double.MIN_VALUE;
	}
}