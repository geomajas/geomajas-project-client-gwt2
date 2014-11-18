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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import junit.framework.Assert;

import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.service.CommandService;
import org.geomajas.gwt2.client.service.EndPointService;
import org.geomajas.gwt2.plugin.print.client.event.PrintFinishedInfo;
import org.geomajas.gwt2.plugin.print.client.event.PrintRequestInfo;
import org.geomajas.gwt2.plugin.print.client.template.TemplateBuilder;
import org.geomajas.gwt2.plugin.print.client.template.TemplateBuilderDataProvider;
import org.geomajas.gwt2.plugin.print.client.util.PrintConfiguration.PostPrintAction;
import org.geomajas.gwt2.plugin.print.client.widget.PrintWidgetMockStart;
import org.geomajas.plugin.printing.command.dto.PrintGetTemplateRequest;
import org.geomajas.plugin.printing.command.dto.PrintTemplateInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import com.google.gwt.core.client.Callback;
import com.google.gwtmockito.GwtMock;
import com.google.gwtmockito.GwtMockitoTestRunner;

/**
 * Test class for {@link org.geomajas.gwt2.plugin.print.client.widget.PrintWidgetPresenterImpl}.
 *
 * @author Jan Venstermans
 */
@RunWith(GwtMockitoTestRunner.class) // for the final class GeomajasServerExtension
public class PrintServiceImplTest extends PrintWidgetMockStart {

	private PrintServiceImpl printService;

	@GwtMock   // used because GeomajasServerExtension is final
	private GeomajasServerExtension serverExtensionMock;

	@Mock
	private CommandService commandServiceMock;

	@Mock
	private EndPointService endPointServiceMock;

	@Mock
	private TemplateBuilderDataProvider dataProviderMock;

	@Mock
	private TemplateBuilder builderMock;

	@Before
	public void before() {
		super.before();
		printService = new PrintServiceImpl();
		reset(mapPresenterMock);
		reset(printWidgetViewMock);

		// stub Geomajas framework for testing
		stub(serverExtensionMock.getCommandService()).toReturn(commandServiceMock);
		stub(serverExtensionMock.getEndPointService()).toReturn(endPointServiceMock);
		stub(endPointServiceMock.getDispatcherUrl()).toReturn("http://localhost:8888/d/");
		stub(mapPresenterMock.getViewPort()).toReturn(viewPortMock);
		stub(mapPresenterMock.getLayersModel()).toReturn(layersModelMock);
		stub(viewPortMock.getBounds()).toReturn(viewPortBounds);
		stub(mapPresenterMock.getConfiguration()).toReturn(mapConfigurationMock);
		stub(mapConfigurationMock.getHintValue(GeomajasServerExtension.MAPINFO)).toReturn(clientMapInfoMock);
		GeomajasServerExtension.setInstance(serverExtensionMock);
	}

	@Test
	public void printWithTemplateArgumentTest() {
		PrintTemplateInfo printTemplateInfo = new PrintTemplateInfo();
		PrintRequestInfo createInfo = new PrintRequestInfo();
		createInfo.setPrintTemplateInfo(printTemplateInfo);
		createInfo.setFileName("test.pdf");
		createInfo.setPostPrintAction(PostPrintAction.OPEN);
		Callback<PrintFinishedInfo, Void> callbackMock = mock(Callback.class);

		printService.print(createInfo, callbackMock);

		ArgumentCaptor<GwtCommand> commandCaptor = ArgumentCaptor.forClass(GwtCommand.class);
		ArgumentCaptor<CommandCallback> callbackCaptor = ArgumentCaptor.forClass(CommandCallback.class);
		verify(commandServiceMock).execute(commandCaptor.capture(), callbackCaptor.capture());

		// assert command
		GwtCommand command = commandCaptor.getValue();
		Assert.assertEquals(PrintGetTemplateRequest.COMMAND, command.getCommandName());
		Assert.assertTrue(command.getCommandRequest() instanceof PrintGetTemplateRequest);
		PrintGetTemplateRequest request = (PrintGetTemplateRequest) command.getCommandRequest();
		Assert.assertEquals(printTemplateInfo, request.getTemplate());

		// assert callback
		//callbackCaptor.getValue().execute();
	}
}