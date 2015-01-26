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
package org.geomajas.gwt2.plugin.corewidget.client.map.layercontrolpanel;


import org.geomajas.gwt2.client.event.LayerHideEvent;
import org.geomajas.gwt2.client.event.LayerShowEvent;
import org.geomajas.gwt2.client.event.LayerVisibilityHandler;
import org.geomajas.gwt2.client.event.LayerVisibilityMarkedEvent;
import org.geomajas.gwt2.client.event.ViewPortChangedEvent;
import org.geomajas.gwt2.client.event.ViewPortChangedHandler;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.AbstractServerLayer;
import org.geomajas.gwt2.client.map.layer.Layer;

import java.util.logging.Level;
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

	private boolean disableToggleOutOfRange;

	private Layer layer;

	public LayerControlPanelPresenterImpl(LayerControlPanelView view, Layer layer, MapPresenter mapPresenter,
										  boolean disableToggleOutOfRange) {
		this.view = view;
		this.layer = layer;
		log.log(Level.INFO, "disableToggleOutOfRange => " + disableToggleOutOfRange);

		this.disableToggleOutOfRange = disableToggleOutOfRange;
		init(mapPresenter);
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	@Override
	public void toggleLayerVisibility() {
		log.log(Level.INFO, "toggleLayerVisibility()");
		layer.setMarkedAsVisible(!layer.isMarkedAsVisible());
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------
	private void init(final MapPresenter mapPresenter) {
		view.setLayerTitle(layer.getTitle());
		view.setLayerVisible(layer.isMarkedAsVisible());

		if (disableToggleOutOfRange) {
			view.enableVisibilityToggle(isLayerOutOfRange(mapPresenter.getViewPort(), layer));
		}

		mapPresenter.getEventBus().addViewPortChangedHandler(new ViewPortChangedHandler() {
			@Override
			public void onViewPortChanged(ViewPortChangedEvent event) {

				if (disableToggleOutOfRange) {
					view.enableVisibilityToggle(isLayerOutOfRange(mapPresenter.getViewPort(), layer));
				}

			}
		});

		// React to layer visibility events:
		mapPresenter.getEventBus().addLayerVisibilityHandler(new LayerVisibilityHandler() {

			public void onShow(LayerShowEvent event) {
			}

			public void onHide(LayerHideEvent event) {
			}

			public void onVisibilityMarked(LayerVisibilityMarkedEvent event) {
				view.setLayerVisible(layer.isMarkedAsVisible());

			}
		}, this.layer);
	}

	protected boolean isLayerOutOfRange(ViewPort viewPort, Layer layer) {

		if (layer instanceof AbstractServerLayer) {
			AbstractServerLayer serverLayer = (AbstractServerLayer) layer;

			double maxResolution = 1 / serverLayer.getLayerInfo().getMinimumScale().getPixelPerUnit();
			double minResolution = 1 / serverLayer.getLayerInfo().getMaximumScale().getPixelPerUnit();

			if (viewPort.getResolution() >= minResolution && viewPort.getResolution() <= maxResolution) {
				return true;
			}

			return false;
		}

		//TODO: implement this for layers other than AbstractServerLayer
		return true;
	}

}

