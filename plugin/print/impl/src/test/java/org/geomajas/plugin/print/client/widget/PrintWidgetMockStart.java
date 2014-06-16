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
import org.geomajas.gwt2.client.map.layer.LayersModel;
import org.geomajas.plugin.print.client.Print;
import org.geomajas.plugin.print.client.PrintService;
import org.geomajas.plugin.print.client.ViewMockData;
import org.geomajas.plugin.print.client.event.PrintRequestInfo;
import org.geomajas.plugin.print.client.template.PageSize;
import org.geomajas.plugin.print.command.dto.PrintTemplateInfo;
import org.geomajas.plugin.print.component.dto.ImageComponentInfo;
import org.geomajas.plugin.print.component.dto.LayoutConstraintInfo;
import org.geomajas.plugin.print.component.dto.MapComponentInfo;
import org.geomajas.plugin.print.component.dto.PageComponentInfo;
import org.geomajas.plugin.print.component.dto.PrintComponentInfo;
import org.geomajas.plugin.print.component.dto.ScaleBarComponentInfo;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

/**
 * Test class for {@link org.geomajas.plugin.print.client.widget.PrintWidgetPresenterImpl}.
 *
 * @author Jan Venstermans
 */
@RunWith(GwtMockitoTestRunner.class) // for the final class GeomajasServerExtension
public abstract class PrintWidgetMockStart {

	protected PrintWidgetPresenterImpl presenter;

	@Mock
	protected MapPresenter mapPresenterMock;

	@Mock
	protected PrintWidgetView printWidgetViewMock;

	@Mock
	protected PrintService printServiceMock;

	@Mock
	protected ViewPort viewPortMock;

	@Mock
	protected MapConfiguration mapConfigurationMock;
	@Mock
	protected ClientMapInfo clientMapInfoMock;
	@Mock
	protected LayersModel layersModelMock;

	// response dummy data
	protected Bbox viewPortBounds = new Bbox(0,0,200,100);

	protected ViewMockData viewData = new ViewMockData();

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		viewData.resetData();
		Print.getInstance().setPrintService(printServiceMock);
		presenter = new PrintWidgetPresenterImpl(mapPresenterMock, viewData.getApplicationId(), printWidgetViewMock);
		reset(mapPresenterMock);
		reset(printWidgetViewMock);

		// stub Geomajas framework for testing
		stub(mapPresenterMock.getViewPort()).toReturn(viewPortMock);
		stub(mapPresenterMock.getLayersModel()).toReturn(layersModelMock);
		stub(layersModelMock.getLayerCount()).toReturn(0);
		stub(viewPortMock.getBounds()).toReturn(viewPortBounds);
		stub(mapPresenterMock.getConfiguration()).toReturn(mapConfigurationMock);
		stub(mapConfigurationMock.getHintValue(GeomajasServerExtension.MAPINFO)).toReturn(clientMapInfoMock);

		// add dummy data to printWidgetViewMock
		stubViewMockWithViewData();
	}

	protected PrintComponentInfo getChildOfType(List<PrintComponentInfo> children, Class<? extends PrintComponentInfo> typeClass) {
		return  getChildOfTypeAndTag(children, typeClass, null);
	}

	protected PrintComponentInfo getChildOfTypeAndTag(List<PrintComponentInfo> children,
											  Class<? extends PrintComponentInfo> typeClass, String tag) {
		for (PrintComponentInfo component : children) {
			if (typeClass.isInstance(component)) {
				if (tag == null || component.getTag().equals(tag)) {
					return component;
				}
			}
		}
		return null;
	}

	protected void captureServiceCallAndAssertTemplate() {
		//attempt to get data from view
		ArgumentCaptor<PrintRequestInfo> templateCaptor = ArgumentCaptor.forClass(PrintRequestInfo.class);
		ArgumentCaptor<Callback> callbackCaptor = ArgumentCaptor.forClass(Callback.class);
		verify(printServiceMock).print(templateCaptor.capture(), callbackCaptor.capture());

		// assert command
		PrintTemplateInfo templateInfo = templateCaptor.getValue().getPrintTemplateInfo();
		assertPageComponentInfo(templateInfo.getPage());
	}

	protected void assertPageComponentInfo(PageComponentInfo pageComponentInfo) {
		assertBounds(pageComponentInfo.getLayoutConstraint(), viewData.isLandscape(), viewData.getPageSize());

		MapComponentInfo mapComponent = (MapComponentInfo) getChildOfType(pageComponentInfo.getChildren(), MapComponentInfo.class);
		Assert.assertNotNull(mapComponent);
		Assert.assertEquals(viewData.getApplicationId(), mapComponent.getApplicationId());
		Assert.assertEquals((int) viewData.getRasterDpi(), (int) mapComponent.getRasterResolution());

		// check scalebar
		ScaleBarComponentInfo scaleBarComponent = (ScaleBarComponentInfo)
				getChildOfType(mapComponent.getChildren(), ScaleBarComponentInfo.class);
		Assert.assertEquals(viewData.isWithScaleBar(), scaleBarComponent != null);

		// check scalebar
		ImageComponentInfo imageComponentInfo = (ImageComponentInfo)
				getChildOfTypeAndTag(mapComponent.getChildren(), ImageComponentInfo.class, "arrow");
		Assert.assertEquals(viewData.isWithArrow(), imageComponentInfo != null);
	}

	protected void assertBounds(LayoutConstraintInfo bounds, boolean landscape, PageSize pageSize) {
		double width = bounds.getWidth();
		double height = bounds.getHeight();
		// assert landscape
		if (landscape) {
			Assert.assertTrue(width >= height);
		} else {
			Assert.assertTrue(width <= height);
		}

		double pageSizeWidth = pageSize.getWidth();
		double pageSizeHeight = pageSize.getHeight();
		Assert.assertEquals(Math.max(width, height), Math.max(pageSizeWidth, pageSizeHeight), 0.005);
		Assert.assertEquals(Math.min(width, height), Math.min(pageSizeWidth, pageSizeHeight), 0.005);
	}

	protected void stubViewMockWithViewData() {
		stub(printWidgetViewMock.getTemplateBuilderDataProvider()).toReturn(viewData);
	}

}