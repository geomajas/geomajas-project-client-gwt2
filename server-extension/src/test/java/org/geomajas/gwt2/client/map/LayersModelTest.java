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

package org.geomajas.gwt2.client.map;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.event.LayerAddedEvent;
import org.geomajas.gwt2.client.event.LayerRemovedEvent;
import org.geomajas.gwt2.client.event.MapCompositionHandler;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.layer.LayersModel;
import org.geomajas.gwt2.client.map.layer.LayersModelImpl;
import org.geomajas.gwt2.client.map.layer.VectorServerLayerImpl;
import org.geomajas.testdata.ReloadContext;
import org.geomajas.testdata.ReloadContextTestExecutionListener;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * Tests for the layersModelImpl class to see if it correctly implements all layersModel methods.
 * 
 * @author Pieter De Graef
 */
@TestExecutionListeners({ ReloadContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "beansContext.xml", "mapBeans.xml",
		"layerBeans1.xml", "layerBeans2.xml", "layerBeans3.xml" })
@ReloadContext
public class LayersModelTest {

	private static final String LAYER1 = "beans1Layer";

	private static final String LAYER2 = "beans2Layer";

	private static final String LAYER3 = "beans3Layer";

	@Autowired
	@Qualifier(value = "mapBeans")
	private ClientMapInfo mapInfo;

	private MapEventBus eventBus;

	private ViewPort viewPort;

	private LayersModel layersModel;

	private int layerCount;

	@Before
	public void initialize() {
		// Initialize main components:
		MapConfiguration mapConfig = TestConfigUtil.create(mapInfo);
		eventBus = new MapEventBusImpl(this, GeomajasImpl.getInstance().getEventBus());
		viewPort = new ViewPortImpl(eventBus);
		((ViewPortImpl) viewPort).initialize(mapConfig);
		layersModel = new LayersModelImpl(viewPort, eventBus);

		// Now add layers:
		for (int i = 1; i < 4; i++) {
			for (ClientLayerInfo layerInfo : mapInfo.getLayers()) {
				if (("beans" + i + "Layer").equals(layerInfo.getId())) {
					Layer layer = new VectorServerLayerImpl(mapConfig, (ClientVectorLayerInfo)layerInfo, viewPort, eventBus);
					layersModel.addLayer(layer);
				}
			}
		}
	}

	@Test
	public void testBeforeInit() {
		Assert.assertNull(layersModel.getSelectedLayer());
	}

	@Test
	public void testInitialize() {
		final MapCompositionHandler layerCounter = new MapCompositionHandler() {

			public void onLayerAdded(LayerAddedEvent event) {
				layerCount++;
			}

			public void onLayerRemoved(LayerRemovedEvent event) {
			}
		};
		eventBus.addMapCompositionHandler(layerCounter);
		initialize();
		Assert.assertEquals(3, layerCount);
		Assert.assertEquals(3, layersModel.getLayerCount());
	}

	@Test
	public void testLayerSelection() {
		Layer layer1 = layersModel.getLayer(LAYER1);
		Layer layer2 = layersModel.getLayer(LAYER2);

		layer1.setSelected(true);
		Assert.assertEquals(layer1, layersModel.getSelectedLayer());

		layer2.setSelected(true);
		Assert.assertEquals(layer2, layersModel.getSelectedLayer());
		Assert.assertEquals(false, layer1.isSelected());

		layer2.setSelected(false);
		Assert.assertEquals(null, layersModel.getSelectedLayer());
	}

	@Test
	public void testGetLayer() {
		Assert.assertEquals(layersModel.getLayer(0), layersModel.getLayer(LAYER1));
		Assert.assertEquals(layersModel.getLayer(1), layersModel.getLayer(LAYER2));
		Assert.assertEquals(layersModel.getLayer(2), layersModel.getLayer(LAYER3));

		// Corner cases:
		try {
			layersModel.getLayer(null);
			Assert.fail();
		} catch (Exception e) {
			// Test passed.
		}
	}

	@Test
	public void testLayerPosition() {
		Assert.assertEquals(0, layersModel.getLayerPosition(layersModel.getLayer(0)));
		Assert.assertEquals(1, layersModel.getLayerPosition(layersModel.getLayer(1)));
		Assert.assertEquals(2, layersModel.getLayerPosition(layersModel.getLayer(2)));

		Assert.assertEquals(0, layersModel.getLayerPosition(layersModel.getLayer(LAYER1)));
		Assert.assertEquals(1, layersModel.getLayerPosition(layersModel.getLayer(LAYER2)));
		Assert.assertEquals(2, layersModel.getLayerPosition(layersModel.getLayer(LAYER3)));

		// Corner cases:
		try {
			layersModel.getLayerPosition(null);
			Assert.fail();
		} catch (Exception e) {
			// Test passed.
		}
	}

