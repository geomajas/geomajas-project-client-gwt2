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

package org.geomajas.gwt2.plugin.wms.client.layer;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.layer.LegendUrlSupported;
import org.geomajas.gwt2.client.map.layer.tile.TileBasedLayer;
import org.geomajas.gwt2.plugin.wms.client.capabilities.WmsLayerInfo;

/**
 * <p> Base client-side WMS layer definition. Note that a WMS service can use either a raster data set or a vector data
 * set, we too make that distinction here. This layer definition does not support interaction with the WMS service.
 * </p>
 *
 * @author Pieter De Graef
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface WmsLayer extends TileBasedLayer, LegendUrlSupported {

	/**
	 * Get the main WMS options. These options are translated into HTTP GET parameters for the WMS calls.
	 *
	 * @return Get the main WMS options object.
	 */
	WmsLayerConfiguration getConfiguration();

	/**
	 * Get the capabilities object that describes this layer. This object is part from the WMS GetCapabilities request
	 * and is only present if the WMS layer has been created using this. This value can be null!
	 *
	 * @return Returns part of the WMS GetCapabilities that describes this layer, or null.
	 */
	WmsLayerInfo getCapabilities();
}
