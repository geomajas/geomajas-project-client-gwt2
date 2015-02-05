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
package org.geomajas.gwt2.plugin.print.client.layerbuilder;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.layer.Layer;

/**
 * Builder that prepares a specific type of layer for printing. Server layers should enhance their existing client info,
 * other layers should return a suitable {@link org.geomajas.configuration.client.ClientLayerInfo}
 * instance that can be picked up by a
 * {@link org.geomajas.plugin.rasterizing.api.LayerFactory} in the rasterizing plugin.
 * 
 * @author Jan De Moerloose
 * @since 2.1.0
 * 
 */
@Api(allMethods = true)
public interface PrintableLayersModelBuilder extends PrintableLayerBuilder<Layer> {

}
