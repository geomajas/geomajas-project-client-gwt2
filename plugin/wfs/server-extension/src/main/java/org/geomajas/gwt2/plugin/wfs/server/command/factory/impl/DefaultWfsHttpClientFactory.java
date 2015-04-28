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
package org.geomajas.gwt2.plugin.wfs.server.command.factory.impl;

import java.net.MalformedURLException;
import java.net.URL;

import org.geomajas.gwt2.plugin.wfs.server.command.factory.WfsHttpClientFactory;
import org.geotools.data.ows.HTTPClient;

/**
 * Default implementation of {@link WfsHttpClientFactory}. Creates a {@link DefaultHttpClientImpl}.
 * 
 * @author Jan De Moerloose
 *
 */
public class DefaultWfsHttpClientFactory implements WfsHttpClientFactory {

	private HTTPClient client = new DefaultHttpClientImpl();

	@Override
	public HTTPClient create(String url) {
		return client;
	}

	@Override
	public URL getTargetUrl(String sourceUrl) {
		try {
			return new URL(sourceUrl);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Malformed WFS source url", e);
		}
	}

}
