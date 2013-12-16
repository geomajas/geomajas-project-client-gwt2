/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.client.map;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.event.ViewPortChangedEvent;
import org.geomajas.gwt2.client.event.ViewPortChangedHandler;
import org.geomajas.gwt2.client.map.MapOptions.CrsType;
import org.junit.Before;
import org.junit.Test;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Unit test that checks if the correct events are fired by the ViewPortImpl.
 * 
 * @author Pieter De Graef
 */
public class ViewPortEventTest {

	private MapEventBus eventBus;

	private ViewPort viewPort;

	private Event<ViewPortChangedHandler> event;

	public ViewPortEventTest() {
		eventBus = new MapEventBusImpl(this, GeomajasImpl.getInstance().getEventBus());
		viewPort = new ViewPortImpl(eventBus, getMapConfig());
		viewPort.setMapSize(1000, 1000);
	}

	@Before
	public void prepareTest() {
		viewPort.applyBounds(viewPort.getMaximumBounds(), ZoomOption.LEVEL_CLOSEST);
		event = null;
	}

	@Test
	public void testApplyPosition() {
		viewPort.applyBounds(viewPort.getMaximumBounds(), ZoomOption.LEVEL_CLOSEST);
		event = null;

		Assert.assertEquals(4.0, viewPort.getScale());
		Assert.assertNull(event);

		HandlerRegistration reg = eventBus.addViewPortChangedHandler(new AllowChangedHandler());

		viewPort.applyPosition(new Coordinate(342, 342));
		Assert.assertEquals(4.0, viewPort.getScale());
		Assert.assertNotNull(event);

		reg.removeHandler();
	}

	@Test
	public void testApplyScale() {
		Assert.assertEquals(4.0, viewPort.getScale());
		Assert.assertNull(event);

		HandlerRegistration reg = eventBus.addViewPortChangedHandler(new AllowChangedHandler());

		viewPort.applyScale(2.0);
		Assert.assertEquals(2.0, viewPort.getScale());
		Assert.assertNotNull(event);

		reg.removeHandler();
	}

	@Test
	public void testApplyBounds() {
		Assert.assertEquals(4.0, viewPort.getScale());
		Assert.assertNull(event);

		HandlerRegistration reg = eventBus.addViewPortChangedHandler(new AllowChangedHandler());

		// Now a changed event should occur:
		viewPort.applyBounds(new Bbox(0, 0, 100, 100));
		Assert.assertEquals(8.0, viewPort.getScale());
		Assert.assertNotNull(event);
		Assert.assertTrue(event instanceof ViewPortChangedEvent);

		reg.removeHandler();
		reg = eventBus.addViewPortChangedHandler(new AllowChangedHandler());

		// Expect to end up at the same scale, so no changed event, but translation only:
		viewPort.applyBounds(new Bbox(-50, -50, 100, 100));
		Assert.assertEquals(8.0, viewPort.getScale());
		Assert.assertNotNull(event);

		reg.removeHandler();
	}

	@Test
	public void testApplySameBounds() {
		Assert.assertEquals(4.0, viewPort.getScale());
		Assert.assertNull(event);

		HandlerRegistration reg = eventBus.addViewPortChangedHandler(new AllowNoEventsHandler());

		viewPort.applyBounds(viewPort.getBounds());
		Assert.assertNull(event);

		reg.removeHandler();
	}

	@Test
	public void testResize() {
		Assert.assertEquals(4.0, viewPort.getScale());
		Assert.assertNull(event);

		HandlerRegistration reg = eventBus.addViewPortChangedHandler(new AllowChangedHandler());
		viewPort.setMapSize(500, 500);

		Assert.assertEquals(4.0, viewPort.getScale());
		Assert.assertTrue(viewPort.getPosition().equalsDelta(new Coordinate(-62.5, 62.5), 0.00001));
		Assert.assertNotNull(event);
		Assert.assertTrue(event instanceof ViewPortChangedEvent);

		reg.removeHandler();
	}

	@Test
	public void testResizeSameSize() {
		Assert.assertEquals(4.0, viewPort.getScale());
		Assert.assertNull(event);

		HandlerRegistration reg = eventBus.addViewPortChangedHandler(new AllowNoEventsHandler());
		viewPort.setMapSize(viewPort.getMapWidth(), viewPort.getMapHeight());

		Assert.assertNull(event);

		reg.removeHandler();
	}

	// ------------------------------------------------------------------------
	// Private classes that allows only one type of event to be fired.
	// ------------------------------------------------------------------------

	/**
	 * ViewPortHandler that allows only ViewPortChangedEvents.
	 * 
	 * @author Pieter De Graef
	 */
	private class AllowChangedHandler implements ViewPortChangedHandler {

		public void onViewPortChanged(ViewPortChangedEvent event) {
			ViewPortEventTest.this.event = event;
		}
	}

	/**
	 * ViewPortHandler that allows no ViewPort events.
	 * 
	 * @author Pieter De Graef
	 */
	private class AllowNoEventsHandler implements ViewPortChangedHandler {

		public void onViewPortChanged(ViewPortChangedEvent event) {
			Assert.fail("No ViewPortChangedEvent should have been fired.");
		}
	}

	private MapConfiguration getMapConfig() {
		MapOptions options = new MapOptions();
		options.setCrs("EPSG:4326", CrsType.DEGREES);
		options.setInitialBounds(new Bbox(-100, -100, 200, 200));
		options.setMaxBounds(new Bbox(-100, -100, 200, 200));
		List<Double> resolutions = new ArrayList<Double>();
		resolutions.add(1.0);
		resolutions.add(2.0);
		resolutions.add(4.0);
		resolutions.add(8.0);
		options.setResolutions(resolutions);

		MapConfigurationImpl config = new MapConfigurationImpl();
		config.setMapOptions(options);
		return config;
	}
}