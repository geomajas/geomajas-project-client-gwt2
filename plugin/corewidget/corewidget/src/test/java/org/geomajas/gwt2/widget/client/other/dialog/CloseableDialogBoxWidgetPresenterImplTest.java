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
package org.geomajas.gwt2.widget.client.other.dialog;

import org.geomajas.gwt2.widget.client.BaseTest;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.verify;

public class CloseableDialogBoxWidgetPresenterImplTest extends BaseTest {

	private CloseableDialogBoxWidgetPresenterImpl presenter;

	@Before
	public void before() throws Exception {
		presenter = new CloseableDialogBoxWidgetPresenterImpl(closeableDialogBoxWidgetView);
	}

	@Test
	public void onShow() {

		presenter.show();
		verify(closeableDialogBoxWidgetView).show();

	}

	@Test
	public void onHide() {

		presenter.hide();
		verify(closeableDialogBoxWidgetView).hide();

	}

}
