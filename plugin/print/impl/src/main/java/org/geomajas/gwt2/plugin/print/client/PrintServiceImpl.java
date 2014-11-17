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
package org.geomajas.gwt2.plugin.print.client;

import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.plugin.print.client.event.PrintFinishedInfo;
import org.geomajas.gwt2.plugin.print.client.event.PrintFinishedInfo.HttpMethod;
import org.geomajas.gwt2.plugin.print.client.event.PrintRequestFinishedEvent;
import org.geomajas.gwt2.plugin.print.client.event.PrintRequestHandler;
import org.geomajas.gwt2.plugin.print.client.event.PrintRequestInfo;
import org.geomajas.gwt2.plugin.print.client.event.PrintRequestStartedEvent;
import org.geomajas.gwt2.plugin.print.client.json.JsonMapper;
import org.geomajas.gwt2.plugin.print.client.util.PrintConfiguration;
import org.geomajas.gwt2.plugin.print.client.util.PrintUrlParameterKey;
import org.geomajas.plugin.printing.command.dto.PrintGetTemplateRequest;
import org.geomajas.plugin.printing.command.dto.PrintGetTemplateResponse;
import org.geomajas.plugin.printing.command.dto.PrintTemplateInfo;

import com.google.gwt.core.client.Callback;

/**
 * Default implementation of {@link PrintService}.
 * 
 * @author An Buyle
 * @author Jan Venstermans
 * 
 */
public class PrintServiceImpl implements PrintService {

	@Override
	public void print(PrintRequestInfo printRequestInfo, final Callback<PrintFinishedInfo, Void> callback) {
		if (printRequestInfo.isSync()) {
			runSync(callback, printRequestInfo);
		} else {
			runAsync(callback, printRequestInfo);
		}
	}

	private void runSync(Callback<PrintFinishedInfo, Void> callback, PrintRequestInfo printRequestInfo) {
		// prepare common url and parameters
		PrintFinishedInfo printFinishedInfo = prepareInfo(printRequestInfo);
		// method = POST
		printFinishedInfo.setMethod(HttpMethod.POST);
		// add the JSON-ized template
		PrintTemplateInfo printTemplateInfo = printRequestInfo.getPrintTemplateInfo();
		printFinishedInfo.addParam(PrintUrlParameterKey.URL_TEMPLATE, JsonMapper.toJson(printTemplateInfo));		
		// synchronous call
		callback.onSuccess(printFinishedInfo);
	}

	private void runAsync(final Callback<PrintFinishedInfo, Void> callback, PrintRequestInfo printRequestInfo) {
		// prepare common url and parameters
		final PrintFinishedInfo printFinishedInfo = prepareInfo(printRequestInfo);
		// method = GET
		printFinishedInfo.setMethod(HttpMethod.GET);
		// run the command
		PrintGetTemplateRequest request = new PrintGetTemplateRequest();
		request.setTemplate(printRequestInfo.getPrintTemplateInfo());
		final GwtCommand command = new GwtCommand(PrintGetTemplateRequest.COMMAND);
		command.setCommandRequest(request);
		GeomajasServerExtension.getInstance().getCommandService()
				.execute(command, new AbstractCommandCallback<PrintGetTemplateResponse>() {

					public void execute(PrintGetTemplateResponse response) {
						// add the document id
						printFinishedInfo.addParam(PrintUrlParameterKey.URL_DOCUMENT_ID, response.getDocumentId());
						callback.onSuccess(printFinishedInfo);
					}
				});
	}

	@Override
	public void print(PrintRequestInfo printRequestInfo, final PrintRequestHandler printRequestHandler) {
		printRequestHandler.onPrintRequestStarted(new PrintRequestStartedEvent(printRequestInfo));
		print(printRequestInfo, new Callback<PrintFinishedInfo, Void>() {

			@Override
			public void onFailure(Void reason) {

			}

			@Override
			public void onSuccess(PrintFinishedInfo result) {
				printRequestHandler.onPrintRequestFinished(new PrintRequestFinishedEvent(result));
			}
		});
	}
	
	private PrintFinishedInfo prepareInfo(PrintRequestInfo printRequestInfo) {
		PrintFinishedInfo printFinishedInfo = new PrintFinishedInfo();		
		// set the url
		GeomajasServerExtension server = GeomajasServerExtension.getInstance();
		printFinishedInfo.setUrl(server.getEndPointService().getDispatcherUrl() + PrintUrlParameterKey.URL_PATH);
		// add parameters
		printFinishedInfo.addParam(PrintUrlParameterKey.URL_NAME, checkFileName(printRequestInfo.getFileName()));
		printFinishedInfo.addParam(PrintUrlParameterKey.URL_TOKEN, server.getCommandService().getUserToken());
		printFinishedInfo.addParam(PrintUrlParameterKey.URL_DPI, printRequestInfo.getDpi() + "");
		switch (printRequestInfo.getPostPrintAction()) {
			case OPEN:
				printFinishedInfo.addParam(PrintUrlParameterKey.URL_DOWNLOAD, PrintConfiguration.URL_DOWNLOAD_NO);
				break;
			case SAVE:
				printFinishedInfo.addParam(PrintUrlParameterKey.URL_DOWNLOAD, PrintConfiguration.URL_DOWNLOAD_YES);
				break;
		}
		printFinishedInfo.setPostPrintAction(printRequestInfo.getPostPrintAction());
		return printFinishedInfo;
	}

	private String checkFileName(String fileName) {
		if (fileName.lastIndexOf(".") < 0) {
			return fileName + PrintConfiguration.DEFAULT_DOWNLOAD_EXTENSION;
		} else {
			return fileName;
		}
	}

}
