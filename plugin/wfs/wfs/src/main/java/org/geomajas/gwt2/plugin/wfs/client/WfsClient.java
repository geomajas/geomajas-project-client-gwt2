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

package org.geomajas.gwt2.plugin.wfs.client;

import org.geomajas.gwt2.plugin.wfs.client.service.WfsService;
import org.geomajas.gwt2.plugin.wfs.client.service.WfsServiceImpl;

/**
 * Starting point for the WF client plugin.
 *
 * @author Jan De Moerloose
 */
public final class WfsClient {

	private static WfsClient instance;

	private WfsService wfsService;

	private WfsClient() {
		wfsService = new WfsServiceImpl();
	}

	/**
	 * Get a singleton instance.
	 *
	 * @return Return WmsClient!
	 */
	public static WfsClient getInstance() {
		if (instance == null) {
			instance = new WfsClient();
		}
		return instance;
	}

	// ------------------------------------------------------------------------
	// Getting services:
	// ------------------------------------------------------------------------

	/**
	 * Get a service that is able to execute various WFS calls.
	 *
	 * @return The WFS service.
	 */
	public WfsService getWfsService() {
		return wfsService;
	}

	/**
	 * 
	 * @param wfsService
	 */
	public void setWfsService(WfsService wfsService) {
		this.wfsService = wfsService;
	}
}
