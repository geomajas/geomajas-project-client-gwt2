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

package org.geomajas.gwt2.plugin.wms.client.widget;

import com.google.gwt.user.client.ui.Image;
import org.geomajas.gwt2.client.event.LayerStyleChangedEvent;
import org.geomajas.gwt2.client.event.LayerStyleChangedHandler;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.plugin.wms.client.layer.WmsLayer;

/**
 * <p>Legend widget that displays an image of the WMS GetLegendGraphic URL for a specific
 * {@link org.geomajas.gwt2.plugin.wms.client.layer.WmsLayer}. This widget
 * will update it's content when the layer changes style, making sure it always shows the correct styles in the legend.
 * </p><p>For more advanced legend widgets, please have a look at the Core Widgets plugin.</p>
 *
 * @author Pieter De Graef
 */
public class WmsLayerLegend extends Image {

	private final WmsLayer layer;

	/**
	 * Create a new legend widget for the given WMS layer.
	 *
	 * @param eventBus The maps event bus. Needed to react to {@link LayerStyleChangedEvent}s. If this value is null,
	 *                 the legend will not change it's content when a layers style changes.
	 * @param layer    The layer to display a legend for.
	 */
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

	/**
	 * Get the layer for which this widget displays a legend.
	 *
	 * @return The layer.
	 */
	public WmsLayer getLayer() {
		return layer;
	}
}