	@Test
	public void testMoveLayerDown() {
		Layer layer1 = layersModel.getLayer(LAYER1);
		Layer layer2 = layersModel.getLayer(LAYER2);
		Layer layer3 = layersModel.getLayer(LAYER3);

		layersModel.moveLayerDown(layer1); // Expect no changes.
		Assert.assertEquals(0, layersModel.getLayerPosition(layer1));
		Assert.assertEquals(1, layersModel.getLayerPosition(layer2));
		Assert.assertEquals(2, layersModel.getLayerPosition(layer3));

		layersModel.moveLayerDown(layer3);
		Assert.assertEquals(0, layersModel.getLayerPosition(layer1));
		Assert.assertEquals(2, layersModel.getLayerPosition(layer2));
		Assert.assertEquals(1, layersModel.getLayerPosition(layer3));

		layersModel.moveLayerDown(layer3);
		Assert.assertEquals(1, layersModel.getLayerPosition(layer1));
		Assert.assertEquals(2, layersModel.getLayerPosition(layer2));
		Assert.assertEquals(0, layersModel.getLayerPosition(layer3));
	}

	@Test
	public void testMoveLayerUp() {
		Layer layer1 = layersModel.getLayer(LAYER1);
		Layer layer2 = layersModel.getLayer(LAYER2);
		Layer layer3 = layersModel.getLayer(LAYER3);

		Assert.assertEquals(0, layersModel.getLayerPosition(layer1));
		Assert.assertEquals(1, layersModel.getLayerPosition(layer2));
		Assert.assertEquals(2, layersModel.getLayerPosition(layer3));

		layersModel.moveLayerUp(layer3); // Expect no changes.
		Assert.assertEquals(0, layersModel.getLayerPosition(layer1));
		Assert.assertEquals(1, layersModel.getLayerPosition(layer2));
		Assert.assertEquals(2, layersModel.getLayerPosition(layer3));

		layersModel.moveLayerUp(layer1);
		Assert.assertEquals(1, layersModel.getLayerPosition(layer1));
		Assert.assertEquals(0, layersModel.getLayerPosition(layer2));
		Assert.assertEquals(2, layersModel.getLayerPosition(layer3));

		layersModel.moveLayerUp(layer1);
		Assert.assertEquals(2, layersModel.getLayerPosition(layer1));
		Assert.assertEquals(0, layersModel.getLayerPosition(layer2));
		Assert.assertEquals(1, layersModel.getLayerPosition(layer3));
	}

	@Test
	public void testMoveLayer() {
		Layer layer1 = layersModel.getLayer(LAYER1);
		Layer layer2 = layersModel.getLayer(LAYER2);
		Layer layer3 = layersModel.getLayer(LAYER3);

		layersModel.moveLayer(layer1, -1); // Expect no changes.
		Assert.assertEquals(0, layersModel.getLayerPosition(layer1));
		Assert.assertEquals(1, layersModel.getLayerPosition(layer2));
		Assert.assertEquals(2, layersModel.getLayerPosition(layer3));

		layersModel.moveLayer(layer2, -1);
		Assert.assertEquals(0, layersModel.getLayerPosition(layer2));
		Assert.assertEquals(1, layersModel.getLayerPosition(layer1));
		Assert.assertEquals(2, layersModel.getLayerPosition(layer3));

		layersModel.moveLayer(layer2, 2);
		Assert.assertEquals(0, layersModel.getLayerPosition(layer1));
		Assert.assertEquals(1, layersModel.getLayerPosition(layer3));
		Assert.assertEquals(2, layersModel.getLayerPosition(layer2));

		layersModel.moveLayer(layer2, 200);
		Assert.assertEquals(0, layersModel.getLayerPosition(layer1));
		Assert.assertEquals(1, layersModel.getLayerPosition(layer3));
		Assert.assertEquals(2, layersModel.getLayerPosition(layer2));

		layersModel.moveLayer(layer3, 0);
		Assert.assertEquals(0, layersModel.getLayerPosition(layer3));
		Assert.assertEquals(1, layersModel.getLayerPosition(layer1));
		Assert.assertEquals(2, layersModel.getLayerPosition(layer2));

		// Corner cases:
		Assert.assertFalse(layersModel.moveLayer(layer3, 0));
		Assert.assertEquals(0, layersModel.getLayerPosition(layer3));
		Assert.assertEquals(1, layersModel.getLayerPosition(layer1));
		Assert.assertEquals(2, layersModel.getLayerPosition(layer2));
	}
}