/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.client.map.layer;

import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.event.LayerStyleChangedEvent;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.render.LayerRenderer;
import org.geomajas.gwt2.client.map.render.dom.FixedScaleLayerRenderer;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * The client side representation of a raster layer defined on the backend.
 * 
 * @author Pieter De Graef
 */
public class RasterServerLayerImpl extends AbstractServerLayer<ClientRasterLayerInfo> implements RasterServerLayer {

	private final FixedScaleLayerRenderer renderer;

	/** The only constructor. */
	public RasterServerLayerImpl(ClientRasterLayerInfo layerInfo, ViewPort viewPort, MapEventBus eventBus) {
		super(layerInfo, viewPort, eventBus);
		renderer = new FixedScaleLayerRenderer(viewPort, this, eventBus);
	}

	// ------------------------------------------------------------------------
	// Layer implementation:
	// ------------------------------------------------------------------------

	@Override
	public LayerRenderer getRenderer() {
		return renderer;
	}

	@Override
	public IsWidget buildLegendWidget() {
		String url = GeomajasImpl.getInstance().getEndPointService().getLegendServiceUrl();
		addPath(url, getServerLayerId() + LEGEND_ICON_EXTENSION);
		return new ServerLayerStyleWidget(URL.encode(url), getTitle(), null);
	}

	/**
	 * Apply a new opacity on the entire raster layer.
	 * 
	 * @param opacity
	 *            The new opacity value. Must be a value between 0 and 1, where 0 means invisible and 1 is totally
	 *            visible.
	 */
	public void setOpacity(double opacity) {
		getLayerInfo().setStyle(Double.toString(opacity));
		eventBus.fireEvent(new LayerStyleChangedEvent(this));
	}

	public double getOpacity() {
		return Double.parseDouble(getLayerInfo().getStyle());
	}
}