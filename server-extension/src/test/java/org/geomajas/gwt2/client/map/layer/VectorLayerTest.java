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

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test-cases for the {@link VectorServerLayerImpl} class.
 * 
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "layerBeans1.xml", "mapBeans.xml" })
public class VectorLayerTest {

	@Autowired
	@Qualifier(value = "mapBeans")
	private ClientMapInfo mapInfo;

	private ClientVectorLayerInfo layerInfo;

	private MapEventBus eventBus;

	private ViewPort viewPort;

	private MapConfiguration mapConfig;

	@Before
	public void initialize() {
		// Initialize main components:
		eventBus = new MapEventBusImpl(this, GeomajasImpl.getInstance().getEventBus());
		viewPort = TestConfigUtil.createViewPort(eventBus, mapInfo, 1000, 1000);
		layerInfo = (ClientVectorLayerInfo) mapInfo.getLayers().get(0);
		mapConfig = new MapConfigurationImpl();
		mapConfig.setHintValue(GeomajasServerExtension.MAPINFO, mapInfo);
	}

	@Test
	public void testServerLayerId() {
		VectorServerLayerImpl layer = new VectorServerLayerImpl(mapConfig, layerInfo, viewPort, eventBus);
		Assert.assertEquals(layerInfo.getServerLayerId(), layer.getServerLayerId());
	}

	@Test
	public void testTitle() {
		VectorServerLayerImpl layer = new VectorServerLayerImpl(mapConfig, layerInfo, viewPort, eventBus);
		Assert.assertEquals(layerInfo.getLabel(), layer.getTitle());
	}

	@Test
	public void testLayerInfo() {
		VectorServerLayerImpl layer = new VectorServerLayerImpl(mapConfig, layerInfo, viewPort, eventBus);
		Assert.assertEquals(layerInfo, layer.getLayerInfo());
	}

	@Test
	public void testSelection() {
		VectorServerLayerImpl layer = new VectorServerLayerImpl(mapConfig, layerInfo, viewPort, eventBus);
		Assert.assertFalse(layer.isSelected());
		layer.setSelected(true);
		Assert.assertTrue(layer.isSelected());
		layer.setSelected(false);
		Assert.assertFalse(layer.isSelected());
	}

	@Test
	public void testMarkedAsVisible() {
		VectorServerLayerImpl layer = new VectorServerLayerImpl(mapConfig, layerInfo, viewPort, eventBus);
		Assert.assertTrue(layer.isMarkedAsVisible());
		layer.setMarkedAsVisible(false);
		Assert.assertFalse(layer.isMarkedAsVisible());
		layer.setMarkedAsVisible(true);
		Assert.assertTrue(layer.isMarkedAsVisible());
	}

	@Test
	public void testShowing() {
		VectorServerLayerImpl layer = new VectorServerLayerImpl(mapConfig, layerInfo, viewPort, eventBus);

		// Scale between 6 and 20 is OK:
		viewPort.applyResolution(viewPort.getResolution(5)); // 32 -> NOK
		Assert.assertFalse(layer.isShowing());

		viewPort.applyResolution(viewPort.getResolution(4)); // 16 -> OK
		Assert.assertTrue(layer.isShowing());

		viewPort.applyResolution(viewPort.getResolution(3)); // 8 -> OK
		Assert.assertTrue(layer.isShowing());

		viewPort.applyResolution(viewPort.getResolution(2)); // 4 -> NOK
		Assert.assertFalse(layer.isShowing());

		viewPort.applyResolution(viewPort.getResolution(1)); // 2 -> NOK
		Assert.assertFalse(layer.isShowing());

		viewPort.applyResolution(viewPort.getResolution(0)); // 1 -> NOK
		Assert.assertFalse(layer.isShowing());

		// Mark as invisible:
		layer.setMarkedAsVisible(false);
		viewPort.applyResolution(viewPort.getResolution(5)); // 32 -> NOK
		Assert.assertFalse(layer.isShowing());

		viewPort.applyResolution(viewPort.getResolution(4)); // 16 -> NOK
		Assert.assertFalse(layer.isShowing());
	}
}