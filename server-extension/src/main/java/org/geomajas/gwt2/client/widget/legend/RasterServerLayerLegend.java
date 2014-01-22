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

package org.geomajas.gwt2.client.widget.legend;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.layer.RasterServerLayer;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Widget that represents the legend for a {@link RasterServerLayer}.
 * 
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public class RasterServerLayerLegend implements IsWidget {

	private final RasterServerLayer layer;

	private ServerLayerStyleWidget delegate;

	/**
	 * Create a new legend widget for the given layer.
	 * 
	 * @param layer
	 *            The layer.
	 */
	public RasterServerLayerLegend(RasterServerLayer layer) {
		this.layer = layer;
	}

	@Override
	public Widget asWidget() {
		if (delegate == null) {
			String url = GeomajasServerExtension.getInstance().getEndPointService().getLegendServiceUrl();
			ServerLayerStyleWidget
					.addPath(url, layer.getServerLayerId() + ServerLayerStyleWidget.LEGEND_ICON_EXTENSION);
			delegate = new ServerLayerStyleWidget(URL.encode(url), layer.getTitle(), null);
		}
		return delegate.asWidget();
	}

	/**
	 * Get the layer for this legend.
	 * 
	 * @return The layer.s
	 */
	public RasterServerLayer getLayer() {
		return layer;
	}
}