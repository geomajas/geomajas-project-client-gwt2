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

import junit.framework.Assert;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.map.MapConfiguration.CrsType;
import org.junit.Test;

/**
 * Test for the maximum resolution that is calculated when a {@link ViewPortImpl}
 * has no resolutions on initialization. This resolution must permit the
 * full map to be shown.
 *
 * @author Jan Venstermans
 */
public class ViewPortMaxBoundsNoResolutionsTest {

	private MapConfiguration mapConfig;

	private MapEventBus eventBus;

	private ViewPortImpl viewPort;

	public ViewPortMaxBoundsNoResolutionsTest() {
		eventBus = new MapEventBusImpl(this, GeomajasImpl.getInstance().getEventBus());
		viewPort = new ViewPortImpl(eventBus);
	}

	@Test
	public void testHighMapNoInitialResolutions() {
		Bbox maxBounds = new Bbox(-1, -100, 2, 200);

		mapConfig = getMapConfigWithoutMaxBounds();
		mapConfig.setMaxBounds(maxBounds);
		Assert.assertEquals(0, viewPort.getResolutionCount());
		viewPort.setMapSize(1000, 1000);
		viewPort.initialize(mapConfig);

		Assert.assertNotSame(0, viewPort.getResolutionCount());

		viewPort.applyResolution(viewPort.getMaximumResolution());
		Bbox boundsOnMaxResolution = viewPort.getBounds();
		Assert.assertTrue(maxBounds.getWidth() >= boundsOnMaxResolution.getWidth());
		Assert.assertTrue(maxBounds.getHeight() >= boundsOnMaxResolution.getHeight());
	}

	@Test
	public void testWideMapNoInitialResolutions() {
		Bbox maxBounds = new Bbox(-100, -1, 200, 2);

		mapConfig = getMapConfigWithoutMaxBounds();
		mapConfig.setMaxBounds(maxBounds);
		Assert.assertEquals(0, viewPort.getResolutionCount());
		viewPort.setMapSize(1000, 1000);
		viewPort.initialize(mapConfig);

		Assert.assertNotSame(0, viewPort.getResolutionCount());

		viewPort.applyResolution(viewPort.getMaximumResolution());
		Bbox boundsOnMaxResolution = viewPort.getBounds();
		Assert.assertTrue(maxBounds.getWidth() >= boundsOnMaxResolution.getWidth());
		Assert.assertTrue(maxBounds.getHeight() >= boundsOnMaxResolution.getHeight());
	}

	private MapConfiguration getMapConfigWithoutMaxBounds() {
		MapConfigurationImpl config = new MapConfigurationImpl();
		config.setCrs("EPSG:4326", CrsType.DEGREES);
		return config;
	}
}