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
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
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
 * Test-cases for the {@link RasterServerLayerImpl#isShowing()} method.
 *
 * @author Jan Venstermans
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "beansContext.xml", "layerBeans1.xml", "mapBeans.xml" })
public class RasterServerLayerIsShowingTest {

	@Autowired
	@Qualifier(value = "mapBeans")
	private ClientMapInfo mapInfo;

	private ClientRasterLayerInfo layerInfo;

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
		layerInfo = new ClientRasterLayerInfo();

		RasterLayerInfo rasterLayerInfo = new RasterLayerInfo();
		rasterLayerInfo.setTileHeight(256);
		rasterLayerInfo.setTileWidth(256);
		rasterLayerInfo.setResolutions(Arrays.asList(resolutions));
		layerInfo.setLayerInfo(rasterLayerInfo);
		layerInfo.setMaxExtent(rasterLayerInfo.getMaxExtent());
		layerInfo.setMinimumScale(new ScaleInfo(1 / resolutionMax));
		layerInfo.setMaximumScale(new ScaleInfo(1 / resolutionMin));

		mapConfig = new MapConfigurationImpl();
		mapConfig.setHintValue(GeomajasServerExtension.MAPINFO, mapInfo);
	}

	@Test
	public void testIsShowingOnInvisible() {
		layerInfo.setVisible(false);

		RasterServerLayerImpl layer = new RasterServerLayerImpl(mapConfig, layerInfo, viewPortMock, eventBus);

		Assert.assertFalse(layer.isShowing());
	}

	@Test
	public void testIsShowingOnVisibleViewPortLargerThanMaxScale() {
		layerInfo.setVisible(true);
		Mockito.when(viewPortMock.getResolution()).thenReturn(resolutionMin - 1);

		RasterServerLayerImpl layer = new RasterServerLayerImpl(mapConfig, layerInfo, viewPortMock, eventBus);

		Assert.assertFalse(layer.isShowing());
	}

	@Test
	public void testIsShowingOnVisibleViewPortLowerThanMinScale() {
		layerInfo.setVisible(true);
		Mockito.when(viewPortMock.getResolution()).thenReturn(resolutionMax + 1);

		RasterServerLayerImpl layer = new RasterServerLayerImpl(mapConfig, layerInfo, viewPortMock, eventBus);

		Assert.assertFalse(layer.isShowing());
	}

	@Test
	public void testIsShowingOnVisibleViewPortMinScaleInclusive() {
		layerInfo.setVisible(true);
		Mockito.when(viewPortMock.getResolution()).thenReturn(resolutionMax);

		RasterServerLayerImpl layer = new RasterServerLayerImpl(mapConfig, layerInfo, viewPortMock, eventBus);

		Assert.assertTrue(layer.isShowing());
	}

	@Test
	public void testIsShowingOnVisibleViewPortMaxScaleExclusive() {
		layerInfo.setVisible(true);
		Mockito.when(viewPortMock.getResolution()).thenReturn(resolutionMin);

		RasterServerLayerImpl layer = new RasterServerLayerImpl(mapConfig, layerInfo, viewPortMock, eventBus);

		Assert.assertFalse(layer.isShowing());
	}

	@Test
	public void testIsShowingOnVisibleViewPortScaleBetweenMaxAndMin() {
		layerInfo.setVisible(true);
		Mockito.when(viewPortMock.getResolution()).thenReturn(resolutionCenter);

		RasterServerLayerImpl layer = new RasterServerLayerImpl(mapConfig, layerInfo, viewPortMock, eventBus);

		Assert.assertTrue(layer.isShowing());
	}
}