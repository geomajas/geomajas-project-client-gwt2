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
package org.geomajas.gwt2.plugin.print.tilebasedlayer.client;

import org.geomajas.gwt2.plugin.print.client.Print;

import com.google.gwt.core.client.EntryPoint;

/**
 * Registers our builder.
 * 
 * @author Jan De Moerloose
 *
 */
public class PrintTileBasedLayer implements EntryPoint {

	@Override
	public void onModuleLoad() {
		Print.getInstance().registerDefaultLayerBuilder(new TileBasedLayerBuilder());
	}

}
