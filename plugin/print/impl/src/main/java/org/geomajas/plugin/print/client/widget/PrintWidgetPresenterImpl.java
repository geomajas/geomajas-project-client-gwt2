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
package org.geomajas.plugin.print.client.widget;


import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.plugin.print.client.PrintSettings;
import org.geomajas.plugin.print.client.template.DefaultTemplateBuilder;
import org.geomajas.plugin.print.client.template.PageSize;
import org.geomajas.plugin.print.client.template.PrintableLayerBuilder;
import org.geomajas.plugin.print.client.template.PrintableMapBuilder;
import org.geomajas.plugin.print.client.util.PrintLayout;
import org.geomajas.plugin.print.client.util.UrlBuilder;
import org.geomajas.plugin.print.command.dto.PrintGetTemplateRequest;
import org.geomajas.plugin.print.command.dto.PrintGetTemplateResponse;
import org.geomajas.plugin.print.command.dto.PrintTemplateInfo;

/**
 * Initializes the GWT2 print plugin.
 * 
 * @author An Buyle
 * @author Jan Venstermans
 * 
 */
public class PrintWidgetPresenterImpl implements PrintWidgetPresenter {

	private MapPresenter mapPresenter;

	private String applicationId;

	// what does this do, when is it used?
	private PrintableMapBuilder mapBuilder = new PrintableMapBuilder();

	private PrintWidgetView view;

	public PrintWidgetPresenterImpl(MapPresenter mapPresenter, String applicationId, PrintWidgetView view) {
		this.view = view;
		this.mapPresenter = mapPresenter;
		this.applicationId = applicationId;
		view.setHandler(this);
	}

	public void registerLayerBuilder(PrintableLayerBuilder layerBuilder) {
		mapBuilder.registerLayerBuilder(layerBuilder);
	}

	public PrintableMapBuilder getMapBuilder() {
		return mapBuilder;
	}

	public void print() {
		if (mapPresenter != null) {
			print(createDefaultTemplateFromViewData(), new Callback<String, Void>() {
				@Override
				public void onFailure(Void reason) {
					// do nothing
				}

				@Override
				public void onSuccess(String encodedUrl) {
					// show the url in appropriate window/iFrame

					if (PrintSettings.SAVE.equals(PrintSettings.OPEN)) {
						// TODO Converted to GWT2
						// // create a hidden iframe to avoid popups ???
						// HTMLPanel hiddenFrame = new HTMLPanel("<iframe src='" + encodedUrl
						// + "'+style='position:absolute;width:0;height:0;border:0'>");
						// hiddenFrame.setVisible(false);
						//
						// addChild(hiddenFrame);
					} else {
						com.google.gwt.user.client.Window.open(encodedUrl, "_blank", null);
					}
				}
			});
		}
	}

	/**
	 * Generic print method based on a {@link PrintTemplateInfo}.
	 * A {@link PrintGetTemplateRequest} is send to server. Based on response, an encode url is created.
	 * This url returned in a callback. <br/>
	 * This method could be moved to a non-widget presenter.
	 *
	 * @param printTemplateInfo the template send to the server
	 * @param callback callback with the encodeUrl
	 */
	public void print(PrintTemplateInfo printTemplateInfo, final Callback<String, Void> callback) {
		PrintGetTemplateRequest request = new PrintGetTemplateRequest();
		request.setTemplate(printTemplateInfo);
		final GwtCommand command = new GwtCommand(PrintGetTemplateRequest.COMMAND);
		command.setCommandRequest(request);
		GeomajasServerExtension.getInstance().getCommandService().execute(command,
				new AbstractCommandCallback<PrintGetTemplateResponse>() {

					public void execute(PrintGetTemplateResponse response) {
						String encodedUrl = getEncodeUrl(response, command.getUserToken());
						callback.onSuccess(encodedUrl);
					}
				});
	}

	/**
	 * Build a PrintTemplateInfo object using the {@link DefaultTemplateBuilder}.
	 * Some values are retrieved from the view element.
	 *
	 * @return constructed template
	 */
	private PrintTemplateInfo createDefaultTemplateFromViewData() {
		DefaultTemplateBuilder builder = new DefaultTemplateBuilder(mapBuilder);

		// non-view data
		builder.setApplicationId(this.applicationId);
		builder.setMapPresenter(mapPresenter);
		builder.setMarginX((int) PrintLayout.templateMarginX);
		builder.setMarginY((int) PrintLayout.templateMarginY);

		// view data
		PageSize size = view.getPageSize();
		if (view.isLandscape()) {
			builder.setPageHeight(size.getWidth());
			builder.setPageWidth(size.getHeight());
		} else {
			builder.setPageHeight(size.getHeight());
			builder.setPageWidth(size.getWidth());
		}
		builder.setTitleText(view.getTitle());
		builder.setWithArrow(view.isWithArrow());
		builder.setWithScaleBar(view.isWithScaleBar());
		builder.setRasterDpi(view.getRasterDpi());
		return builder.buildTemplate();
	}

	/**
	 * Processing of a {@link PrintGetTemplateResponse} to create a parameterized url for the created pdf.
	 * @param response response of type {@link PrintGetTemplateResponse}
	 * @param userToken token of the user that had send the command.
	 * @return parameterized url
	 */
	private String getEncodeUrl(PrintGetTemplateResponse response, String userToken) {
		UrlBuilder url = new UrlBuilder(GWT.getHostPageBaseURL());
		url.addPath(PrintSettings.URL_PATH);
		url.addParameter(PrintSettings.URL_DOCUMENT_ID, response.getDocumentId());
		// url.addParameter(URL_NAME, (String) fileNameItem.getValue());
		url.addParameter(PrintSettings.URL_NAME, "mapPrint.pdf");
		url.addParameter(PrintSettings.URL_TOKEN, userToken);
		// TODO String downloadType = downloadTypeGroup.getValue()
		if (PrintSettings.SAVE.equals(PrintSettings.OPEN)) {
			url.addParameter(PrintSettings.URL_DOWNLOAD, PrintSettings.URL_DOWNLOAD_YES);
		} else {
			url.addParameter(PrintSettings.URL_DOWNLOAD, PrintSettings.URL_DOWNLOAD_NO);
		}
		return url.toString();
	}

}
