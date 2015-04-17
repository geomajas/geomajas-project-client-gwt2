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

import org.geomajas.gwt2.plugin.wfs.client.service.WfsProxyServiceImpl;
import org.geomajas.gwt2.plugin.wfs.client.service.WfsService;

/**
 * Entrypoint for WFS server extension.
 * 
 * @author Jan De Moerloose
 *
 */
public final class WfsServerExtension {

	private static WfsServerExtension instance;

	private WfsService wfsService;

	private WfsServerExtension() {
		wfsService = new WfsProxyServiceImpl();
	}

	/**
	 * Get a singleton instance.
	 *
	 * @return Return WfsServerExtension!
	 */
	public static WfsServerExtension getInstance() {
		if (instance == null) {
			instance = new WfsServerExtension();
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

}
