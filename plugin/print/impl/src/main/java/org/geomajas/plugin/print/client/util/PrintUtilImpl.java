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
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.plugin.print.client.Print;
import org.geomajas.plugin.print.client.i18n.PrintMessages;
import org.geomajas.plugin.print.client.template.PageSize;
import org.geomajas.plugin.print.client.template.TemplateBuilder;
import org.geomajas.plugin.print.client.template.TemplateBuilderDataProvider;
import org.geomajas.plugin.print.command.dto.PrintTemplateInfo;

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
	public String getPrintEncodeUrl(String documentId, String fileName, String userToken,
									PrintSettings.PostPrintAction postPrintAction) {
		UrlBuilder url = Print.getInstance().getPrintUtil().createUrlBuilder(GWT.getHostPageBaseURL());
		url.addPath(PrintUrlParameterKey.URL_PATH);
		url.addParameter(PrintUrlParameterKey.URL_DOCUMENT_ID, documentId);
		if (fileName.lastIndexOf(".") < 0) {
		   fileName += PrintSettings.DEFAULT_DOWNLOAD_EXTENSION;
		}
		url.addParameter(PrintUrlParameterKey.URL_NAME, fileName);
		url.addParameter(PrintUrlParameterKey.URL_TOKEN, userToken);
		switch (postPrintAction) {
			case OPEN:
				url.addParameter(PrintUrlParameterKey.URL_DOWNLOAD, PrintSettings.URL_DOWNLOAD_NO);
				break;
			case SAVE:
				url.addParameter(PrintUrlParameterKey.URL_DOWNLOAD, PrintSettings.URL_DOWNLOAD_YES);
				break;
		}
		return url.toString();
	}

	/**
	 * This method will use GWT.create !
	 * @param postPrintAction
	 * @return
	 */
	@Override
	public String toString(PrintSettings.PostPrintAction postPrintAction) {
		// create locally, because of GWT.create
		PrintMessages messages = GWT.create(PrintMessages.class);
		switch (postPrintAction) {
			case OPEN:
				return messages.printPrefsOpenInBrowserWindow();
			case SAVE:
				return messages.printPrefsSaveAsFile();
		}
		return null;
	}

	@Override
	public PrintTemplateInfo createPrintTemplateInfo(MapPresenter mapPresenter,
													 String applicationId,
													 TemplateBuilder builder,
													 TemplateBuilderDataProvider templateBuilderDataProvider) {
		builder.setApplicationId(applicationId);
		builder.setMapPresenter(mapPresenter);
		builder.setMarginX((int) PrintLayout.templateMarginX);
		builder.setMarginY((int) PrintLayout.templateMarginY);

		copyProviderDataToBuilder(builder, templateBuilderDataProvider);

		return builder.buildTemplate();
	}
}
