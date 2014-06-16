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

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.map.MapPresenter;

/**
 * Empty interface for turning a specific layer into a print layer.
 *
 * @param <T> type of object that is supported by the interface.
 *           This is to distinguish between larger types of objects.
 *
 * @author Jan Venstermans
 */
public interface PrintableLayerBuilder<T> {

	/**
	 * Is the specified object supported ?
	 * @param object
	 * @return
	 */
	boolean supports(T object);

	/**
	 *
	 * @param mapPresenter
	 * @param object
	 * @param worldBounds
	 * @param rasterResolution
	 * @return
	 */
	ClientLayerInfo build(MapPresenter mapPresenter, T object, Bbox worldBounds, double rasterResolution);

}
