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
package org.geomajas.plugin.print.client.template;

import org.geomajas.plugin.print.client.util.PrintSettings;

/**
 * Interface for data provider of a Builder.
 * This provider can give the builder all custom information necessary to create a print.
 * 
 * @author Jan Venstermans
 */
public interface PrintRequestDataProvider {

	TemplateBuilderDataProvider getTemplateBuilderDataProvider();

	PrintSettings.PostPrintAction getPostPrintAction();

	String getFileName();
}
