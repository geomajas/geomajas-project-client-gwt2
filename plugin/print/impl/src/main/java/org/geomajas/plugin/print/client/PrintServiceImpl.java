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
package org.geomajas.plugin.print.client;


import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.plugin.print.client.event.PrintFinishedInfo;
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
public class PrintServiceImpl implements PrintService {

	/**
	 * Generic print method based on a {@link org.geomajas.plugin.print.command.dto.PrintTemplateInfo}.
	 * A {@link org.geomajas.plugin.print.command.dto.PrintGetTemplateRequest} is send to server.
	 * Based on response, an encode url is created.
	 * This url returned in a callback. <br/>
	 * This method could be moved to a non-widget presenter.
	 *
	 * @param printTemplateInfo the template send to the server
	 * @param callback callback with the encodeUrl
	 */
	@Override
	public void print(PrintTemplateInfo printTemplateInfo, final Callback<PrintFinishedInfo, Void> callback) {
		PrintGetTemplateRequest request = new PrintGetTemplateRequest();
		request.setTemplate(printTemplateInfo);
		final GwtCommand command = new GwtCommand(PrintGetTemplateRequest.COMMAND);
		command.setCommandRequest(request);
		GeomajasServerExtension.getInstance().getCommandService().execute(command,
				new AbstractCommandCallback<PrintGetTemplateResponse>() {

					public void execute(PrintGetTemplateResponse response) {
						PrintFinishedInfo printFinishedInfo = getEncodeUrl(response, command.getUserToken());
						callback.onSuccess(printFinishedInfo);
					}
				});
	}

	/**
	 * Processing of a {@link org.geomajas.plugin.print.command.dto.PrintGetTemplateResponse}
	 * to create a parameterized url for the created pdf.
	 * @param response response of type {@link org.geomajas.plugin.print.command.dto.PrintGetTemplateResponse}
	 * @param userToken token of the user that had send the command.
	 * @return info object containing parameterized url
	 */
	private PrintFinishedInfo getEncodeUrl(PrintGetTemplateResponse response, String userToken) {
		UrlBuilder url = new UrlBuilder(GWT.getHostPageBaseURL());
		url.addPath(PrintSettings.URL_PATH);
		url.addParameter(PrintSettings.URL_DOCUMENT_ID, response.getDocumentId());
		// url.addParameter(URL_NAME, (String) fileNameItem.getValue());
		url.addParameter(PrintSettings.URL_NAME, "mapPrint.pdf");
		url.addParameter(PrintSettings.URL_TOKEN, userToken);
		// TODO String downloadType = downloadTypeGroup.getValue()
		PrintSettings.ActionType actionType = PrintSettings.ActionType.OPEN;
		switch (actionType) {
			case OPEN:
				url.addParameter(PrintSettings.URL_DOWNLOAD, PrintSettings.URL_DOWNLOAD_NO);
				break;
			case SAVE:
				url.addParameter(PrintSettings.URL_DOWNLOAD, PrintSettings.URL_DOWNLOAD_YES);
				break;
		}
		PrintFinishedInfo printFinishedInfo = new PrintFinishedInfo();
		printFinishedInfo.setEncodedUrl(url.toString());
		printFinishedInfo.setActionType(actionType);
		return printFinishedInfo;
	}
}
