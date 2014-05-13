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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.WktException;
import org.geomajas.geometry.service.WktService;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.ViewPortTransformationService;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.feature.FeatureMapFunction;
import org.geomajas.gwt2.client.map.feature.ServerFeatureService;
import org.geomajas.gwt2.client.map.layer.FeaturesSupported;
import org.geomajas.gwt2.widget.client.BaseTest;
import org.geomajas.gwt2.widget.client.featureselectbox.event.FeatureClickedEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

public class FeatureSelectBoxPresenterImplTest extends BaseTest {

	@Mock
	private MapPresenter mapPresenter;

	@Mock
	private ViewPort viewPort;

	@Mock
	private MapEventBus eventBus;

	@Mock
	private ViewPortTransformationService transformationService;

	@Before
	public void before() throws Exception {
		when(mapPresenter.getViewPort()).thenReturn(viewPort);
		when(mapPresenter.getEventBus()).thenReturn(eventBus);
		when(viewPort.getTransformationService()).thenReturn(transformationService);
		when(transformationService.transform(new Coordinate(0, 0), RenderSpace.SCREEN, RenderSpace.WORLD)).thenReturn(new Coordinate(0, 0));
		when(transformationService.transform(new Coordinate(10, 0), RenderSpace.SCREEN, RenderSpace.WORLD)).thenReturn(new Coordinate(200, 0));
	}

	@Test
	public void onFeatureSelected() {
		// TODO
	}

	@Test
	public void onActivate() {
		FeatureSelectBoxPresenterImpl presenter = new FeatureSelectBoxPresenterImpl(featureSelectBoxView);
		presenter.onActivate(mapPresenter);
		Assert.assertEquals(mapPresenter, presenter.getMapPresenter());
	}

	@Test
	public void onDeactivate() {

	}

	@Test
	public void onClick2() throws WktException {
		FeatureSelectBoxPresenterImpl presenter = new FeatureSelectBoxPresenterImpl(featureSelectBoxView);
		presenter.onActivate(mapPresenter);
		presenter.onClick(new Coordinate(100, 80));
		// capturing arguments to verify in special way or to perform callback !
		ArgumentCaptor<Geometry> geom = ArgumentCaptor.forClass(Geometry.class);
		ArgumentCaptor<FeatureMapFunction> function = ArgumentCaptor.forClass(FeatureMapFunction.class);
		verify(serverFeatureService).search(eq(mapPresenter), geom.capture(), eq(200.0),
				eq(ServerFeatureService.QueryType.INTERSECTS),
				eq(ServerFeatureService.SearchLayerType.SEARCH_ALL_LAYERS), eq(-1f), function.capture());
		// verify geometry
		Assert.assertEquals("POINT (100.0 80.0)", WktService.toWkt(geom.getValue()));
		// make the callback with some mocks
		Map<FeaturesSupported, List<Feature>> features = new HashMap<FeaturesSupported, List<Feature>>();
		FeaturesSupported layer = mock(FeaturesSupported.class);
		Feature f1 = mock(Feature.class);
		Feature f2 = mock(Feature.class);
		when(f1.getLabel()).thenReturn("label1");
		when(f2.getLabel()).thenReturn("label2");
		features.put(layer, Arrays.asList(f1,f2));
		function.getValue().execute(features);
		// verify view is called
		verify(featureSelectBoxView).clearLabels();
		verify(featureSelectBoxView).addLabel("label1");
		verify(featureSelectBoxView).addLabel("label2");
		verify(featureSelectBoxView).show(false);
	}

	@Test
	public void onClick1() throws WktException {
		FeatureSelectBoxPresenterImpl presenter = new FeatureSelectBoxPresenterImpl(featureSelectBoxView);
		presenter.onActivate(mapPresenter);
		presenter.onClick(new Coordinate(100, 80));
		// capturing arguments to verify in special way or to perform callback !
		ArgumentCaptor<Geometry> geom = ArgumentCaptor.forClass(Geometry.class);
		ArgumentCaptor<FeatureMapFunction> function = ArgumentCaptor.forClass(FeatureMapFunction.class);
		verify(serverFeatureService).search(eq(mapPresenter), geom.capture(), eq(200.0),
				eq(ServerFeatureService.QueryType.INTERSECTS),
				eq(ServerFeatureService.SearchLayerType.SEARCH_ALL_LAYERS), eq(-1f), function.capture());
		// verify geometry
		Assert.assertEquals("POINT (100.0 80.0)", WktService.toWkt(geom.getValue()));
		// make the callback with some mocks
		Map<FeaturesSupported, List<Feature>> features = new HashMap<FeaturesSupported, List<Feature>>();
		FeaturesSupported layer = mock(FeaturesSupported.class);
		Feature f1 = mock(Feature.class);
		when(f1.getLabel()).thenReturn("label1");
		features.put(layer, Arrays.asList(f1));
		function.getValue().execute(features);
		// verify event is sent
		verify(eventBus).fireEvent(any(FeatureClickedEvent.class));
	}

	@Test
	public void setPixelBuffer() {

	}

}
