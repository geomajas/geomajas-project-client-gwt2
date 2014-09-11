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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.stub;
import junit.framework.Assert;

import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.plugin.print.client.Print;
import org.geomajas.gwt2.plugin.print.client.PrintViewFactory;
import org.geomajas.gwt2.plugin.print.client.widget.PrintWidget;
import org.geomajas.gwt2.plugin.print.client.widget.PrintWidgetView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwtmockito.GwtMockitoTestRunner;

/**
 * Test class for {@link org.geomajas.gwt2.plugin.print.client.widget.PrintWidgetPresenterImpl}.
 *
 * @author Jan Venstermans
 */
@RunWith(GwtMockitoTestRunner.class) // for the final class GeomajasServerExtension
public class PrintWidgetTest {

	private PrintWidget printWidget;

	@Mock
	private MapPresenter mapPresenterMock;

	@Mock
	private PrintWidgetView printWidgetViewMock;

	@Mock
	private Widget widgetMock;

	@Before
	public void constructDefaultWidget() {
		MockitoAnnotations.initMocks(this);
		Print.getInstance().setViewFactory(new PrintViewFactory() {
			public PrintWidgetView createPrintWidgetView() {
				return printWidgetViewMock;
			}
		});
		printWidget = new PrintWidget(mapPresenterMock, "applicationId");
		reset(mapPresenterMock);
		reset(printWidgetViewMock);

		stub(printWidgetViewMock.asWidget()).toReturn(widgetMock);
	}

	@Test
	public void defaultWidgetAsWidgetTest() {
		Assert.assertEquals(widgetMock, printWidget.asWidget());
	}

	@Test
	public void customViewAsWidgetTest() {
		PrintWidgetView customPrintWidgetViewMock = mock(PrintWidgetView.class);
		Widget customWidgetMock = mock(Widget.class);
		stub(customPrintWidgetViewMock.asWidget()).toReturn(customWidgetMock);
		printWidget = new PrintWidget(mapPresenterMock, "applicationId", customPrintWidgetViewMock);
		Assert.assertEquals(customWidgetMock, printWidget.asWidget());
	}
}