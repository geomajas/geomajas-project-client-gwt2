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
import com.google.gwtmockito.GwtMockitoTestRunner;
import junit.framework.Assert;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.layer.LayersModel;
import org.geomajas.plugin.print.client.Print;
import org.geomajas.plugin.print.client.PrintService;
import org.geomajas.plugin.print.client.event.PrintFinishedEvent;
import org.geomajas.plugin.print.client.event.PrintFinishedHandler;
import org.geomajas.plugin.print.client.event.PrintFinishedInfo;
import org.geomajas.plugin.print.client.template.PrintableLayerBuilder;
import org.geomajas.plugin.print.client.template.PrintableMapBuilder;
import org.geomajas.plugin.print.client.template.TemplateBuilder;
import org.geomajas.plugin.print.client.template.TemplateBuilderFactory;
import org.geomajas.plugin.print.command.dto.PrintTemplateInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

/**
 * Testse custom configuration of the {@link org.geomajas.plugin.print.client.widget.PrintWidget}.
 * Tests are perfomed on {@link org.geomajas.plugin.print.client.widget.PrintWidgetPresenterImpl}.
 *
 * @author Jan Venstermans
 */
@RunWith(GwtMockitoTestRunner.class) // for the final class GeomajasServerExtension
public class PrintWidgetConfigurationTest extends PrintWidgetMockStart {

	@Mock
	private Layer layerMock;

	@Before
	public void before() {
		super.before();
		// overwrite layersModelMock stubs
		stub(layersModelMock.getLayerCount()).toReturn(1);
		stub(layersModelMock.getLayer(0)).toReturn(layerMock);
	}

	@Test
	public void customPrintFinishedHandlerTest() {
		PrintFinishedHandler printFinishedHandlerMock = mock(PrintFinishedHandler.class);
		presenter.setPrintFinishedHandler(printFinishedHandlerMock);
		PrintFinishedInfo printFinishedInfo = new PrintFinishedInfo();

		presenter.print();

		ArgumentCaptor<Callback> callbackCaptor = ArgumentCaptor.forClass(Callback.class);
		verify(printServiceMock).print(any(PrintTemplateInfo.class), callbackCaptor.capture());
		callbackCaptor.getValue().onSuccess(printFinishedInfo);

		ArgumentCaptor<PrintFinishedEvent> printFinishedEventCaptor = ArgumentCaptor.forClass(PrintFinishedEvent.class);
		verify(printFinishedHandlerMock).onPrintFinished(printFinishedEventCaptor.capture());
		PrintFinishedEvent event = printFinishedEventCaptor.getValue();
		Assert.assertEquals(printFinishedInfo, event.getPrintFinishedInfo());
	}

	@Test
	public void registerLayerBuilderTest() {
		PrintableLayerBuilder printableLayerBuilderMock = mock(PrintableLayerBuilder.class);
		stub(printableLayerBuilderMock.supports(layerMock)).toReturn(true);
		presenter.registerLayerBuilder(printableLayerBuilderMock);

		presenter.print();

		verify(printableLayerBuilderMock).supports(layerMock);
		verify(printableLayerBuilderMock).build(eq(mapPresenterMock), eq(layerMock), any(Bbox.class), anyDouble());
	}

	@Test
	public void customTemplateBuilderFactoryTest() {
		TemplateBuilderFactory templateBuilderFactoryMock = mock(TemplateBuilderFactory.class);
		TemplateBuilder templateBuilderMock = mock(TemplateBuilder.class);
		stub(templateBuilderFactoryMock.createTemplateBuilder(any(PrintableMapBuilder.class))).toReturn(templateBuilderMock);
		presenter.setTemplateBuilderFactory(templateBuilderFactoryMock);

		presenter.print();

		verify(templateBuilderFactoryMock).createTemplateBuilder(any(PrintableMapBuilder.class));
	}

	@Test
	public void customMapBuilderTest() {
		PrintableMapBuilder mapBuilderMock = mock(PrintableMapBuilder.class);
		TemplateBuilderFactory templateBuilderFactoryMock = mock(TemplateBuilderFactory.class);
		TemplateBuilder templateBuilderMock = mock(TemplateBuilder.class);
		stub(templateBuilderFactoryMock.createTemplateBuilder(any(PrintableMapBuilder.class))).toReturn(templateBuilderMock);
		presenter.setMapBuilder(mapBuilderMock);
		presenter.setTemplateBuilderFactory(templateBuilderFactoryMock);

		presenter.print();

		verify(templateBuilderFactoryMock).createTemplateBuilder(mapBuilderMock);
	}
}