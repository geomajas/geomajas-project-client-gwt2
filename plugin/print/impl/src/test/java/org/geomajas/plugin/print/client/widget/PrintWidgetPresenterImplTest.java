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
import org.geomajas.plugin.print.client.PrintServiceImpl;
import org.geomajas.plugin.print.client.event.PrintFinishedEvent;
import org.geomajas.plugin.print.client.event.PrintFinishedHandler;
import org.geomajas.plugin.print.client.event.PrintFinishedInfo;
import org.geomajas.plugin.print.client.template.PageSize;
import org.geomajas.plugin.print.client.template.PrintableLayerBuilder;
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

import java.util.List;

import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.any;

/**
 * Test class for {@link PrintWidgetPresenterImpl}.
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
	public void printMethodUsesViewDataToCreateTemplateTest() {
		presenter.print();

		//attempt to get data from view
		verify(printWidgetViewMock).isLandscape();
		verify(printWidgetViewMock).isWithArrow();
		verify(printWidgetViewMock).isWithScaleBar();
		verify(printWidgetViewMock).getPageSize();
		verify(printWidgetViewMock).getRasterDpi();
		verify(printWidgetViewMock).getTitle();
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