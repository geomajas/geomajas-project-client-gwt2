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
package org.geomajas.gwt2.plugin.print.client.util;

import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.plugin.print.client.i18n.PrintMessages;
import org.geomajas.gwt2.plugin.print.client.template.PageSize;
import org.geomajas.gwt2.plugin.print.client.template.TemplateBuilder;
import org.geomajas.gwt2.plugin.print.client.template.TemplateBuilderDataProvider;
import org.geomajas.plugin.printing.command.dto.PrintTemplateInfo;

import com.google.gwt.core.client.GWT;

/**
 * Default implementation of {@link PrintUtil}.
 * 
 * @author Jan Venstermans
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
		builder.setDpi(templateBuilderDataProvider.getDpi());
	}

	@Override
	public String getPrintEncodeUrl(String documentId, String fileName, String userToken,
									PrintConfiguration.PostPrintAction postPrintAction) {
		GeomajasServerExtension server = GeomajasServerExtension.getInstance();		
		UrlBuilder url = new UrlBuilder(server.getEndPointService().getDispatcherUrl());
		url.addPath(PrintUrlParameterKey.URL_PATH);
		url.addParameter(PrintUrlParameterKey.URL_DOCUMENT_ID, documentId);
		if (fileName.lastIndexOf(".") < 0) {
		   fileName += PrintConfiguration.DEFAULT_DOWNLOAD_EXTENSION;
		}
		url.addParameter(PrintUrlParameterKey.URL_NAME, fileName);
		url.addParameter(PrintUrlParameterKey.URL_TOKEN, userToken);
		switch (postPrintAction) {
			case OPEN:
				url.addParameter(PrintUrlParameterKey.URL_DOWNLOAD, PrintConfiguration.URL_DOWNLOAD_NO);
				break;
			case SAVE:
				url.addParameter(PrintUrlParameterKey.URL_DOWNLOAD, PrintConfiguration.URL_DOWNLOAD_YES);
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
	public String toString(PrintConfiguration.PostPrintAction postPrintAction) {
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
