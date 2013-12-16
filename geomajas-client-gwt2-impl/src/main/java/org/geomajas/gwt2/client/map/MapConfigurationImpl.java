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

import org.geomajas.gwt2.client.map.layer.Layer;

/**
 * Default map configuration implementation.
 * 
 * @author Pieter De Graef
 */
public class MapConfigurationImpl implements MapConfiguration {

	public static final Integer DEFAULT_ANIMATION_TIME = 400;

	public static final Integer DEFAULT_FADE_IN_TIME = 250;

	public static final Boolean DEFAULT_ANIMATION_CANCEL_SUPPORT = false;

	/** This default DPI value equals the default as proposed in the WMS specification. */
	public static final Double DEFAULT_DPI = 90.714285714;

	private final Map<Layer, Boolean> layerAnimation;

	private Map<MapHint<?>, Object> hintValues;

	private MapOptions mapOptions;

	// ------------------------------------------------------------------------
	// Constructor:
	// ------------------------------------------------------------------------

	public MapConfigurationImpl() {
		hintValues = new HashMap<MapHint<?>, Object>();
		layerAnimation = new HashMap<Layer, Boolean>();

		// Now apply the default values:
		setMapHintValue(MapConfiguration.ANIMATION_TIME, DEFAULT_ANIMATION_TIME);
		setMapHintValue(MapConfiguration.FADE_IN_TIME, DEFAULT_FADE_IN_TIME);
		setMapHintValue(MapConfiguration.ANIMATION_CANCEL_SUPPORT, DEFAULT_ANIMATION_CANCEL_SUPPORT);
		setMapHintValue(MapConfiguration.DPI, DEFAULT_DPI);
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
	public MapOptions getMapOptions() {
		return mapOptions;
	}

	/**
	 * Protected method used by the MapPresenterImpl to set the options object.
	 * 
	 * @param mapOptions
	 *            The map options configuration object.
	 */
	protected void setMapOptions(MapOptions mapOptions) {
		this.mapOptions = mapOptions;
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