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

package org.geomajas.plugin.wms.client.widget;

import com.google.gwt.user.client.ui.Image;
import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.event.LayerStyleChangedEvent;
import org.geomajas.gwt2.client.event.LayerStyleChangedHandler;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.plugin.wms.client.layer.WmsLayer;

/**
 * Legend widget that displays an image of the WMS GetLegendGraphic URL.
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public class WmsLayerLegend extends Image {

	private final WmsLayer layer;

	public WmsLayerLegend(MapEventBus eventBus, WmsLayer layer) {
		super();
		this.layer = layer;
		setUrl(layer.getLegendImageUrl());

		eventBus.addLayerStyleChangedHandler(new LayerStyleChangedHandler() {

			@Override
			public void onLayerStyleChanged(LayerStyleChangedEvent event) {
				if (event.getLayer() == WmsLayerLegend.this.layer) {
					setUrl(WmsLayerLegend.this.layer.getLegendImageUrl());
				}
			}
		});
	}

	public WmsLayer getLayer() {
		return layer;
	}
}
