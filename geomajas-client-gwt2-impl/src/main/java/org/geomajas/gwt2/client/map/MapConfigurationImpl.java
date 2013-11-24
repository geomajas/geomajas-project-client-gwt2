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

package org.geomajas.gwt2.client.map;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.gwt2.client.map.layer.Layer;

/**
 * Default map configuration implementation.
 * 
 * @author Pieter De Graef
 */
public class MapConfigurationImpl implements MapConfiguration {

	public static final Integer ANIMATION_TIME_DEFAULT = 4000;

	public static final Integer FADE_IN_TIME_DEFAULT = 250;

	public static final Boolean ANIMATION_CANCEL_SUPPORT_DEFAULT = false;

	private final Map<Layer, Boolean> layerAnimation;

	private Map<MapHint<?>, Object> hintValues;

	private ClientMapInfo mapInfo;

	// ------------------------------------------------------------------------
	// Constructor:
	// ------------------------------------------------------------------------

	public MapConfigurationImpl() {
		hintValues = new HashMap<MapHint<?>, Object>();
		layerAnimation = new HashMap<Layer, Boolean>();

		// Now apply the default values:
		setMapHintValue(MapConfiguration.ANIMATION_TIME, ANIMATION_TIME_DEFAULT);
		setMapHintValue(MapConfiguration.FADE_IN_TIME, FADE_IN_TIME_DEFAULT);
		setMapHintValue(MapConfiguration.ANIMATION_CANCEL_SUPPORT, ANIMATION_CANCEL_SUPPORT_DEFAULT);
	}

	// ------------------------------------------------------------------------
	// Working with map hints:
	// ------------------------------------------------------------------------

	@Override
	public <T> void setMapHintValue(MapHint<T> hint, T value) {
		if (value == null) {
			throw new IllegalArgumentException("Null value passed.");
		}
		hintValues.put(hint, value);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getMapHintValue(MapHint<T> hint) {
		return (T) hintValues.get(hint);
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	@Override
	public ClientMapInfo getServerConfiguration() {
		return mapInfo;
	}

	/**
	 * Protected method used by the MapPresenterImpl to set the server configuration (when it arrives from the server).
	 * 
	 * @param mapInfo
	 *            The server configuration object.
	 */
	protected void setServerConfiguration(ClientMapInfo mapInfo) {
		this.mapInfo = mapInfo;
	}

	@Override
	public boolean isAnimated(Layer layer) {
		if (!layerAnimation.containsKey(layer)) {
			return false;
		}
		return layerAnimation.get(layer);
	}

	@Override
	public void setAnimated(Layer layer, boolean animated) {
		if (layerAnimation.containsKey(layer)) {
			layerAnimation.remove(layer);
		}
		layerAnimation.put(layer, animated);
	}
}