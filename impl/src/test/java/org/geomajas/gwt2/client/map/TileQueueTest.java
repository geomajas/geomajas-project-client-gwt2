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

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.render.TileCode;
import org.geomajas.gwt2.client.map.render.TilePriorityFunction;
import org.geomajas.gwt2.client.map.render.TileQueue;
import org.geomajas.gwt2.client.map.render.dom.DomTile;
import org.geomajas.gwt2.client.map.render.dom.LoadableTile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Test for the {@link TileQueue} implementation {@link TileQueueImpl}.
 *
 * @author Youri Flement
 */
public class TileQueueTest {

	private TileQueue queue;

	private static final TilePriorityFunction priorityFunction = new TilePriorityFunctionImpl();

	private static final TileKeyProviderImpl keyProvider = new TileKeyProviderImpl();

	@Before
	public void setup() {
		queue = new TileQueueImpl<String>(priorityFunction, keyProvider);
	}

	@Test
	public void testPrioritize() {
		Coordinate[] centers = new Coordinate[] {
				new Coordinate(0, 0),
				new Coordinate(100, 100),
				new Coordinate(200, 0),
				new Coordinate(0, 200),
				new Coordinate(200, 200)
		};

		Double[] resolutionArray = new Double[] {
				1.0,
				2.0,
				4.0,
				8.0,
		};
		List<Double> resolutions = Arrays.asList(resolutionArray);
		int tileDimensions = 200;

		for (int i = 0; i < resolutions.size(); i++) {
			for (Coordinate center : centers) {
				// add tiles
				addTiles(queue, tileDimensions, 3);

				// prioritize
				queue.prioritize(i + 1, resolutions.get(i), center);

				// put ordered tiles in map
				LoadableTile[] prioritizedTiles = getTilesInOrder(queue);

				// get tiles from the zoom level
				List<LoadableTile> tilesFromZoomLevel = tilesFromZoomLevel(prioritizedTiles, i + 1);

				// assert that the closest ones to the focus are prioritized within one zoom level
				testDistanceToFocus(tilesFromZoomLevel, center);

				// assert that tiles from levels higher than the current zoom level have lower priority
				testHigherZoomLevels(i + 1, prioritizedTiles);
			}
		}
	}

	private LoadableTile[] getTilesInOrder(TileQueue queue) {
		int size = queue.size();
		LoadableTile[] tiles = new LoadableTile[size];
		for (int i = 0; i < size; i++) {
			tiles[i] = queue.poll();
		}
		return tiles;
	}

	private List<LoadableTile> tilesFromZoomLevel(LoadableTile[] tiles, int level) {
		List<LoadableTile> result = new ArrayList<LoadableTile>();
		for (LoadableTile tile : tiles) {
			if (level == tile.getCode().getTileLevel()) {
				result.add(tile);
			}
		}
		return result;
	}

	private Double distanceToCenter(Bbox bounds, Coordinate coordinate) {
		double xCenter = bounds.getX() + bounds.getWidth() / 2;
		double yCenter = bounds.getY() + bounds.getHeight() / 2;
		Coordinate center = new Coordinate(xCenter, yCenter);
		return center.distance(coordinate);
	}

	private void testDistanceToFocus(List<LoadableTile> tiles, Coordinate center) {
		double previousDistance = 0.0;
		for (LoadableTile tile : tiles) {
			double distance = distanceToCenter(tile.getBounds(), center);
			Assert.assertTrue(distance >= previousDistance);
			previousDistance = distance;
		}
	}

	private void addTiles(TileQueue queue, int tileDim, int maxZoomLevel) {
		queue.add(makeTile(0, 0, 0, tileDim, tileDim));
		for (int zoomLevel = 1; zoomLevel <= maxZoomLevel; zoomLevel++) {
			double maxTiles = Math.pow(2, zoomLevel);
			for (int x = 0; x < maxTiles; x++) {
				for (int y = 0; y < maxTiles; y++) {
					queue.add(makeTile(zoomLevel, x, y, tileDim / Math.pow(2, zoomLevel), tileDim / Math.pow(2, zoomLevel)));
				}
			}
		}
	}

