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

package org.geomajas.plugin.wms.client.layer;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.layer.LegendUrlSupported;
import org.geomajas.gwt2.client.map.render.Tile;
import org.geomajas.plugin.wms.client.capabilities.WmsLayerInfo;

import java.util.List;

/**
 * <p> Base client-side WMS layer definition. Note that a WMS service can use either a raster data set or a vector data
 * set, we too make that distinction here. This layer definition does not support interaction with the WMS service. If
 * you need support for features or a GetFeatureInfo request, have a look at the <code>FeaturesSupportedWmsLayer</code>.
 * </p>
 *
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface WmsLayer extends Layer, LegendUrlSupported {

	/**
	 * Get the main WMS options. These options are translated into HTTP GET parameters for the WMS calls.
	 *
	 * @return Get the main WMS options object.
	 */
	WmsLayerConfiguration getConfig();

	/**
	 * Get this layers tile configuration object.
	 *
	 * @return The tile configuration object.
	 */
	WmsTileConfiguration getTileConfig();

	/**
	 * Get the capabilities object that describes this layer. This object is part from the WMS GetCapabilities request
	 * and is only present if the WMS layer has been created using this. This value can be null.
	 *
	 * @return Returns part of the WMS GetCapabilities that describes this layer. This value can be null.
	 */
	WmsLayerInfo getCapabilities();

	/**
	 * Get the tiles for the specified scale and world bounds.
	 *
	 * @param scale       The scale at which to ask for tiles.
	 * @param worldBounds The bounds in WorldSpace at which to ask for tiles.
	 * @return The list of tiles fitting the given parameters.
	 */
	List<Tile> getTiles(double scale, Bbox worldBounds);
}
