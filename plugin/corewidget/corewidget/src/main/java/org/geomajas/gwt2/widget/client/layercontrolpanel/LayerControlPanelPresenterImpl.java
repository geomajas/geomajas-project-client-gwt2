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
package org.geomajas.gwt2.widget.client.layercontrolpanel;


import org.geomajas.gwt2.client.event.LayerHideEvent;
import org.geomajas.gwt2.client.event.LayerShowEvent;
import org.geomajas.gwt2.client.event.LayerVisibilityHandler;
import org.geomajas.gwt2.client.event.LayerVisibilityMarkedEvent;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.layer.LegendUrlSupported;

import java.util.logging.Logger;

/**
 * Default implementation of {@link LayerControlPanelPresenter}.
 * 
 * @author Dosi Bingov
 * 
 */
public class LayerControlPanelPresenterImpl implements LayerControlPanelPresenter {

	private Logger log = Logger.getLogger(LayerControlPanelPresenterImpl.class.getName());

	private LayerControlPanelView view;

	private Layer layer;

	public LayerControlPanelPresenterImpl(LayerControlPanelView view, Layer layer, MapEventBus eventBus) {
		this.view = view;
		this.layer = layer;
		init(eventBus);
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------
	@Override
	public Layer getLayer() {
		return layer;
	}

	@Override
	public void toggleLayerVisibility() {
		layer.setMarkedAsVisible(!layer.isMarkedAsVisible());
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------
	private void init(MapEventBus eventBus) {
		view.setLayerTitle(layer.getTitle());
		view.setLayerVisible(layer.isMarkedAsVisible());

		// React to layer visibility events:
		eventBus.addLayerVisibilityHandler(new LayerVisibilityHandler() {

			public void onShow(LayerShowEvent event) {
				view.enableVisibilityToggle(true);
			}

			public void onHide(LayerHideEvent event) {
				// If a layer hides while it is marked as visible, it means it has gone beyond it's allowed scale range.
				// If so, disable the CheckBox. It's no use to try to mark the layer as visible anyway:
				if (layer.isMarkedAsVisible()) {
					view.enableVisibilityToggle(false);
				}
			}

			public void onVisibilityMarked(LayerVisibilityMarkedEvent event) {
				//visibilityToggle.setValue(layer.isMarkedAsVisible());
				view.setLayerVisible(layer.isMarkedAsVisible());

			}
		}, this.layer);


		// Add the legend if supported
		if (layer instanceof LegendUrlSupported) {
			view.setLegendUrl(((LegendUrlSupported) layer).getLegendImageUrl());
		}
	}

}

