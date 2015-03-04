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
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.layer.LayerUtil;

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

	private MapPresenter mapPresenter;

	public LayerControlPanelPresenterImpl(LayerControlPanelView view, Layer layer, MapPresenter mapPresenter,
										  boolean disableToggleOutOfRange) {
		this.view = view;
		this.layer = layer;
		this.mapPresenter = mapPresenter;
		log.log(Level.INFO, "disableToggleOutOfRange => " + disableToggleOutOfRange);

		this.disableToggleOutOfRange = disableToggleOutOfRange;
		init();
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
	private void init() {
		view.setLayerTitle(layer.getTitle());
		view.setLayerVisible(layer.isMarkedAsVisible());

		updateVisibilityToggle();

		mapPresenter.getEventBus().addViewPortChangedHandler(new ViewPortChangedHandler() {
			@Override
			public void onViewPortChanged(ViewPortChangedEvent event) {
				updateVisibilityToggle();
			}
		});

		// React to layer visibility events:
		mapPresenter.getEventBus().addLayerVisibilityHandler(new LayerVisibilityHandler() {

			@Override
			public void onShow(LayerShowEvent event) {
			}

			@Override
			public void onHide(LayerHideEvent event) {
			}

			@Override
			public void onVisibilityMarked(LayerVisibilityMarkedEvent event) {
				view.setLayerVisible(layer.isMarkedAsVisible());

			}
		}, this.layer);
	}

	protected void updateVisibilityToggle() {
		if (disableToggleOutOfRange) {
			view.enableVisibilityToggle(
					LayerUtil.isLayerVisibleAtViewPortResolution(mapPresenter.getViewPort(), layer));
		}
	}

}

