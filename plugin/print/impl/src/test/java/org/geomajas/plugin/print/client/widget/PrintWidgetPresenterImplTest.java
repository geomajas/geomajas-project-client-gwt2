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
import org.geomajas.plugin.print.client.template.PageSize;
import org.geomajas.plugin.print.command.dto.PrintGetTemplateRequest;
import org.geomajas.plugin.print.command.dto.PrintTemplateInfo;
import org.geomajas.plugin.print.component.ScaleBarComponent;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;

/**
 * Test class for {@link PrintWidgetPresenterImpl}.
 *
 * @author Jan Venstermans
 */
@RunWith(GwtMockitoTestRunner.class) // for the final class GeomajasServerExtension
public class PrintWidgetPresenterImplTest {

	private PrintWidgetPresenterImpl presenter;

	@Mock
	private MapPresenter mapPresenterMock;

	@Mock
	private PrintWidgetView printWidgetViewMock;

	@GwtMock   // used because GeomajasServerExtension is final
	private GeomajasServerExtension serverExtensionMock;

	@Mock
	private CommandService commandServiceMock;

	//@Mock
	//private ClientUserDataInfo infoMock;

	@Mock
	private ViewPort viewPortMock;

	@Mock
	private MapConfiguration mapConfigurationMock;
	@Mock
	private ClientMapInfo clientMapInfoMock;
	@Mock
	private LayersModel layersModelMock;

	// response dummy data
	private Bbox viewPortBounds = new Bbox(0,0,200,100);

	private ViewData viewData = new ViewData();

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		viewData.resetData();
		presenter = new PrintWidgetPresenterImpl(mapPresenterMock, viewData.getApplicationId(), printWidgetViewMock);
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

