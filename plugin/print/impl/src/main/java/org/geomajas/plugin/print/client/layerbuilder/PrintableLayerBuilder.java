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
package org.geomajas.plugin.print.client.layerbuilder;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.map.MapPresenter;

/**
 * Interface for building printable layers. The interface requires an object as the data source for the print layer.
 *
 * @param <T> type of object that is supported as the data source for creating the print layer.
 *           This is to distinguish between larger types of objects.
 *
 * @author Jan Venstermans
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface PrintableLayerBuilder<T> {

	/**
	 * Is the specified object supported as the data source?
	 * @param object
	 * @return boolean value
	 */
	boolean supports(T object);

	/**
	 * Builds the {@link ClientLayerInfo} based on the data source object. The return value contains all info
	 * for printing the layer.
	 *
	 * @param mapPresenter
	 * @param object
	 * @param worldBounds
	 * @param rasterResolution
	 * @return
	 */
	ClientLayerInfo build(MapPresenter mapPresenter, T object, Bbox worldBounds, double rasterResolution);

}
