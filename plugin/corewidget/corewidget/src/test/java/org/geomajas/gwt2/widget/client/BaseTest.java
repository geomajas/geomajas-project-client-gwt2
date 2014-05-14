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
package org.geomajas.gwt2.widget.client;

import static org.mockito.Mockito.when;

import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.feature.ServerFeatureService;
import org.geomajas.gwt2.widget.client.featureselectbox.view.FeatureSelectBoxView;
import org.geomajas.gwt2.widget.client.featureselectbox.view.FeatureSelectBoxViewFactory;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import com.google.gwtmockito.GwtMockitoTestRunner;


@RunWith(GwtMockitoTestRunner.class)
public abstract class BaseTest {

	@Mock
	protected FeatureSelectBoxView featureSelectBoxView;

	@Mock
	protected ServerFeatureService serverFeatureService;

	@Mock
	protected GeomajasImpl geomajasImpl;

	@Mock
	protected GeomajasServerExtension geomajasServerExtension;

	@Mock
	protected FeatureSelectBoxViewFactory viewFactory;

	@Before
	public void setUp() {
		GeomajasImpl.setInstance(geomajasImpl);
		GeomajasServerExtension.setInstance(geomajasServerExtension);
		CoreWidget.getInstance().setFeatureSelectBoxViewFactory(viewFactory);
		when(viewFactory.create()).thenReturn(featureSelectBoxView);
		when(geomajasServerExtension.getServerFeatureService()).thenReturn(serverFeatureService);
	}


}