		// add dummy data to printWidgetViewMock
		stubViewMockWithViewData();
	}

	@Test
	public void setHandlerToViewOnConstructionTest() {
		presenter = new PrintWidgetPresenterImpl(mapPresenterMock, viewData.getApplicationId(), printWidgetViewMock);
		verify(printWidgetViewMock).setHandler(presenter);
	}

	@Test
	public void printWithTemplateArgumentTest() {
		PrintTemplateInfo templateInfo = new PrintTemplateInfo();
		Callback<String, Void> callbackMock = mock(Callback.class);

		presenter.print(templateInfo, callbackMock);

		ArgumentCaptor<GwtCommand> commandCaptor = ArgumentCaptor.forClass(GwtCommand.class);
		ArgumentCaptor<CommandCallback> callbackCaptor = ArgumentCaptor.forClass(CommandCallback.class);
		verify(commandServiceMock).execute(commandCaptor.capture(), callbackCaptor.capture());

		// assert command
		GwtCommand command = commandCaptor.getValue();
		Assert.assertEquals(PrintGetTemplateRequest.COMMAND, command.getCommandName());
		Assert.assertTrue(command.getCommandRequest() instanceof PrintGetTemplateRequest);
		PrintGetTemplateRequest request = (PrintGetTemplateRequest) command.getCommandRequest();
		Assert.assertEquals(templateInfo, request.getTemplate());

		// assert callback
		//callbackCaptor.getValue().execute();
	}

	@Test
	public void printMethodWithoutArgumentsUsesViewDataToCreateTemplateTest() {
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

		capturePrintGetTemplateCommandAndAssertPage();
	}

	@Test
	public void noScaleBarTest() {
		viewData.setWithScaleBar(false);
		stubViewMockWithViewData();

		presenter.print();

		capturePrintGetTemplateCommandAndAssertPage();
	}

	@Test
	public void noArrowTest() {
		viewData.setWithArrow(false);
		stubViewMockWithViewData();

		presenter.print();

		capturePrintGetTemplateCommandAndAssertPage();
	}

	@Test
	public void noLandscapeTest() {
		viewData.setLandscape(false);
		stubViewMockWithViewData();

		presenter.print();

		capturePrintGetTemplateCommandAndAssertPage();
	}

	@Test
	public void pageSizeA4Test() {
		viewData.setPageSize(PageSize.A4);
		stubViewMockWithViewData();

		presenter.print();

		capturePrintGetTemplateCommandAndAssertPage();
	}

	@Test
	public void rasterDpi200Test() {
		viewData.setRasterDpi(200);
		stubViewMockWithViewData();

		presenter.print();

		capturePrintGetTemplateCommandAndAssertPage();
	}

	private PrintComponentInfo getChildOfType(List<PrintComponentInfo> children, Class<? extends PrintComponentInfo> typeClass) {
		return  getChildOfTypeAndTag(children, typeClass, null);
	}

	private PrintComponentInfo getChildOfTypeAndTag(List<PrintComponentInfo> children,
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

	private void capturePrintGetTemplateCommandAndAssertPage() {
		//attempt to get data from view
		ArgumentCaptor<GwtCommand> commandCaptor = ArgumentCaptor.forClass(GwtCommand.class);
		ArgumentCaptor<CommandCallback> callbackCaptor = ArgumentCaptor.forClass(CommandCallback.class);
		verify(commandServiceMock).execute(commandCaptor.capture(), callbackCaptor.capture());

		// assert command
		GwtCommand command = commandCaptor.getValue();
		Assert.assertEquals(PrintGetTemplateRequest.COMMAND, command.getCommandName());
		Assert.assertTrue(command.getCommandRequest() instanceof PrintGetTemplateRequest);
		PrintGetTemplateRequest request = (PrintGetTemplateRequest) command.getCommandRequest();
		assertPageComponentInfo(request.getTemplate().getPage());
	}

	private void assertPageComponentInfo(PageComponentInfo pageComponentInfo) {
		assertBounds(pageComponentInfo.getLayoutConstraint(), viewData.isLandscape(), viewData.getPageSize());

		MapComponentInfo mapComponent = (MapComponentInfo) getChildOfType(pageComponentInfo.getChildren(), MapComponentInfo.class);
		Assert.assertNotNull(mapComponent);
		Assert.assertEquals(viewData.getApplicationId(), mapComponent.getApplicationId());
		Assert.assertEquals(viewData.getRasterDpi(), (int) mapComponent.getRasterResolution());

		// check scalebar
		ScaleBarComponentInfo scaleBarComponent = (ScaleBarComponentInfo)
				getChildOfType(mapComponent.getChildren(), ScaleBarComponentInfo.class);
		Assert.assertEquals(viewData.isWithScaleBar(), scaleBarComponent != null);

		// check scalebar
		ImageComponentInfo imageComponentInfo = (ImageComponentInfo)
				getChildOfTypeAndTag(mapComponent.getChildren(), ImageComponentInfo.class, "arrow");
		Assert.assertEquals(viewData.isWithArrow(), imageComponentInfo != null);
	}

	private void assertBounds(LayoutConstraintInfo bounds, boolean landscape, PageSize pageSize) {
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

	private void stubViewMockWithViewData() {
		stub(printWidgetViewMock.getPageSize()).toReturn(viewData.getPageSize());
		stub(printWidgetViewMock.getRasterDpi()).toReturn(viewData.getRasterDpi());
		stub(printWidgetViewMock.isLandscape()).toReturn(viewData.isLandscape());
		stub(printWidgetViewMock.isWithArrow()).toReturn(viewData.isWithArrow());
		stub(printWidgetViewMock.isWithScaleBar()).toReturn(viewData.isWithScaleBar());
	}

	public class ViewData {
		private String applicationId;
		private PageSize pageSize;
		private int rasterDpi;
		private boolean landscape;
		private boolean withArrow;
		private boolean withScaleBar;

		public void resetData() {
			applicationId = "applicationId";
			pageSize = PageSize.A2;
			rasterDpi = 123;
			landscape = true;
			withArrow = true;
			withScaleBar = true;
		}

		public String getApplicationId() {
			return applicationId;
		}

		public void setApplicationId(String applicationId) {
			this.applicationId = applicationId;
		}

		public PageSize getPageSize() {
			return pageSize;
		}

		public void setPageSize(PageSize pageSize) {
			this.pageSize = pageSize;
		}

		public int getRasterDpi() {
			return rasterDpi;
		}

		public void setRasterDpi(int rasterDpi) {
			this.rasterDpi = rasterDpi;
		}

		public boolean isLandscape() {
			return landscape;
		}

		public void setLandscape(boolean landscape) {
			this.landscape = landscape;
		}

		public boolean isWithArrow() {
			return withArrow;
		}

		public void setWithArrow(boolean withArrow) {
			this.withArrow = withArrow;
		}

		public boolean isWithScaleBar() {
			return withScaleBar;
		}

		public void setWithScaleBar(boolean withScaleBar) {
			this.withScaleBar = withScaleBar;
		}
	}
}