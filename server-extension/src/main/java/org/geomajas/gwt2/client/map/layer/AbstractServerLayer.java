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

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.tile.TileConfiguration;
import org.geomajas.gwt2.client.map.render.TileRenderer;

/**
 * Abstraction of the basic layer interface. Specific layer implementations should use this as a base.
 * 
 * @param <T> The layer meta-data. Some extension of {@link ClientLayerInfo}.
 * @author Pieter De Graef
 */
public abstract class AbstractServerLayer<T extends ClientLayerInfo> extends AbstractTileBasedLayer {

	protected ClientMapInfo mapInfo;

	protected T layerInfo;

	protected TileRenderer tileRenderer;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a new layer that belongs to the given map model, using the given meta-data.
	 * 
	 * @param layerInfo The layer configuration from which to create the layer.
	 * @param viewPort The view port of the map.
	 * @param eventBus The map centric event bus.
	 */
	public AbstractServerLayer(MapConfiguration mapConfig, T layerInfo, TileConfiguration tileConfig,
			ViewPort viewPort, MapEventBus eventBus) {
		super(layerInfo.getId(), tileConfig);
		this.mapInfo = mapConfig.getHintValue(GeomajasServerExtension.MAPINFO);
		this.layerInfo = layerInfo;
		this.markedAsVisible = layerInfo.isVisible();
		this.title = layerInfo.getLabel();
		setMaxBounds(layerInfo.getMaxExtent());
		setViewPort(viewPort);
		setEventBus(eventBus);
		eventBus.addViewPortChangedHandler(new LayerScaleVisibilityHandler());
	}

	// ------------------------------------------------------------------------
	// Layer implementation:
	// ------------------------------------------------------------------------

	public String getServerLayerId() {
		return layerInfo.getServerLayerId();
	}

	public ClientMapInfo getMapInfo() {
		return mapInfo;
	}

	public T getLayerInfo() {
		return layerInfo;
	}

	@Override
	public boolean isShowing() {
		if (markedAsVisible) {
			// Check if current viewPort scale is between the minimum (inclusive) and
			// the maximum scale (exclusive) of the layer.
			// Inclusive/exclusive follows SLD convention.
			double maxResolution = 1 / layerInfo.getMinimumScale().getPixelPerUnit(); //inclusive
			double minResolution = 1 / layerInfo.getMaximumScale().getPixelPerUnit(); //exclusive
			if (viewPort.getResolution() > minResolution && viewPort.getResolution() <= maxResolution) {
				return true;
			}
		}
		return false;
	}
}
