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
package org.geomajas.plugin.print.client.util;


import com.google.gwt.core.client.Callback;
import org.geomajas.plugin.print.client.event.PrintFinishedInfo;
import org.geomajas.plugin.print.client.event.PrintRequestInfo;
import org.geomajas.plugin.print.client.template.TemplateBuilder;
import org.geomajas.plugin.print.client.template.TemplateBuilderDataProvider;

/**
 * Presenter for print method.
 * 
 * @author Jan Venstermans
 * 
 */
public interface PrintUtil {

	/**
	 *  Fill the builder with information from the data provider.
	 *  UTIL method.
	 *
	 * @param builder
	 * @param templateBuilderDataProvider provides data for the builder
	 * @return
	 */
	public void copyProviderDataToBuilder(TemplateBuilder builder,
												 TemplateBuilderDataProvider templateBuilderDataProvider);

	UrlBuilder createUrlBuilder(String baseUrl);

	/**
	 * Create a parameterized url for the created pdf.
	 *
	 * @param documentId a String value
	 * @param userToken token of the user that had send the command.
	 * @return info object containing parameterized url
	 */
	String getPrintEncodeUrl(String documentId, String userToken, PrintSettings.ActionType actionType);
}
