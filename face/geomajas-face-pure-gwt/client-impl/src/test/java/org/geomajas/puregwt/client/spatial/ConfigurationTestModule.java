/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.spatial;

import org.geomajas.puregwt.client.service.MathService;
import org.geomajas.puregwt.client.service.MathServiceImpl;

import com.google.inject.AbstractModule;

/**
 * Module for testing.
 *
 * @author Jan De Moerloose
 */
public class ConfigurationTestModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GeometryFactory.class).to(GeometryFactoryImpl.class);
		bind(MathService.class).to(MathServiceImpl.class);
	}

}
