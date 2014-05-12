package org.geomajas.gwt2.widget.client;

import org.geomajas.gwt2.widget.client.featureselectbox.FeatureSelectBoxView;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import com.google.gwtmockito.GwtMockitoTestRunner;


@RunWith(GwtMockitoTestRunner.class)
public class BaseTest {
	
	@Mock
	protected FeatureSelectBoxView featureSelectBoxView;

	@Before
	public void setUp() {
		CoreWidget.setViewFactory(new ViewFactory() {

			@Override
			public FeatureSelectBoxView createFeatureSelectBox() {
				return featureSelectBoxView;
			}
		});
	}
	

}
