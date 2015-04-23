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

import junit.framework.Assert;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.map.MapConfiguration.CrsType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO incorporate in ViewPort tests.
 * 
 * 
 * Test case that tests the FreeForAll ZoomStrategy.
 * 
 * @author Pieter De Graef
 */
public class ViewPortScaleTest {

	private static final double[] SCALES = new double[] { 8.0, 4.0, 2.0, 1.0 };

	private ViewPortImpl viewPort;

	private MapEventBus eventBus;

	public ViewPortScaleTest() {
		eventBus = new MapEventBusImpl(this, GeomajasImpl.getInstance().getEventBus());
		viewPort = new ViewPortImpl(eventBus);
		viewPort.initialize(getMapConfig());
		viewPort.setMapSize(100, 100);
	}

	@Test
	public void testMinimumResolution() {
		Assert.assertEquals(SCALES[3], viewPort.getMinimumResolution());
	}

	@Test
	public void testMaximumResolution() {
		// Should be the minimum ratio (hor/vert) of the map width/height divided by the maxbounds width/height.
		Assert.assertEquals(SCALES[0], viewPort.getMaximumResolution());

		// Change map size - this does NOT change the minimum scale:
		viewPort.setMapSize(120, 120);
		Assert.assertEquals(SCALES[0], viewPort.getMaximumResolution());

		// Reset map size:
		viewPort.setMapSize(100, 100);
	}

	@Test
	public void testZoomStepCount() {
		// Allowed scales: 0.5, 1, 2, 4. Count = 4.
		Assert.assertEquals(4, viewPort.getResolutionCount());

		// Change map size. Allowed scales remain the same...
		viewPort.setMapSize(120, 120);
		Assert.assertEquals(4, viewPort.getResolutionCount());

		// Reset map size:
		viewPort.setMapSize(100, 100);
	}

	@Test
	public void testZoomStepScale() {
		Assert.assertEquals(SCALES[0], viewPort.getResolution(0));
		Assert.assertEquals(SCALES[1], viewPort.getResolution(1));
		Assert.assertEquals(SCALES[2], viewPort.getResolution(2));
		Assert.assertEquals(SCALES[3], viewPort.getResolution(3));

		// Change map size. Allowed scales: 0.6, 1.2, 2.4 (4.8 is larger than maximum allowed scale).
		viewPort.setMapSize(120, 120);
		Assert.assertEquals(SCALES[0], viewPort.getResolution(0));
		Assert.assertEquals(SCALES[1], viewPort.getResolution(1));
		Assert.assertEquals(SCALES[2], viewPort.getResolution(2));
		Assert.assertEquals(SCALES[3], viewPort.getResolution(3));

		// Reset map size:
		viewPort.setMapSize(100, 100);
	}

	@Test
	public void testZoomStepIndex() {
		for (int i = 0; i < 4; i++) {
			Assert.assertEquals(i, viewPort.getResolutionIndex(SCALES[i]));
		}
		Assert.assertEquals(0, viewPort.getResolutionIndex(8.5));
		Assert.assertEquals(0, viewPort.getResolutionIndex(7.0));
		Assert.assertEquals(1, viewPort.getResolutionIndex(4.1));
		Assert.assertEquals(1, viewPort.getResolutionIndex(3.9));
		Assert.assertEquals(2, viewPort.getResolutionIndex(3.0)); // Perfectly half-way. Prefer the zoomed out step.
		Assert.assertEquals(2, viewPort.getResolutionIndex(2.9));
		Assert.assertEquals(2, viewPort.getResolutionIndex(2.1));
		Assert.assertEquals(3, viewPort.getResolutionIndex(1.1));
		Assert.assertEquals(3, viewPort.getResolutionIndex(0.9));
		Assert.assertEquals(3, viewPort.getResolutionIndex(-1.0));
	}

	@Test
	public void testZoomOptionFit() {
		viewPort.applyResolution(SCALES[0] + 0.001, ZoomOption.LEVEL_FIT);
		Assert.assertEquals(SCALES[0], viewPort.getResolution());
		viewPort.applyResolution(SCALES[1] + 0.001, ZoomOption.LEVEL_FIT);
		Assert.assertEquals(SCALES[0], viewPort.getResolution());
		viewPort.applyResolution(SCALES[2] + 0.001, ZoomOption.LEVEL_FIT);
		Assert.assertEquals(SCALES[1], viewPort.getResolution());
		viewPort.applyResolution(SCALES[3] + 0.001, ZoomOption.LEVEL_FIT);
		Assert.assertEquals(SCALES[2], viewPort.getResolution());

		viewPort.applyResolution(0, ZoomOption.LEVEL_FIT);
		Assert.assertEquals(SCALES[3], viewPort.getResolution());
	}

	@Test
	public void testZoomOptionClosest() {
		viewPort.applyResolution(SCALES[0] + 0.001, ZoomOption.LEVEL_CLOSEST);
		Assert.assertEquals(SCALES[0], viewPort.getResolution());
		viewPort.applyResolution(SCALES[1] + 0.001, ZoomOption.LEVEL_CLOSEST);
		Assert.assertEquals(SCALES[1], viewPort.getResolution());
		viewPort.applyResolution(SCALES[1] - 0.001, ZoomOption.LEVEL_CLOSEST);
		Assert.assertEquals(SCALES[1], viewPort.getResolution());
		viewPort.applyResolution(SCALES[3] + 0.001, ZoomOption.LEVEL_CLOSEST);
		Assert.assertEquals(SCALES[3], viewPort.getResolution());

		viewPort.applyResolution(0, ZoomOption.LEVEL_CLOSEST);
		Assert.assertEquals(SCALES[3], viewPort.getResolution());
	}

	private MapConfiguration getMapConfig() {
		MapConfigurationImpl config = new MapConfigurationImpl();
		config.setCrs("EPSG:4326", CrsType.DEGREES);
		config.setMaxBounds(new Bbox(-100, -100, 200, 200));
		List<Double> resolutions = new ArrayList<Double>();
		resolutions.add(1.0);
		resolutions.add(2.0);
		resolutions.add(4.0);
		resolutions.add(8.0);
		config.setResolutions(resolutions);
		return config;
	}
}