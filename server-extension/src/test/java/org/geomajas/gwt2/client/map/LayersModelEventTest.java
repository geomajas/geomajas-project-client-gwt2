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
import org.geomajas.gwt2.client.event.LayerDeselectedEvent;
import org.geomajas.gwt2.client.event.LayerOrderChangedEvent;
import org.geomajas.gwt2.client.event.LayerOrderChangedHandler;
import org.geomajas.gwt2.client.event.LayerRemovedEvent;
import org.geomajas.gwt2.client.event.LayerSelectedEvent;
import org.geomajas.gwt2.client.event.LayerSelectionHandler;
import org.geomajas.gwt2.client.event.MapCompositionHandler;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.layer.LayersModel;
import org.geomajas.gwt2.client.map.layer.LayersModelImpl;
import org.geomajas.gwt2.client.map.layer.VectorServerLayerImpl;
import org.geomajas.testdata.ReloadContext;
import org.geomajas.testdata.ReloadContext.ClassMode;
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

import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Tests for the layersModelImpl class to see if it fires the correct events.
 * 
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "beansContext.xml", "mapBeans.xml",
		"layerBeans1.xml", "layerBeans2.xml", "layerBeans3.xml" })
@TestExecutionListeners(listeners = { ReloadContextTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class })
@ReloadContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class LayersModelEventTest {

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

	private String selectId;

	private String deselectId;

	private int fromIndex;

	private int toIndex;

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
					Layer layer = new VectorServerLayerImpl((ClientVectorLayerInfo) layerInfo, viewPort, eventBus);
					layersModel.addLayer(layer);
				}
			}
		}
	}

	@Test
	public void testInitialize() {
		final MapCompositionHandler layerCounter = new MapCompositionHandler() {

			public void onLayerAdded(LayerAddedEvent event) {
				layerCount++;
			}

			public void onLayerRemoved(LayerRemovedEvent event) {
				layerCount--;
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

		selectId = null;
		deselectId = null;

		HandlerRegistration reg = eventBus.addLayerSelectionHandler(new LayerSelectionHandler() {

			public void onSelectLayer(LayerSelectedEvent event) {
				selectId = event.getLayer().getId();
			}

			public void onDeselectLayer(LayerDeselectedEvent event) {
				deselectId = event.getLayer().getId();
			}
		});

		layer1.setSelected(true);
		Assert.assertEquals(LAYER1, selectId);
		Assert.assertNull(deselectId);

		layer2.setSelected(true);
		Assert.assertEquals(LAYER1, deselectId);
		Assert.assertEquals(LAYER2, selectId);

		layer2.setSelected(false);
		Assert.assertEquals(LAYER2, deselectId);
		Assert.assertEquals(LAYER2, selectId);

		reg.removeHandler();
	}

	@Test
	public void testMoveLayerDown() {
		Layer layer1 = layersModel.getLayer(LAYER1);
		Layer layer3 = layersModel.getLayer(LAYER3);

		fromIndex = 342;
		toIndex = 342;

		HandlerRegistration reg = eventBus.addLayerOrderChangedHandler(new LayerOrderChangedHandler() {

			public void onLayerOrderChanged(LayerOrderChangedEvent event) {
				fromIndex = event.getFromIndex();
				toIndex = event.getToIndex();
			}
		});

		layersModel.moveLayerDown(layer1); // Expect no changes, and so no event.
		Assert.assertEquals(342, fromIndex);
		Assert.assertEquals(342, toIndex);

		layersModel.moveLayerDown(layer3);
		Assert.assertEquals(2, fromIndex);
		Assert.assertEquals(1, toIndex);

		reg.removeHandler();
	}

	@Test
	public void testMoveLayerUp() {
		Layer layer1 = layersModel.getLayer(LAYER1);
		Layer layer3 = layersModel.getLayer(LAYER3);

		fromIndex = 342;
		toIndex = 342;

		HandlerRegistration reg = eventBus.addLayerOrderChangedHandler(new LayerOrderChangedHandler() {

			public void onLayerOrderChanged(LayerOrderChangedEvent event) {
				fromIndex = event.getFromIndex();
				toIndex = event.getToIndex();
			}
		});

		layersModel.moveLayerUp(layer3); // Expect no changes, and so no event.
		Assert.assertEquals(342, fromIndex);
		Assert.assertEquals(342, toIndex);

		layersModel.moveLayerUp(layer1);
		Assert.assertEquals(0, fromIndex);
		Assert.assertEquals(1, toIndex);

		reg.removeHandler();
	}

	@Test
	public void testMoveLayer() {
		Layer layer1 = layersModel.getLayer(LAYER1);
		Layer layer2 = layersModel.getLayer(LAYER2);
		Layer layer3 = layersModel.getLayer(LAYER3);

		fromIndex = 342;
		toIndex = 342;

		HandlerRegistration reg = eventBus.addLayerOrderChangedHandler(new LayerOrderChangedHandler() {

			public void onLayerOrderChanged(LayerOrderChangedEvent event) {
				fromIndex = event.getFromIndex();
				toIndex = event.getToIndex();
			}
		});

		layersModel.moveLayer(layer1, -1); // Expect no changes, and so no event.
		Assert.assertEquals(342, fromIndex);
		Assert.assertEquals(342, toIndex);

		layersModel.moveLayer(layer2, -1);
		Assert.assertEquals(1, fromIndex);
		Assert.assertEquals(0, toIndex);

		layersModel.moveLayer(layer2, 2);
		Assert.assertEquals(0, fromIndex);
		Assert.assertEquals(2, toIndex);

		layersModel.moveLayer(layer2, 200); // Expect no changes.
		Assert.assertEquals(0, fromIndex);
		Assert.assertEquals(2, toIndex);

		layersModel.moveLayer(layer3, 0);
		Assert.assertEquals(1, fromIndex);
		Assert.assertEquals(0, toIndex);

		fromIndex = 342;
		toIndex = 342;

		// Corner case - move to same position. We don't expect an event.
		layersModel.moveLayer(layer3, 0);
		Assert.assertEquals(342, fromIndex);
		Assert.assertEquals(342, toIndex);

		reg.removeHandler();
	}

	@Test
	public void testLayerRemoval() {
		layerCount = layersModel.getLayerCount();
		Assert.assertEquals(3, layerCount);

		final MapCompositionHandler layerCounter = new MapCompositionHandler() {

			public void onLayerAdded(LayerAddedEvent event) {
				layerCount++;
			}

			public void onLayerRemoved(LayerRemovedEvent event) {
				layerCount--;
			}
		};
		HandlerRegistration reg = eventBus.addMapCompositionHandler(layerCounter);

		layersModel.removeLayer(LAYER1);
		Assert.assertEquals(2, layerCount);
		layersModel.removeLayer(LAYER3);
		Assert.assertEquals(1, layerCount);
		layersModel.removeLayer(LAYER2);
		Assert.assertEquals(0, layerCount);

		// Corner cases:
		Assert.assertFalse(layersModel.removeLayer("this-layer-does-not-exist"));
		try {
			layersModel.removeLayer(null);
			Assert.fail();
		} catch (Exception e) {
			// Test passed.
		}

		reg.removeHandler();
	}
}