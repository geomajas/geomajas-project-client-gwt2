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

package org.geomajas.gwt2.client.map.layer.tile;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.GeomajasImpl;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.MapEventBusImpl;
import org.geomajas.gwt2.client.map.TestConfigUtil;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.render.TileCode;
import org.geomajas.gwt2.client.service.TileService;
import org.junit.Assert;
import org.junit.Before;

import java.util.List;

/**
 * Testcases for the TileService.
 *
 * @author Pieter De Graef
 */
public class TileServiceTest {

	private static final double DELTA = 1e-10;

	private MapEventBus eventBus;

	private ViewPort viewPort;

	private TileConfiguration tileConfig;

	@Before
	public void initialize2() {
		eventBus = new MapEventBusImpl(this, GeomajasImpl.getInstance().getEventBus());
		viewPort = TestConfigUtil.createViewPort(eventBus, TestConfigUtil.getMapConfig(), 1000, 1000);

		Bbox max = viewPort.getMaximumBounds();
		tileConfig = new TileConfiguration(200, 200, new Coordinate(max.getX(), max.getY()), viewPort);
	}

	//	@Test
	public void testTileCodeForLocation() {
		// Test scale 8: tileWidth=25 (200/8), origin=[-100,-100]:
		TileCode tileCode = TileService.getTileCodeForLocation(tileConfig, new Coordinate(0, 0), 0.1);
		Assert.assertNotNull(tileCode);
		Assert.assertEquals(3, tileCode.getTileLevel());
		Assert.assertEquals(4, tileCode.getX());
		Assert.assertEquals(4, tileCode.getY());
		tileCode = TileService.getTileCodeForLocation(tileConfig, new Coordinate(-1, -1), 0.125);
		Assert.assertEquals(3, tileCode.getTileLevel());
		Assert.assertEquals(3, tileCode.getX());
		Assert.assertEquals(3, tileCode.getY());

		// Test scale 4: tileWidth=50 (200/4), origin=[-100,-100]:
		tileCode = TileService.getTileCodeForLocation(tileConfig, new Coordinate(0, 0), 0.25);
		Assert.assertEquals(2, tileCode.getTileLevel());
		Assert.assertEquals(2, tileCode.getX());
		Assert.assertEquals(2, tileCode.getY());
		tileCode = TileService.getTileCodeForLocation(tileConfig, new Coordinate(-1, -1), 0.25);
		Assert.assertEquals(2, tileCode.getTileLevel());
		Assert.assertEquals(1, tileCode.getX());
		Assert.assertEquals(1, tileCode.getY());

		// Test scale 2: tileWidth=100 (200/2), origin=[-100,-100]:
		tileCode = TileService.getTileCodeForLocation(tileConfig, new Coordinate(0, 0), 0.5);
		Assert.assertEquals(1, tileCode.getTileLevel());
		Assert.assertEquals(1, tileCode.getX());
		Assert.assertEquals(1, tileCode.getY());
		tileCode = TileService.getTileCodeForLocation(tileConfig, new Coordinate(-1, -1), 0.5);
		Assert.assertEquals(1, tileCode.getTileLevel());
		Assert.assertEquals(0, tileCode.getX());
		Assert.assertEquals(0, tileCode.getY());

		// Test scale 1: tileWidth=200 (200/1), origin=[-100,-100]:
		tileCode = TileService.getTileCodeForLocation(tileConfig, new Coordinate(0, 0), 1);
		Assert.assertEquals(0, tileCode.getTileLevel());
		Assert.assertEquals(0, tileCode.getX());
		Assert.assertEquals(0, tileCode.getY());
		tileCode = TileService.getTileCodeForLocation(tileConfig, new Coordinate(-1, -1), 1);
		Assert.assertEquals(0, tileCode.getTileLevel());
		Assert.assertEquals(0, tileCode.getX());
		Assert.assertEquals(0, tileCode.getY());
		tileCode = TileService.getTileCodeForLocation(tileConfig, new Coordinate(100, 100), 1);
		Assert.assertEquals(0, tileCode.getTileLevel());
		Assert.assertEquals(1, tileCode.getX());
		Assert.assertEquals(1, tileCode.getY());
	}

