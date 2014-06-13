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
import org.geomajas.plugin.print.client.util.PrintUtil;
import org.geomajas.plugin.print.client.util.PrintUtilImpl;

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

	private PrintService printService;

	private PrintUtil printUtil;

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

	public PrintService getPrintService() {
		if (printService == null) {
			printService = new PrintServiceImpl();
		}
		return printService;
	}

	public PrintUtil getPrintUtil() {
		if (printUtil == null) {
			printUtil = new PrintUtilImpl();
		}
		return printUtil;
	}

	public void setPrintService(PrintService printService) {
		this.printService = printService;
	}

	public void setViewFactory(PrintViewFactory viewFactory) {
		this.viewFactory = viewFactory;
	}

	public void setBundleFactory(PrintClientBundleFactory bundleFactory) {
		this.bundleFactory = bundleFactory;
	}

	public void setPrintUtil(PrintUtil printUtil) {
		this.printUtil = printUtil;
	}
}
