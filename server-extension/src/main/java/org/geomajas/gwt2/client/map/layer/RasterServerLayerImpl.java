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

package org.geomajas.gwt2.client.map.layer;

import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.View;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.render.FixedScaleLayerRenderer;
import org.geomajas.gwt2.client.map.render.FixedScaleRenderer;
import org.geomajas.gwt2.client.map.render.LayerRenderer;
import org.geomajas.gwt2.client.map.render.dom.RasterServerLayerScaleRenderer;
import org.geomajas.gwt2.client.map.render.dom.container.HtmlContainer;

/**
 * The client side representation of a raster layer defined on the backend.
 * 
 * @author Pieter De Graef
 */
public class RasterServerLayerImpl extends AbstractServerLayer<ClientRasterLayerInfo> implements RasterServerLayer {

	private final FixedScaleLayerRenderer renderer;

	/** The only constructor. */
	public RasterServerLayerImpl(ClientRasterLayerInfo layerInfo, final ViewPort viewPort, MapEventBus eventBus) {
		super(layerInfo, viewPort, eventBus);
		renderer = new FixedScaleLayerRenderer(viewPort, this, eventBus) {

			@Override
			public FixedScaleRenderer createNewScaleRenderer(int tileLevel, View view, HtmlContainer scaleContainer) {
				return new RasterServerLayerScaleRenderer(RasterServerLayerImpl.this, tileLevel,
						viewPort.getFixedScale(tileLevel), viewPort, scaleContainer);
			}

		};
	}

	// ------------------------------------------------------------------------
	// Layer implementation:
	// ------------------------------------------------------------------------

	@Override
	public LayerRenderer getRenderer() {
		return renderer;
	}

	/**
	 * Apply a new opacity on the entire raster layer. Changing the opacity on a layer does NOT fire a layer style
	 * changed event.
	 * 
	 * @param opacity
	 *            The new opacity value. Must be a value between 0 and 1, where 0 means invisible and 1 is totally
	 *            visible.
	 */
	public void setOpacity(double opacity) {
		getLayerInfo().setStyle(Double.toString(opacity));
		renderer.setOpacity(opacity);
	}

	public double getOpacity() {
		return Double.parseDouble(getLayerInfo().getStyle());
	}
}