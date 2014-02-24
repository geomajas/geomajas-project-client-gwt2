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
import org.geomajas.gwt2.client.event.LayerHideEvent;
import org.geomajas.gwt2.client.event.LayerShowEvent;
import org.geomajas.gwt2.client.event.LayerVisibilityHandler;
import org.geomajas.gwt2.client.event.LayerVisibilityMarkedEvent;
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
public class VectorLayerEventTest {

	@Autowired
	@Qualifier(value = "mapBeans")
	private ClientMapInfo mapInfo;

	private ClientVectorLayerInfo layerInfo;

	private MapEventBus eventBus;

	private ViewPort viewPort;

	private int count;

	private boolean isShowing;

	@Before
	public void initialize() {
		// Initialize main components:
		eventBus = new MapEventBusImpl(this, GeomajasImpl.getInstance().getEventBus());
		viewPort = TestConfigUtil.createViewPort(eventBus, mapInfo, 1000, 1000);
		layerInfo = (ClientVectorLayerInfo) mapInfo.getLayers().get(0);
	}

	@Test
	public void testMarkedAsVisibleEvents() {
		VectorServerLayerImpl layer = new VectorServerLayerImpl(layerInfo, viewPort, eventBus);
		count = 0;

		eventBus.addLayerVisibilityHandler(new LayerVisibilityHandler() {

			public void onVisibilityMarked(LayerVisibilityMarkedEvent event) {
				count++;
			}

			public void onShow(LayerShowEvent event) {
			}

			public void onHide(LayerHideEvent event) {
			}
		});

		// Scale between 6 and 20 is OK:
		viewPort.applyResolution(viewPort.getResolution(0)); // 32 -> NOK
		Assert.assertFalse(layer.isShowing());
		Assert.assertEquals(0, count);

		layer.setMarkedAsVisible(true);
		Assert.assertEquals(1, count);
		layer.setMarkedAsVisible(false);
		Assert.assertEquals(2, count);
	}

	@Test
	public void testShowHideEvents() {
		VectorServerLayerImpl layer = new VectorServerLayerImpl(layerInfo, viewPort, eventBus);

		eventBus.addLayerVisibilityHandler(new LayerVisibilityHandler() {

			public void onVisibilityMarked(LayerVisibilityMarkedEvent event) {
			}

			public void onShow(LayerShowEvent event) {
				isShowing = true;
			}

			public void onHide(LayerHideEvent event) {
				isShowing = false;
			}
		});

		// Scale between 6 and 20 is OK:
		viewPort.applyResolution(viewPort.getResolution(5)); // 32 -> NOK
		Assert.assertFalse(layer.isShowing());
		Assert.assertEquals(layer.isShowing(), isShowing);

		viewPort.applyResolution(viewPort.getResolution(4)); // 16 -> OK
		Assert.assertTrue(layer.isShowing());
		Assert.assertEquals(layer.isShowing(), isShowing);

		viewPort.applyResolution(viewPort.getResolution(3)); // 8 -> OK
		Assert.assertTrue(layer.isShowing());
		Assert.assertEquals(layer.isShowing(), isShowing);

		viewPort.applyResolution(viewPort.getResolution(2)); // 4 -> NOK
		Assert.assertFalse(layer.isShowing());
		Assert.assertEquals(layer.isShowing(), isShowing);

		viewPort.applyResolution(viewPort.getResolution(1)); // 2 -> NOK
		Assert.assertFalse(layer.isShowing());
		Assert.assertEquals(layer.isShowing(), isShowing);

		viewPort.applyResolution(viewPort.getResolution(0)); // 1 -> NOK
		Assert.assertFalse(layer.isShowing());
		Assert.assertEquals(layer.isShowing(), isShowing);

		// Mark as invisible:
		layer.setMarkedAsVisible(false);
		viewPort.applyResolution(viewPort.getResolution(5)); // 32 -> NOK
		Assert.assertFalse(layer.isShowing());
		Assert.assertEquals(layer.isShowing(), isShowing);

		viewPort.applyResolution(viewPort.getResolution(4)); // 16 -> NOK
		Assert.assertFalse(layer.isShowing());
		Assert.assertEquals(layer.isShowing(), isShowing);

		// Mark as visible while scale is within limits:
		layer.setMarkedAsVisible(true);
		Assert.assertTrue(layer.isShowing());
		Assert.assertEquals(layer.isShowing(), isShowing);
	}
}