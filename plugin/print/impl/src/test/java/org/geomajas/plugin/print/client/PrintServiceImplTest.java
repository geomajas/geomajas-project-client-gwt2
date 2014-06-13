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
import com.google.gwtmockito.GwtMock;
import com.google.gwtmockito.GwtMockitoTestRunner;
import junit.framework.Assert;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.LayersModel;
import org.geomajas.gwt2.client.service.CommandService;
import org.geomajas.plugin.print.client.event.PrintFinishedInfo;
import org.geomajas.plugin.print.client.event.PrintRequestInfo;
import org.geomajas.plugin.print.client.template.PageSize;
import org.geomajas.plugin.print.client.template.TemplateBuilder;
import org.geomajas.plugin.print.client.template.TemplateBuilderDataProvider;
import org.geomajas.plugin.print.client.widget.PrintWidgetMockStart;
import org.geomajas.plugin.print.client.widget.PrintWidgetView;
import org.geomajas.plugin.print.command.dto.PrintGetTemplateRequest;
import org.geomajas.plugin.print.command.dto.PrintTemplateInfo;
import org.geomajas.plugin.print.component.dto.ImageComponentInfo;
import org.geomajas.plugin.print.component.dto.LayoutConstraintInfo;
import org.geomajas.plugin.print.component.dto.MapComponentInfo;
import org.geomajas.plugin.print.component.dto.PageComponentInfo;
import org.geomajas.plugin.print.component.dto.PrintComponentInfo;
import org.geomajas.plugin.print.component.dto.ScaleBarComponentInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.constraints.AssertTrue;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

/**
 * Test class for {@link org.geomajas.plugin.print.client.widget.PrintWidgetPresenterImpl}.
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