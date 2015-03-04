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

package org.geomajas.gwt2.client.map.layer;

import org.geomajas.gwt2.client.event.LayerAddedEvent;
import org.geomajas.gwt2.client.event.LayerDeselectedEvent;
import org.geomajas.gwt2.client.event.LayerOrderChangedEvent;
import org.geomajas.gwt2.client.event.LayerRemovedEvent;
import org.geomajas.gwt2.client.event.LayerSelectedEvent;
import org.geomajas.gwt2.client.event.LayerSelectionHandler;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.ViewPort;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of the {@link LayersModel} interface.
 *
 * @author Pieter De Graef
 */
public class LayersModelImpl implements LayersModel {

	private final ViewPort viewPort;

	private final MapEventBus eventBus;

	/**
	 * An ordered list of layers. The drawing order on the map is as follows: the first layer will be placed at the
	 * bottom, the last layer on top.
	 */
	private final List<Layer> layers;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a new LayersModel.
	 *
	 * @param viewPort The map ViewPort.
	 * @param eventBus The same maps eventBus
	 */
	public LayersModelImpl(ViewPort viewPort, MapEventBus eventBus) {
		this.viewPort = viewPort;
		this.eventBus = eventBus;
		this.layers = new ArrayList<Layer>();

		// Add a layer selection handler that allows only one selected layer at a time:
		eventBus.addLayerSelectionHandler(new LayerSelectionHandler() {

			public void onSelectLayer(LayerSelectedEvent event) {
				for (Layer layer : layers) {
					if (layer.isSelected() && !layer.equals(event.getLayer())) {
						layer.setSelected(false);
					}
				}
			}

			public void onDeselectLayer(LayerDeselectedEvent event) {
			}
		});
	}

	// ------------------------------------------------------------------------
	// MapModel implementation:
	// ------------------------------------------------------------------------

	@Override
	public boolean addLayer(Layer layer) {
		if (layer == null) {
			throw new IllegalArgumentException("Layer is null.");
		}
		if (getLayer(layer.getId()) == null) {
			layers.add(layer);
			if (layer instanceof AbstractLayer) {
				AbstractLayer aLayer = (AbstractLayer) layer;
				aLayer.setViewPort(viewPort);
				aLayer.setEventBus(eventBus);
			}
			eventBus.fireEvent(new LayerAddedEvent(layer));
			return true;
		}
		return false;
	}

	@Override
	public boolean removeLayer(String id) {
		Layer layer = getLayer(id);
		if (layer != null) {
			int index = getLayerPosition(layer);
			layers.remove(layer);
			eventBus.fireEvent(new LayerRemovedEvent(layer, index));
			return true;
		}
		return false;
	}

	@Override
	public Layer getLayer(String id) {
		if (id == null) {
			throw new IllegalArgumentException("Null ID passed to the getLayer method.");
		}
		for (Layer layer : layers) {
			if (id.equals(layer.getId())) {
				return layer;
			}
		}
		return null;
	}

	@Override
	public int getLayerCount() {
		return layers.size();
	}

	@Override
	public boolean moveLayer(Layer layer, int index) {
		int currentIndex = getLayerPosition(layer);
		// Check the new index:
		if (index < 0) {
			index = 0;
		} else if (index > layers.size() - 1) {
			index = layers.size() - 1;
		}
		if (currentIndex < 0 || currentIndex == index) {
			return false;
		}

		// Index might have been altered; check again if it is really a change:
		if (currentIndex == index) {
			return false;
		}

		// First remove the layer from the list:
		layers.remove(layer);
		// Change the order:
		layers.add(index, layer);

		// Send out the correct event:
		eventBus.fireEvent(new LayerOrderChangedEvent(currentIndex, index));
		return true;
	}

	@Override
	public boolean moveLayerUp(Layer layer) {
		return moveLayer(layer, getLayerPosition(layer) + 1);
	}

	@Override
	public boolean moveLayerDown(Layer layer) {
		return moveLayer(layer, getLayerPosition(layer) - 1);
	}

	@Override
	public int getLayerPosition(Layer layer) {
		if (layer == null) {
			throw new IllegalArgumentException("Null value passed to the getLayerPosition method.");
		}
		for (int i = 0; i < layers.size(); i++) {
			if (layer.getId().equals(layers.get(i).getId())) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public Layer getLayer(int index) {
		return layers.get(index);
	}

	@Override
	public Layer getSelectedLayer() {
		if (layers != null) {
			for (Layer layer : layers) {
				if (layer.isSelected()) {
					return layer;
				}
			}
		}
		return null;
	}

	@Override
	public void clear() {
		while (layers.size() > 0) {
			removeLayer(layers.get(0).getId());
		}
	}
}