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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ScaleInfo;

/**
 * Extension of the default {@link MapOptions} object to include some extra information from the server.
 * 
 * @author Pieter De Graef
 */
public class MapOptionsExt extends MapOptions {

	private final ClientMapInfo mapInfo;

	/**
	 * Create a new instance from a server configuration object.
	 * 
	 * @param mapInfo
	 *            The actual configuration.
	 */
	public MapOptionsExt(ClientMapInfo mapInfo) {
		this.mapInfo = mapInfo;
		setCrs(mapInfo.getCrs());
		setInitialBounds(mapInfo.getInitialBounds());
		setMaxBounds(mapInfo.getMaxBounds());
		setMaximumScale(mapInfo.getScaleConfiguration().getMaximumScale().getPixelPerUnit());
		setPixelLength(mapInfo.getPixelLength());
		setUnitLength(mapInfo.getUnitLength());
		List<Double> resolutions = new ArrayList<Double>();
		for (ScaleInfo scale : mapInfo.getScaleConfiguration().getZoomLevels()) {
			resolutions.add(scale.getPixelPerUnit());
		}
		setResolutions(resolutions);
	}

	/**
	 * Get the ID of the map as it is known by on the server.
	 * 
	 * @return The map ID.
	 */
	public String getServerId() {
		return mapInfo.getId();
	}

	/**
	 * Get the server-side configuration object.
	 * 
	 * @return The configuration.
	 */
	public ClientMapInfo getServerConfiguration() {
		return mapInfo;
	}
}