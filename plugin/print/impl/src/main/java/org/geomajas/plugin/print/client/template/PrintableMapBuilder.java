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

package org.geomajas.plugin.print.client.template;

import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.plugin.print.client.layerbuilder.PrintableLayerBuilder;

/**
 * Interface for building a printable version of a map.
 * 
 * @author Jan De Moerloose
 * @author An Buyle (support for extra layer with e.g. selected geometries)
 * @author Jan Venstermans
 */
public interface PrintableMapBuilder {

	void registerLayerBuilder(PrintableLayerBuilder layerBuilder);

	void build(MapPresenter mapPresenter, Bbox worldBounds, double rasterResolution);
}
