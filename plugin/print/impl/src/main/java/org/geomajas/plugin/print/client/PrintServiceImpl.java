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
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.plugin.print.client.event.PrintFinishedInfo;
import org.geomajas.plugin.print.client.event.PrintRequestInfo;
import org.geomajas.plugin.print.client.util.PrintSettings;
import org.geomajas.plugin.print.command.dto.PrintGetTemplateRequest;
import org.geomajas.plugin.print.command.dto.PrintGetTemplateResponse;

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
		final PrintSettings.PostPrintAction postPrintAction = printRequestInfo.getPostPrintAction();
		final String fileName = printRequestInfo.getFileName();
		PrintGetTemplateRequest request = new PrintGetTemplateRequest();
		request.setTemplate(printRequestInfo.getPrintTemplateInfo());
		final GwtCommand command = new GwtCommand(PrintGetTemplateRequest.COMMAND);
		command.setCommandRequest(request);
		GeomajasServerExtension.getInstance().getCommandService().execute(command,
				new AbstractCommandCallback<PrintGetTemplateResponse>() {

					public void execute(PrintGetTemplateResponse response) {
						PrintFinishedInfo printFinishedInfo = new PrintFinishedInfo();
						printFinishedInfo.setEncodedUrl(Print.getInstance().getPrintUtil()
								.getPrintEncodeUrl(response.getDocumentId(), fileName,
										command.getUserToken(), postPrintAction));
						printFinishedInfo.setPostPrintAction(postPrintAction);
						callback.onSuccess(printFinishedInfo);
					}
				});
	}
}
