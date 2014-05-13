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
package org.geomajas.gwt2.widget.client.featureselectbox.presenter;

import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.widget.client.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.logging.Logger;

import static org.mockito.Mockito.verify;

public class FeatureSelectBoxPresenterImplTest extends BaseTest {

	private Logger log = Logger.getLogger(FeatureSelectBoxPresenterImplTest.class.getName());

	@Mock
	private MapPresenter mapPresenter;

	@Before
	public void before() {
		log.info("Test started ...");
	}

	@Test
	public void testOnActive() {

		FeatureSelectBoxPresenterImpl presenter = new FeatureSelectBoxPresenterImpl();
		presenter.onActivate(mapPresenter);
		verify(featureSelectBoxView).show(true);

	}

}
