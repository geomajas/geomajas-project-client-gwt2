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

package org.geomajas.plugin.editing.client.service;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases testing the methods in the {@link GeometryIndex}.
 * 
 * @author Pieter De Graef
 */
public class GeometryIndexTest {

	private GeometryIndexService service = new GeometryIndexService();

	// ------------------------------------------------------------------------
	// Test the GeometryIndexService.
	// ------------------------------------------------------------------------

	@Test
	public void testNoArgsConstructor() {
		Assert.assertNotNull(new GeometryIndex());
	}

	@Test
	public void testHashCode() {
		GeometryIndex index1 = service.create(GeometryIndexType.TYPE_VERTEX, 0, 1);
		GeometryIndex index2 = service.create(GeometryIndexType.TYPE_VERTEX, 0, 1);
		Assert.assertNotSame(index1.hashCode(), index2.hashCode());
		Assert.assertArrayEquals(new int[] {0, 1}, index1.getAllValues());
	}

	@Test
	public void testGetAllValues() {
		GeometryIndex index1 = service.create(GeometryIndexType.TYPE_VERTEX, 0, 1);
		Assert.assertArrayEquals(new int[] {0, 1}, index1.getAllValues());
		GeometryIndex index2 = service.create(GeometryIndexType.TYPE_EDGE, 0, 1);
		Assert.assertArrayEquals(new int[] {0, 1}, index2.getAllValues());
		GeometryIndex index3 = service.create(GeometryIndexType.TYPE_GEOMETRY, 1, 2, 3);
		Assert.assertArrayEquals(new int[] {1, 2, 3}, index3.getAllValues());
	}

	@Test
	public void testToString() {
		GeometryIndex index = service.create(GeometryIndexType.TYPE_VERTEX, 0, 1);
		Assert.assertEquals("TYPE_GEOMETRY-0 / TYPE_VERTEX-1", index.toString());
	}
}