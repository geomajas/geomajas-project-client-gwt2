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
package org.geomajas.gwt2.widget.client.map.mapcontrolpanel;

import org.geomajas.gwt2.client.event.LayerAddedEvent;
import org.geomajas.gwt2.client.event.LayerOrderChangedEvent;
import org.geomajas.gwt2.client.event.LayerOrderChangedHandler;
import org.geomajas.gwt2.client.event.LayerRemovedEvent;
import org.geomajas.gwt2.client.event.MapCompositionHandler;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.widget.client.map.layercontrolpanel.LayerControlPanel;


import java.util.logging.Logger;

/**
 * Default implementation of {@link org.geomajas.gwt2.widget.client.map.layercontrolpanel.LayerControlPanelPresenter}.
 *
 * @author Dosi Bingov
 *
 */
public class MapControlPanelPresenterImpl implements MapControlPanelPresenter {

	private Logger log = Logger.getLogger(MapControlPanelPresenterImpl.class.getName());

	private MapControlPanelView view;

	private boolean disableToggleOutOfRange;

	private  MapPresenter mapPresenter;

	public MapControlPanelPresenterImpl(MapControlPanelView view, MapPresenter mapPresenter) {
		this.view = view;
		this.mapPresenter = mapPresenter;
		this.disableToggleOutOfRange = true; //default it is true
		init();
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Protected methods:
	// ------------------------------------------------------------------------

	/**
	 * Add a layer to the legend drop down panel.
	 *
	 * @param layer
	 *            The layer who's legend to add to the drop down panel.
	 * @return success or not.
	 */
	protected boolean addLayer(Layer layer) {
		int index = getLayerIndex(layer);
		if (index < 0) {
			view.add(new LayerControlPanel(mapPresenter, layer, disableToggleOutOfRange));

			return true;
		}
		return false;
	}

	/**
	 * Remove a layer from the drop down content panel again.
	 *
	 * @param layer
	 *            The layer to remove.
	 * @return success or not.
	 */
	protected boolean removeLayer(Layer layer) {
		int index = getLayerIndex(layer);
		if (index >= 0) {
			//contentPanel.remove(index);
			view.removeWidget(index);

			return true;
		}
		return false;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private int getLayerIndex(Layer layer) {
		for (int i = 0; i < view.getWidgetCount(); i++) {
			LayerControlPanel layerPanel = (LayerControlPanel) view.getWidgetAt(i);
			if (layerPanel.getLayer() == layer) {
				return i;
			}
		}
		return -1;
	}

	private void init() {

		// Add all layers (if there are any):
		for (int i = mapPresenter.getLayersModel().getLayerCount() - 1 ; i >= 0 ; i--) {
			addLayer(mapPresenter.getLayersModel().getLayer(i));
		}

		// Keep track of new layers being added or layers being removed. Change the legend accordingly:
		mapPresenter.getEventBus().addMapCompositionHandler(new MapCompositionHandler() {

			public void onLayerRemoved(LayerRemovedEvent event) {
				removeLayer(event.getLayer());
			}

			public void onLayerAdded(LayerAddedEvent event) {
				addLayer(event.getLayer());
			}
		});

		// Keep track of layer order within the LayersModel:
		mapPresenter.getEventBus().addLayerOrderChangedHandler(new LayerOrderChangedHandler() {

			public void onLayerOrderChanged(LayerOrderChangedEvent event) {
				view.moveWidget(event.getFromIndex(), event.getToIndex());
			}
		});

	}

	@Override
	public void setDisableToggleOutOfRange(boolean disable) {
		this.disableToggleOutOfRange = disable;
	}
}

