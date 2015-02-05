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
package org.geomajas.gwt2.plugin.print.client.template;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.plugin.print.client.util.PrintConfiguration;

/**
 * Setter extension of {@link PrintRequestDataProvider}.
 *
 * @author Jan Venstermans
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface DefaultPrintRequestDataProvider extends PrintRequestDataProvider {

	/**
	 * Getter for the {@link DefaultTemplateBuilderDataProvider}.
	 * @return
	 */
	DefaultTemplateBuilderDataProvider getDefaultTemplateBuilderDataProvider();

	/**
	 * Setter for the file name.
	 * @param fileName
	 */
	void setFileName(String fileName);

	/**
	 * Setter for the {@link org.geomajas.gwt2.plugin.print.client.util.PrintConfiguration.PostPrintAction}.
	 * @param postPrintAction
	 */
	void setPostPrintAction(PrintConfiguration.PostPrintAction postPrintAction);
	
	/**
	 * Should the service be called synchronously (using JSON POST) or asynchronously ?
	 * @param sync
	 */
	void setSync(boolean sync);

	/**
	 * Set the overall image DPI.
	 * 
	 * @param dpi
	 */
	void setDpi(int dpi);

	/**
	 * Setter extension of {@link TemplateBuilderDataProvider}.
	 *
	 * @author Jan Venstermans
	 * @since 2.1.0
	 */
	@Api(allMethods = true)
	public interface DefaultTemplateBuilderDataProvider extends TemplateBuilderDataProvider {

		void setTitle(String title);

		void setPageSize(PageSize pageSize);

		void setRasterDpi(int rasterDpi);

		void setLandscape(boolean landscape);

		void setWithArrow(boolean withArrow);

		void setWithScaleBar(boolean withScaleBar);

		void setDpi(int dpi);
	}
}
