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
package org.geomajas.plugin.print.client;


import com.google.gwt.core.client.GWT;

/**
 * Start point class for the print plugin.
 * 
 * @author Jan Venstermans
 * 
 */
public final class Print {

	private static Print instance;

	private PrintViewFactory viewFactory;

	private PrintClientBundleFactory bundleFactory;

	private Print() {
	}

	public static Print getInstance() {
		if (instance == null) {
			instance = new Print();
		}
		return instance;
	}

	public PrintClientBundleFactory getBundleFactory() {
		if (bundleFactory == null) {
			bundleFactory = GWT.create(PrintClientBundleFactory.class);
		}
		return bundleFactory;
	}

	public PrintViewFactory getViewFactory() {
		if (viewFactory == null) {
			viewFactory = GWT.create(PrintViewFactory.class);
		}
		return viewFactory;
	}

	public void setViewFactory(PrintViewFactory viewFactory) {
		this.viewFactory = viewFactory;
	}

	public void setBundleFactory(PrintClientBundleFactory bundleFactory) {
		this.bundleFactory = bundleFactory;
	}
}
