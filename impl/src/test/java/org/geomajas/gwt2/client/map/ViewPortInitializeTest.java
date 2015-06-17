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
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Test cases for resolutions on viewPort's initialize(MapConfiguration configuration) method.
 * 
 * @author Jan Venstermans
 */
public class ViewPortInitializeTest {

	private static final Double[] RESOLUTIONS = new Double[] { 8.0, 4.0, 2.0, 1.0 };

	private ViewPortImpl viewPort;

	private MapEventBus eventBus;

	@Before
	public void setUp() {
		eventBus = new MapEventBusImpl(this, GeomajasImpl.getInstance().getEventBus());
		viewPort = new ViewPortImpl(eventBus);
	}

	@Test(expected = IllegalStateException.class)
	public void testInitializeWithResolutionsEmptyAndMinimumResolutionZero() {
		MapConfiguration mapConfiguration = getMapConfig(Collections.EMPTY_LIST);
		mapConfiguration.setMinimumResolution(0);

		viewPort.initialize(mapConfiguration);
	}

	@Test
	public void testInitializeWithResolutionsEmptyAndMapNoSize() {
		MapConfiguration mapConfiguration = getMapConfig(Collections.EMPTY_LIST);
		mapConfiguration.setMinimumResolution(getInputMinResolution());
		// use reference to assert result
		ViewPortImpl viewPortReference = new ViewPortImpl(eventBus);
		viewPortReference.setMapSize(ViewPortImpl.MAP_SIZE_FALLBACK_VALUE, ViewPortImpl.MAP_SIZE_FALLBACK_VALUE);
		viewPortReference.initialize(mapConfiguration);

		viewPort.initialize(mapConfiguration);

		Assert.assertEquals(viewPortReference.getResolutionCount(), viewPort.getResolutionCount());
		for (int i = 0; i < viewPortReference.getResolutionCount(); i++) {
			Assert.assertEquals(viewPortReference.getResolution(i), viewPort.getResolution(i));
		}
	}

	@Test
	public void testInitializeWithResolutionsEmptyAndMapHasSizeEqualFor3Resolutions() {
		MapConfiguration mapConfiguration = getMapConfig(Collections.EMPTY_LIST);
		mapConfiguration.setMinimumResolution(getInputMinResolution());
		int amountOfResolutionsWanted = 3;
		double expectedMaxResolutionForWantedAmount = mapConfiguration.getMinimumResolution() *
				Math.pow(2, amountOfResolutionsWanted - 2) * 1.5;
		viewPort.setMapSize((int) (mapConfiguration.getMaxBounds().getWidth() / expectedMaxResolutionForWantedAmount),
				(int) (mapConfiguration.getMaxBounds().getHeight() / expectedMaxResolutionForWantedAmount));

		viewPort.initialize(mapConfiguration);

		Assert.assertEquals(amountOfResolutionsWanted, viewPort.getResolutionCount());
		// min resolution
		Assert.assertTrue(mapConfiguration.getMinimumResolution() > viewPort.getMinimumResolution());
		// max resolution: can be some deviation because of integer casting
		Assert.assertEquals(expectedMaxResolutionForWantedAmount, viewPort.getMaximumResolution(), expectedMaxResolutionForWantedAmount / 4);
	}

	@Test
	public void testInitializeWithResolutionsEmptyAndMapHasSizeUnequalFor5Resolutions() {
		MapConfiguration mapConfiguration = getMapConfig(Collections.EMPTY_LIST);
		mapConfiguration.setMinimumResolution(getInputMinResolution());
		int amountOfResolutionsWanted = 5;
		double expectedMaxResolutionForWantedAmount = mapConfiguration.getMinimumResolution() *
				Math.pow(2, amountOfResolutionsWanted - 2) * 1.5;
		// use height as delimiting factor
		viewPort.setMapSize((int) (mapConfiguration.getMaxBounds().getWidth() / expectedMaxResolutionForWantedAmount) / 4,
				(int) (mapConfiguration.getMaxBounds().getHeight() / expectedMaxResolutionForWantedAmount));

		viewPort.initialize(mapConfiguration);

		Assert.assertEquals(amountOfResolutionsWanted, viewPort.getResolutionCount());
		// min resolution
		Assert.assertTrue(mapConfiguration.getMinimumResolution() > viewPort.getMinimumResolution());
		// max resolution: can be some deviation because of integer casting
		Assert.assertEquals(expectedMaxResolutionForWantedAmount, viewPort.getMaximumResolution(), expectedMaxResolutionForWantedAmount/4);
	}

	@Test
	public void testInitializeWithResolutionsNotEmpty() {
		MapConfiguration mapConfiguration = getMapConfig(Arrays.asList(RESOLUTIONS));

		viewPort.initialize(mapConfiguration);

		Assert.assertEquals(RESOLUTIONS.length, viewPort.getResolutionCount());
		// resolutions should be ordered from large to small
		for (int i = 0; i < RESOLUTIONS.length; i++) {
			Assert.assertEquals(RESOLUTIONS[i], viewPort.getResolution(i));
		}
	}

	@Test
	public void testInitializeCopiesCrsAndMaxBounds() {
		MapConfiguration mapConfiguration = getMapConfig(Arrays.asList(RESOLUTIONS));
		Bbox maxBoundsExpected = mapConfiguration.getMaxBounds();

		viewPort.initialize(mapConfiguration);

		Assert.assertEquals(mapConfiguration.getCrs(), viewPort.getCrs());
		Bbox maxBoundsResult = viewPort.getMaximumBounds();
		Assert.assertNotNull(maxBoundsResult);
		Assert.assertEquals(maxBoundsExpected.getX(), maxBoundsResult.getX());
		Assert.assertEquals(maxBoundsExpected.getY(), maxBoundsResult.getY());
		Assert.assertEquals(maxBoundsExpected.getMaxX(), maxBoundsResult.getMaxX());
		Assert.assertEquals(maxBoundsExpected.getMaxY(), maxBoundsResult.getMaxY());
	}

	/**
	 * Create {@link MapConfiguration} with specified resolution list.
	 * @param resolutions
	 * @return
	 */
	private MapConfiguration getMapConfig(List<Double> resolutions) {
		MapConfiguration config = new MapConfigurationForTestImpl();
		config.setCrs("EPSG:4326", CrsType.DEGREES);
		config.setMaxBounds(new Bbox(-100, -100, 200, 200));
		config.setResolutions(resolutions);
		return config;
	}

	private double getInputMinResolution() {
		return RESOLUTIONS[RESOLUTIONS.length - 1];
	}
}