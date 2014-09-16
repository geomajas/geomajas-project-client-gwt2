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

package org.geomajas.gwt2.client.map.layer;

import junit.framework.Assert;
import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.GeomajasServerExtension;
import org.geomajas.gwt2.client.map.MapConfiguration;
import org.geomajas.gwt2.client.map.MapConfigurationImpl;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.MapEventBusImpl;
import org.geomajas.gwt2.client.map.TestConfigUtil;
import org.geomajas.gwt2.client.map.ViewPort;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

/**
 * Test-cases for the {@link org.geomajas.gwt2.client.map.layer.RasterServerLayerImpl#isShowing()} method.
 *
 * @author Jan Venstermans
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "beansContext.xml", "layerBeans1.xml", "mapBeans.xml" })
public class VectorServerLayerIsShowingTest {

	@Autowired
	@Qualifier(value = "mapBeans")
	private ClientMapInfo mapInfo;

	private ClientVectorLayerInfo clientVectorLayerInfo;

	private MapEventBus eventBus;

	private ViewPort viewPortMock;

	private MapConfiguration mapConfig;

	private static final double resolutionMin = 3.0;
	private static final double resolutionCenter = resolutionMin + 1;
	private static final double resolutionMax = resolutionMin + 2;

	private Double[] resolutions = new Double[] { resolutionMax, resolutionCenter, resolutionMin};

	@Before
	public void initialize() {
		// Initialize main components:
		eventBus = new MapEventBusImpl(this, GeomajasImpl.getInstance().getEventBus());
		viewPortMock = Mockito.mock(ViewPort.class);

		// server layer
		clientVectorLayerInfo = new ClientVectorLayerInfo();
		VectorLayerInfo vectorLayerInfo = new VectorLayerInfo();
		clientVectorLayerInfo.setLayerInfo(vectorLayerInfo);
		clientVectorLayerInfo.setMaxExtent(vectorLayerInfo.getMaxExtent());
		clientVectorLayerInfo.setMinimumScale(new ScaleInfo(1 / resolutionMax));
		clientVectorLayerInfo.setMaximumScale(new ScaleInfo(1 / resolutionMin));

		mapConfig = new MapConfigurationImpl();
		mapConfig.setHintValue(GeomajasServerExtension.MAPINFO, mapInfo);
	}

	@Test
	public void testVectorLayerIsShowingOnInvisible() {
		clientVectorLayerInfo.setVisible(false);

		VectorServerLayerImpl layer = new VectorServerLayerImpl(mapConfig, clientVectorLayerInfo, viewPortMock, eventBus);

		Assert.assertFalse(layer.isShowing());
	}

	@Test
	public void testVectorLayerIsShowingOnVisibleViewPortLargerThanMaxScale() {
		clientVectorLayerInfo.setVisible(true);
		Mockito.when(viewPortMock.getResolution()).thenReturn(resolutionMin - 1);

		VectorServerLayerImpl layer = new VectorServerLayerImpl(mapConfig, clientVectorLayerInfo, viewPortMock, eventBus);

		Assert.assertFalse(layer.isShowing());
	}

	@Test
	public void testVectorLayerIsShowingOnVisibleViewPortLowerThanMinScale() {
		clientVectorLayerInfo.setVisible(true);
		Mockito.when(viewPortMock.getResolution()).thenReturn(resolutionMax + 1);

		VectorServerLayerImpl layer = new VectorServerLayerImpl(mapConfig, clientVectorLayerInfo, viewPortMock, eventBus);

		Assert.assertFalse(layer.isShowing());
	}

	@Test
	public void testVectorLayerIsShowingOnVisibleViewPortMinScaleInclusive() {
		clientVectorLayerInfo.setVisible(true);
		Mockito.when(viewPortMock.getResolution()).thenReturn(resolutionMax);

		VectorServerLayerImpl layer = new VectorServerLayerImpl(mapConfig, clientVectorLayerInfo, viewPortMock, eventBus);

		Assert.assertTrue(layer.isShowing());
	}

	@Test
	public void testVectorLayerIsShowingOnVisibleViewPortMaxScaleExclusive() {
		clientVectorLayerInfo.setVisible(true);
		Mockito.when(viewPortMock.getResolution()).thenReturn(resolutionMin);

		VectorServerLayerImpl layer = new VectorServerLayerImpl(mapConfig, clientVectorLayerInfo, viewPortMock, eventBus);

		Assert.assertFalse(layer.isShowing());
	}

	@Test
	public void testVectorLayerIsShowingOnVisibleViewPortScaleBetweenMaxAndMin() {
		clientVectorLayerInfo.setVisible(true);
		Mockito.when(viewPortMock.getResolution()).thenReturn(resolutionCenter);

		VectorServerLayerImpl layer = new VectorServerLayerImpl(mapConfig, clientVectorLayerInfo, viewPortMock, eventBus);

		Assert.assertTrue(layer.isShowing());
	}
}