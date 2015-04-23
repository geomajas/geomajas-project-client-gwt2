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
package org.geomajas.gwt2.plugin.print.client;


import org.geomajas.annotation.Api;
import org.geomajas.gwt2.plugin.print.client.layerbuilder.PrintableLayerBuilder;
import org.geomajas.gwt2.plugin.print.client.template.impl.DefaultPrintableMapBuilder;
import org.geomajas.gwt2.plugin.print.client.util.PrintUtil;
import org.geomajas.gwt2.plugin.print.client.util.PrintUtilImpl;

import com.google.gwt.core.client.GWT;

/**
 * Start point class for the print plugin.
 * 
 * @author Jan Venstermans
 * @since 2.1.0
 */
@Api(allMethods = true)
public final class Print {

	private static Print instance;

	private PrintViewFactory viewFactory;

	private PrintClientBundleFactory bundleFactory;

	private PrintService printService;

	private PrintUtil printUtil;

	private Print() {
	}

	/**
	 * Get the {@link Print} singleton instance.
	 * @return
	 */
	public static Print getInstance() {
		if (instance == null) {
			instance = new Print();
		}
		return instance;
	}

	/**
	 * Getter for the {@link PrintClientBundleFactory} instance.
	 *
	 * @return bundle factory
	 */
	public PrintClientBundleFactory getBundleFactory() {
		if (bundleFactory == null) {
			bundleFactory = GWT.create(PrintClientBundleFactory.class);
		}
		return bundleFactory;
	}

	/**
	 * Getter for the {@link PrintViewFactory} used for default view creation.
	 * @return view factory
	 */
	public PrintViewFactory getViewFactory() {
		if (viewFactory == null) {
			viewFactory = GWT.create(PrintViewFactory.class);
		}
		return viewFactory;
	}

	/**
	 * Getter for the {@link PrintService}.
	 * @return print service
	 */
	public PrintService getPrintService() {
		if (printService == null) {
			printService = new PrintServiceImpl();
		}
		return printService;
	}

	/**
	 * Getter for the {@link org.geomajas.gwt2.plugin.print.client.util.PrintUtil} instance.
	 * @return print util instance
	 */
	public PrintUtil getPrintUtil() {
		if (printUtil == null) {
			printUtil = new PrintUtilImpl();
		}
		return printUtil;
	}

	/**
	 * Setter for the {@link PrintService}.
	 * @param printService
	 */
	public void setPrintService(PrintService printService) {
		this.printService = printService;
	}

	/**
	 * Setter for the {@link PrintViewFactory}.
	 * @param viewFactory
	 */
	public void setViewFactory(PrintViewFactory viewFactory) {
		this.viewFactory = viewFactory;
	}

	/**
	 *  Setter for the {@link PrintClientBundleFactory}.
	 * @param bundleFactory
	 */
	public void setBundleFactory(PrintClientBundleFactory bundleFactory) {
		this.bundleFactory = bundleFactory;
	}

	/**
	 * Setter for the {@link PrintUtil} instance.
	 * @param printUtil
	 */
	public void setPrintUtil(PrintUtil printUtil) {
		this.printUtil = printUtil;
	}
	
	/**
	 * Register a default layer builder.
	 * 
	 * @param layerBuilder
	 */
	public void registerDefaultLayerBuilder(PrintableLayerBuilder<?> layerBuilder) {
		DefaultPrintableMapBuilder.registerDefaultLayerBuilder(layerBuilder);
	}
}
