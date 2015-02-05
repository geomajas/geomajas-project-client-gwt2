/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.client.map;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.HandlerRegistration;
import junit.framework.Assert;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.event.ViewPortChangedEvent;
import org.geomajas.gwt2.client.event.ViewPortChangedHandler;
import org.geomajas.gwt2.client.map.MapConfiguration.CrsType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit test that checks if the correct events are fired by the ViewPortImpl.
 * 
 * @author Pieter De Graef
 */
public class ViewPortEventTest {

	private MapEventBus eventBus;

	private ViewPortImpl viewPort;

	private Event<ViewPortChangedHandler> event;

	public ViewPortEventTest() {
		eventBus = new MapEventBusImpl(this, GeomajasImpl.getInstance().getEventBus());
		viewPort = new ViewPortImpl(eventBus);
		viewPort.initialize(getMapConfig());
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

		Assert.assertEquals(0.25, viewPort.getResolution());
		Assert.assertNull(event);

		HandlerRegistration reg = eventBus.addViewPortChangedHandler(new AllowChangedHandler());

		viewPort.applyPosition(new Coordinate(342, 342));
		Assert.assertEquals(0.25, viewPort.getResolution());
		Assert.assertNotNull(event);

		reg.removeHandler();
	}

	@Test
	public void testApplyResolution() {
		Assert.assertEquals(0.25, viewPort.getResolution());
		Assert.assertNull(event);

		HandlerRegistration reg = eventBus.addViewPortChangedHandler(new AllowChangedHandler());

		viewPort.applyResolution(0.5);
		Assert.assertEquals(0.5, viewPort.getResolution());
		Assert.assertNotNull(event);

		reg.removeHandler();
	}

	@Test
	public void testApplyBounds() {
		Assert.assertEquals(0.25, viewPort.getResolution());
		Assert.assertNull(event);

		HandlerRegistration reg = eventBus.addViewPortChangedHandler(new AllowChangedHandler());

		// Now a changed event should occur:
		viewPort.applyBounds(new Bbox(0, 0, 100, 100));
		Assert.assertEquals(0.125, viewPort.getResolution());
		Assert.assertNotNull(event);
		Assert.assertTrue(event instanceof ViewPortChangedEvent);

		reg.removeHandler();
		reg = eventBus.addViewPortChangedHandler(new AllowChangedHandler());

		// Expect to end up at the same scale, so no changed event, but translation only:
		viewPort.applyBounds(new Bbox(-50, -50, 100, 100));
		Assert.assertEquals(0.125, viewPort.getResolution());
		Assert.assertNotNull(event);

		reg.removeHandler();
	}

	@Test
	public void testApplySameBounds() {
		Assert.assertEquals(0.25, viewPort.getResolution());
		Assert.assertNull(event);

		HandlerRegistration reg = eventBus.addViewPortChangedHandler(new AllowNoEventsHandler());

		viewPort.applyBounds(viewPort.getBounds());
		Assert.assertNull(event);

		reg.removeHandler();
	}

	@Test
	public void testResize() {
		Assert.assertEquals(0.25, viewPort.getResolution());
		Assert.assertNull(event);

		HandlerRegistration reg = eventBus.addViewPortChangedHandler(new AllowChangedHandler());
		viewPort.setMapSize(500, 500);

		Assert.assertEquals(0.25, viewPort.getResolution());
		Assert.assertTrue(viewPort.getPosition().equalsDelta(new Coordinate(-62.5, 62.5), 0.00001));
		Assert.assertNotNull(event);
		Assert.assertTrue(event instanceof ViewPortChangedEvent);

		reg.removeHandler();
	}

	@Test
	public void testResizeSameSize() {
		Assert.assertEquals(0.25, viewPort.getResolution());
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
		MapConfigurationImpl config = new MapConfigurationImpl();
		config.setCrs("EPSG:4326", CrsType.DEGREES);
		config.setMaxBounds(new Bbox(-100, -100, 200, 200));
		List<Double> resolutions = new ArrayList<Double>();
		resolutions.add(1.0);
		resolutions.add(0.5);
		resolutions.add(0.25);
		resolutions.add(0.125);
		config.setResolutions(resolutions);
		return config;
	}
}