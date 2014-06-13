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

import com.google.gwt.core.client.GWT;
import org.geomajas.plugin.print.client.Print;
import org.geomajas.plugin.print.client.event.PrintFinishedInfo;
import org.geomajas.plugin.print.client.template.PageSize;
import org.geomajas.plugin.print.client.template.TemplateBuilder;
import org.geomajas.plugin.print.client.template.TemplateBuilderDataProvider;
import org.geomajas.plugin.print.command.dto.PrintGetTemplateResponse;

/**
 * Builds parametrized URL from a base URL.
 * 
 * @author Jan De Moerloose
 * 
 */
public class PrintUtilImpl implements PrintUtil {

	/**
	 *  Fill the builder with information from the data provider.
	 *  UTIL method.
	 *
	 * @param builder
	 * @param templateBuilderDataProvider provides data for the builder
	 * @return
	 */
	public void copyProviderDataToBuilder(TemplateBuilder builder,
										  TemplateBuilderDataProvider templateBuilderDataProvider) {
		PageSize size = templateBuilderDataProvider.getPageSize();
		if (templateBuilderDataProvider.isLandscape()) {
			builder.setPageHeight(size.getWidth());
			builder.setPageWidth(size.getHeight());
		} else {
			builder.setPageHeight(size.getHeight());
			builder.setPageWidth(size.getWidth());
		}
		builder.setTitleText(templateBuilderDataProvider.getTitle());
		builder.setWithArrow(templateBuilderDataProvider.isWithArrow());
		builder.setWithScaleBar(templateBuilderDataProvider.isWithScaleBar());
		builder.setRasterDpi(templateBuilderDataProvider.getRasterDpi());
	}

	@Override
	public UrlBuilder createUrlBuilder(String baseUrl) {
		return new UrlBuilder(baseUrl);
	}

	@Override
	public String getPrintEncodeUrl(String documentId, String userToken,
									PrintSettings.ActionType actionType) {
		UrlBuilder url = Print.getInstance().getPrintUtil().createUrlBuilder(GWT.getHostPageBaseURL());
		url.addPath(PrintSettings.URL_PATH);
		url.addParameter(PrintSettings.URL_DOCUMENT_ID, documentId);
		// url.addParameter(URL_NAME, (String) fileNameItem.getValue());
		url.addParameter(PrintSettings.URL_NAME, "mapPrint.pdf");
		url.addParameter(PrintSettings.URL_TOKEN, userToken);
		// TODO String downloadType = downloadTypeGroup.getValue()
		switch (actionType) {
			case OPEN:
				url.addParameter(PrintSettings.URL_DOWNLOAD, PrintSettings.URL_DOWNLOAD_NO);
				break;
			case SAVE:
				url.addParameter(PrintSettings.URL_DOWNLOAD, PrintSettings.URL_DOWNLOAD_YES);
				break;
		}
		return url.toString();
	}
}
