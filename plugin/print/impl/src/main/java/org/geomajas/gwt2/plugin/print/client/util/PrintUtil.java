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
package org.geomajas.gwt2.plugin.print.client.util;


import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.plugin.print.client.template.TemplateBuilder;
import org.geomajas.gwt2.plugin.print.client.template.TemplateBuilderDataProvider;
import org.geomajas.plugin.printing.command.dto.PrintTemplateInfo;

/**
 * Presenter for print method.
 * 
 * @author Jan Venstermans
 * @since 2.1.0
 */
@Api(allMethods = true)
public interface PrintUtil {

	/**
	 *  Fill the builder with information from the data provider.
	 *
	 * @param builder
	 * @param templateBuilderDataProvider provides data for the builder
	 * @return
	 */
	void copyProviderDataToBuilder(TemplateBuilder builder,
												 TemplateBuilderDataProvider templateBuilderDataProvider);

	/**
	 * Create a parameterized url for the created pdf.
	 *
	 * @param documentId a String value
	 * @param fileName name given to the downloaded file.
	 * @param userToken token of the user that had send the command.
	 * @return info object containing parameterized url
	 */
	String getPrintEncodeUrl(String documentId, String fileName,
							 String userToken, PrintConfiguration.PostPrintAction postPrintAction);

	/**
	 * Returns a text representation of a {@link PrintConfiguration.PostPrintAction}.
	 *
	 * @param postPrintAction
	 * @return
	 */
	String toString(PrintConfiguration.PostPrintAction postPrintAction);

	/**
	 * Returns a new {@link PrintTemplateInfo} instance, created by the builder, based on the data provided by
	 * the templateBuilderDataProvider.
	 *
	 * @param mapPresenter
	 * @param applicationId
	 * @param builder
	 * @param templateBuilderDataProvider
	 * @return
	 */
	PrintTemplateInfo createPrintTemplateInfo(MapPresenter mapPresenter, String applicationId,
											  TemplateBuilder builder,
											  TemplateBuilderDataProvider templateBuilderDataProvider);
}
