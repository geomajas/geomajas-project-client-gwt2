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
package org.geomajas.gwt2.plugin.print.client.template;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.plugin.print.client.util.PrintConfiguration;

/**
 * Interface for data provider of a Builder.
 * This provider can give the builder all custom information necessary to create a print.
 * 
 * @author Jan Venstermans
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface PrintRequestDataProvider {

	/**
	 * Getter for the {@link TemplateBuilderDataProvider}.
	 *
	 * @return
	 */
	TemplateBuilderDataProvider getTemplateBuilderDataProvider();

	/**
	 * Getter for the {@link org.geomajas.gwt2.plugin.print.client.util.PrintConfiguration.PostPrintAction}.
	 * @return
	 */
	PrintConfiguration.PostPrintAction getPostPrintAction();

	/**
	 * Getter for the file name.
	 * @return
	 */
	String getFileName();
	
	/**
	 * Is the service synchronous.
	 * @return
	 */
	boolean isSync();
	
	/**
	 * Get the DPI (for image prints).
	 * 
	 * @return
	 */
	int getDpi();
}
