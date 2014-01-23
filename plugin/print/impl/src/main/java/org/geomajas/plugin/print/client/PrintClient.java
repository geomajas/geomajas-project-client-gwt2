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
package org.geomajas.plugin.print.client;

import org.geomajas.plugin.print.client.service.PrintService;
import org.geomajas.plugin.print.client.service.PrintServiceImpl;

import com.google.gwt.core.client.EntryPoint;

/**
 * Initializes the puregwt print plugin.
 * 
 * @author Jan De Moerloose
 * 
 */
public class PrintClient implements EntryPoint {

	private static PrintClient instance;

	public static final String SHOW_DEFAULT_PRINT_KEY = "ShowDefaultPrint";

	private PrintService printService;

	private PrintClient() {
		printService = new PrintServiceImpl();
	}

	public void onModuleLoad() {
	}
	
	public static PrintClient getInstance() {
		return instance; 
	}
	
	public PrintService getPrintService() {
		return printService;
	}

}
