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
package org.geomajas.gwt2.plugin.corewidget.client;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.feature.ServerFeatureService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.Mockito.when;


@RunWith(GwtMockitoTestRunner.class)
public abstract class BaseTest {

	@Mock
	protected ServerFeatureService serverFeatureService;

	@Mock
	protected GeomajasImpl geomajasImpl;

	@Mock
	protected GeomajasServerExtension geomajasServerExtension;

	@Before
	public void setUp() {
		GeomajasImpl.setInstance(geomajasImpl);
		GeomajasServerExtension.setInstance(geomajasServerExtension);
		when(geomajasServerExtension.getServerFeatureService()).thenReturn(serverFeatureService);
	}


}
