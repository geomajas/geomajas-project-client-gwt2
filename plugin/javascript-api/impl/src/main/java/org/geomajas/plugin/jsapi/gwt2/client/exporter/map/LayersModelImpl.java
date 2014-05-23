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
package org.geomajas.plugin.jsapi.gwt2.client.exporter.map;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.layer.FeaturesSupported;
import org.geomajas.plugin.jsapi.client.map.LayersModel;
import org.geomajas.plugin.jsapi.client.map.layer.Layer;
import org.geomajas.plugin.jsapi.gwt2.client.exporter.map.layer.LayerImpl;
import org.geomajas.plugin.jsapi.gwt2.client.exporter.map.layer.VectorLayer;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Javascript exportable facade for the model of layers that form a map.
 * 
 * @author Oliver May
 * @author Pieter De Graef
 */
@Export("LayersModel")
@ExportPackage("org.geomajas.jsapi.map")
public class LayersModelImpl implements Exportable, LayersModel {

	private org.geomajas.gwt2.client.map.layer.LayersModel layersModel;

	public LayersModelImpl() {
	}

	/**
	 * Construct the wrapper based on the given mapModel.
	 * 
	 * @param mapModel
	 *            the mapModel to wrap.
	 * @since 1.0.0
	 */
	@Api
	public LayersModelImpl(org.geomajas.gwt2.client.map.layer.LayersModel layersModel) {
		this.layersModel = layersModel;
	}

	// ------------------------------------------------------------------------
	// LayersModel implementation:
	// ------------------------------------------------------------------------

	/**
	 * Get a single layer by its identifier.
	 * 
	 * @param id
	 *            The layers unique identifier within this map.
	 * @return Returns the layer, or null if it could not be found.
	 */
	public Layer getLayer(String layerId) {
		org.geomajas.gwt2.client.map.layer.Layer layer = layersModel.getLayer(layerId);
		if (layer instanceof FeaturesSupported) {
			return new VectorLayer((FeaturesSupported) layer);
		}
		return new LayerImpl(layer);
	}

	/**
	 * Return the layer at a certain index. If the index can't be found, null is returned.
	 * 
	 * @param index
	 *            The specified index.
	 * @return Returns the layer, or null if the index can't be found.
	 */
	public Layer getLayerAt(int index) {
		org.geomajas.gwt2.client.map.layer.Layer layer = layersModel.getLayer(index);
		if (layer instanceof FeaturesSupported) {
			return new VectorLayer((FeaturesSupported) layer);
		}
		return new LayerImpl(layer);
	}

	/**
	 * Return the total number of layers within this map.
	 * 
	 * @return The layer count.
	 */
	public int getLayerCount() {
		return layersModel.getLayerCount();
	}
}