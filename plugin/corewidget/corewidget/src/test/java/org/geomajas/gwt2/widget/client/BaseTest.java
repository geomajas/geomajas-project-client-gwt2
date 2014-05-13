package org.geomajas.gwt2.widget.client;

import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.feature.ServerFeatureService;
import org.geomajas.gwt2.widget.client.featureselectbox.view.FeatureSelectBoxView;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import com.google.gwtmockito.GwtMockitoTestRunner;
import static org.mockito.Mockito.when;


@RunWith(GwtMockitoTestRunner.class)
public class BaseTest {

	@Mock
	protected FeatureSelectBoxView featureSelectBoxView;

	@Mock
	protected ServerFeatureService serverFeatureService;

	@Mock
	protected GeomajasImpl geomajasImpl;

	@Mock
	protected CoreWidget coreWidget;

	@Mock
	protected GeomajasServerExtension geomajasServerExtension;
	
	@Mock
	protected ViewFactory viewFactory;

	@Before
	public void setUp() {
		GeomajasImpl.setInstance(geomajasImpl);
		CoreWidget.setInstance(coreWidget);
		GeomajasServerExtension.setInstance(geomajasServerExtension);
		when(coreWidget.getViewFactory()).thenReturn(viewFactory);
		when(viewFactory.createFeatureSelectBox()).thenReturn(featureSelectBoxView);
		when(geomajasServerExtension.getServerFeatureService()).thenReturn(serverFeatureService);		
	}


}
