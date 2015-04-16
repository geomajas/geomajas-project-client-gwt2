package org.geomajas.gwt2.plugin.wfs.client;

import org.geomajas.gwt2.plugin.wfs.client.service.WfsProxyServiceImpl;
import org.geomajas.gwt2.plugin.wfs.client.service.WfsService;
import org.geomajas.gwt2.plugin.wfs.client.service.WfsServiceImpl;


public class WfsServerExtension {

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
