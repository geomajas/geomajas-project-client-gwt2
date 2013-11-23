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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.gwt2.client.event.LayerAddedEvent;
import org.geomajas.gwt2.client.event.LayerDeselectedEvent;
import org.geomajas.gwt2.client.event.LayerOrderChangedEvent;
import org.geomajas.gwt2.client.event.LayerRemovedEvent;
import org.geomajas.gwt2.client.event.LayerSelectedEvent;
import org.geomajas.gwt2.client.event.LayerSelectionHandler;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.ViewPort;

/**
 * Default implementation of the {@link LayersModel} interface.
 * 
 * @author Pieter De Graef
 */
public class LayersModelImpl implements LayersModel {

	private final ViewPort viewPort;

	private final MapEventBus eventBus;

	private final MapConfiguration configuration;

	/**
	 * An ordered list of layers. The drawing order on the map is as follows: the first layer will be placed at the
	 * bottom, the last layer on top.
	 */
	private final List<Layer> layers;

	private ClientMapInfo mapInfo;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a new LayersModel.
	 * 
	 * @param viewPort
	 *            The map ViewPort.
	 * @param eventBus
	 *            The same maps eventBus
	 * @param configuration
	 *            The maps configuration object.
	 */
	public LayersModelImpl(ViewPort viewPort, MapEventBus eventBus, MapConfiguration configuration) {
		this.viewPort = viewPort;
		this.eventBus = eventBus;
		this.configuration = configuration;
		this.layers = new ArrayList<Layer>();

	}

	// ------------------------------------------------------------------------
	// MapModel implementation:
	// ------------------------------------------------------------------------

	@Override
	public void initialize(ClientMapInfo mapInfo) {
		this.mapInfo = mapInfo;

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

		// Create all the layers:
		layers.clear();
		for (ClientLayerInfo layerInfo : mapInfo.getLayers()) {
			Layer layer = createLayer(layerInfo);
			addLayer(layer);
		}
	}

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
			configuration.setAnimated(layer, layers.size() == 1);
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
			if (layer instanceof ServerLayer) {
				ServerLayer<?> serverLayer = (ServerLayer<?>) layer;
				mapInfo.getLayers().remove(serverLayer.getLayerInfo());
			}
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

		ClientLayerInfo layerInfo = null;
		int newIndexMapInfo = -1;
		// Check if both the layer with whom the specified layer will swap (concerning the ordering) and
		// the specified layer are server layers. If so their position in the mapInfo.getLayers() must also be swapped
		if (layer instanceof ServerLayer && layers.get(index) instanceof ServerLayer) {
			ServerLayer<?> serverLayer = (ServerLayer<?>) layer;
			layerInfo = serverLayer.getLayerInfo();

			int idx = 0;
			for (ClientLayerInfo layerInMapInfo : mapInfo.getLayers()) {
				if (layerInMapInfo.getId().equals(layerInfo.getId())) {

					if (index > currentIndex) {
						newIndexMapInfo = idx + 1;
					} else {
						newIndexMapInfo = idx - 1;
					}
					break; // Stop when found
				}
				idx++;
			}
		}

		// Index might have been altered; check again if it is really a change:
		if (currentIndex == index) {
			return false;
		}

		// First remove the layer from the list:
		layers.remove(layer);
		// Change the order:
		layers.add(index, layer);

		if (layerInfo != null && newIndexMapInfo >= 0) {
			mapInfo.getLayers().remove(layerInfo); // remove from mapInfo.layers
			mapInfo.getLayers().add(newIndexMapInfo, layerInfo); // put back (changed order)
		}

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

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private Layer createLayer(ClientLayerInfo layerInfo) {
		ServerLayer<?> layer = null;
		switch (layerInfo.getLayerType()) {
			case RASTER:
				layer = new RasterServerLayerImpl((ClientRasterLayerInfo) layerInfo, viewPort, eventBus);
				break;
			default:
				layer = new VectorServerLayerImpl((ClientVectorLayerInfo) layerInfo, viewPort, eventBus);
				break;
		}
		if (!mapInfo.getLayers().contains(layer.getLayerInfo())) {
			mapInfo.getLayers().add(layer.getLayerInfo());
		}
		return layer;
	}
}