	//	@Test
	public void testWorldBoundsForTile() {
		// TileLevel 0: scale=8 => tileWidth=200/8=25
		Bbox bounds = TileService.getWorldBoundsForTile(tileConfig, new TileCode(3, 0, 0));
		Assert.assertEquals(-100.0, bounds.getX(), DELTA);
		Assert.assertEquals(-100.0, bounds.getY(), DELTA);
		Assert.assertEquals(-75.0, bounds.getMaxX(), DELTA);
		Assert.assertEquals(-75.0, bounds.getMaxY(), DELTA);

		bounds = TileService.getWorldBoundsForTile(tileConfig, new TileCode(3, 1, 1));
		Assert.assertEquals(-75.0, bounds.getX(), DELTA);
		Assert.assertEquals(-75.0, bounds.getY(), DELTA);
		Assert.assertEquals(-50.0, bounds.getMaxX(), DELTA);
		Assert.assertEquals(-50.0, bounds.getMaxY(), DELTA);

		// TileLevel 1: scale=4 => tileWidth=200/4=50
		bounds = TileService.getWorldBoundsForTile(tileConfig, new TileCode(2, 0, 0));
		Assert.assertEquals(-100.0, bounds.getX(), DELTA);
		Assert.assertEquals(-100.0, bounds.getY(), DELTA);
		Assert.assertEquals(-50.0, bounds.getMaxX(), DELTA);
		Assert.assertEquals(-50.0, bounds.getMaxY(), DELTA);

		bounds = TileService.getWorldBoundsForTile(tileConfig, new TileCode(2, 1, 1));
		Assert.assertEquals(-50.0, bounds.getX(), DELTA);
		Assert.assertEquals(-50.0, bounds.getY(), DELTA);
		Assert.assertEquals(0.0, bounds.getMaxX(), DELTA);
		Assert.assertEquals(0.0, bounds.getMaxY(), DELTA);

		// TileLevel 2: scale=2 => tileWidth=200/2=100
		bounds = TileService.getWorldBoundsForTile(tileConfig, new TileCode(1, 0, 0));
		Assert.assertEquals(-100.0, bounds.getX(), DELTA);
		Assert.assertEquals(-100.0, bounds.getY(), DELTA);
		Assert.assertEquals(0.0, bounds.getMaxX(), DELTA);
		Assert.assertEquals(0.0, bounds.getMaxY(), DELTA);

		bounds = TileService.getWorldBoundsForTile(tileConfig, new TileCode(1, 1, 1));
		Assert.assertEquals(0.0, bounds.getX(), DELTA);
		Assert.assertEquals(0.0, bounds.getY(), DELTA);
		Assert.assertEquals(100.0, bounds.getMaxX(), DELTA);
		Assert.assertEquals(100.0, bounds.getMaxY(), DELTA);

		// TileLevel 3: scale=1 => tileWidth=200/1=200
		bounds = TileService.getWorldBoundsForTile(tileConfig, new TileCode(0, 0, 0));
		Assert.assertEquals(-100.0, bounds.getX(), DELTA);
		Assert.assertEquals(-100.0, bounds.getY(), DELTA);
		Assert.assertEquals(100.0, bounds.getMaxX(), DELTA);
		Assert.assertEquals(100.0, bounds.getMaxY(), DELTA);

		bounds = TileService.getWorldBoundsForTile(tileConfig, new TileCode(0, 1, 1));
		Assert.assertEquals(100.0, bounds.getX(), DELTA);
		Assert.assertEquals(100.0, bounds.getY(), DELTA);
		Assert.assertEquals(300.0, bounds.getMaxX(), DELTA);
		Assert.assertEquals(300.0, bounds.getMaxY(), DELTA);
	}

	//	@Test
	public void testTileCodesForBounds() {
		// Scale 8: TileLevel=0 => tileWidth=200/8=25
		List<TileCode> tiles = TileService.getTileCodesForBounds(tileConfig, new Bbox(0, 0, 50, 50), 0.125);
		Assert.assertEquals(9, tiles.size());

		tiles = TileService.getTileCodesForBounds(tileConfig, new Bbox(0, 0, 49.9, 49.9), 0.125);
		Assert.assertEquals(4, tiles.size());
		Assert.assertEquals(3, tiles.get(0).getTileLevel());
		Assert.assertEquals(4, tiles.get(0).getX());
		Assert.assertEquals(4, tiles.get(0).getY());
		Assert.assertEquals(3, tiles.get(3).getTileLevel());
		Assert.assertEquals(5, tiles.get(3).getX());
		Assert.assertEquals(5, tiles.get(3).getY());

		// Scale 4: TileLevel=1 => tileWidth=200/4=50
		tiles = TileService.getTileCodesForBounds(tileConfig, new Bbox(0, 0, 99.9, 99.9), 0.25);
		Assert.assertEquals(4, tiles.size());
		Assert.assertEquals(2, tiles.get(0).getTileLevel());
		Assert.assertEquals(2, tiles.get(0).getX());
		Assert.assertEquals(2, tiles.get(0).getY());
		Assert.assertEquals(2, tiles.get(3).getTileLevel());
		Assert.assertEquals(3, tiles.get(3).getX());
		Assert.assertEquals(3, tiles.get(3).getY());

		// Scale 2: TileLevel=2 => tileWidth=200/2=100
		tiles = TileService.getTileCodesForBounds(tileConfig, new Bbox(-100, -100, 199.9, 199.9), 0.5);
		Assert.assertEquals(4, tiles.size());
		Assert.assertEquals(1, tiles.get(0).getTileLevel());
		Assert.assertEquals(0, tiles.get(0).getX());
		Assert.assertEquals(0, tiles.get(0).getY());
		Assert.assertEquals(1, tiles.get(3).getTileLevel());
		Assert.assertEquals(1, tiles.get(3).getX());
		Assert.assertEquals(1, tiles.get(3).getY());

		// Scale 1: TileLevel=3 => tileWidth=200/1=200
		tiles = TileService.getTileCodesForBounds(tileConfig, new Bbox(-100, -100, 200, 200), 1);
		Assert.assertEquals(4, tiles.size());
		Assert.assertEquals(0, tiles.get(0).getTileLevel());
		Assert.assertEquals(0, tiles.get(0).getX());
		Assert.assertEquals(0, tiles.get(0).getY());
		Assert.assertEquals(0, tiles.get(3).getTileLevel());
		Assert.assertEquals(1, tiles.get(3).getX());
		Assert.assertEquals(1, tiles.get(3).getY());
	}
}