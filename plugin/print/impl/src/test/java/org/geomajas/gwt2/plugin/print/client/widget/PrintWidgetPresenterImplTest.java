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

package org.geomajas.gwt2.plugin.print.client.widget;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.geomajas.gwt2.plugin.print.client.template.PageSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.any;

/**
 * Test class for {@link org.geomajas.gwt2.plugin.print.client.widget.PrintWidgetPresenterImpl}.
 *
 * @author Jan Venstermans
 */
@RunWith(GwtMockitoTestRunner.class) // for the final class GeomajasServerExtension
public class PrintWidgetPresenterImplTest extends PrintWidgetMockStart {


	@Before
	public void before() {
		super.before();
	}

	@Test
	public void setHandlerToViewOnConstructionTest() {
		presenter = new PrintWidgetPresenterImpl(mapPresenterMock, viewData.getApplicationId(), printWidgetViewMock);
		verify(printWidgetViewMock).setHandler(presenter);
	}

	@Test
	public void dummyDataTest() {
		presenter.print();

		captureServiceCallAndAssertTemplate();
	}

	@Test
	public void noScaleBarTest() {
		viewData.setWithScaleBar(false);
		stubViewMockWithViewData();

		presenter.print();

		captureServiceCallAndAssertTemplate();
	}

	@Test
	public void noArrowTest() {
		viewData.setWithArrow(false);
		stubViewMockWithViewData();

		presenter.print();

		captureServiceCallAndAssertTemplate();
	}

	@Test
	public void noLandscapeTest() {
		viewData.setLandscape(false);
		stubViewMockWithViewData();

		presenter.print();

		captureServiceCallAndAssertTemplate();
	}

	@Test
	public void pageSizeA4Test() {
		viewData.setPageSize(PageSize.A4);
		stubViewMockWithViewData();

		presenter.print();

		captureServiceCallAndAssertTemplate();
	}

	@Test
	public void rasterDpi200Test() {
		viewData.setRasterDpi(200);
		stubViewMockWithViewData();

		presenter.print();

		captureServiceCallAndAssertTemplate();
	}
}