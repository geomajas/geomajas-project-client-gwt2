package org.geomajas.gwt2.widget.client.featureselectbox.presenter;

import static org.mockito.Mockito.verify;

import org.geomajas.gwt2.widget.client.BaseTest;
import org.junit.Test;
import org.mockito.Mock;

public class FeatureSelectBoxPresenterTest extends BaseTest {

	/* services */
	@Mock
	protected FeatureSelectBoxPresenter presenter;

	@Test
	public void testOnActive() {
		FeatureSelectBoxPresenter presenter = new FeatureSelectBoxPresenter();
		presenter.onActive();
		verify(featureSelectBoxView).show(true);	
	}

}
