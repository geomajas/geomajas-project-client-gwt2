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

package org.geomajas.plugin.wms.server.command;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.plugin.wms.server.command.dto.WfsGetFeaturesRequest;
import org.geomajas.plugin.wms.server.command.dto.WfsGetFeaturesResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test case for the WfsGetFeaturesCommand.
 *
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "commandContext.xml" })
public class WfsGetFeaturesCommandTest {

	private static final String WMS_BASE_URL = "http://apps.geomajas.org/geoserver/demo_world/ows";

	private static final String LAYER = "demo_world:simplified_country_borders";

	@Autowired
//	private WfsGetFeaturesCommand command;

	@Test
	public void testGetCountries() throws Exception {
//		WfsGetFeaturesResponse response = command.getEmptyCommandResponse();
//		Assert.assertNotNull(response);
//
//		Bbox bounds = new Bbox(-180, -90, 360, 180);
//		Geometry geometry = GeometryService.toPolygon(bounds);
//
//		WfsGetFeaturesRequest request = new WfsGetFeaturesRequest(WMS_BASE_URL, LAYER, geometry);
//		request.setMaxCoordsPerFeature(100);
//		request.setMaxNumOfFeatures(100);
//
//		command.execute(request, response);
//		Assert.assertNotNull(response.getFeatures());
//		Assert.assertTrue(response.getFeatures().size() > 0);
	}
}