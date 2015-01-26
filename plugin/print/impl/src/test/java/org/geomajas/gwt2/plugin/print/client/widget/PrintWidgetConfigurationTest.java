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

package org.geomajas.gwt2.plugin.print.client.widget;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import junit.framework.Assert;

import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.plugin.print.client.event.PrintRequestFinishedEvent;
import org.geomajas.gwt2.plugin.print.client.event.PrintRequestHandler;
import org.geomajas.gwt2.plugin.print.client.event.PrintFinishedInfo;
import org.geomajas.gwt2.plugin.print.client.event.PrintRequestInfo;
import org.geomajas.gwt2.plugin.print.client.event.PrintRequestStartedEvent;
import org.geomajas.gwt2.plugin.print.client.layerbuilder.PrintableLayersModelBuilder;
import org.geomajas.gwt2.plugin.print.client.template.PrintableMapBuilder;
import org.geomajas.gwt2.plugin.print.client.template.TemplateBuilder;
import org.geomajas.gwt2.plugin.print.client.template.TemplateBuilderFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import com.google.gwt.core.client.Callback;
import com.google.gwtmockito.GwtMockitoTestRunner;

/**
 * Testse custom configuration of the {@link org.geomajas.gwt2.plugin.print.client.widget.PrintWidget}.
 * Tests are perfomed on {@link org.geomajas.gwt2.plugin.print.client.widget.PrintWidgetPresenterImpl}.
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
		PrintRequestHandler printFinishedHandlerMock = mock(PrintRequestHandler.class);
		presenter.setPrintRequestHandler(printFinishedHandlerMock);
		PrintFinishedInfo printFinishedInfo = new PrintFinishedInfo();

		presenter.print();

		verify(printFinishedHandlerMock).onPrintRequestStarted(any(PrintRequestStartedEvent.class));
		ArgumentCaptor<Callback> callbackCaptor = ArgumentCaptor.forClass(Callback.class);
		verify(printServiceMock).print(any(PrintRequestInfo.class), callbackCaptor.capture());
		callbackCaptor.getValue().onSuccess(printFinishedInfo);

		ArgumentCaptor<PrintRequestFinishedEvent> printFinishedEventCaptor = ArgumentCaptor.forClass(PrintRequestFinishedEvent.class);
		verify(printFinishedHandlerMock).onPrintRequestFinished(printFinishedEventCaptor.capture());
		PrintRequestFinishedEvent event = printFinishedEventCaptor.getValue();
		Assert.assertEquals(printFinishedInfo, event.getPrintFinishedInfo());
	}

	@Test
	public void registerLayerBuilderTest() {
		PrintableLayersModelBuilder printableLayerBuilderMock = mock(PrintableLayersModelBuilder.class);
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
}