	private void testHigherZoomLevels(int zoomLevel, LoadableTile[] prioritizedTiles) {
		Map<Integer, Integer> indices = new TreeMap<Integer, Integer>();
		int i = 0;
		for (LoadableTile tile : prioritizedTiles) {
			int level = tile.getCode().getTileLevel();
			if (!indices.containsKey(level)) {
				indices.put(level, i);
				i++;
			}
		}
		if (indices.size() > 1) {
			for (Entry<Integer, Integer> currentZoomLevel : indices.entrySet()) {
				if (currentZoomLevel.getKey() > zoomLevel) {
					int currentPriorityIndex = currentZoomLevel.getValue();
					int zoomPriorityIndex = indices.get(zoomLevel);
					Assert.assertTrue(zoomPriorityIndex < currentPriorityIndex);
				}
			}
		}
	}

	@Test
	public void testClear() throws Exception {
		Assert.assertEquals(0, queue.size());

		queue.add(makeTile());
		queue.add(makeTile(1, 0, 0));
		Assert.assertEquals(2, queue.size());

		queue.clear();
		Assert.assertEquals(0, queue.size());
	}

	@Test
	public void testSize() {
		Assert.assertEquals(0, queue.size());

		queue.add(makeTile());
		Assert.assertEquals(1, queue.size());

		queue.add(makeTile(1, 0, 0));
		Assert.assertEquals(2, queue.size());

		queue.poll();
		Assert.assertEquals(1, queue.size());

		queue.poll();
		Assert.assertEquals(0, queue.size());
	}

	@Test
	public void testRemove() throws Exception {
		LoadableTile firstTile = makeTile(1, 0, 0);

		queue.add(firstTile);
		Assert.assertEquals(1, queue.size());

		LoadableTile secondTile = makeTile();
		queue.remove(secondTile);
		Assert.assertEquals(1, queue.size());

		queue.add(secondTile);
		Assert.assertEquals(2, queue.size());

		queue.remove(firstTile);
		Assert.assertEquals(1, queue.size());
		Assert.assertFalse(queue.contains(firstTile));

		queue.remove(secondTile);
		Assert.assertEquals(0, queue.size());
		Assert.assertFalse(queue.contains(secondTile));
	}

	@Test
	public void testContains() {
		LoadableTile firstTile = makeTile(1, 0, 0);
		Assert.assertFalse(queue.contains(firstTile));

		queue.add(firstTile);
		Assert.assertTrue(queue.contains(firstTile));

		LoadableTile secondTile = makeTile();
		Assert.assertFalse(queue.contains(secondTile));

		queue.remove(firstTile);
		Assert.assertFalse(queue.contains(firstTile));
	}

	@Test
	public void testPoll() {
		LoadableTile topLeft = makeTile(1, 0, 0);
		LoadableTile topRight = makeTile(1, 0, 1);
		LoadableTile bottomLeft = makeTile(1, 1, 0);
		LoadableTile bottomRight = makeTile(1, 1, 1);
		queue.add(topLeft);
		queue.add(topRight);
		queue.add(bottomLeft);
		queue.add(bottomRight);

		queue.prioritize(1, 2.0, new Coordinate(0, 0));

		LoadableTile firstPriority = queue.poll();
		LoadableTile secondPriority = queue.poll();
		LoadableTile thirdPriority = queue.poll();
		LoadableTile fourthPriority = queue.poll();

		Assert.assertEquals(topLeft, firstPriority);
		Assert.assertTrue(topRight.equals(secondPriority) || bottomLeft.equals(secondPriority));
		Assert.assertTrue(topRight.equals(thirdPriority) || bottomLeft.equals(thirdPriority));
		Assert.assertEquals(bottomRight, fourthPriority);
	}

	private LoadableTile makeTile() {
		return makeTile(0, 0, 0);
	}

	private LoadableTile makeTile(int level, int x, int y) {
		return makeTile(level, x, y, 256, 256);
	}

	private LoadableTile makeTile(int level, int x, int y, double width, double height) {
		// make a new dom tile without callback and underlying image
		return new DomTile(null, new TileCode(level, x, y), new Bbox(x * width, y * height, width, height), null);
	}

}
