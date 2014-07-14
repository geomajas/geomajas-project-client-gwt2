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

import com.google.gwt.core.client.Callback;
import com.google.gwtmockito.GwtMock;
import com.google.gwtmockito.GwtMockitoTestRunner;
import junit.framework.Assert;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.service.CommandService;
import org.geomajas.plugin.print.client.PrintServiceImpl;
import org.geomajas.plugin.print.client.event.PrintFinishedInfo;
import org.geomajas.plugin.print.client.event.PrintRequestInfo;
import org.geomajas.plugin.print.client.template.PageSize;
import org.geomajas.plugin.print.client.template.TemplateBuilder;
import org.geomajas.plugin.print.client.template.TemplateBuilderDataProvider;
import org.geomajas.plugin.print.client.widget.PrintWidgetMockStart;
import org.geomajas.plugin.print.command.dto.PrintGetTemplateRequest;
import org.geomajas.plugin.print.command.dto.PrintTemplateInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

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
public class PrintUtilImplTest extends PrintWidgetMockStart {

	private PrintUtilImpl printUtil;

	@Mock
	private TemplateBuilderDataProvider dataProviderMock;

	@Mock
	private TemplateBuilder builderMock;

	@Before
	public void before() {
		super.before();
		printUtil = new PrintUtilImpl();
		stubDataProvider();
	}

	@Test
	public void copyProviderDataToBuilderTest() {
		printUtil.copyProviderDataToBuilder(builderMock, dataProviderMock);

		verify(dataProviderMock).getTitle();
		verify(builderMock).setTitleText(viewData.getTitle());
		verify(dataProviderMock).isWithArrow();
		verify(builderMock).setWithArrow(viewData.isWithArrow());
		verify(dataProviderMock).isWithScaleBar();
		verify(builderMock).setWithScaleBar(viewData.isWithScaleBar());
		verify(dataProviderMock).getRasterDpi();
		verify(builderMock).setRasterDpi(viewData.getRasterDpi());

		assertDimensions();
	}

	@Test
	public void landscapeTest() {
		viewData.setLandscape(true);
		stubDataProvider();

		printUtil.copyProviderDataToBuilder(builderMock, dataProviderMock);

		assertDimensions();
	}

	@Test
	public void noLandscapeTest() {
		viewData.setLandscape(false);
		stubDataProvider();

		printUtil.copyProviderDataToBuilder(builderMock, dataProviderMock);

		assertDimensions();
	}

	@Test
	public void noLandscapeOtherPageSizeTest() {
		viewData.setLandscape(false);
		viewData.setPageSize(PageSize.A4);
		stubDataProvider();

		printUtil.copyProviderDataToBuilder(builderMock, dataProviderMock);

		assertDimensions();
	}

	private void stubDataProvider() {
		stub(dataProviderMock.getTitle()).toReturn(viewData.getTitle());
		stub(dataProviderMock.getPageSize()).toReturn(viewData.getPageSize());
		stub(dataProviderMock.getRasterDpi()).toReturn(viewData.getRasterDpi());
		stub(dataProviderMock.isLandscape()).toReturn(viewData.isLandscape());
		stub(dataProviderMock.isWithArrow()).toReturn(viewData.isWithArrow());
		stub(dataProviderMock.isWithScaleBar()).toReturn(viewData.isWithScaleBar());
	}

	private void assertDimensions() {
		ArgumentCaptor<Double> heightCaptor = ArgumentCaptor.forClass(Double.class);
		ArgumentCaptor<Double> widthCaptor = ArgumentCaptor.forClass(Double.class);
		verify(builderMock).setPageHeight(heightCaptor.capture());
		verify(builderMock).setPageWidth(widthCaptor.capture());
		double height = heightCaptor.getValue();
		double width = widthCaptor.getValue();
		if (width != height) {  // if square, does not matter
			Assert.assertEquals(viewData.isLandscape(), width > height);
		}
		double pageSizeWidth = viewData.getPageSize().getWidth();
		double pageSizeHeight = viewData.getPageSize().getHeight();
		Assert.assertEquals(Math.max(width, height), Math.max(pageSizeWidth, pageSizeHeight), 0.005);
		Assert.assertEquals(Math.min(width, height), Math.min(pageSizeWidth, pageSizeHeight), 0.005);
	}
}